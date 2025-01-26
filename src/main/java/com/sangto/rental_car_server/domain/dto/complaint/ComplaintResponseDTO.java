package com.sangto.rental_car_server.domain.dto.complaint;

import com.sangto.rental_car_server.constant.TimeFormatConstant;
import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.domain.entity.User;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Builder
public record ComplaintResponseDTO(
        Booking booking,
        String description,
        String status,
        @Temporal(TemporalType.TIMESTAMP) @DateTimeFormat(pattern = TimeFormatConstant.DATETIME_FORMAT)
        Date created_at
) {
}
