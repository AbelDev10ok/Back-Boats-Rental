package com.manager.boats.rental.boats_rental.configuration.security.filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGrentesAuthorityJson {

    @JsonCreator
    public SimpleGrentesAuthorityJson(@JsonProperty("authority") String role) {
        
    }
}
