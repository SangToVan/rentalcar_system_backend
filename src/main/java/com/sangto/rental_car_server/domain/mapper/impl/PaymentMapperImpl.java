package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.payment.AddPaymentRequestDTO;
import com.sangto.rental_car_server.domain.dto.payment.PaymentResponseDTO;
import com.sangto.rental_car_server.domain.entity.Payment;
import com.sangto.rental_car_server.domain.mapper.PaymentMapper;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapperImpl implements PaymentMapper {
    @Override
    public Payment toPaymentEntity(AddPaymentRequestDTO requestDTO) {
        return null;
    }

    @Override
    public PaymentResponseDTO toPaymentResponseDTO(Payment entity) {
        return null;
    }
}
