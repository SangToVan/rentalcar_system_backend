package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.auth.LoginResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.AddUserRequestDTO;
import com.sangto.rental_car_server.domain.dto.user.UpdUserRequestDTO;
import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.response.Response;

public interface UserService {

    Response<UserDetailResponseDTO> getUserDetail(Integer id);

    Response<LoginResponseDTO> addUser(AddUserRequestDTO requestDTO);

    Response<UserDetailResponseDTO> updateUser(Integer id, UpdUserRequestDTO requestDTO);
}
