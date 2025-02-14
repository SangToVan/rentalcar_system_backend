package com.sangto.rental_car_server.domain.dto.payment;

import com.sangto.rental_car_server.domain.enums.EPaymentMethod;
import com.sangto.rental_car_server.domain.enums.EPaymentType;
import lombok.Builder;

import java.util.Date;

@Builder
public record AddPaymentRequestDTO(
        Integer booking_id,
        Double amount,
        EPaymentType paymentType,
        EPaymentMethod paymentMethod,
        Date transaction_date
) {
}
