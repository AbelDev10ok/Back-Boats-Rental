package com.manager.boats.rental.boats_rental.services.exception;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidationMonth.class)
@Documented
public @interface IValidationMonth {
    String message() default "Verifique unda fecha valida YY--MM--DD";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}