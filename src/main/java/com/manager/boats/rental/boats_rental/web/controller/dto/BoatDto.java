package com.manager.boats.rental.boats_rental.web.controller.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

public class BoatDto {
    private String type;
    private Long ability;
    private String name;
    private String model;
    
    
    
    public BoatDto() {
    }
    
    public BoatDto(String type, Long ability, String name, String model) {
        this.type = type;
        this.ability = ability;
        this.name = name;
        this.model = model;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Long getAbility() {
        return ability;
    }
    public void setAbility(Long ability) {
        this.ability = ability;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    
}
