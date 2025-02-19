package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Payment;
import com.sangto.rental_car_server.domain.enums.EPaymentStatus;
import com.sangto.rental_car_server.domain.enums.EPaymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Query(
            value = "SELECT * FROM payment p "
                    + "JOIN booking b ON p.booking_id = b.booking_id "
                    + "JOIN user u ON b.customer_id = u.user_id "
                    + "AND ((:startTime IS NULL) OR (:endTime IS NULL) OR "
                    + "(t.created_at <= :endTime AND t.created_at >= :startTime))",
            nativeQuery = true)
    Page<Payment> getListByUserId(@Param("userId") Integer userId,
                                  @Param("startTime") String startDate,
                                  @Param("endTime") String endDate, Pageable pageable);
    @Query("SELECT DISTINCT p FROM Payment p " + "JOIN fetch  p.booking b " + "WHERE b.id = :bookingId")
    Page<Payment> getListByBookingId(@Param("bookingId") Integer bookingId, Pageable pageable);

    List<Payment> findAllByBookingIdAndStatus(Integer bookingId, EPaymentStatus status);

    Optional<Payment> findByBookingIdAndType(Integer bookingId, EPaymentType type);




}
