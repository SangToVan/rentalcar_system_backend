package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.auth.ChangePasswordRequestDTO;
import com.sangto.rental_car_server.domain.dto.auth.ForgetPasswordRequestDTO;
import com.sangto.rental_car_server.domain.dto.auth.LoginRequestDTO;
import com.sangto.rental_car_server.domain.dto.auth.LoginResponseDTO;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.mapper.UserMapper;
import com.sangto.rental_car_server.exception.AppException;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.response.Response;
import com.sangto.rental_car_server.service.AuthenticationService;
import com.sangto.rental_car_server.utility.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepo;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapper userMapper;


    @Override
    public Response<LoginResponseDTO> login(LoginRequestDTO requestDTO) {

        Optional<User> userOpt = userRepo.findByEmail(requestDTO.email());

        if (userOpt.isEmpty())
            throw  new AppException("Email or password is incorrect. Please try again");

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(requestDTO.password(), userOpt.get().getPassword());

        if (authenticated == false)
            throw  new AppException("Email or password is incorrect. Please try again");

        String token = jwtTokenUtil.generateToken(userOpt.get());

        LoginResponseDTO responseDTO = LoginResponseDTO.builder()
                .authenticated(true)
                .token(token)
                .userDetail(userMapper.toUserDetailResponseDTO(userOpt.get()))
                .build();

        return Response.successfulResponse("Login successful", responseDTO);
    }

    @Override
    public Response<String> changePassword(Integer userId, ChangePasswordRequestDTO requestDTO) {

        Optional<User> userOpt = userRepo.findById(userId);
        if (userOpt.isEmpty())
            throw  new AppException("User not found");

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches(requestDTO.old_password(), userOpt.get().getPassword());

        if (authenticated == false)
            throw  new AppException("Current password is incorrect.");

        userOpt.get().setPassword(passwordEncoder.encode(requestDTO.new_password()));
        try {
            userRepo.save(userOpt.get());
            return Response.successfulResponse("Password changed successfully");
        } catch (Exception e) {
            throw  new AppException("Error while changing password");
        }

    }

    @Override
    public Response<String> forgetPassword(ForgetPasswordRequestDTO requestDTO) {

        Optional<User> userOpt = userRepo.findByEmail(requestDTO.email());
        if (userOpt.isEmpty())
            throw  new AppException("This email is not existed");

        User user = userOpt.get();

        String randomPassword_plain = "randomPassword";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String randomPassword_cipher = passwordEncoder.encode(randomPassword_plain);
        user.setPassword(randomPassword_cipher);
        try {
            userRepo.save(user);
        } catch (Exception e) {
            throw  new AppException("Error while reset password");
        }

        return Response.successfulResponse("Password reset successfully");
    }
}
