package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.booking.AddBookingRequestDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseForOwnerDTO;
import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.domain.mapper.BookingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingMapperImpl implements BookingMapper {
    @Override
    public BookingResponseDTO toBookingResponseDTO(Booking entity) {
        return null;
    }

    @Override
    public BookingResponseForOwnerDTO toBookingResponseForOwnerDTO(Booking entity) {
        return null;
    }

    @Override
    public BookingDetailResponseDTO toBookingDetailResponseDTO(Booking entity) {
        return null;
    }

    @Override
    public Booking addBookingRequestDTOtoBookingEntity(AddBookingRequestDTO requestDTO) {
        return null;
    }
}
