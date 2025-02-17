package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.booking.AddBookingRequestDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseForOwnerDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.response.MetaResponse;
import com.sangto.rental_car_server.response.Response;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    Booking verifyBookingOwner(Integer ownerId, Integer bookingId);

    Booking verifyBookingCustomer(Integer customerId, Integer bookingId);

    MetaResponse<MetaResponseDTO, List<BookingResponseDTO>> getAllBookings(MetaRequestDTO requestDTO);

    MetaResponse<MetaResponseDTO, List<BookingResponseDTO>> getListBookingForUser(MetaRequestDTO requestDTO, Integer userId);

    MetaResponse<MetaResponseDTO, List<BookingResponseForOwnerDTO>> getListBookingByCarId(MetaRequestDTO requestDTO, Integer carId, Integer userId);

    Response<BookingDetailResponseDTO> getDetailBooking(Integer bookingId);

    Response<BookingDetailResponseDTO> addBooking(Integer userId, AddBookingRequestDTO requestDTO);

    Response<String> confirmBooking(Integer userId, Integer bookingId);

    Response<String> confirmBring(Integer bookingId);

    Response<String> confirmPickup(Integer bookingId);

    Response<String> confirmPayment(Integer userId, Integer bookingId);

    Response<String> cancelBooking(Integer userId, Integer bookingId);

    Response<String> returnCar(Integer userId, Integer bookingId);

    List<Booking> findOverdueBookings(LocalDateTime overdueTime);

    void updateBookingStatus(Integer bookingId);

    List<Booking> getCompletedBookings(Integer ownerId);
}
