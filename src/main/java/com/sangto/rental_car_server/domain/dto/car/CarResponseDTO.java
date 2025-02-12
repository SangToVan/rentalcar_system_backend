package com.sangto.rental_car_server.domain.dto.car;

import com.sangto.rental_car_server.domain.dto.image.ImageResponseDTO;
import com.sangto.rental_car_server.domain.dto.location.LocationRequestDTO;
import com.sangto.rental_car_server.domain.enums.ECarStatus;
import lombok.Builder;

import java.util.List;

@Builder
public record CarResponseDTO(
        Integer id,
        String name,
        String brand,
        String model,
        LocationRequestDTO location,
        Double rating,
        ECarStatus status,
        Double price_per_day,
        List<ImageResponseDTO> images
) {
}
