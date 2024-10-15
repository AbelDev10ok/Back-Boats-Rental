package com.manager.boats.rental.boats_rental.persistence.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Table
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="date_init")
    private Date dateInit;
    @Column(name="date_end")
    private Date dateEnd;
    private String state;//pend,conf,canc
    private Long hours;
    @Column(name="total_hours")
    private Long total;

    @ManyToOne
    @JoinColumn(name = "user_id")//fk
    private Users user;

    @ManyToOne
    @JoinColumn(name = "boat_id")//fk
    private Boat boat;
    
    public Rental() {
    }


    public Rental(Date dateInit, Date dateEnd, String state, Long hours, Long total) {
        this.dateInit = dateInit;
        this.dateEnd = dateEnd;
        this.state = state;
        this.hours = hours;
        this.total = total;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getHours() {
        return hours;
    }

    public void setHours(Long hours) {
        this.hours = hours;
    }

    public Long getTotalHours() {
        return total;
    } 

    public void setTotalHours(Long total) {
        this.total = total;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Boat getBoat() {
        return boat;
    }

    public void setBoat(Boat boat) {
        this.boat = boat;
    }

    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dateInit == null) ? 0 : dateInit.hashCode());
        result = prime * result + ((dateEnd == null) ? 0 : dateEnd.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = prime * result + ((boat == null) ? 0 : boat.hashCode());
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
        Rental other = (Rental) obj;
        if (dateInit == null) {
            if (other.dateInit != null)
                return false;
        } else if (!dateInit.equals(other.dateInit))
            return false;
        if (dateEnd == null) {
            if (other.dateEnd != null)
                return false;
        } else if (!dateEnd.equals(other.dateEnd))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        if (boat == null) {
            if (other.boat != null)
                return false;
        } else if (!boat.equals(other.boat))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return "Rental [dateInit=" + dateInit + ", dateEnd=" + dateEnd + ", state=" + state + ", hours="
                + hours + ", total=" + total + ", user=" + user + ", boat=" + boat + "]";
    }

    

}
