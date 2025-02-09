package com.sangto.rental_car_server.domain.mapper;

import com.sangto.rental_car_server.domain.dto.car.*;
import com.sangto.rental_car_server.domain.entity.Car;

public interface CarMapper {

    CarResponseDTO toCarResponseDTO(Car entity);

    CarDetailResponseDTO toCarDetailResponseDTO(Car entity);

    CarDetailResponseForOwnerDTO toCarDetailResponseForOwnerDTO(Car entity);

    Car addCarRequestDTOtoCarEntity(AddCarRequestDTO requestDTO);

    Car updateCarRequestDTOtoCarEntity(Car old_car, UpdCarRequestDTO requestDTO);
}
