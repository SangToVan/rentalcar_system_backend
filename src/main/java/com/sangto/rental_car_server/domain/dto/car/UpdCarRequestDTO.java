package com.sangto.rental_car_server.domain.dto.car;

import com.sangto.rental_car_server.domain.dto.image.UpdImageRequestDTO;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdCarRequestDTO(
        Float fuel_consumption,
        Integer mileage,
        String additional_functions,
        List<UpdImageRequestDTO> images,
        String description,
        String terms_of_use,
//        LocationRequestDTO location,
        String address,
        Double price_per_day
) {
}
