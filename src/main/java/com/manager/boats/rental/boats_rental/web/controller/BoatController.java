package com.manager.boats.rental.boats_rental.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.manager.boats.rental.boats_rental.services.implementation.BoatServices;
import com.manager.boats.rental.boats_rental.util.ApiResponse;
import com.manager.boats.rental.boats_rental.util.ValidationEntities;
import com.manager.boats.rental.boats_rental.web.controller.dto.BoatResponse;
import com.manager.boats.rental.boats_rental.web.controller.dto.BoateRequest;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("${api.base.path}/boats")
public class BoatController {   

    @Autowired
    private BoatServices boatServices;

    @Autowired
    private ValidationEntities validationEntities;


    @GetMapping
    public ResponseEntity<ApiResponse> getAllBoats() {
        List<BoatResponse> boats = boatServices.getAll();
        return ResponseEntity.ok().body(new ApiResponse("boats",boats));
    }

    @GetMapping("/{tuition}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> getBoat(@PathVariable Long tuition) {
        try {
            return ResponseEntity.ok().body(new ApiResponse("get boat",boatServices.getByTuition(tuition)));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/available")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> getBoatsAvaiable(@RequestParam String dateInit, @RequestParam String dateEnd) {

        try {
            return ResponseEntity.ok().body(new ApiResponse("sucess",boatServices.getBoatsAvaiable(dateInit,dateEnd)));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error",e.getMessage()));
        }

    }
    
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveboat(@Valid @RequestBody BoateRequest entity, BindingResult result) {
        if(result.hasFieldErrors()){
            return validationEntities.validation(result);
        }
        try{
            boatServices.save(entity);
            return ResponseEntity.ok().body(new ApiResponse("create",entity));
        }catch(Exception e){
            return ResponseEntity.badRequest()
            .body(new ApiResponse("error",e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/insert/marin/{marinId}/boat/{boatId}")
    public ResponseEntity<ApiResponse> insertMarinIntoBoat(@PathVariable Long marinId, @PathVariable Long boatId) {
        try {
            boatServices.insertMarinInBoat(marinId,boatId);
            return ResponseEntity.ok().body(new ApiResponse("update",null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{tuition}")
    public ResponseEntity<ApiResponse> updateBoat(@Valid @RequestBody BoateRequest entity,BindingResult result ,@PathVariable Long tuition) {
        if(result.hasFieldErrors()){
            return validationEntities.validation(result);
        }
        try {
            boatServices.updateBoat(entity, tuition);
            return ResponseEntity.ok().body(new ApiResponse("update boat",entity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{tuition}")
    public ResponseEntity<ApiResponse> deleteBoat(@PathVariable Long tuition){
        try{
            boatServices.delete(tuition);
            return ResponseEntity.ok().body(new ApiResponse("delete",null));
        }catch (Exception e) {
            String message = getRootCauseMessage(e); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", message));
        }
    }

    private String getRootCauseMessage(Throwable e) {
        Throwable rootCause = e;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }
        return rootCause.getMessage();
    }

}
