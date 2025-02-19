package com.sangto.rental_car_server.domain.dto.user;

import com.sangto.rental_car_server.constant.TimeFormatConstant;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import com.sangto.rental_car_server.domain.enums.EUserStatus;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
public record UserDetailResponseDTO(
        Integer id,
        String username,
        String email,
        @Temporal(TemporalType.DATE) @DateTimeFormat(pattern = TimeFormatConstant.DATE_FORMAT)
        LocalDateTime birthday,
        String citizen_id,
        String phone_number,
//        LocationResponseDTO location,
        String address,
        String driving_license,
        Double wallet,
        Date created_at,
        Date updated_at,
        EUserStatus status,
        String avatar,
        EUserRole role
) {
}
