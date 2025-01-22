package com.sangto.rental_car_server.domain.dto.feedback;

public record AddFeedbackRequestDTO(
        Integer rating,
        String comment
) {
}
