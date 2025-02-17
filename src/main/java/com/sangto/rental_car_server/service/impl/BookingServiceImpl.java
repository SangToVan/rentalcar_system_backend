package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.constant.MetaConstant;
import com.sangto.rental_car_server.constant.TimeFormatConstant;
import com.sangto.rental_car_server.domain.dto.booking.AddBookingRequestDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseForOwnerDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.domain.dto.meta.SortingDTO;
import com.sangto.rental_car_server.domain.dto.payment.PaymentRequestDTO;
import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.domain.entity.Car;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import com.sangto.rental_car_server.domain.enums.ECarStatus;
import com.sangto.rental_car_server.domain.enums.EPaymentMethod;
import com.sangto.rental_car_server.domain.enums.EPaymentType;
import com.sangto.rental_car_server.domain.mapper.BookingMapper;
import com.sangto.rental_car_server.exception.AppException;
import com.sangto.rental_car_server.repository.BookingRepository;
import com.sangto.rental_car_server.repository.CarRepository;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.response.MetaResponse;
import com.sangto.rental_car_server.response.Response;
import com.sangto.rental_car_server.service.BookingService;
import com.sangto.rental_car_server.service.CarService;
import com.sangto.rental_car_server.service.PaymentService;
import com.sangto.rental_car_server.utility.RentalCalculateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class BookingServiceImpl implements BookingService {

    private final CarRepository carRepo;
    private final UserRepository userRepo;
    private final BookingRepository bookingRepo;
    private final CarService carService;
    private final PaymentService paymentService;
    private final BookingMapper bookingMapper;

    @Override
    public Booking verifyBookingOwner(Integer ownerId, Integer bookingId) {
        Optional<Booking> findBooking = bookingRepo.findById(bookingId);
        if (findBooking.isEmpty()) throw new AppException("This booking is not existed");
        Booking booking = findBooking.get();
        if (booking.getCar().getCar_owner().getId() != ownerId) throw new AppException("Unauthorized");
        return booking;
    }

    @Override
    public Booking verifyBookingCustomer(Integer customerId, Integer bookingId) {
        Optional<Booking> findBooking = bookingRepo.findById(bookingId);
        if (findBooking.isEmpty()) throw new AppException("This booking is not existed");
        Booking booking = findBooking.get();
        if (booking.getCustomer().getId() != customerId) throw new AppException("Unauthorized");
        return booking;
    }

    @Override
    public MetaResponse<MetaResponseDTO, List<BookingResponseDTO>> getAllBookings(MetaRequestDTO requestDTO) {
        Sort sort = requestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(requestDTO.sortField()).ascending()
                : Sort.by(requestDTO.sortField()).descending();
        Pageable pageable = PageRequest.of(requestDTO.currentPage(), requestDTO.pageSize(), sort);
        Page<Booking> page = bookingRepo.findAll(pageable);
        if (page.getContent().isEmpty()) throw new AppException("List bookings is empty");

        List<BookingResponseDTO> list = page.getContent().stream()
                .map(bookingMapper::toBookingResponseDTO)
                .toList();

        return MetaResponse.successfulResponse(
                "Get all bookings success",
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
    public MetaResponse<MetaResponseDTO, List<BookingResponseDTO>> getListBookingForUser(MetaRequestDTO requestDTO, Integer userId) {
        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("User is not existed");
        Sort sort = requestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(requestDTO.sortField()).ascending()
                : Sort.by(requestDTO.sortField()).descending();
        Pageable pageable = PageRequest.of(requestDTO.currentPage(), requestDTO.pageSize(), sort);
        Page<Booking> page = requestDTO.keyword() == null
                ? bookingRepo.getListBookingByUserId(userId, pageable)
                : bookingRepo.getListBookingByUserIdWithKeyword(userId, requestDTO.keyword(), pageable);
        if (page.getContent().isEmpty()) throw new AppException("List bookings is empty");

        List<BookingResponseDTO> list = page.getContent().stream()
                .map(bookingMapper::toBookingResponseDTO)
                .toList();

        return MetaResponse.successfulResponse(
                "Get list bookings success",
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
    public MetaResponse<MetaResponseDTO, List<BookingResponseForOwnerDTO>> getListBookingByCarId(MetaRequestDTO requestDTO, Integer carId, Integer userId) {
        Car car = carService.verifyCarOwner(userId, carId);

        Sort sort = requestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(requestDTO.sortField()).ascending()
                : Sort.by(requestDTO.sortField()).descending();
        Pageable pageable = PageRequest.of(requestDTO.currentPage(), requestDTO.pageSize(), sort);
        Page<Booking> page = bookingRepo.getListBookingByCarId(car.getId(), pageable);
        if (page.getContent().isEmpty()) throw new AppException("List bookings is empty");
        List<BookingResponseForOwnerDTO> list = page.getContent().stream()
                .map(bookingMapper::toBookingResponseForOwnerDTO)
                .toList();

        return MetaResponse.successfulResponse(
                "Get list bookings success",
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
    public Response<BookingDetailResponseDTO> getDetailBooking(Integer bookingId) {
        Optional<Booking> findBooking = bookingRepo.findById(bookingId);
        if (findBooking.isEmpty()) throw new AppException("Booking is not existed");

        return Response.successfulResponse(
                "Get detail booking successful",
                bookingMapper.toBookingDetailResponseDTO(findBooking.get())
        );
    }

    @Override
    @Transactional
    public Response<BookingDetailResponseDTO> addBooking(Integer userId, AddBookingRequestDTO requestDTO) {
        Optional<Car> findCar = carRepo.findById(requestDTO.car_id());
        if (findCar.isEmpty()) throw new AppException("Car is not existed");
        Car car = findCar.get();
        // Check the condition of car
        if (car.getStatus() != ECarStatus.ACTIVE) throw new AppException("Car is not ready to booking");

        // Check schedule to rent Car
        Optional<Car> checkScheduleCar = carRepo.checkScheduleCar(requestDTO.car_id(), requestDTO.start_date(), requestDTO.end_date());
        if (checkScheduleCar.isEmpty()) throw new AppException("Car is not existed");

        // Customer
        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("User is not existed");
        User customer = findUser.get();

        // Convert startDate and endDate from String to Date
        SimpleDateFormat sdf = new SimpleDateFormat(TimeFormatConstant.DATETIME_FORMAT);
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = sdf.parse(requestDTO.start_date());
            endDate = sdf.parse(requestDTO.end_date());

        } catch (ParseException e) {
            throw new AppException("Invalid date format", e);
        }

        // Calculate rental duration and cost
        long rentalDurationHours = RentalCalculateUtil.calculateHour(startDate, endDate);
        if (rentalDurationHours <= 0) throw new AppException("Rental duration must be greater than zero");

        double rentalFee = RentalCalculateUtil.calculatePrice(startDate, endDate, car.getPrice_per_day());
        double serviceFee = rentalFee * 0.1;
        double totalPrice = rentalFee + serviceFee;

        // Case: Payment method is wallet
        if (requestDTO.paymentMethod() == EPaymentMethod.WALLET) {
            if (customer.getWallet().getAmount() < totalPrice) throw new AppException("Customer wallet not have enough money");
        }

        Booking newBooking = bookingMapper.addBookingRequestDTOtoBookingEntity(requestDTO);
        newBooking.setCar(car);
        newBooking.setCustomer(customer);
        newBooking.setRental_fee(rentalFee);
        newBooking.setService_fee(serviceFee);
        newBooking.setTotal_price(totalPrice);
        newBooking.setStatus(EBookingStatus.PAID);
        try {
            paymentService.processPayment(PaymentRequestDTO.builder()
                            .booking_id(newBooking.getId())
                            .amount(newBooking.getTotal_price())
                            .paymentType(EPaymentType.TOTAL_FEE)
                            .paymentMethod(requestDTO.paymentMethod())
                            .transaction_date(new Date())
                    .build());
        } catch (Exception e) {
            throw new AppException("Payment failed", e);
        }
        Booking savedBooking = bookingRepo.save(newBooking);
        return Response.successfulResponse(
                "Add new booking successful",
                bookingMapper.toBookingDetailResponseDTO(savedBooking)
        );
    }

    @Override
    public Response<String> confirmBooking(Integer userId, Integer bookingId) {
        Booking booking = this.verifyBookingOwner(userId, bookingId);
        if (booking.getStatus() == EBookingStatus.PAID) {
            // Updated booking
            booking.setStatus(EBookingStatus.CONFIRMED);
            bookingRepo.save(booking);
            return Response.successfulResponse(
                    "Confirm booking successfully"
            );
        } else {
            throw new AppException("This booking is not allowed to confirm");
        }
    }

    @Override
    public Response<String> confirmBring(Integer bookingId) {
        Optional<Booking> findBooking = bookingRepo.findById(bookingId);
        if (findBooking.isEmpty()) throw new AppException("Booking is not existed");
        Booking booking = findBooking.get();
        if (booking.getStatus() == EBookingStatus.CONFIRMED) {
            booking.setStatus(EBookingStatus.PICK_UP);
            bookingRepo.save(booking);

            return Response.successfulResponse("Confirm pickup successfully");
        } else throw new AppException("This booking is not allowed to pick-up");
    }

    @Override
    public Response<String> confirmPickup(Integer bookingId) {
        Optional<Booking> findBooking = bookingRepo.findById(bookingId);
        if (findBooking.isEmpty()) throw new AppException("Booking is not existed");
        Booking booking = findBooking.get();
        if (booking.getStatus() == EBookingStatus.PICK_UP) {
            booking.setStatus(EBookingStatus.IN_PROGRESS);
            bookingRepo.save(booking);

            return Response.successfulResponse("Confirm pickup successfully");
        } else throw new AppException("This booking is not allowed to pick-up");
    }

    @Override
    public Response<String> confirmPayment(Integer userId, Integer bookingId) {

        Booking booking = this.verifyBookingOwner(userId, bookingId);
        if (booking.getStatus() != EBookingStatus.PENDING_PAYMENT) throw new AppException("This booking is not allowed to confirm payment");

        try {
            paymentService.completePayment(booking.getId());
        } catch (Exception e) {
            throw new AppException("Payment failed", e);
        }
        booking.setStatus(EBookingStatus.COMPLETED);
        bookingRepo.save(booking);
        return Response.successfulResponse("Confirm payment successfully");
    }

    @Override
    public Response<String> cancelBooking(Integer userId, Integer bookingId) {

        Booking booking = this.verifyBookingOwner(userId, bookingId);
        Optional<Car> findCar = carRepo.findById(booking.getCar().getId());
        if (findCar.isEmpty()) throw new AppException("Car is not existed");
        Car car = findCar.get();

        User owner = car.getCar_owner();

        Optional<User> findCustomer = userRepo.findById(booking.getCustomer().getId());
        if (findCustomer.isEmpty()) throw new AppException("Customer is not existed");
        User customer = findCustomer.get();

        if (booking.getStatus() == EBookingStatus.PAID) {
            booking.setStatus(EBookingStatus.CANCELLED);
            bookingRepo.save(booking);
            try {
                paymentService.cancelPayment(booking.getId());
            } catch (Exception e) {
                throw new AppException("Payment failed", e);
            }
        }
        return Response.successfulResponse("Cancel booking successfully");
    }

    @Override
    public Response<String> returnCar(Integer userId, Integer bookingId) {

        Booking booking = this.verifyBookingOwner(userId, bookingId);
        if (booking.getStatus() != EBookingStatus.IN_PROGRESS) throw new AppException("This booking is not allowed to return");

        booking.setStatus(EBookingStatus.PENDING_PAYMENT);
        bookingRepo.save(booking);
        return Response.successfulResponse("Return car successfully");
    }

    @Override
    public List<Booking> findOverdueBookings(LocalDateTime overdueTime) {
        return bookingRepo.findByStatusAndStartDateTime(EBookingStatus.CONFIRMED, overdueTime);
    }

    @Override
    public void updateBookingStatus(Integer bookingId) {

    }

    @Override
    public List<Booking> getCompletedBookings(Integer ownerId) {
        return bookingRepo.findByCarOwnerAndBookingStatus(ownerId, EBookingStatus.COMPLETED);
    }
}
