package com.sangto.rental_car_server.domain.dto.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sangto.rental_car_server.annotation.RentalTimeMatching;
import com.sangto.rental_car_server.constant.TimeFormatConstant;
import org.springframework.web.bind.annotation.RequestParam;

@RentalTimeMatching(start_date = "start_date", end_date = "end_date")
public record FilterTransactionByTimeRequestDTO(
        @RequestParam(name = "start_date", required = false) @JsonFormat(pattern = TimeFormatConstant.DATE_FORMAT)
        String start_date,
        @RequestParam(name = "end_date", required = false) @JsonFormat(pattern = TimeFormatConstant.DATE_FORMAT)
        String end_date
) {
}
