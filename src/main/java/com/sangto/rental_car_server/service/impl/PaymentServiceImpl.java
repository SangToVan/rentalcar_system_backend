package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.constant.MetaConstant;
import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.domain.dto.meta.SortingDTO;
import com.sangto.rental_car_server.domain.dto.payment.FilterPaymentByTimeRequestDTO;
import com.sangto.rental_car_server.domain.dto.payment.PaymentResponseDTO;
import com.sangto.rental_car_server.domain.entity.Payment;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.mapper.PaymentMapper;
import com.sangto.rental_car_server.exception.AppException;
import com.sangto.rental_car_server.repository.PaymentRepository;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.response.MetaResponse;
import com.sangto.rental_car_server.service.PaymentService;
import com.sangto.rental_car_server.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class PaymentServiceImpl implements PaymentService {

    private final UserRepository userRepo;
    private final PaymentRepository paymentRepo;
    private final TransactionService transactionService;
    private final PaymentMapper paymentMapper;

    @Override
    public MetaResponse<MetaResponseDTO, List<PaymentResponseDTO>> getListByUserId(Integer userId, MetaRequestDTO requestDTO, FilterPaymentByTimeRequestDTO filterDTO) {
        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("User not exists");

        String sortField = requestDTO.sortField();
        if (sortField.compareTo(MetaConstant.Sorting.DEFAULT_FIELD) == 0) sortField = "payment_id";
        Sort sort = requestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(requestDTO.sortField()).ascending()
                : Sort.by(requestDTO.sortField()).descending();
        Pageable pageable = PageRequest.of(requestDTO.currentPage(), requestDTO.pageSize(), sort);

        Page<Payment> page = paymentRepo.getListByUserId(userId, filterDTO.start_date(), filterDTO.end_date(), pageable);
        if (page.isEmpty()) throw new AppException("No payment found");

        List<PaymentResponseDTO> list = page.getContent().stream()
                .map(paymentMapper::toPaymentResponseDTO)
                .toList();

        return MetaResponse.successfulResponse(
                "Get list payment success",
                MetaResponseDTO.builder()
                        .totalItems((int) page.getTotalElements())
                        .totalPages(page.getTotalPages())
                        .currentPage(requestDTO.currentPage())
                        .pageSize(requestDTO.pageSize())
                        .sorting(SortingDTO.builder()
                                .sortField(requestDTO.sortField())
                                .sortDir(requestDTO.sortDir())
                                .build())
                        .build(), list
        );

    }
}
