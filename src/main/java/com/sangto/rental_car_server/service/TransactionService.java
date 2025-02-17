package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.domain.dto.transaction.TransactionRequestDTO;
import com.sangto.rental_car_server.domain.dto.transaction.FilterTransactionByTimeRequestDTO;
import com.sangto.rental_car_server.domain.dto.transaction.TransactionResponseDTO;
import com.sangto.rental_car_server.response.MetaResponse;
import com.sangto.rental_car_server.response.Response;

import java.util.List;

public interface TransactionService {

    MetaResponse<MetaResponseDTO, List<TransactionResponseDTO>> getListByUserId(Integer userId, MetaRequestDTO requestDTO, FilterTransactionByTimeRequestDTO filterDTO);

    MetaResponse<MetaResponseDTO, List<TransactionResponseDTO>> getListByWalletId(Integer walletId, MetaRequestDTO requestDTO, FilterTransactionByTimeRequestDTO filterDTO);

    Response<TransactionResponseDTO> walletTransaction(TransactionRequestDTO requestDTO);

    Response<TransactionResponseDTO> bookingTransaction(TransactionRequestDTO requestDTO);

}
