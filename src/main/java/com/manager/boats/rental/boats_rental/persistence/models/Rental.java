package com.manager.boats.rental.boats_rental.persistence.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
@Table
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)//ESPECIFICO QUE SOLO GUARDO LA FECHA SIN HORAS
    @Column(name="date_init")
    private Date dateInit;
    @Column(name="date_end")
    @Temporal(TemporalType.DATE)
    private Date dateEnd;
    @Enumerated(EnumType.STRING)
    private EstateRental state;//pend,conf,canc
    @Column(name="total")
    private Long total;

    @Column(name = "confirmation_token")
    private String confirmationToken;

    @Column(name = "confirmed")
    private boolean confirmed = false;

    @ManyToOne
    @JoinColumn(name = "user_id")//fk
    private Users user;

    // RENTAS PUEDEN TENER UN BOTE
    @ManyToOne
    @JoinColumn(name = "boat_id")//fk
    @JsonBackReference //otra forma de solucionar bucle se coloca en padre en este caso renta
    private Boat boat;
    
    public Rental() {
    }

    @PrePersist
    private void totalRent(){
        long diff = dateEnd.getTime() - dateInit.getTime();
        if(diff==0){
            this.total = this.boat.getPriceHours();
            return;
        }
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        this.total = this.boat.getPriceHours() * days;
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

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
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
            return "Rental [id=" + id + ", dateInit=" + dateInit + ", dateEnd=" + dateEnd + ", state=" + state + ", total="
                    + total + ", confirmationToken=" + confirmationToken + ", confirmed=" + confirmed + ", user=" + user
                    + ", boat=" + boat + "]";
        }



}
