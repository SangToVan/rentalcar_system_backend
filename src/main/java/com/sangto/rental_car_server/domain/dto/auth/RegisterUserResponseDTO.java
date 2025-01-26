package com.sangto.rental_car_server.domain.dto.auth;

import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import lombok.Builder;

@Builder
public record RegisterUserResponseDTO(
        String access_token,
        UserDetailResponseDTO userDetail
) {
}
