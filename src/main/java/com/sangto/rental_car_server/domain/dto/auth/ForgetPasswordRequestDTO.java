package com.sangto.rental_car_server.domain.dto.auth;

import lombok.Builder;

@Builder
public record ForgetPasswordRequestDTO(
        String email
) {
}
