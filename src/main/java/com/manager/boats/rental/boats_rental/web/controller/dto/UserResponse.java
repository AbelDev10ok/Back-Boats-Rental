package com.manager.boats.rental.boats_rental.web.controller.dto;


public class UserResponse {
    private String name;
    private String lastname;    
    private String email;
    private String phoneNumber;
    private String addres;

    public UserResponse() {
    }
    
    public UserResponse(String name, String lastname, String email, String phoneNumber, String addres) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.addres = addres;
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

    @Override
    public String toString() {
        return "UserResponse [name=" + name + ", lastname=" + lastname + ", email=" + email + ", phoneNumber="
                + phoneNumber + ", addres=" + addres + "]";
    }

}
