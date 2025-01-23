package com.sangto.rental_car_server.domain.dto.car;

import com.sangto.rental_car_server.domain.dto.image.UpdImageRequestDTO;
import com.sangto.rental_car_server.domain.dto.location.LocationRequestDTO;

import java.util.List;

public record UpdCarRequestDTO(
        Float fuel_consumption,
        Integer mileage,
        String additional_functions,
        List<UpdImageRequestDTO> images,
        String description,
        String terms_of_use,
        LocationRequestDTO location,
        Double price_per_day
) {
}
