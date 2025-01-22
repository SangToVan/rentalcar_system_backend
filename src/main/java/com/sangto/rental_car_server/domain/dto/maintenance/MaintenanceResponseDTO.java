package com.sangto.rental_car_server.domain.dto.maintenance;

public record MaintenanceResponseDTO(
        Integer car_id,
        String created_at,
        String updated_at,
        String maintenance_date,
        String document,
        Double price
) {
}
