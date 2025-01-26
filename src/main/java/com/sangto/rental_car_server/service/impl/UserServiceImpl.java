package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.auth.LoginResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.AddUserRequestDTO;
import com.sangto.rental_car_server.domain.dto.user.UpdUserRequestDTO;
import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.mapper.UserMapper;
import com.sangto.rental_car_server.exception.AppException;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.response.Response;
import com.sangto.rental_car_server.service.UserService;
import com.sangto.rental_car_server.utility.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepo;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public Response<UserDetailResponseDTO> getUserDetail(Integer id) {

        Optional<User> user = userRepo.findById(id);
        if (user.isEmpty()) throw new AppException("This user is not existed");

        return Response.successfulResponse(
                "Get detail user successfully", userMapper.toUserDetailResponseDTO(user.get()));
    }

    @Override
    public Response<LoginResponseDTO> addUser(AddUserRequestDTO requestDTO) {

        Optional<User> user = userRepo.findByEmail(requestDTO.email());
        if (user.isPresent()) throw new AppException("Email already existed");

        User newUser = userMapper.addUserRequestDTOtoUserEntity(requestDTO);

        // Set password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        try {
            User saveUser = userRepo.save(newUser);
            return Response.successfulResponse(
                    "Register successfully",
                    LoginResponseDTO.builder()
                            .authenticated(true)
                            .token(jwtTokenUtil.generateToken(saveUser))
                            .userDetail(userMapper.toUserDetailResponseDTO(saveUser))
                            .build());
        } catch (Exception e) {
            throw new AppException("Register unsuccessfully");
        }
    }

    @Override
    public Response<UserDetailResponseDTO> updateUser(Integer id, UpdUserRequestDTO requestDTO) {

        Optional<User> oldUserOpt = userRepo.findById(id);
        if (oldUserOpt.isEmpty()) throw new AppException("This user is not existed");

        User oldUser = oldUserOpt.get();

        User newUser = userMapper.updateUserRequestDTOtoUserEntity(oldUser, requestDTO);

        try {
            User saveUser = userRepo.save(newUser);
            return Response.successfulResponse("Update user successfully", userMapper.toUserDetailResponseDTO(saveUser));
        } catch (Exception e) {
            throw new AppException("Update user unsuccessfully");
        }
    }
}
