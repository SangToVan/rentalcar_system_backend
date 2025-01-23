package com.sangto.rental_car_server.domain.mapper;

import com.sangto.rental_car_server.domain.dto.maintenance.AddMaintenanceRequestDTO;
import com.sangto.rental_car_server.domain.dto.maintenance.MaintenanceResponseDTO;
import com.sangto.rental_car_server.domain.entity.Maintenance;

public interface MaintenanceMapper {

    MaintenanceResponseDTO toMaintenanceResponseDTO(Maintenance entity);

    Maintenance addMaintenanceRequestDTOtoMaintenanceEntity(AddMaintenanceRequestDTO requestDTO);
}
