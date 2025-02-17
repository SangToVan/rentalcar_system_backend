package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("SELECT b FROM Booking b " + "JOIN FETCH b.car c " + "JOIN FETCH b.customer cus " + "WHERE cus.id = :userId")
    Page<Booking> getListBookingByUserId(@Param("userId")Integer userId, Pageable pageable);

    @Query("SELECT b FROM Booking b " + "JOIN FETCH b.car c "
            + "JOIN FETCH b.customer u "
            + "JOIN FETCH b.payments p "
            + "WHERE u.id = :userId AND CONCAT(b.status,' ',p.payment_method) LIKE %:keyword%")
    Page<Booking> getListBookingByUserIdWithKeyword(@Param("userId")Integer userId,
                                                    @Param("keyword")String keyword,
                                                    Pageable pageable);

    @Query("SELECT b FROM Booking b " + "JOIN FETCH b.car c " + "JOIN FETCH b.customer cus " + "WHERE c.id = :carId")
    Page<Booking> getListBookingByCarId(@Param("carId")Integer carId, Pageable pageable);

    List<Booking> findAllByUserId(Integer userId);

    @Query("SELECT b FROM Booking b WHERE b.status = :status AND b.start_date < :startDate")
    List<Booking> findByStatusAndStartDateTime(@Param("status")EBookingStatus status, @Param("startDate")LocalDateTime startDateTime);

    @Query("SELECT b FROM Booking b WHERE b.status = :status AND b.end_date > :endDate")
    List<Booking> findByStatusAndEndDateTime(@Param("status")EBookingStatus status, @Param("endDate")LocalDateTime endDateTime);

    @Query("SELECT b FROM Booking b WHERE b.car.car_owner.id = :ownerId AND b.status = :status")
    List<Booking> findByCarOwnerAndBookingStatus(@Param("ownerId")Integer carOwner, @Param("status")EBookingStatus bookingStatus);

    @Query("SELECT b FROM Booking b WHERE b.car.id = :carId AND b.start_date <= :now AND b.end_date > :now AND b.status = 'CONFIRMED'")
    List<Booking> findActiveBookingByCarId(@Param("carId")Integer carId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.car.id = :carId AND b.start_date > :now AND b.status = 'CONFIRMED'")
    List<Booking> findFutureBookingByCarId(@Param("carId")Integer carId, @Param("now") LocalDateTime now);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.start_date BETWEEN :startOfMonth AND :endOfMonth")
    int countBookingInMonth(@Param("startOfMonth")LocalDateTime startOfMonth, @Param("endOfMonth")LocalDateTime endOfMonth);




}
