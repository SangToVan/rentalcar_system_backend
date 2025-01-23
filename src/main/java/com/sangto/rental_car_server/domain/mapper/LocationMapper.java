package com.sangto.rental_car_server.domain.mapper;

import com.sangto.rental_car_server.domain.dto.location.LocationRequestDTO;
import com.sangto.rental_car_server.domain.dto.location.LocationResponseDTO;
import com.sangto.rental_car_server.domain.entity.Location;

public interface LocationMapper {

    LocationResponseDTO toLocationResponseDTO(Location entity);

    Location locationRequestDTOtoLocationEntity(LocationRequestDTO requestDTO);
}
