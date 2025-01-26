package com.sangto.rental_car_server.domain.dto.image;

import lombok.Builder;

@Builder
public record ImageResponseDTO(
        Integer id,
        String image_name,
        String image_url
) {
}
