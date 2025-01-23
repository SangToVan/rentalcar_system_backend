package com.sangto.rental_car_server.domain.mapper;

import com.sangto.rental_car_server.domain.dto.complaint.AddComplaintRequestDTO;
import com.sangto.rental_car_server.domain.dto.complaint.ComplaintResponseDTO;
import com.sangto.rental_car_server.domain.entity.Complaint;

public interface ComplaintMapper {
    ComplaintResponseDTO toComplaintResponseDTO(Complaint entity);

    Complaint addComplaintRequestDTOtoComplaintEntity(AddComplaintRequestDTO requestDTO);
}
