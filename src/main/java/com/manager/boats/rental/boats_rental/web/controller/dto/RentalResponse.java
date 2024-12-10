package com.manager.boats.rental.boats_rental.web.controller.dto;

import java.util.Date;

import com.manager.boats.rental.boats_rental.persistence.models.EstateRental;



public class RentalResponse {

    private Long id;
    private Date dateInit;
    private Date dateEnd;
    private EstateRental state;//pend,conf,canc
    private Long hours;
    private Long total;

    private UserResponse user;

    private BoatDto boat;

    public RentalResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateInit() {
        return dateInit;
    }

    public void setDateInit(Date dateInit) {
        this.dateInit = dateInit;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public EstateRental getState() {
        return state;
    }

    public void setState(EstateRental state) {
        this.state = state;
    }

    public Long getHours() {
        return hours;
    }

    public void setHours(Long hours) {
        this.hours = hours;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public BoatDto getBoat() {
        return boat;
    }

    public void setBoat(BoatDto boat) {
        this.boat = boat;
    }

    
}
