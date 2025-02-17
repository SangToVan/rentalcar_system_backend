package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Wallet;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    Wallet findByUserId(Integer userId);

    Wallet findByUserRole_Admin(EUserRole userRole);

    List<Wallet> findAllByUserId(Integer userId);
}
