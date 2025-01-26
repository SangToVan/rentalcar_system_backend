package com.sangto.rental_car_server.domain.dto.complaint;

import lombok.Builder;

@Builder
public record AddComplaintRequestDTO(
        String description
) {
}
