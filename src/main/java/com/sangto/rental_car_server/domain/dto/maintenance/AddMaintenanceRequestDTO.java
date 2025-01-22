package com.sangto.rental_car_server.domain.dto.maintenance;

import com.sangto.rental_car_server.constant.TimeFormatConstant;
import org.springframework.format.annotation.DateTimeFormat;

public record AddMaintenanceRequestDTO(
        @DateTimeFormat(pattern = TimeFormatConstant.DATE_FORMAT)
        String maintenance_date,
        String document,
        Double price
) {
}
