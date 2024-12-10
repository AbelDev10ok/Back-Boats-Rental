package com.manager.boats.rental.boats_rental.web.controller.dto;

import java.util.ArrayList;
import java.util.List;

import com.manager.boats.rental.boats_rental.persistence.models.Role;

public class UserResponse {
    private Long id;
    private String email;
    private List<Role> roles;
    private boolean isEnabled;
    
    public UserResponse() {
        this.roles = new ArrayList<>();
    }
    
    public UserResponse(String email, boolean isEnabled) {
        this.email = email;
        this.roles = new ArrayList<>();
        this.isEnabled = isEnabled;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    @Override
    public String toString() {
        return "UserResponse [id=" + id + ", email=" + email + ", roles=" + roles + ", isEnabled=" + isEnabled + "]";
    }
    
}
