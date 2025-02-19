package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query(
            value = "SELECT * FROM transaction t "
                    + "JOIN wallet w ON w.wallet_id = b.wallet_id "
                    + "JOIN user u ON w.user_id = u.user_id "
                    + "AND ((:startTime IS NULL) OR (:endTime IS NULL) OR "
                    + "(t.created_at <= :endTime AND t.created_at >= :startTime))",
            nativeQuery = true)
    Page<Transaction> getListByUserId(@Param("userId") Integer userId,
                                      @Param("startTime") String startDate,
                                      @Param("endTime") String endDate, Pageable pageable);
}
