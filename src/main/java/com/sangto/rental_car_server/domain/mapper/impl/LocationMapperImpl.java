package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.location.LocationRequestDTO;
import com.sangto.rental_car_server.domain.dto.location.LocationResponseDTO;
import com.sangto.rental_car_server.domain.entity.Location;
import com.sangto.rental_car_server.domain.mapper.LocationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationMapperImpl implements LocationMapper {
    @Override
    public LocationResponseDTO toLocationResponseDTO(Location entity) {
        return null;
    }

    @Override
    public Location locationRequestDTOtoLocationEntity(LocationRequestDTO requestDTO) {
        return null;
    }
}
