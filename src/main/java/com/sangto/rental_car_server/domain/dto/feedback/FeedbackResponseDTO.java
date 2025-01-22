package com.sangto.rental_car_server.domain.dto.feedback;

import com.sangto.rental_car_server.constant.TimeFormatConstant;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public record FeedbackResponseDTO(
        String avatar,
        String username,
        Integer rating,
        String comment,
        @Temporal(TemporalType.TIMESTAMP) @DateTimeFormat(pattern = TimeFormatConstant.DATETIME_FORMAT)
        Date created_at,
        List<String> car_image,
        String car_name,
        String start_date,
        String end_date
) {
}
