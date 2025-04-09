package com.mvfiscal.api.taskservice.validator;

import com.mvfiscal.api.taskservice.annotation.OneOf;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OneOfValidator implements ConstraintValidator<OneOf, String> {

    private Set<String> allowedValues;
    private String messageTemplate;

    @Override
    public void initialize(OneOf annotation) {
        this.allowedValues = new HashSet<>(Arrays.asList(annotation.values()));
        this.messageTemplate = annotation.message().replace("{values}", allowedValues.toString());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        boolean isValid = allowedValues.contains(value);
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation();
        }

        return isValid;
    }
}
