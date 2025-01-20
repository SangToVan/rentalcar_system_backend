package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EBookingStatus {
    PENDING_DEPOSIT("Pending deposit"),
    CONFIRMED("Confirmed"),
    CANCELLED("Cancelled"),
    PICK_UP("Pick up"),
    IN_PROGRESS("In progress"),
    PENDING_PAYMENT("Pending payment"),
    COMPLETED("Completed");

    private final String title;
}
