package com.manager.boats.rental.boats_rental.persistence.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manager.boats.rental.boats_rental.services.exception.IExistsUserByEmail;

import java.util.HashSet;


@Entity
@Table
public class Users{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String password;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String addres;
    
    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = jakarta.persistence.CascadeType.ALL,orphanRemoval = true)
    private Set<Rental> rentals;

    public Users() {
        this.rentals = new HashSet<>();
    }

    public Users(String name, String lastname, String email, String password, String phoneNumber, String addres) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.addres = addres;
        this.rentals = new HashSet<>();
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String passwrod) {
        this.password = passwrod;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getAddres() {
        return addres;
    }
    public void setAddres(String addres) {
        this.addres = addres;
    }

    public Set<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(Set<Rental> rentals) {
        this.rentals = rentals;
    }

    @Override
    public String toString() {
        return "Users [id=" + id + ", name=" + name + ", lastname=" + lastname + ", email=" + email + ", password="
                + password + ", phoneNumber=" + phoneNumber + ", addres=" + addres + ", rentals=" + rentals + "]";
    }

}
