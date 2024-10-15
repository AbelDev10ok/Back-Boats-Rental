package com.manager.boats.rental.boats_rental.services.exception;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistsMarinDb.class)

public @interface IExistsMarinDb{
    String message() default "Already exists in Db";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
