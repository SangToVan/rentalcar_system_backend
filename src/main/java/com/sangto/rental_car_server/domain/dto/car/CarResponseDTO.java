package com.sangto.rental_car_server.domain.dto.car;

import com.sangto.rental_car_server.domain.dto.image.ImageResponseDTO;
import com.sangto.rental_car_server.domain.dto.location.LocationRequestDTO;

import java.util.List;

public record CarResponseDTO(
        Integer id,
        String name,
        String brand,
        String model,
        LocationRequestDTO location,
        Double rating,
        String status,
        Double price_per_day,
        List<ImageResponseDTO> images
) {
}
