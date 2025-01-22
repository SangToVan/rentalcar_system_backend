package com.sangto.rental_car_server.domain.dto.car;

import com.sangto.rental_car_server.domain.dto.image.ImageResponseDTO;
import com.sangto.rental_car_server.domain.dto.location.LocationDTO;

import java.util.List;

public record CarResponseDTO(
        Integer id,
        String name,
        String brand,
        String model,
        LocationDTO location,
        Double rating,
        String status,
        Double price_per_day,
        List<ImageResponseDTO> images
) {
}
