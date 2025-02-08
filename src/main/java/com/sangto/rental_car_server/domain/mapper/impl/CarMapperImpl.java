package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.car.AddCarRequestDTO;
import com.sangto.rental_car_server.domain.dto.car.CarDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.car.CarResponseDTO;
import com.sangto.rental_car_server.domain.dto.car.UpdCarRequestDTO;
import com.sangto.rental_car_server.domain.entity.Car;
import com.sangto.rental_car_server.domain.mapper.CarMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarMapperImpl implements CarMapper {
    @Override
    public CarResponseDTO toCarResponseDTO(Car entity) {
        return null;
    }

    @Override
    public CarDetailResponseDTO toCarDetailResponseDTO(Car entity) {
        return null;
    }

    @Override
    public Car addCarRequestDTOoCarEntity(AddCarRequestDTO requestDTO) {
        return null;
    }

    @Override
    public Car updateCarRequestDTOtoCarEntity(Car old_car, UpdCarRequestDTO requestDTO) {
        return null;
    }
}
