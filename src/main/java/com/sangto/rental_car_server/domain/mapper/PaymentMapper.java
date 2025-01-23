package com.sangto.rental_car_server.domain.mapper;

import com.sangto.rental_car_server.domain.dto.payment.PaymentResponseDTO;
import com.sangto.rental_car_server.domain.entity.Payment;

public interface PaymentMapper {
    PaymentResponseDTO toPaymentResponseDTO(Payment entity);
}
