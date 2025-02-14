package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ETransactionType {
    CREDIT("Credit"),
    DEBIT("Debit");

    private final String title;
}
