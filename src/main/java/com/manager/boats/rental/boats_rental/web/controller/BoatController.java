package com.manager.boats.rental.boats_rental.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.manager.boats.rental.boats_rental.persistence.models.Boat;
import com.manager.boats.rental.boats_rental.persistence.models.Rental;
import com.manager.boats.rental.boats_rental.services.implementation.BoatServices;
import com.manager.boats.rental.boats_rental.util.ApiResponse;
import com.manager.boats.rental.boats_rental.util.ValidationEntities;
import com.manager.boats.rental.boats_rental.web.controller.dto.BoatDto;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.ParseException;
import java.text.SimpleDateFormat;  





@RestController
@RequestMapping("/api/v1/boats")
@CrossOrigin(origins = "http://localhost:5173")
public class BoatController {   

    @Autowired
    private BoatServices boatServices;

    @Autowired
    private ValidationEntities validationEntities;


    @GetMapping
    public ResponseEntity<?> getAllBoats() {
        List<Boat> boats = boatServices.getAll();
        return ResponseEntity.ok().body(boats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getBoat(@PathVariable Long id) {
        return ResponseEntity.ok().body(new ApiResponse("get boat",boatServices.getById(id)));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse> getBoatsAvaiable(@RequestParam String dateInit, @RequestParam String dateEnd) {

        try {
            return ResponseEntity.ok().body(new ApiResponse("sucess",boatServices.getBoatsAvaiable(dateInit,dateEnd)));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error",e.getMessage()));
        }

    }
    
    
    
    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveboat(@Valid @RequestBody BoatDto entity, BindingResult result) {
        if(result.hasFieldErrors()){
            return validationEntities.validation(result);
        }
        boatServices.save(entity);
        return ResponseEntity.ok().body(new ApiResponse("create",entity));
    }

    @PutMapping("/insert/marin/{marinId}/boat/{boatId}")
    public ResponseEntity<ApiResponse> insertMarinIntoBoat(@PathVariable Long marinId, @PathVariable Long boatId) {
        try {
            boatServices.insertMarinInBoat(marinId,boatId);
            return ResponseEntity.ok().body(new ApiResponse("update",null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> putMethodName(@Valid @RequestBody BoatDto entity,BindingResult result ,@PathVariable Long id) {
        if(result.hasFieldErrors()){
            return validationEntities.validation(result);
        }
        try {
            boatServices.updateProduct(entity, id);
            return ResponseEntity.ok().body(new ApiResponse("update boat",entity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBoat(@PathVariable Long id){
        try{
            boatServices.delete(id);
            return ResponseEntity.ok().body(new ApiResponse("delete",null));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

}
