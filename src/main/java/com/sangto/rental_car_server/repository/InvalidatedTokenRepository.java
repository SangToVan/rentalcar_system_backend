package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
}
