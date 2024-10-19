package com.manager.boats.rental.boats_rental.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manager.boats.rental.boats_rental.persistence.models.Rental;
import com.manager.boats.rental.boats_rental.services.interfaces.IRentalServices;
import com.manager.boats.rental.boats_rental.util.ApiResponse;
import com.manager.boats.rental.boats_rental.util.ValidationEntities;
import com.manager.boats.rental.boats_rental.web.controller.dto.RentalDto;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

 


@RestController
@RequestMapping("/api/v1/rentals")
@CrossOrigin(origins = "http://localhost:5173")
public class RentalController {

    @Autowired
    private IRentalServices rentalServices;
    @Autowired
    private ValidationEntities validationEntities;

    @GetMapping
    public ResponseEntity<ApiResponse> getMethodName() {
        return ResponseEntity.ok().body(new ApiResponse("suscces",rentalServices.getAll()));
    }

    @PostMapping("/clientId/{clientId}/boatsId/{boatsId}")
    public ResponseEntity<ApiResponse> postMethodName(@Valid @RequestBody RentalDto entity, BindingResult result ,@PathVariable Long clientId,@PathVariable Long boatsId) {
        if(result.hasFieldErrors()){
            return validationEntities.validation(result);
        }
        
        rentalServices.save(entity, clientId,boatsId);
        return ResponseEntity.ok().body(new ApiResponse("sucess",entity));
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<ApiResponse> updateRental(@Valid @RequestBody RentalDto entity, BindingResult result ,@PathVariable Long userId ) {
        if(result.hasFieldErrors()){
            return validationEntities.validation(result);
        }
        try {
            rentalServices.updateProduct(entity, userId);
            return ResponseEntity.ok().body(new ApiResponse("update",entity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteRental(@PathVariable Long id){
        rentalServices.delete(id);
        return ResponseEntity.ok().body(new ApiResponse("delete",null));
    }
    
}
