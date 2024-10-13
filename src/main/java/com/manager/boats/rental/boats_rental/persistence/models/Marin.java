package com.manager.boats.rental.boats_rental.persistence.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Marin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastname;
    
    @OneToMany(mappedBy = "marin")
    // one marin have many boats
    List<Boat> boats;

    public Marin() {
        this.boats = new ArrayList<>();
    }

    
    public Marin(Long id, String name, String lastname) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
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

    

}
