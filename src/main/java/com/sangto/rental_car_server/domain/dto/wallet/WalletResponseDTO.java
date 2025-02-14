package com.sangto.rental_car_server.domain.dto.wallet;

import lombok.Builder;

import java.util.Date;

@Builder
public record WalletResponseDTO(
        Double amount,
        Date updated_at
) {
}
