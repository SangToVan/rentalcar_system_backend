package com.sangto.rental_car_server.domain.dto.transaction;

import com.sangto.rental_car_server.domain.enums.ETransactionStatus;
import com.sangto.rental_car_server.domain.enums.ETransactionType;
import lombok.Builder;

import java.util.Date;

@Builder
public record TransactionResponseDTO(
        Integer transaction_id,
        Double amount,
        ETransactionType type,
        ETransactionStatus status,
        String description,
        Date transaction_date
) {
}
