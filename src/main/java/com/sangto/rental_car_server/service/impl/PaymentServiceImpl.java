package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.constant.MetaConstant;
import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.domain.dto.meta.SortingDTO;
import com.sangto.rental_car_server.domain.dto.payment.FilterPaymentByTimeRequestDTO;
import com.sangto.rental_car_server.domain.dto.payment.PaymentRequestDTO;
import com.sangto.rental_car_server.domain.dto.payment.PaymentResponseDTO;
import com.sangto.rental_car_server.domain.dto.transaction.TransactionRequestDTO;
import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.domain.entity.Payment;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.entity.Wallet;
import com.sangto.rental_car_server.domain.enums.*;
import com.sangto.rental_car_server.domain.mapper.PaymentMapper;
import com.sangto.rental_car_server.exception.AppException;
import com.sangto.rental_car_server.repository.BookingRepository;
import com.sangto.rental_car_server.repository.PaymentRepository;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.repository.WalletRepository;
import com.sangto.rental_car_server.response.MetaResponse;
import com.sangto.rental_car_server.response.Response;
import com.sangto.rental_car_server.service.PaymentService;
import com.sangto.rental_car_server.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class PaymentServiceImpl implements PaymentService {

    private final UserRepository userRepo;
    private final PaymentRepository paymentRepo;
    private final BookingRepository bookingRepo;
    private final WalletRepository walletRepo;
    private final TransactionService transactionService;
    private final PaymentMapper paymentMapper;

    @Override
    public MetaResponse<MetaResponseDTO, List<PaymentResponseDTO>> getListByUserId(Integer userId, MetaRequestDTO requestDTO, FilterPaymentByTimeRequestDTO filterDTO) {
        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("User not exists");

        String sortField = requestDTO.sortField();
        if (sortField.compareTo(MetaConstant.Sorting.DEFAULT_FIELD) == 0) sortField = "payment_id";
        Sort sort = requestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(requestDTO.sortField()).ascending()
                : Sort.by(requestDTO.sortField()).descending();
        Pageable pageable = PageRequest.of(requestDTO.currentPage(), requestDTO.pageSize(), sort);

        Page<Payment> page = paymentRepo.getListByUserId(userId, filterDTO.start_date(), filterDTO.end_date(), pageable);
        if (page.isEmpty()) throw new AppException("No payment found");

        List<PaymentResponseDTO> list = page.getContent().stream()
                .map(paymentMapper::toPaymentResponseDTO)
                .toList();

        return MetaResponse.successfulResponse(
                "Get list payment success",
                MetaResponseDTO.builder()
                        .totalItems((int) page.getTotalElements())
                        .totalPages(page.getTotalPages())
                        .currentPage(requestDTO.currentPage())
                        .pageSize(requestDTO.pageSize())
                        .sorting(SortingDTO.builder()
                                .sortField(requestDTO.sortField())
                                .sortDir(requestDTO.sortDir())
                                .build())
                        .build(), list
        );

    }

    @Override
    public Response<PaymentResponseDTO> processPayment(PaymentRequestDTO requestDTO) {
        // Tìm đơn đặt xe
        Booking booking = bookingRepo.findById(requestDTO.booking_id())
                .orElseThrow(() -> new AppException("Booking not found"));

        // Tạo hoá đơn
        Payment depositPayment = Payment.builder()
                .booking(booking)
                .amount(booking.getTotal_price())
                .type(requestDTO.paymentType())
                .method(requestDTO.paymentMethod())
                .status(EPaymentStatus.HELD)
                .transaction_date(new Date())
                .build();
        paymentRepo.save(depositPayment);

        // Tạo giao dịch thanh toán
        User customer = booking.getCustomer();
        Wallet wallet = walletRepo.findByUserId(customer.getId());
        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(
                wallet.getId(),
                booking.getId(),
                depositPayment.getAmount(),
                ETransactionType.DEBIT,
                "Transfer deposit for process booking",
                depositPayment.getTransaction_date()
        );
        transactionService.bookingTransaction(transactionRequestDTO);

        booking.setStatus(EBookingStatus.PAID);
        bookingRepo.save(booking);

        return Response.successfulResponse(
                "Booking payment processed successfully",
                PaymentResponseDTO.builder()
                        .payment_id(depositPayment.getId())
                        .amount(depositPayment.getAmount())
                        .paymentType(depositPayment.getType())
                        .paymentMethod(depositPayment.getMethod())
                        .status(depositPayment.getStatus())
                        .transaction_date(depositPayment.getTransaction_date())
                        .build()
        );
    }

    @Override
    public Response<PaymentResponseDTO> cancelPayment(Integer bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new AppException("Booking not found"));

        if (booking.getStatus().equals(EBookingStatus.COMPLETED)) {
            throw new AppException("Booking is already completed");
        }

        //Lấy danh sách các hoá đơn theo đơn đặt xe
        List<Payment> payments = paymentRepo.findAllByBookingIdAndStatus(bookingId, EPaymentStatus.HELD);
        Double totalRefund = 0.0;
        for (Payment payment : payments) {
            totalRefund += payment.getAmount();
        }

        // Tạo hoá đơn trả lại phí đặt xe
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(totalRefund);
        payment.setType(EPaymentType.REFUND);
        payment.setMethod(EPaymentMethod.WALLET);
        payment.setStatus(EPaymentStatus.REFUNDED);
        payment.setTransaction_date(new Date());
        paymentRepo.save(payment);


        // Tạo giao dịch hoàn trả phí
        User customer = booking.getCustomer();
        Wallet wallet = walletRepo.findByUserId(customer.getId());
        TransactionRequestDTO requestDTO = new TransactionRequestDTO(
                wallet.getId(),
                bookingId,
                totalRefund,
                ETransactionType.CREDIT,
                "Refund deposit for cancelled booking",
                payment.getTransaction_date()
        );
        transactionService.bookingTransaction(requestDTO);

        booking.setStatus(EBookingStatus.CANCELLED);
        bookingRepo.save(booking);

        return Response.successfulResponse(
                "Booking cancelled and refund processed successfully",
                paymentMapper.toPaymentResponseDTO(payment)
        );
    }

    @Override
    public Response<PaymentResponseDTO> completePayment(Integer bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new AppException("Booking not found"));

        if (booking.getStatus().equals(EBookingStatus.PAID)) {
            throw new AppException("Booking is not in a valid state for complete payment");
        }
        Optional<Payment> findPayment = paymentRepo.findByBookingIdAndType(bookingId, EPaymentType.RENTAL_FEE);
        if (findPayment.isEmpty()) throw new AppException("No payment found");
        Payment depositPayment = findPayment.get();

        // Tạo hoá đơn thanh toán cho chủ xe
        Payment transferPayment = Payment.builder()
                .booking(booking)
                .amount(depositPayment.getAmount())
                .type(EPaymentType.TRANSFER_TO_OWNER)
                .method(EPaymentMethod.WALLET)
                .status(EPaymentStatus.RELEASED)
                .transaction_date(new Date())
                .build();
        paymentRepo.save(transferPayment);

        // Tạo giao dịch thanh toán cho chủ xe
        User carOwner = booking.getCar().getCar_owner();
        Wallet wallet = walletRepo.findByUserId(carOwner.getId());
        TransactionRequestDTO requestDTO = new TransactionRequestDTO(
                wallet.getId(),
                bookingId,
                transferPayment.getAmount(),
                ETransactionType.CREDIT,
                "Transfer deposit for complete booking",
                transferPayment.getTransaction_date()
        );
        transactionService.bookingTransaction(requestDTO);

        return Response.successfulResponse(
                "Booking completed and fund transfer completed",
                paymentMapper.toPaymentResponseDTO(transferPayment)
        );
    }
}
