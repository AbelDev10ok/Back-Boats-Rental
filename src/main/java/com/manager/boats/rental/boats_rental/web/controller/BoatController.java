package com.manager.boats.rental.boats_rental.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manager.boats.rental.boats_rental.persistence.models.Boat;
import com.manager.boats.rental.boats_rental.services.implementation.BoatServices;
import com.manager.boats.rental.boats_rental.util.ApiResponse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/api/v1/boats")
public class BoatController {   

    @Autowired
    private BoatServices boatServices;


    @GetMapping
    public ResponseEntity<?> getMethodName() {
        List<Boat> boats = boatServices.getAll();
        return ResponseEntity.ok().body(boats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getMethodName(@PathVariable Long id) {
        return ResponseEntity.ok().body(new ApiResponse("get boat",boatServices.getById(id)));
    }
    
    
    @PostMapping("/save")
    public ResponseEntity<ApiResponse> postMethodName(@RequestBody Boat entity) {
        boatServices.save(entity);
        return ResponseEntity.ok().body(new ApiResponse("create",entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> putMethodName(@PathVariable Long id, @RequestBody Boat entity) {
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
