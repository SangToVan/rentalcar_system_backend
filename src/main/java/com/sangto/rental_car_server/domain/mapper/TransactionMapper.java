package com.sangto.rental_car_server.domain.mapper;

import com.sangto.rental_car_server.domain.dto.transaction.TransactionRequestDTO;
import com.sangto.rental_car_server.domain.dto.transaction.TransactionResponseDTO;
import com.sangto.rental_car_server.domain.entity.Transaction;

public interface TransactionMapper {

    Transaction toTransactionEntity(TransactionRequestDTO requestDTO);

    TransactionResponseDTO toTransactionResponseDTO(Transaction entity);
}
