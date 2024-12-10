package com.manager.boats.rental.boats_rental.web.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MarinDto {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 15)
    private String name;

    @NotBlank(message = "Lastname is required")
    @Size(min = 4, max = 20)
    private String lastname;

    @NotBlank(message = "DNI is required")
    @Size(min = 8, max = 8)
    private String dni;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
}

