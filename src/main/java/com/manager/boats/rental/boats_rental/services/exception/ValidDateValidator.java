package com.manager.boats.rental.boats_rental.services.exception;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class ValidDateValidator implements ConstraintValidator<ValidDate, Date> {

    @Override
    public void initialize(ValidDate constraintAnnotation) {
        //No hay inicialización necesaria
    }

    @Override
    public boolean isValid(Date date, ConstraintValidatorContext context) {
        if (date == null) {
            return true; // O false si la fecha es obligatoria
        }

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();

        // Validaciones básicas:
        if (year < 1900 || year > 2100) {  // Rango de años razonable
            return false;
        }
        if (month < 1 || month > 12) {
            return false;
        }

        // Validación de días según el mes:
        int maxDays = localDate.lengthOfMonth(); // Obtiene la cantidad de días del mes, incluyendo años bisiestos
        if (day < 1 || day > maxDays) {
            return false;
        }

        // Validaciones adicionales si son necesarias

        return true;
    }
}
