package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.car.*;
import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.domain.entity.Car;
import com.sangto.rental_car_server.response.MetaResponse;
import com.sangto.rental_car_server.response.Response;

import java.util.List;

public interface CarService {

    Car verifyCarOwner(Integer ownerId, Integer carId);

    MetaResponse<MetaResponseDTO, List<CarResponseDTO>> getListCarByOwner(MetaRequestDTO requestDTO, Integer ownerId);

    Response<CarDetailResponseDTO> getCarDetail(Integer carId);

    Response<CarDetailResponseForOwnerDTO> getCarDetailForOwner(Integer carId);

    Response<CarDetailResponseDTO> addCar(Integer ownerId, AddCarRequestDTO requestDTO);

    Response<CarDetailResponseDTO> updateCar(Integer id, UpdCarRequestDTO requestDTO);

    MetaResponse<MetaResponseDTO, List<CarResponseDTO>> getAllCars(MetaRequestDTO requestDTO);

    Response<String> changeStatus(Integer carId);

    Response<String> toggleCarStatus(Integer carId);

    MetaResponse<MetaResponseDTO, List<CarResponseDTO>> searchCar(String address, String startTime, String endTime, MetaRequestDTO requestDTO);
}
