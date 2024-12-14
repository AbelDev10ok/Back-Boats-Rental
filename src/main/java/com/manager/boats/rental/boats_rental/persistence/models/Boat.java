package com.manager.boats.rental.boats_rental.persistence.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table
public class Boat {

    @Id
    // @IExsitsBoatDb(message="Boat already exists") // gestiono que sea unico a nivel de api back
    @Column(unique = true)//gestiono que sea unico a nivel de base de datos
    private Long tuition;
    @Enumerated(EnumType.STRING)
    private BoatType type;    
    private Long ability;
    private String name;
    private String model;
    @Column(name = "enabled") // Nombre de la columna y valor por defecto
    private boolean enabled;
    
    @NotNull(message = "Price hours is required")
    @Min(value = 1, message = "Price hours must be greater than 0")
    @Column(name="price_for_day")
    private Long priceHours;
    
    // los botes tienen un marin
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "marin_id")//fk
    private Marin marin;

    @OneToMany(mappedBy = "boat")
    @JsonManagedReference // otra forma de solucionar bucle ,se coloca en hijo en este caso boat
    private List<Rental> rentals;


    public Boat() {
        this.enabled = true;
    }
    
    public Boat(Long tuition,BoatType type, Long ability, String name, String model,Long priceHours) {
        this.tuition = tuition;
        this.type = type;
        this.ability = ability;
        this.name = name;
        this.model = model;
        this.enabled = true;
        this.priceHours = priceHours;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public Marin getMarin() {
        return marin;
    }

    public void setMarin(Marin marin) {
        this.marin = marin;
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

    
    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
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
