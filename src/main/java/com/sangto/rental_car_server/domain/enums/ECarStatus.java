package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ECarStatus {
    UNVERIFIED("Unverified"),
    INACTIVE("Inactive"),
    ACTIVE("Active");
    private final String title;
}
