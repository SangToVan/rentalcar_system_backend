package com.sangto.rental_car_server.controller;

import com.sangto.rental_car_server.constant.Endpoint;
import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.domain.dto.payment.FilterPaymentByTimeRequestDTO;
import com.sangto.rental_car_server.domain.dto.payment.PaymentResponseDTO;
import com.sangto.rental_car_server.response.MetaResponse;
import com.sangto.rental_car_server.service.PaymentService;
import com.sangto.rental_car_server.utility.AuthUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Payments")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping(Endpoint.V1.Payment.BASE)
    public ResponseEntity<MetaResponse<MetaResponseDTO, List<PaymentResponseDTO>>> getListByUserId(
            @ParameterObject MetaRequestDTO metaRequestDTO,
            @ParameterObject FilterPaymentByTimeRequestDTO filterRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(paymentService.getListByUserId(
                        AuthUtil.getRequestedUser().getId(), metaRequestDTO, filterRequestDTO));
    }
}
