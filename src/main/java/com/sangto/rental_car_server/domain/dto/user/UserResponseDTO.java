package com.sangto.rental_car_server.domain.dto.user;

import com.sangto.rental_car_server.domain.enums.EUserRole;

public record UserResponseDTO(
        Integer id,
        String username,
        String email,
        String phone_number,
        Double wallet,
        String avatar,
        String status,
        EUserRole role
) {
}
