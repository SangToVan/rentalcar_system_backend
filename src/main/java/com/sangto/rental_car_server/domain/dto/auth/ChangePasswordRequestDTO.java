package com.sangto.rental_car_server.domain.dto.auth;

import com.sangto.rental_car_server.annotation.StrongPassword;
import lombok.Builder;

@Builder
public record ChangePasswordRequestDTO(
        String old_password,
        @StrongPassword
        String new_password
) {
}
