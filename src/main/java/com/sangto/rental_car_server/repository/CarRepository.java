package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Integer> {

    Page<Car> getListCarByOwner(Integer ownerId, Pageable pageable);

    Page<Car> getListCarByOwnerWithKeyword(Integer ownerId, String keyword, Pageable pageable);

    Optional<Car> getCarById(Integer id);

    Optional<Car> searchCar(String address, String start_time, String end_time);

    Optional<Car> checkScheduleCar(Integer id, String start_time, String end_time);

    List<Car> getListCarByOwner(Integer ownerId);

    int countAllBy();
}
