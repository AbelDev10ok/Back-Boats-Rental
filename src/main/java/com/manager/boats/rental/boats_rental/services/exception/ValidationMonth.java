package com.manager.boats.rental.boats_rental.services.exception;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidationMonth implements ConstraintValidator<IValidationMonth, String> {

    @Override
    public boolean isValid(String dateStr, ConstraintValidatorContext context) {
        if (dateStr == null) {
            return true; // o false si la fecha es obligatoria
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false); // Crucial para un parseo estricto
            Date date = sdf.parse(dateStr);
            int month = date.getMonth() + 1; // getMonth() devuelve 0-11, sumamos 1
            int day = date.getDate()+1;
            int year = date.getYear()+1900;
            if(year < 1900 || year > 2100) return false;

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // Obtiene el máximo de días del mes
            if (day < 1 || day > maxDays) {
                return false;
            }
            if(month < 1 || month > 12){
                return false;
            }


            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}