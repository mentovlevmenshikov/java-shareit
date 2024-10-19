package ru.practicum.shareit.booking.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalDateTime;

public class StartBeforEndValidator implements ConstraintValidator<StartBeforeEnd, Object>  {

    private String startDateField;
    private String endDateField;

    public void initialize(StartBeforeEnd constraintAnnotation) {
        this.startDateField = constraintAnnotation.startField();
        this.endDateField = constraintAnnotation.endField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object startDateValue = new BeanWrapperImpl(value).getPropertyValue(startDateField);
        Object endDateValue = new BeanWrapperImpl(value).getPropertyValue(endDateField);

        if (startDateValue instanceof LocalDateTime && endDateValue instanceof LocalDateTime) {
            return ((LocalDateTime)startDateValue).isBefore((LocalDateTime)endDateValue);
        } else {
            return false;
        }

    }
}
