package com.sangto.rental_car_server.domain.dto.booking;

import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import com.sangto.rental_car_server.domain.enums.EPaymentMethod;

public record BookingResponseForOwnerDTO(
        Integer booking_id,
        User customer_id,
        String start_date,
        String end_date,
        EPaymentMethod paymentMethod,
        EBookingStatus status
) {
}
