package com.sangto.rental_car_server.domain.dto.user;

import com.sangto.rental_car_server.annotation.EnumValid;
import com.sangto.rental_car_server.annotation.PasswordMatch;
import com.sangto.rental_car_server.annotation.StrongPassword;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
@PasswordMatch
public record AddUserRequestDTO(
    @StrongPassword
    String password,
    String confirm_password,
    String username,
    String email,
    @Pattern(regexp = "^0[0-9]{7,}$", message = "Phone number must start with 0 and contain at least 8 digits")
    String phone_number,
    @EnumValid(name = "role", enum_class = EUserRole.class)
    String role
) {
}
