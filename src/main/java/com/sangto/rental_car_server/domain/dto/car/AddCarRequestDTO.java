package com.sangto.rental_car_server.domain.dto.car;

import com.sangto.rental_car_server.domain.dto.location.LocationDTO;
import com.sangto.rental_car_server.domain.entity.Location;
import com.sangto.rental_car_server.domain.enums.ECarTransmission;
import com.sangto.rental_car_server.domain.enums.EFuelType;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public record AddCarRequestDTO(
        String plate_number,
        String name,
        String brand,
        String model,
        String color,
        Integer production_year,
        Integer seat_number,
        ECarTransmission car_transmission,
        EFuelType fuel_type,
        Float fuel_consumption,
        Integer mileage,
        String additional_functions,
        String[] images,
        String description,
        String terms_of_use,
        LocationDTO location,
        Double price_per_day,
        Boolean availability,
        String status) {
}
