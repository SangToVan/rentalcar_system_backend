package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EUserStatus {
    INACTIVE("Inactive"),
    ACTIVE("Active");
    private final String title;
}
