package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EPaymentType {
    RENTAL_FEE("Rental Fee"),
    SERVICE_FEE("Service Fee"),
    TOTAL_FEE("Total Fee"),
    TRANSFER_TO_OWNER("Transfer to Owner"),
    REFUND("Refund"),
    ADDITIONAL_FEE("Additional Fee");

    private final String title;
}
