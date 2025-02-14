package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.wallet.WalletResponseDTO;
import com.sangto.rental_car_server.domain.entity.Wallet;
import com.sangto.rental_car_server.domain.mapper.WalletMapper;
import org.springframework.stereotype.Component;

@Component
public class WalletMapperImpl implements WalletMapper {
    @Override
    public WalletResponseDTO toWalletResponseDTO(Wallet entity) {
        return null;
    }
}
