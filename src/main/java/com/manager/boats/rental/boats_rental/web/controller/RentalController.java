package com.manager.boats.rental.boats_rental.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manager.boats.rental.boats_rental.services.interfaces.IRentalServices;
import com.manager.boats.rental.boats_rental.util.ApiResponse;
import com.manager.boats.rental.boats_rental.web.controller.dto.RentalDto;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
 


@RestController
@RequestMapping("/api/v1/rentals")
public class RentalController {

    @Autowired
    private IRentalServices rentalServices;

    @GetMapping
    public ResponseEntity<ApiResponse> getMethodName() {
        return ResponseEntity.ok().body(new ApiResponse("suscces",rentalServices.getAll()));
    }

    @PostMapping("/clientId/{clientId}/boatsId/{boatsId}")
    public ResponseEntity<ApiResponse> postMethodName(@RequestBody RentalDto entity,@PathVariable Long clientId,@PathVariable Long boatsId) {
        rentalServices.save(entity, clientId,boatsId);
        return ResponseEntity.ok().body(new ApiResponse("sucess",null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteRental(@PathVariable Long id){
        rentalServices.delete(id);
        return ResponseEntity.ok().body(new ApiResponse("delete",null));
    }
    
}
