package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.domain.dto.payment.FilterPaymentByTimeRequestDTO;
import com.sangto.rental_car_server.domain.dto.payment.PaymentResponseDTO;
import com.sangto.rental_car_server.response.MetaResponse;

import java.util.List;

public interface PaymentService {

    MetaResponse<MetaResponseDTO, List<PaymentResponseDTO>> getListByUserId(Integer userId, MetaRequestDTO requestDTO, FilterPaymentByTimeRequestDTO filterDTO);
}
