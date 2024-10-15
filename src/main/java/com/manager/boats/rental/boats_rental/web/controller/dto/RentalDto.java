package com.manager.boats.rental.boats_rental.web.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public class RentalDto {
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",message="Format date invalid, yyyy-MM-dd")
    private String dateInit;
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",message="Format date invalid, yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String dateEnd;
    @Min(1)
    private Long hours;
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
    
    
}
