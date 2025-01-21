package com.sangto.rental_car_server.validator;

import com.sangto.rental_car_server.annotation.PasswordMatch;
import com.sangto.rental_car_server.domain.dto.user.AddUserRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, AddUserRequestDTO> {

    @Override
    public boolean isValid(AddUserRequestDTO value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value.password() != null && value.password().equals(value.confirm_password());
    }
}
