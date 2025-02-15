package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.domain.dto.transaction.AddTransactionRequestDTO;
import com.sangto.rental_car_server.domain.dto.transaction.FilterTransactionByTimeRequestDTO;
import com.sangto.rental_car_server.domain.dto.transaction.TransactionResponseDTO;
import com.sangto.rental_car_server.response.MetaResponse;
import com.sangto.rental_car_server.response.Response;

import java.util.List;

public interface TransactionService {

    MetaResponse<MetaResponseDTO, List<TransactionResponseDTO>> getListByUserId(Integer userId, MetaRequestDTO requestDTO, FilterTransactionByTimeRequestDTO filterDTO);

    Response<TransactionResponseDTO> createTransaction(AddTransactionRequestDTO requestDTO);

    Response<TransactionResponseDTO> depositTransaction(AddTransactionRequestDTO requestDTO);

    Response<TransactionResponseDTO> withdrawTransaction(AddTransactionRequestDTO requestDTO);
}
