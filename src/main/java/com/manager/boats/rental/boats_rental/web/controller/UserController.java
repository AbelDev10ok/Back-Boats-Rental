package com.manager.boats.rental.boats_rental.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manager.boats.rental.boats_rental.services.interfaces.IUserServices;
import com.manager.boats.rental.boats_rental.util.ApiResponse;
import com.manager.boats.rental.boats_rental.web.controller.dto.UserDto;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import com.manager.boats.rental.boats_rental.util.ValidationEntities;



@RestController 
@RequestMapping("/api/v1/users")
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
        userServices.deleteUserById(id);
        return ResponseEntity.ok().body(new ApiResponse("delete",null));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody UserDto entity ,BindingResult result) {   
        if(result.hasFieldErrors()){
            return validationEntities.validation(result);
        }
        try {
            userServices.saveUser(entity);     
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("craete user",entity));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error",e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(@Valid @RequestBody UserDto entity,BindingResult result, @PathVariable Long id) {
        if(result.hasFieldErrors()){
            return validationEntities.validation(result);
        }
        userServices.updateUser(entity, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("update",entity));
    }
    

}
