package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.constant.MetaConstant;
import com.sangto.rental_car_server.domain.dto.auth.LoginResponseDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.domain.dto.meta.SortingDTO;
import com.sangto.rental_car_server.domain.dto.user.AddUserRequestDTO;
import com.sangto.rental_car_server.domain.dto.user.UpdUserRequestDTO;
import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.UserResponseDTO;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.entity.Wallet;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import com.sangto.rental_car_server.domain.enums.ECarStatus;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import com.sangto.rental_car_server.domain.enums.EUserStatus;
import com.sangto.rental_car_server.domain.mapper.UserMapper;
import com.sangto.rental_car_server.exception.AppException;
import com.sangto.rental_car_server.repository.BookingRepository;
import com.sangto.rental_car_server.repository.CarRepository;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.repository.WalletRepository;
import com.sangto.rental_car_server.response.MetaResponse;
import com.sangto.rental_car_server.response.Response;
import com.sangto.rental_car_server.service.UserService;
import com.sangto.rental_car_server.utility.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepo;
    private final WalletRepository walletRepo;
    private final JwtTokenUtil jwtTokenUtil;
    private final BookingRepository bookingRepo;
    private final CarRepository carRepo;


    @Override
    public Response<UserDetailResponseDTO> getUserDetail(Integer id) {

        Optional<User> findUser = userRepo.findById(id);
        if (findUser.isEmpty()) throw new AppException("This user is not existed");

        return Response.successfulResponse(
                "Get detail user successfully", userMapper.toUserDetailResponseDTO(findUser.get()));
    }

    @Override
    public Response<LoginResponseDTO> addUser(AddUserRequestDTO requestDTO) {

        Optional<User> findUser = userRepo.findByEmail(requestDTO.email());
        if (findUser.isPresent()) throw new AppException("Email already existed");

        User newUser = userMapper.addUserRequestDTOtoUserEntity(requestDTO);

        Wallet wallet = Wallet.builder()
                .user(newUser)
                .amount(0.0)
                .build();
        walletRepo.save(wallet);
        newUser.setWallet(wallet);

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

        Optional<User> findOldUser = userRepo.findById(id);
        if (findOldUser.isEmpty()) throw new AppException("This user is not existed");

        User oldUser = findOldUser.get();

        User newUser = userMapper.updateUserRequestDTOtoUserEntity(oldUser, requestDTO);

        try {
            User saveUser = userRepo.save(newUser);
            return Response.successfulResponse("Update user successfully", userMapper.toUserDetailResponseDTO(saveUser));
        } catch (Exception e) {
            throw new AppException("Update user unsuccessfully");
        }
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {

        Optional<User> findUser = userRepo.findById(id);
        if (findUser.isEmpty()) throw new AppException("This user is not existed");
        User user = findUser.get();

        if (user.getStatus() == EUserStatus.INACTIVE) throw new AppException("This user is inactive");

        user.setStatus(EUserStatus.INACTIVE);

        bookingRepo.findAllByCustomerId(user.getId()).forEach(booking -> {
            booking.setStatus(EBookingStatus.CANCELLED);
            bookingRepo.save(booking);
        });

        carRepo.getListCarByOwner(user.getId()).forEach(car -> {
            car.setStatus(ECarStatus.INACTIVE);
            carRepo.save(car);
        });
        userRepo.save(user);
    }

    @Override
    public MetaResponse<MetaResponseDTO, List<UserResponseDTO>> getAllUsers(MetaRequestDTO requestDTO) {
        Sort sort = requestDTO.sortDir().equalsIgnoreCase(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(requestDTO.sortField()).ascending()
                : Sort.by(requestDTO.sortField()).descending();

        Pageable pageable = PageRequest.of(requestDTO.currentPage(), requestDTO.pageSize(), sort);

        Page<User> page = userRepo.findAllExcludingRole(EUserRole.ADMIN, pageable);

        if (page.isEmpty()) throw new AppException("No users found");

        List<UserResponseDTO> list = page.getContent().stream()
                .map(user -> UserResponseDTO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .phone_number(user.getPhone_number())
                        .wallet(user.getWallet().getAmount())
                        .avatar(user.getAvatar())
                        .status(user.getStatus())
                        .role(user.getRole())
                        .build())
                .toList();

        return MetaResponse.successfulResponse(
                "Retrieved all users successfully",
                MetaResponseDTO.builder()
                        .totalItems((int) page.getTotalElements())
                        .totalPages(page.getTotalPages())
                        .currentPage(requestDTO.currentPage())
                        .pageSize(requestDTO.pageSize())
                        .sorting(SortingDTO.builder()
                                .sortField(requestDTO.sortField())
                                .sortDir(requestDTO.sortDir())
                                .build())
                        .build(),
                list
        );
    }

    @Override
    public User getUserById(Integer id) {
        Optional<User> findUser = userRepo.findById(id);
        if (findUser.isEmpty()) throw new AppException("This user is not existed");
        return findUser.get();
    }
}
