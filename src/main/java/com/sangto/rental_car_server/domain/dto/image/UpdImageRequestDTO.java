package com.sangto.rental_car_server.domain.dto.image;

import lombok.Builder;

@Builder
public record UpdImageRequestDTO(
        Integer id,
        String image_url
) {
}
