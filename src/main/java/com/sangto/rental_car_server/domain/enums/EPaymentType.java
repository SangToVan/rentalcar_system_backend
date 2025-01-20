package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EPaymentType {
    TOP_UP("Top up"),
    WITHDRAW("Withdraw"),
    PAY_DEPOSIT("Pay deposit"),
    RECEIVE_DEPOSIT("Receive deposit"),
    REFUND_DEPOSIT("Refund deposit"),
    OFFSET_FINAL_PAYMENT("Offset final payment");

    private final String title;
}
