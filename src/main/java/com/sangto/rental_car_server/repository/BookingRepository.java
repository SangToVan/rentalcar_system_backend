package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Page<Booking> getListBookingByUserId(Integer userId, Pageable pageable);

    Page<Booking> getListBookingByUserIdWithKeyword(Integer userId, String keyword, Pageable pageable);

    Page<Booking> getListBookingByCarId(Integer carId, Pageable pageable);

    List<Booking> findAllByUserId(Integer userId);

    List<Booking> findByStatusAndStartDateTime(EBookingStatus status, LocalDateTime startDateTime);

    List<Booking> findByStatusAndEndDateTime(EBookingStatus status, LocalDateTime endDateTime);

    List<Booking> findByCarOwnerAndBookingStatus(Integer carOwner, EBookingStatus bookingStatus);

    List<Booking> findActiveBookingByCarId(Integer carId);

    List<Booking> findFutureBookingByCarId(Integer carId);

    int countBookingInMonth(LocalDateTime startOfMonth, LocalDateTime endOfMonth);




}
