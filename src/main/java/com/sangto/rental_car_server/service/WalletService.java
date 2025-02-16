package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.wallet.WalletResponseDTO;
import com.sangto.rental_car_server.response.Response;

public interface WalletService {

    Response<WalletResponseDTO> getWalletByUserId(Integer userId);

}
