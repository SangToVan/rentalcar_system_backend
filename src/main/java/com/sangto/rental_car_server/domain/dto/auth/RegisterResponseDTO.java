package com.sangto.rental_car_server.domain.dto.auth;

import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;

public record RegisterResponseDTO(
        String access_token,
        UserDetailResponseDTO userDetailResponseDTO
) {
}
