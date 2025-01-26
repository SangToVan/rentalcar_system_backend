package com.sangto.rental_car_server.domain.dto.booking;

import com.sangto.rental_car_server.domain.dto.car.CarDetailResponseDTO;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import com.sangto.rental_car_server.domain.enums.EPaymentMethod;
import lombok.Builder;

@Builder
public record BookingDetailResponseDTO(
        Integer booking_id,
        CarDetailResponseDTO car_detail,
        User customer,
        String start_date,
        String end_date,
        EPaymentMethod paymentMethod,
        EBookingStatus status,
        Double deposit,
        Double total_price
) {
}
