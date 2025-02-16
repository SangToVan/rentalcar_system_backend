package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.constant.MetaConstant;
import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.domain.dto.meta.SortingDTO;
import com.sangto.rental_car_server.domain.dto.transaction.TransactionRequestDTO;
import com.sangto.rental_car_server.domain.dto.transaction.FilterTransactionByTimeRequestDTO;
import com.sangto.rental_car_server.domain.dto.transaction.TransactionResponseDTO;
import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.domain.entity.Transaction;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.entity.Wallet;
import com.sangto.rental_car_server.domain.enums.ETransactionStatus;
import com.sangto.rental_car_server.domain.enums.ETransactionType;
import com.sangto.rental_car_server.domain.mapper.TransactionMapper;
import com.sangto.rental_car_server.exception.AppException;
import com.sangto.rental_car_server.repository.BookingRepository;
import com.sangto.rental_car_server.repository.TransactionRepository;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.repository.WalletRepository;
import com.sangto.rental_car_server.response.MetaResponse;
import com.sangto.rental_car_server.response.Response;
import com.sangto.rental_car_server.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service

@RequiredArgsConstructor
@EnableTransactionManagement
public class TransactionServiceImpl implements TransactionService {

    private final UserRepository userRepo;
    private final WalletRepository walletRepo;
    private final TransactionRepository transactionRepo;
    private final BookingRepository bookingRepo;
    private final TransactionMapper transactionMapper;

    @Override
    public MetaResponse<MetaResponseDTO, List<TransactionResponseDTO>> getListByUserId(Integer userId, MetaRequestDTO requestDTO, FilterTransactionByTimeRequestDTO filterDTO) {
        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("User not exists");

        String sortField = requestDTO.sortField();
        if (sortField.compareTo(MetaConstant.Sorting.DEFAULT_FIELD) == 0) sortField = "transaction_id";
        Sort sort = requestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(requestDTO.sortField()).ascending()
                : Sort.by(requestDTO.sortField()).descending();
        Pageable pageable = PageRequest.of(requestDTO.currentPage(), requestDTO.pageSize(), sort);

        Page<Transaction> page = transactionRepo.getListByUserId(userId, filterDTO.start_date(), filterDTO.end_date(), pageable);
        if (page.isEmpty()) throw new AppException("No payment found");

        List<TransactionResponseDTO> list = page.getContent().stream()
                .map(transactionMapper::toTransactionResponseDTO)
                .toList();

        return MetaResponse.successfulResponse(
                "Get list transaction success",
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
    public MetaResponse<MetaResponseDTO, List<TransactionResponseDTO>> getListByWalletId(Integer walletId, MetaRequestDTO requestDTO, FilterTransactionByTimeRequestDTO filterDTO) {
        Optional<Wallet> findWallet = walletRepo.findById(walletId);
        if (findWallet.isEmpty()) throw new AppException("Wallet is not exist");

        String sortField = requestDTO.sortField();
        if (sortField.compareTo(MetaConstant.Sorting.DEFAULT_FIELD) == 0) sortField = "transaction_id";
        Sort sort = requestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(requestDTO.sortField()).ascending()
                : Sort.by(requestDTO.sortField()).descending();
        Pageable pageable = PageRequest.of(requestDTO.currentPage(), requestDTO.pageSize(), sort);

        Page<Transaction> page = transactionRepo.getListByUserId(walletId, filterDTO.start_date(), filterDTO.end_date(), pageable);
        if (page.isEmpty()) throw new AppException("No transaction found");

        List<TransactionResponseDTO> list = page.getContent().stream()
                .map(transactionMapper::toTransactionResponseDTO)
                .toList();

        return MetaResponse.successfulResponse(
                "Get list transaction success",
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
    @Transactional
    public Response<TransactionResponseDTO> createTransaction(TransactionRequestDTO requestDTO) {

        Optional<Wallet> findWallet = walletRepo.findById(requestDTO.wallet_id());
        if (findWallet.isEmpty()) throw new AppException("Wallet is not exist");
        Wallet wallet = findWallet.get();

        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(requestDTO.amount());
        transaction.setType(requestDTO.type());
        transaction.setDescription(requestDTO.description());
        transaction.setTransaction_date(requestDTO.transaction_date());

        if (requestDTO.booking_id() != null) {
            Booking booking = bookingRepo.findById(requestDTO.booking_id()).orElseThrow(() -> new AppException("Booking not found"));
            transaction.setBooking(booking);
        }

        try {
            if (requestDTO.type() == ETransactionType.CREDIT) {
                wallet.setAmount(wallet.getAmount() + requestDTO.amount());
            } else if (requestDTO.type() == ETransactionType.DEBIT) {
                if (wallet.getAmount() < requestDTO.amount()) {
                    throw new AppException("Debit credit amount exceeded");
                }
                wallet.setAmount(wallet.getAmount() - requestDTO.amount());
            }
            walletRepo.save(wallet);
            transaction.setStatus(ETransactionStatus.COMPLETED);
        } catch (AppException e) {
            transaction.setStatus(ETransactionStatus.FAILED);
            transaction.setError_message(e.getMessage());
            transactionRepo.save(transaction);
            throw new AppException("Create transaction failed", e.getCause());
        }

        Transaction saveTransaction = transactionRepo.save(transaction);

        return Response.successfulResponse(
                "Transaction processed successfully",
                TransactionResponseDTO.builder()
                        .transaction_id(saveTransaction.getId())
                        .amount(saveTransaction.getAmount())
                        .type(saveTransaction.getType())
                        .status(saveTransaction.getStatus())
                        .description(saveTransaction.getDescription())
                        .transaction_date(saveTransaction.getTransaction_date())
                        .build()
        );

    }
}
