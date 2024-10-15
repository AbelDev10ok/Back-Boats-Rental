package com.manager.boats.rental.boats_rental.services.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.manager.boats.rental.boats_rental.services.interfaces.IUserServices;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistsUserDb implements ConstraintValidator<IExistsUserByEmail,String> {
    
    @Autowired
    private IUserServices userServices;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(userServices==null){
            return true;
        }
        return !userServices.existsUser(value);
    }
}
