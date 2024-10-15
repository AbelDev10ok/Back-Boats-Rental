package com.manager.boats.rental.boats_rental.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manager.boats.rental.boats_rental.persistence.models.Boat;
import com.manager.boats.rental.boats_rental.services.implementation.BoatServices;
import com.manager.boats.rental.boats_rental.util.ApiResponse;
import com.manager.boats.rental.boats_rental.util.ValidationEntities;
import com.manager.boats.rental.boats_rental.web.controller.dto.BoatDto;

import jakarta.validation.Valid;

import java.util.List;

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
    
    
    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveboat(@Valid @RequestBody BoatDto entity, BindingResult result) {
        if(result.hasFieldErrors()){
            return validationEntities.validation(result);
        }
        boatServices.save(entity);
        return ResponseEntity.ok().body(new ApiResponse("create",entity));
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

    // public ResponseEntity<ApiResponse> validation(BindingResult result){
    //     Map<String,String> errores = new HashMap<>();
    //     result.getFieldErrors().forEach(err -> {
    //         errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
    //     });
    //     return ResponseEntity.badRequest().body(new ApiResponse("erros",errores));
    // }
}
