package com.sangto.rental_car_server.domain.dto.transaction;

import com.sangto.rental_car_server.domain.enums.ETransactionType;
import lombok.Builder;

import java.util.Date;

@Builder
public record AddTransactionRequestDTO(
        Integer userId,
        Double amount,
        ETransactionType type,
        String description,
        Date transaction_date
) {
}
