package com.sangto.rental_car_server.domain.dto.location;

public record LocationRequestDTO(
        String address,
        String city,
        String district,
        String ward,
        Double latitude,
        Double longitude) {
}
