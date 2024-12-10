package com.manager.boats.rental.boats_rental.persistence.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

import com.manager.boats.rental.boats_rental.services.exception.IExistsMarinDb;

@Entity
@Table
public class Marin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name is required")
    @Size(min = 3,max = 15)
    private String name;
    @NotBlank(message = "Lastname is required")
    @Size(min=4,max = 20)
    private String lastname;
    @NotBlank(message = "Name is required")
    @Size(min = 8,max = 8)
    @IExistsMarinDb(message = "Dni already exists")
    private String dni;
    
    @OneToMany(mappedBy = "marin")
    // one marin have many boats
    List<Boat> boats;

    public Marin() {
        this.boats = new ArrayList<>();
    }

    public Marin(String name, String lastname, String dni) {
        this.name = name;
        this.lastname = lastname;
        this.dni = dni;
        this.boats = new ArrayList<>();
    }




    public Marin(Long id, String name, String lastname, String dni) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.dni = dni;
        this.boats = new ArrayList<>();
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

    public List<Boat> getBoats() {
        return boats;
    }


    public void setBoats(List<Boat> boats) {
        this.boats = boats;
    }


    @Override
    public String toString() {
        return "Marin [id=" + id + ", name=" + name + ", lastname=" + lastname + ", dni=" + dni + ", boats=" + boats
                + "]";
    }

    

}
