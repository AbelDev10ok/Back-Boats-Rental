package com.manager.boats.rental.boats_rental.services.exception;

public class AlreadyExistsException extends RuntimeException{
    public AlreadyExistsException(String message) {
        super(message);
    }

}
