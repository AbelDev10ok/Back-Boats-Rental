package com.manager.boats.rental.boats_rental.web.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manager.boats.rental.boats_rental.persistence.models.EstateRental;
import com.manager.boats.rental.boats_rental.services.exception.IValidationMonth;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public class RentalDto {
    @IValidationMonth
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",message="Format date invalid, yyyy-MM-dd")
    private String dateInit;
    @IValidationMonth
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",message="Format date invalid, yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String dateEnd;
    @Min(1)
    private Long hours;
    @Enumerated(EnumType.STRING)
    private EstateRental state;//pend,conf,canc
    @JsonIgnore
    private String confirmationToken;
    @JsonIgnore
    private boolean confirmed = false; // Valor por defecto: false

    

    public RentalDto() {
    }
    public RentalDto(String dateInit, String dateEnd, Long hours) {
        this.dateInit = dateInit;
        this.dateEnd = dateEnd;
        this.hours = hours;
    }
    public String getDateInit() {
        return dateInit;
    }
    public void setDateInit(String dateInit) {
        this.dateInit = dateInit;
    }
    public String getDateEnd() {
        return dateEnd;
    }
    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }
    public Long getHours() {
        return hours;
    }
    public void setHours(Long hours) {
        this.hours = hours;
    }


    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
    public EstateRental getState() {
        return state;
    }
    public void setState(EstateRental state) {
        this.state = state;
    }
    
    
}
