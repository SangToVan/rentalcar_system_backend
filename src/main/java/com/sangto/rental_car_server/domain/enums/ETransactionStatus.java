package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ETransactionStatus {
    PENDING("Pending"),
    COMPLETED("Completed"),
    FAILED("Failed");

    private final String title;
}
