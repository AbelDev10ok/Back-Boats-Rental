package com.manager.boats.rental.boats_rental.web.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class UserDtoPasword {
    @NotBlank(message = "La nueva contraseña es requerida")
    private String newPassword;

    @NotBlank(message = "La confirmación de la nueva contraseña es requerida")
    private String confirmNewPassword;

    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }
    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    
}
