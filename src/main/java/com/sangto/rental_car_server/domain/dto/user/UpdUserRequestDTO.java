package com.sangto.rental_car_server.domain.dto.user;

import com.sangto.rental_car_server.constant.TimeFormatConstant;
import com.sangto.rental_car_server.domain.dto.location.LocationDTO;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record UpdUserRequestDTO(
        String username,
        @Temporal(TemporalType.DATE) @DateTimeFormat(pattern = TimeFormatConstant.DATE_FORMAT)
        LocalDateTime birthday,
        String citizen_id,
        String avatar,
        @Pattern(regexp = "^0[0-9]{7,}$", message = "Phone number must start with 0 and contain at least 8 digits")
        String phone_number,
        LocationDTO location,
        String driving_license
) {
}
