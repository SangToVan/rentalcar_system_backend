package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.transaction.AddTransactionRequestDTO;
import com.sangto.rental_car_server.domain.dto.transaction.TransactionResponseDTO;
import com.sangto.rental_car_server.domain.entity.Transaction;
import com.sangto.rental_car_server.domain.mapper.TransactionMapper;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapperImpl implements TransactionMapper {
    @Override
    public Transaction toTransactionEntity(AddTransactionRequestDTO requestDTO) {
        return null;
    }

    @Override
    public TransactionResponseDTO toTransactionResponseDTO(Transaction entity) {
        return null;
    }
}
