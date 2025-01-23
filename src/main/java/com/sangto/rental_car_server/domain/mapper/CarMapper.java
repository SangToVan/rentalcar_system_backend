package com.sangto.rental_car_server.domain.mapper;

import com.sangto.rental_car_server.domain.dto.car.AddCarRequestDTO;
import com.sangto.rental_car_server.domain.dto.car.CarDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.car.CarResponseDTO;
import com.sangto.rental_car_server.domain.dto.car.UpdCarRequestDTO;
import com.sangto.rental_car_server.domain.entity.Car;

public interface CarMapper {

    CarResponseDTO toCarResponseDTO(Car entity);

    CarDetailResponseDTO toCarDetailResponseDTO(Car entity);

    Car addCarRequestDTOoCarEntity(AddCarRequestDTO requestDTO);

    Car updateCarRequestDTOtoCarEntity(Car old_car, UpdCarRequestDTO requestDTO);
}
