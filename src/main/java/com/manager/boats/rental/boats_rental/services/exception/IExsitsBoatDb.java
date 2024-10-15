package com.manager.boats.rental.boats_rental.services.exception;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExsitsBoatDbValidator.class)
public @interface IExsitsBoatDb {

    String message() default "Already exists in Db";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
