package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    Page<Transaction> getListByUserId(Integer userId, String start_date, String end_date, Pageable pageable);
}
