package com.manager.boats.rental.boats_rental.web.controller.dto;

import java.util.List;

import com.manager.boats.rental.boats_rental.persistence.models.BoatType;

public class BoatResponse {
    private Long tuition;
    private BoatType type;
    private Long ability;
    private String name;
    private String model;
    private Long priceHours;

    private MarinResponse marin; // DTO para Marin
    private List<RentalDto> rentals; // Lista de DTOs para Rentals

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
    public MarinResponse getMarin() {
        return marin;
    }
    public void setMarin(MarinResponse marin) {
        this.marin = marin;
    }
    public List<RentalDto> getRentals() {
        return rentals;
    }
    public void setRentals(List<RentalDto> rentals) {
        this.rentals = rentals;
    }

    
}
