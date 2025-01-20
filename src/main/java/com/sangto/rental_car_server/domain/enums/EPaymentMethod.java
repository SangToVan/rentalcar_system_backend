package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EPaymentMethod {
    WALLET("Wallet"),
    CASH("Cash"),
    BANK("Bank transfer");

    private final String title;
}
