package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.auth.ChangePasswordRequestDTO;
import com.sangto.rental_car_server.domain.dto.auth.ForgetPasswordRequestDTO;
import com.sangto.rental_car_server.domain.dto.auth.LoginRequestDTO;
import com.sangto.rental_car_server.domain.dto.auth.LoginResponseDTO;
import com.sangto.rental_car_server.response.Response;

public interface AuthenticationService {

    Response<LoginResponseDTO> login(LoginRequestDTO requestDTO);

    Response<String> changePassword(Integer userId, ChangePasswordRequestDTO requestDTO);

    Response<String> forgetPassword(ForgetPasswordRequestDTO requestDTO);
}
