package com.manager.boats.rental.boats_rental.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manager.boats.rental.boats_rental.persistence.models.Marin;
import com.manager.boats.rental.boats_rental.services.interfaces.IMarinServices;
import com.manager.boats.rental.boats_rental.util.ApiResponse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/marins")
public class MarinController {

    @Autowired
    private IMarinServices marinServices;



    @GetMapping
    public ResponseEntity<?> getMethodName() {
        List<Marin> marins = marinServices.getAllMarins();
        return ResponseEntity.ok().body(marins);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getMethodName(@PathVariable Long id) {
        return ResponseEntity.ok().body(new ApiResponse("get boat",marinServices.getMarinById(id)));
    }
    
    @PostMapping("/save")
    public ResponseEntity<ApiResponse> postMethodName(@RequestBody Marin entity) {
        marinServices.saveMarin(entity);
        return ResponseEntity.ok().body(new ApiResponse("create",entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> putMethodName(@PathVariable Long id, @RequestBody Marin entity) {
        try {
            marinServices.updateMarin(entity, id);
            return ResponseEntity.ok().body(new ApiResponse("update marin",entity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBoat(@PathVariable Long id){
        try{
            marinServices.deleteMarinById(id);
            return ResponseEntity.ok().body(new ApiResponse("delete",null));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
