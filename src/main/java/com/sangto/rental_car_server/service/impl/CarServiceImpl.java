package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.constant.MetaConstant;
import com.sangto.rental_car_server.domain.dto.car.*;
import com.sangto.rental_car_server.domain.dto.image.UpdImageRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.domain.dto.meta.SortingDTO;
import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.domain.entity.Car;
import com.sangto.rental_car_server.domain.entity.Image;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import com.sangto.rental_car_server.domain.enums.ECarStatus;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import com.sangto.rental_car_server.domain.mapper.CarMapper;
import com.sangto.rental_car_server.exception.AppException;
import com.sangto.rental_car_server.repository.BookingRepository;
import com.sangto.rental_car_server.repository.CarRepository;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.response.MetaResponse;
import com.sangto.rental_car_server.response.Response;
import com.sangto.rental_car_server.service.CarService;
import com.sangto.rental_car_server.service.CloudinaryService;
import com.sangto.rental_car_server.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final UserRepository userRepo;
    private final CarRepository carRepo;
    private final BookingRepository bookingRepo;
    private final CarMapper carMapper;
    private final CloudinaryService cloudinaryService;
    private final ImageService imageService;

    @Value("${cloudinary.folder.car}")
    private String carFolder;

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
                "Get list car for owner success",
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
    @Transactional
    public Response<CarDetailResponseDTO> addCar(Integer ownerId, AddCarRequestDTO requestDTO) {
        Optional<User> ownerOpt = userRepo.findUserByIdAndUserType(ownerId, EUserRole.OWNER);
        if (ownerOpt.isEmpty()) throw new AppException("This owner is not existed");

        Car newCar = carMapper.addCarRequestDTOtoCarEntity(requestDTO);
        newCar.setCar_owner(ownerOpt.get());

        // Set image for Car
        try {
            for (String item : requestDTO.images()) {
                Map resultUpload = cloudinaryService.uploadFileBase64(item, carFolder);
                Image imageUpload = Image.builder()
                        .image_name((String) resultUpload.get("original_filename"))
                        .image_url((String) resultUpload.get("url"))
                        .image_public_id((String) resultUpload.get("public_id"))
                        .created_at(new Date())
                        .build();
                newCar.addImage(imageUpload);
            }
            Car savedCar = carRepo.save(newCar);
            return Response.successfulResponse(
                    "Add new car successful", carMapper.toCarDetailResponseDTO(savedCar)
            );
        } catch (IOException e) {
            throw new AppException("Add new car failed");
        }
    }

    @Override
    public Response<CarDetailResponseDTO> updateCar(Integer id, UpdCarRequestDTO requestDTO) {
        Optional<Car> oldCar = carRepo.findById(id);
        if (oldCar.isEmpty()) throw new AppException("This car is not existed");
        Car newCar = carMapper.updateCarRequestDTOtoCarEntity(oldCar.get(), requestDTO);

        // Update image for car
        if (!requestDTO.images().isEmpty()) {
            for (UpdImageRequestDTO item : requestDTO.images()) {
                imageService.updImage(item, carFolder);
            }
        }
        List<Image> images = newCar.getImages();
        newCar.setImages(images);

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
        Optional<Car> findCar = carRepo.findById(carId);
        if (findCar.isEmpty()) throw new AppException("This car is not existed");

        Car car = findCar.get();
        String message = "Stop renting car successful";
        if (car.getStatus() == ECarStatus.ACTIVE) {
            car.setStatus(ECarStatus.INACTIVE);
        } else if (car.getStatus() == ECarStatus.INACTIVE) {
            car.setStatus(ECarStatus.ACTIVE);
            message = "Re-renting car successful";
        }
        carRepo.save(car);
        return Response.successfulResponse(message);
    }

    @Override
    public Response<String> toggleCarStatus(Integer carId) {

        Optional<Car> findCar = carRepo.findById(carId);
        if (findCar.isEmpty()) throw new AppException("This car is not existed");
        Car car = findCar.get();

        // Check booking
        List<Booking> activeBookings = bookingRepo.findActiveBookingByCarId(carId);
        if (!activeBookings.isEmpty() && car.getStatus() == ECarStatus.INACTIVE) {
            throw new AppException("Cannot deactivate car");
        }

        if (car.getStatus() == ECarStatus.ACTIVE) {
            List<Booking> futureBookings = bookingRepo.findFutureBookingByCarId(carId);
            for (Booking booking : futureBookings) {
                booking.setStatus(EBookingStatus.CANCELLED);
            }
            bookingRepo.saveAll(futureBookings);
        }
        String message = "Stop renting car successful";
        if (car.getStatus() == ECarStatus.ACTIVE) {
            car.setStatus(ECarStatus.INACTIVE);
        } else if (car.getStatus() == ECarStatus.INACTIVE) {
            car.setStatus(ECarStatus.ACTIVE);
            message = "Re-renting car successful";
        }
        carRepo.save(car);
        return Response.successfulResponse(message);
    }

    @Override
    public MetaResponse<MetaResponseDTO, List<CarResponseDTO>> searchCar(String address, String startTime, String endTime, MetaRequestDTO requestDTO) {

        Sort sort = requestDTO.sortDir().equalsIgnoreCase(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(requestDTO.sortField()).ascending()
                : Sort.by(requestDTO.sortField()).descending();
        Pageable pageable = PageRequest.of(requestDTO.currentPage(), requestDTO.pageSize(), sort);
        Page<Car> page = carRepo.searchCar(address, startTime, endTime, pageable);

        if (page.getContent().isEmpty()) throw new AppException("List car is empty");

        List<CarResponseDTO> carList = page.getContent().stream()
                .map(carMapper::toCarResponseDTO)
                .toList();

        return MetaResponse.successfulResponse(
                "Search car success",
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
}
