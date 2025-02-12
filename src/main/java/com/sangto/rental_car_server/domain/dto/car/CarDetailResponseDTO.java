package com.sangto.rental_car_server.domain.dto.car;

import com.sangto.rental_car_server.domain.dto.image.ImageResponseDTO;
import com.sangto.rental_car_server.domain.dto.location.LocationRequestDTO;
import com.sangto.rental_car_server.domain.enums.ECarStatus;
import com.sangto.rental_car_server.domain.enums.ECarTransmission;
import com.sangto.rental_car_server.domain.enums.EFuelType;
import lombok.Builder;

import java.util.Date;
import java.util.List;

@Builder
public record CarDetailResponseDTO(
        Integer id,
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
        List<ImageResponseDTO> images,
        String description,
        String terms_of_use,
        LocationRequestDTO location,
        Double price_per_day,
        Boolean availability,
        ECarStatus status,
        Double rating,
        Date created_at,
        Date updated_at
) {
}
