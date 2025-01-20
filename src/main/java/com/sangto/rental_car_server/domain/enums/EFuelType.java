package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EFuelType {
    PETRO("Petro"),
    DIESEL("Diesel");

    private final String title;
}
