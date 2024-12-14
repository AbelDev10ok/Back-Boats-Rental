package com.manager.boats.rental.boats_rental.web.controller.dto;

import com.manager.boats.rental.boats_rental.persistence.models.BoatType;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BoateRequest {
    @NotNull(message = "Tuition is required")
    private Long tuition;

    @NotNull(message = "Type is required")
    @Enumerated(EnumType.STRING)
    private BoatType type;

    @NotNull(message = "Ability is required")
    private Long ability;
    
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Model is required")
    private String model;
    
    @NotNull(message = "Price hours is required")
    @Min(value = 1, message = "Price hours must be greater than 0")
    @Column(name="price_for_day")
    private Long priceHours;

    

    public BoateRequest() {
    }

    
    public BoateRequest(Long tuition, BoatType type, Long ability, String name, String model, Long priceHours) {
        this.tuition = tuition;
        this.type = type;
        this.ability = ability;
        this.name = name;
        this.model = model;
        this.priceHours = priceHours;
    }


    public Long getTuition() {
        return tuition;
    }
    public void setTuition(Long tuition) {
        this.tuition = tuition;
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
    public Long getPriceHours() {
        return priceHours;
    }
    public void setPriceHours(Long priceHours) {
        this.priceHours = priceHours;
    }

    
}
