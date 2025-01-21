package com.sangto.rental_car_server.annotation;

import com.sangto.rental_car_server.validator.PasswordMatchValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = PasswordMatchValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PasswordMatch {
    String message() default "Password and confirm password must match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
