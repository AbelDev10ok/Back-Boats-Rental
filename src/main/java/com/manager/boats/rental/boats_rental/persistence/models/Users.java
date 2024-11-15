package com.manager.boats.rental.boats_rental.persistence.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.List;


@Entity
@Table
public class Users{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
     
    @NotBlank(message = "email is required")
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    // @IExistsUserByEmail
    @Column(unique=true)
    private String email;
    @NotBlank(message = "password is required")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    // clase Boolean por defecto null 
    private boolean enabled;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean admin;

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = jakarta.persistence.CascadeType.ALL,orphanRemoval = true)
    private Set<Rental> rentals;

    // OTRA FORMA DE EVITAR BUCLE JSON
    // @JsonIgnoreProperties({"users"})
    // @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToMany
    @JoinTable(
        name = "users_roles",
        joinColumns = @jakarta.persistence.JoinColumn(name = "user_id"),
        inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "role_id"),
        uniqueConstraints = @jakarta.persistence.UniqueConstraint(columnNames = {"user_id", "role_id"}))
    List<Role> roles;

    public Users() {
        this.rentals = new HashSet<>();
    }

    public Users(String email, String password) {
        this.email = email;
        this.password = password;
        this.rentals = new HashSet<>();
    }

    @PrePersist
    public void enabled(){
        this.enabled = true;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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

    public Set<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(Set<Rental> rentals) {
        this.rentals = rentals;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "Users [id=" + id + ", email=" + email + ", password=" + password + ", enabled=" + enabled + ", admin="
                + admin + ", rentals=" + rentals + ", roles=" + roles + "]";
    }


    
}
