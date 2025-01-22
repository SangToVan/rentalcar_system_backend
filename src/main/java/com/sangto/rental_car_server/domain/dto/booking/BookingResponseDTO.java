package com.sangto.rental_car_server.domain.dto.booking;

import com.sangto.rental_car_server.domain.enums.EBookingStatus;

import java.util.List;

public record BookingResponseDTO(
        Integer booking_id,
        Integer car_id,
        String car_name,
        Double price_per_day,
        Double total_price,
        Double deposit,
        EBookingStatus status,
        String start_date,
        String end_date,
        List<String> images
) {
}
