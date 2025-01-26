package com.sangto.rental_car_server.domain.dto.location;

import lombok.Builder;

@Builder
public record LocationResponseDTO(
        Integer id,
        String address,
        String city,
        String district,
        String ward,
        Double latitude,
        Double longitude
) {
}
