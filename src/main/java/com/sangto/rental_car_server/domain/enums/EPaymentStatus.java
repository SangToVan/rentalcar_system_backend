package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EPaymentStatus {
    HELD("Held"),
    RELEASED("Released"),
    REFUNDED("Refunded");

    private final String title;
}
