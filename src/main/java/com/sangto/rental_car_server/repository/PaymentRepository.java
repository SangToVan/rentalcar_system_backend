package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    Page<Payment> getListByUserId(Integer userId, String startDate, String endDate, Pageable pageable);

    Page<Payment> getListByBookingId(Integer bookingId, Pageable pageable);
}
