package com.mvfiscal.api.taskservice.annotation;

import com.mvfiscal.api.taskservice.validator.OneOfValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OneOfValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface OneOf {

    String message() default "Valor inválido. Os valores permitidos são: {values}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] values();
}

