package com.sangto.rental_car_server.validator;

import com.sangto.rental_car_server.annotation.RentalTimeMatching;
import com.sangto.rental_car_server.utility.TimeUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class RentalTimeMatchingValidator implements ConstraintValidator<RentalTimeMatching, String> {

    String start_date;
    String end_date;

    @Override
    public void initialize(RentalTimeMatching constraintAnnotation) {
        this.start_date = constraintAnnotation.start_date();
        this.end_date = constraintAnnotation.end_date();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String start_date_value = (String) new BeanWrapperImpl(value).getPropertyValue(start_date);
        String end_date_value = (String) new BeanWrapperImpl(value).getPropertyValue(end_date);
        return TimeUtil.convertToDateTime(end_date_value).isAfter(TimeUtil.convertToDateTime(start_date_value));
    }
}
