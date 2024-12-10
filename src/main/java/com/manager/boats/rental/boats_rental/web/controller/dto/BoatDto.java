package com.manager.boats.rental.boats_rental.web.controller.dto;

import com.manager.boats.rental.boats_rental.persistence.models.BoatType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public class BoatDto {
    private Long tuition;
    @NotNull
    @Enumerated(EnumType.STRING)
    private BoatType type;
    private Long ability;
    private String name;
    private String model;
    private Long priceHours;
    private boolean enabled;    
    
    
    public BoatDto() {
    }
    
    public BoatDto(Long tuition,BoatType type, Long ability, String name, String model,Long priceHours, boolean enabled) {
        this.tuition = tuition;
        this.type = type;
        this.ability = ability;
        this.name = name;
        this.model = model;
        this.priceHours = priceHours;
        this.enabled = enabled;
    }

    


    public BoatType getType() {
        return type;
    }
    public void setType(BoatType type) {
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

    public Long getTuition() {
        return tuition;
    }

    public void setTuition(Long tuition) {
        this.tuition = tuition;
    }

    public Long getPriceHours() {
        return priceHours;
    }

    public void setPriceHours(Long priceHours) {
        this.priceHours = priceHours;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }    
    
}
