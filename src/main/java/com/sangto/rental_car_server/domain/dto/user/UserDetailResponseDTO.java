package com.sangto.rental_car_server.domain.dto.user;

import com.sangto.rental_car_server.constant.TimeFormatConstant;
import com.sangto.rental_car_server.domain.dto.location.LocationRequestDTO;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

public record UserDetailResponseDTO(
        Integer id,
        String username,
        String email,
        @Temporal(TemporalType.DATE) @DateTimeFormat(pattern = TimeFormatConstant.DATE_FORMAT)
        LocalDateTime birthday,
        String citizen_id,
        String phone_number,
        LocationRequestDTO location,
        String driving_license,
        Double wallet,
        Date created_at,
        Date updated_at,
        String status,
        String avatar,
        EUserRole role
) {
}
