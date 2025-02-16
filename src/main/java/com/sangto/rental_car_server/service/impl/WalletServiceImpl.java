package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.wallet.WalletResponseDTO;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.entity.Wallet;
import com.sangto.rental_car_server.exception.AppException;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.repository.WalletRepository;
import com.sangto.rental_car_server.response.Response;
import com.sangto.rental_car_server.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepo;
    private final UserRepository userRepo;

    @Override
    public Response<WalletResponseDTO> getWalletByUserId(Integer userId) {
        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("User not exist");

        Wallet wallet = walletRepo.findByUserId(findUser.get().getId());
        return Response.successfulResponse(
                "Get wallet successful",
                WalletResponseDTO.builder()
                        .amount(wallet.getAmount())
                        .updated_at(wallet.getUpdated_at())
                        .build()
        );
    }

}
