package com.sangto.rental_car_server.validator;

import com.sangto.rental_car_server.annotation.EnumValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<EnumValid, String> {

    private String validator_name;
    private String enum_class_name;
    private final String MESSAGE = " must be any of enum ";
    private Enum<?>[] enum_values;

    @Override
    public void initialize(EnumValid enumValid) {
        this.validator_name = enumValid.name();
        this.enum_class_name = enumValid.enum_class().getSimpleName();
        this.enum_values = enumValid.enum_class().getEnumConstants();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        for (Enum<?> e : enum_values) {
            if (e.name().equals(value)) {
                return true;
            }
        }
        context.buildConstraintViolationWithTemplate(this.validator_name + MESSAGE + this.enum_class_name)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }
}
