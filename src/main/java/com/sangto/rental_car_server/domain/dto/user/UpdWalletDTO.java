package com.sangto.rental_car_server.domain.dto.user;

import com.sangto.rental_car_server.domain.enums.EPaymentType;
import jakarta.validation.constraints.Min;
import lombok.Builder;

@Builder
public record UpdWalletDTO(
        EPaymentType type,
        @Min(value = 100000)
        Double money
) {
}
