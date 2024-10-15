package com.manager.boats.rental.boats_rental.services.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.manager.boats.rental.boats_rental.services.interfaces.IMarinServices;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistsMarinDb implements ConstraintValidator<IExistsMarinDb,String>{
    @Autowired
    private IMarinServices marinServices;
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(marinServices==null){
            return true;
        }
        
        return !marinServices.existsMarin(value);
    }
}
