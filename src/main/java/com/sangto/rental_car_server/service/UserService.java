package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.auth.LoginResponseDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.AddUserRequestDTO;
import com.sangto.rental_car_server.domain.dto.user.UpdUserRequestDTO;
import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.UserResponseDTO;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.response.MetaResponse;
import com.sangto.rental_car_server.response.Response;

import java.util.List;

public interface UserService {

    Response<UserDetailResponseDTO> getUserDetail(Integer id);

    Response<LoginResponseDTO> addUser(AddUserRequestDTO requestDTO);

    Response<UserDetailResponseDTO> updateUser(Integer id, UpdUserRequestDTO requestDTO);

    void deleteUser(Integer id);

    MetaResponse<MetaResponseDTO, List<UserResponseDTO>> getAllUsers(MetaRequestDTO requestDTO);

    User getUserById(Integer id);
}
