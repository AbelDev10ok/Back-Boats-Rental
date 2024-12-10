package com.manager.boats.rental.boats_rental.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.manager.boats.rental.boats_rental.persistence.models.Users;
import com.manager.boats.rental.boats_rental.services.interfaces.IUserServices;
import com.manager.boats.rental.boats_rental.util.ApiResponse;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import com.manager.boats.rental.boats_rental.util.ValidationEntities;
import com.manager.boats.rental.boats_rental.web.controller.dto.UserDtoEmail;
import com.manager.boats.rental.boats_rental.web.controller.dto.UserDtoPasword;



@RestController 
@RequestMapping("${api.base.path}/users")
public class UserController {

    @Autowired
    private IUserServices userServices;
    @Autowired
    private ValidationEntities validationEntities;

    @GetMapping
    public ResponseEntity<ApiResponse> getUsers() {
        return ResponseEntity.ok().body(new ApiResponse("success",userServices.getAllUsers()));
    }

    @DeleteMapping("/{idUser}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long idUser){
        try {
            userServices.deleteUserById(idUser);
            return ResponseEntity.ok().body(new ApiResponse("delete",null));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PutMapping("/email")
    @PreAuthorize("hasRole('ROLE_USER') and #user.id == authentication.principal.id")
    public ResponseEntity<ApiResponse> updateEmailUser(
                @Valid 
                @RequestBody UserDtoEmail entity,
                BindingResult result, 
                @AuthenticationPrincipal Users user
                ) 
            {
        if(result.hasFieldErrors()){
            return validationEntities.validation(result);
        }
        try {
            Users updateUser = userServices.updateUserEmail(entity, user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("update",updateUser));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PutMapping("/password")
    @PreAuthorize("hasRole('ROLE_USER') and #user.id == authentication.principal.id\"")
    public ResponseEntity<ApiResponse> updatePasswordlUser(@Valid @RequestBody UserDtoPasword entity,BindingResult result, @AuthenticationPrincipal Users user) {
        if(result.hasFieldErrors()){
            return validationEntities.validation(result);
        }
        try {
            Users updateUser = userServices.updateUserPassword(user.getId(),entity.getNewPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("update",updateUser));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PutMapping("/{idUser}/enabled")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateUserEnabledStatus(@PathVariable Long idUser, @RequestParam boolean enabled) {
        userServices.disableUser(idUser, enabled);
        return ResponseEntity.noContent().build();
    }
  
}
