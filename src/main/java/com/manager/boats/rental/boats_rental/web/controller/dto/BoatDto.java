package com.manager.boats.rental.boats_rental.web.controller.dto;

public class BoatDto {
    private Long tuition;
    private String type;
    private Long ability;
    private String name;
    private String model;
    private Long priceHours;
    
    
    
    public BoatDto() {
    }
    
    public BoatDto(Long tuition,String type, Long ability, String name, String model,Long priceHours) {
        this.tuition = tuition;
        this.type = type;
        this.ability = ability;
        this.name = name;
        this.model = model;
        this.priceHours = priceHours;
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
    
}
