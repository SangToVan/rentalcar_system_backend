package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.constant.MetaConstant;
import com.sangto.rental_car_server.domain.dto.car.*;
import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.domain.dto.meta.SortingDTO;
import com.sangto.rental_car_server.domain.entity.Car;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import com.sangto.rental_car_server.domain.mapper.CarMapper;
import com.sangto.rental_car_server.exception.AppException;
import com.sangto.rental_car_server.repository.CarRepository;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.response.MetaResponse;
import com.sangto.rental_car_server.response.Response;
import com.sangto.rental_car_server.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final UserRepository userRepo;
    private final CarRepository carRepo;
    private final CarMapper carMapper;


    @Override
    public Car verifyCarOwner(Integer ownerId, Integer carId) {
        Optional<Car> carOpt = carRepo.findById(carId);
        if (carOpt.isEmpty()) throw new AppException("This car is not existed");
        Car car = carOpt.get();
        if (car.getCar_owner().getId() != ownerId) throw new AppException("This car is not owned");
        return car;
    }

    @Override
    public MetaResponse<MetaResponseDTO, List<CarResponseDTO>> getListCarByOwner(MetaRequestDTO requestDTO, Integer ownerId) {
        Optional<User> userOpt = userRepo.findById(ownerId);
        if (userOpt.isEmpty()) throw new AppException("This owner is not existed");

        Sort sort = requestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(requestDTO.sortField()).ascending()
                : Sort.by(requestDTO.sortField()).descending();
        Pageable pageable = PageRequest.of(requestDTO.currentPage(), requestDTO.pageSize(), sort);
        Page<Car> page = requestDTO.keyword() == null
                ? carRepo.getListCarByOwner(ownerId, pageable)
                : carRepo.getListCarByOwnerWithKeyword(ownerId, requestDTO.keyword(), pageable);

        if (page.getContent().isEmpty()) throw new AppException("List car is empty");
        List<CarResponseDTO> list = page.getContent().stream()
                .map(carMapper::toCarResponseDTO)
                .toList();

        return MetaResponse.successfulResponse(
                "Get list car success",
                MetaResponseDTO.builder()
                        .totalItems((int) page.getTotalElements())
                        .totalPages(page.getTotalPages())
                        .currentPage(requestDTO.currentPage())
                        .pageSize(requestDTO.pageSize())
                        .sorting(SortingDTO.builder()
                                .sortField(requestDTO.sortField())
                                .sortDir(requestDTO.sortDir())
                                .build())
                        .build(), list
        );
    }

    @Override
    public Response<CarDetailResponseDTO> getCarDetail(Integer carId) {
        Optional<Car> carOpt = carRepo.findById(carId);
        if (carOpt.isEmpty()) throw new AppException("This car is not existed");
        return Response.successfulResponse(
                "Get detail car successful", carMapper.toCarDetailResponseDTO(carOpt.get())
        );
    }

    @Override
    public Response<CarDetailResponseForOwnerDTO> getCarDetailForOwner(Integer carId) {
        Optional<Car> carOpt = carRepo.findById(carId);
        if (carOpt.isEmpty()) throw new AppException("This car is not existed");
        return Response.successfulResponse(
                "Get detail car successful", carMapper.toCarDetailResponseForOwnerDTO(carOpt.get())
        );
    }

    @Override
    public Response<CarDetailResponseDTO> addCar(Integer ownerId, AddCarRequestDTO requestDTO) {
        Optional<User> ownerOpt = userRepo.findUserByIdAndUserType(ownerId, EUserRole.OWNER);
        if (ownerOpt.isEmpty()) throw new AppException("This owner is not existed");

        Car newCar = carMapper.addCarRequestDTOtoCarEntity(requestDTO);
        newCar.setCar_owner(ownerOpt.get());

        Car savedCar = carRepo.save(newCar);
        return Response.successfulResponse("Add new car successful", carMapper.toCarDetailResponseDTO(savedCar));
    }

    @Override
    public Response<CarDetailResponseDTO> updateCar(Integer id, UpdCarRequestDTO requestDTO) {
        Optional<Car> oldCar = carRepo.findById(id);
        if (oldCar.isEmpty()) throw new AppException("This car is not existed");
        Car newCar = carMapper.updateCarRequestDTOtoCarEntity(oldCar.get(), requestDTO);
        newCar.setCar_owner(oldCar.get().getCar_owner());
        Car savedCar = carRepo.save(newCar);

        return Response.successfulResponse("Update car successful", carMapper.toCarDetailResponseDTO(savedCar));
    }

    @Override
    public MetaResponse<MetaResponseDTO, List<CarResponseDTO>> getAllCars(MetaRequestDTO requestDTO) {
        Sort sort = requestDTO.sortDir().equalsIgnoreCase(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(requestDTO.sortField()).ascending()
                : Sort.by(requestDTO.sortField()).descending();
        Pageable pageable = PageRequest.of(requestDTO.currentPage(), requestDTO.pageSize(), sort);
        Page<Car> page = carRepo.findAll(pageable);

        if (page.isEmpty()) throw new AppException("List car is empty");

        List<CarResponseDTO> carList = page.getContent().stream()
                .map(carMapper::toCarResponseDTO)
                .toList();

        return MetaResponse.successfulResponse(
                "Get list car success",
                MetaResponseDTO.builder()
                        .totalItems((int) page.getTotalElements())
                        .totalPages(page.getTotalPages())
                        .currentPage(requestDTO.currentPage())
                        .pageSize(requestDTO.pageSize())
                        .sorting(SortingDTO.builder()
                                .sortField(requestDTO.sortField())
                                .sortDir(requestDTO.sortDir())
                                .build())
                        .build(), carList
        );
    }

    @Override
    public Response<String> changeStatus(Integer carId) {
        return null;
    }

    @Override
    public MetaResponse<MetaResponseDTO, List<CarResponseDTO>> searchCar(String address, String startTime, String endTime, MetaRequestDTO requestDTO) {
        return null;
    }
}
