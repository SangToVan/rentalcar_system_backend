package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Payment;
import com.sangto.rental_car_server.domain.enums.EPaymentStatus;
import com.sangto.rental_car_server.domain.enums.EPaymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    Page<Payment> getListByUserId(Integer userId, String startDate, String endDate, Pageable pageable);

    Page<Payment> getListByBookingId(Integer bookingId, Pageable pageable);

    List<Payment> getListByBookingAndStatus(Integer bookingId, EPaymentStatus status);

    Optional<Payment> getByBookingAndType(Integer bookingId, EPaymentType type);


}
