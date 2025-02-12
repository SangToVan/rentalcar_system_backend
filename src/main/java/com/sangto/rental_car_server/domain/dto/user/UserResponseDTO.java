package com.sangto.rental_car_server.domain.dto.user;

import com.sangto.rental_car_server.domain.enums.EUserRole;
import com.sangto.rental_car_server.domain.enums.EUserStatus;
import lombok.Builder;

@Builder
public record UserResponseDTO(
        Integer id,
        String username,
        String email,
        String phone_number,
        Double wallet,
        EUserStatus status,
        String avatar,
        EUserRole role
) {
}
