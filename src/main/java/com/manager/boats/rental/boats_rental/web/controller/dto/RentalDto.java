package com.manager.boats.rental.boats_rental.web.controller.dto;

public class RentalDto {
    private String dateInit;
    private String dateEnd;
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
