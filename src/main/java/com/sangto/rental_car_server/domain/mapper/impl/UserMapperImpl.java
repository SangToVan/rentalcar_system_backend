package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.auth.RegisterUserResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.AddUserRequestDTO;
import com.sangto.rental_car_server.domain.dto.user.UpdUserRequestDTO;
import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.UserResponseDTO;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import com.sangto.rental_car_server.domain.mapper.LocationMapper;
import com.sangto.rental_car_server.domain.mapper.UserMapper;
import com.sangto.rental_car_server.utility.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final JwtTokenUtil jwtTokenUtil;
    private final LocationMapper locationMapper;

    @Override
    public RegisterUserResponseDTO toRegisterUserResponseDTO(User entity) {
        return RegisterUserResponseDTO.builder()
                .access_token(jwtTokenUtil.generateToken(entity))
                .userDetail(toUserDetailResponseDTO(entity))
                .build();
    }

    @Override
    public UserDetailResponseDTO toUserDetailResponseDTO(User entity) {
        return UserDetailResponseDTO.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .birthday(entity.getBirthday())
                .citizen_id(entity.getCitizen_id())
                .phone_number(entity.getPhone_number())
                .location(locationMapper.toLocationResponseDTO(entity.getLocation()))
                .driving_license(entity.getDriving_license())
                .wallet(entity.getWallet())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .status(entity.getStatus())
                .avatar(entity.getAvatar())
                .role(entity.getRole())
                .build();
    }

    @Override
    public UserResponseDTO toUserResponseDTO(User entity) {
        return UserResponseDTO.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .phone_number(entity.getPhone_number())
                .wallet(entity.getWallet())
                .status(entity.getStatus())
                .avatar(entity.getAvatar())
                .role(entity.getRole())
                .build();
    }

    @Override
    public User addUserRequestDTOtoUserEntity(AddUserRequestDTO requestDTO) {
        return User.builder()
                .email(requestDTO.email())
                .username(requestDTO.username())
                .full_name(requestDTO.username())
                .phone_number(requestDTO.phone_number())
                .role(EUserRole.valueOf(requestDTO.role()))
                .birthday(null)
                .citizen_id(null)
                .location(null)
                .driving_license(null)
                .created_at(new Date())
                .updated_at(null)
                .status(null)
                .wallet(0.0)
                .build();
    }

    @Override
    public User updateUserRequestDTOtoUserEntity(User oldUser, UpdUserRequestDTO requestDTO) {

        oldUser.setUsername(requestDTO.username());
        oldUser.setBirthday(requestDTO.birthday());
        oldUser.setCitizen_id(requestDTO.citizen_id());
        oldUser.setAvatar(requestDTO.avatar());
        oldUser.setPhone_number(requestDTO.phone_number());
        oldUser.setLocation(locationMapper.locationRequestDTOtoLocationEntity(requestDTO.location()));
        oldUser.setDriving_license(requestDTO.driving_license());

        return oldUser;
    }
}
