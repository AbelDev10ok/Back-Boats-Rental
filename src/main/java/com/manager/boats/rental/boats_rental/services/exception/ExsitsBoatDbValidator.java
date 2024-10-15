package com.manager.boats.rental.boats_rental.services.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.manager.boats.rental.boats_rental.services.interfaces.IBoatServices;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component 
public class ExsitsBoatDbValidator implements ConstraintValidator<IExsitsBoatDb,Long> {
    @Autowired
    private IBoatServices boatServices;
    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if(boatServices==null){
            return true;
        }
        return !boatServices.existsByTuition(value);
    }
}
