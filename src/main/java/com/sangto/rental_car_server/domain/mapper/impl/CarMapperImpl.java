package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.car.*;
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
    public CarDetailResponseForOwnerDTO toCarDetailResponseForOwnerDTO(Car entity) {
        return null;
    }

    @Override
    public Car addCarRequestDTOtoCarEntity(AddCarRequestDTO requestDTO) {
        return null;
    }

    @Override
    public Car updateCarRequestDTOtoCarEntity(Car old_car, UpdCarRequestDTO requestDTO) {
        return null;
    }
}
