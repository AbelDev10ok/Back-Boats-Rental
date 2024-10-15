package com.manager.boats.rental.boats_rental.persistence.models;

import org.hibernate.validator.constraints.UniqueElements;

import com.manager.boats.rental.boats_rental.services.exception.IExsitsBoatDb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table
public class Boat {
    @Id
    @NotNull(message = "Tuition is required")
    @IExsitsBoatDb
    private Long tuition;

    @NotBlank(message = "Type is required")
    private String type;
    
    @NotNull(message = "Ability is required")
    private Long ability;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Model is required")
    private String model;
    
    @NotBlank(message = "State is required")    
    private String state;
    
    @NotNull(message = "Price hours is required")
    @Min(value = 1, message = "Price hours must be greater than 0")
    @Column(name="price_hours")
    private Long priceHours;
    
    //the boats have one marin
    @ManyToOne
    @JoinColumn(name = "marin_id")//fk
    private Marin marin;


    public Boat() {
    }
    
    public Boat(Long tuition,String type, Long ability, String name, String model, String state, Long priceHours) {
        this.tuition = tuition;
        this.type = type;
        this.ability = ability;
        this.name = name;
        this.model = model;
        this.state = state;
        this.priceHours = priceHours;
    }

    public Long getTuition() {
        return tuition;
    }
    public void setTuition(Long tuition) {
        this.tuition = tuition;
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
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public Long getPriceHours() {
        return priceHours;
    }  
    public void setPriceHours(Long priceHours) {
        this.priceHours = priceHours;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tuition == null) ? 0 : tuition.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((model == null) ? 0 : model.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Boat other = (Boat) obj;
        if (tuition == null) {
            if (other.tuition != null)
                return false;
        } else if (!tuition.equals(other.tuition))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (model == null) {
            if (other.model != null)
                return false;
        } else if (!model.equals(other.model))
            return false;
        return true;
    }


}
