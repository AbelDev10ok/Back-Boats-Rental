package com.manager.boats.rental.boats_rental.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manager.boats.rental.boats_rental.persistence.models.Users;
import com.manager.boats.rental.boats_rental.services.exception.AlreadyExistsException;
import com.manager.boats.rental.boats_rental.services.interfaces.IUserServices;
import com.manager.boats.rental.boats_rental.util.ApiResponse;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import com.manager.boats.rental.boats_rental.util.ValidationEntities;



@RestController 
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private IUserServices userServices;
    @Autowired
    private ValidationEntities validationEntities;

    @GetMapping
    public ResponseEntity<ApiResponse> getMethodName() {
        return ResponseEntity.ok().body(new ApiResponse("success",userServices.getAllUsers()));
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id){
        try {
            userServices.deleteUserById(id);
            return ResponseEntity.ok().body(new ApiResponse("delete",null));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody Users entity ,BindingResult result) {   
        if(result.hasFieldErrors()){
            return validationEntities.validation(result);
        }
        try {
            userServices.saveUser(entity);     
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("create user", entity));
        } catch (AlreadyExistsException e) {
            // Devolver una respuesta 409 Conflict con un mensaje más descriptivo
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse("error", "El email ya está en uso."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(@Valid @RequestBody Users entity,BindingResult result, @PathVariable Long id) {
        if(result.hasFieldErrors()){
            return validationEntities.validation(result);
        }
        try {
            userServices.updateUser(entity, id);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("update",entity));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
  
}
