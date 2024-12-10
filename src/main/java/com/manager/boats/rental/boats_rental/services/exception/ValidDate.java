package com.manager.boats.rental.boats_rental.services.exception;


import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidDateValidator.class)
@Documented
public @interface ValidDate {
    String message() default "Fecha inválida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}