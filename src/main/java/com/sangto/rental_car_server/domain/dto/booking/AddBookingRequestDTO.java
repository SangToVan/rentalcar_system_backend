package com.sangto.rental_car_server.domain.dto.booking;

import com.sangto.rental_car_server.domain.enums.EPaymentMethod;

public record AddBookingRequestDTO(
        Integer car_id,
        Integer customer_id,
        EPaymentMethod paymentMethod,
        String start_date,
        String end_date
) {
}
