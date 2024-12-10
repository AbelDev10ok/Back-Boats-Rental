package com.manager.boats.rental.boats_rental.web.controller.dto;

import java.util.List;

public class MarinResponse {
    private Long id;
    private String name;
    private String lastname;
    private String dni;

    private List<BoatDto> boats;

    public MarinResponse() {
    }
    
    public MarinResponse(String name, String lastname, String dni) {
        this.name = name;
        this.lastname = lastname;
        this.dni = dni;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
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
    public List<BoatDto> getBoats() {
        return boats;
    }
    public void setBoats(List<BoatDto> boats) {
        this.boats = boats;
    }

    
}
