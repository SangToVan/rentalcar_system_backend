package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Wallet;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    @Query("SELECT w FROM Wallet w " + "JOIN FETCH  w.user u " + "WHERE u.id = :userId")
    Wallet findByUserId(@Param(("userId")) Integer userId);

    @Query("SELECT w FROM Wallet w " + "JOIN FETCH  w.user u " + "WHERE u.role = :userRole")
    Wallet findByUserRole_Admin(@Param(("userRole")) EUserRole userRole);

    List<Wallet> findAllByUserId(Integer userId);
}
