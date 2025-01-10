package com.manager.boats.rental.boats_rental.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manager.boats.rental.boats_rental.persistence.models.Marin;
import com.manager.boats.rental.boats_rental.services.exception.NotFoundException;
import com.manager.boats.rental.boats_rental.services.interfaces.IMarinServices;
import com.manager.boats.rental.boats_rental.util.ApiResponse;
import com.manager.boats.rental.boats_rental.util.ValidationEntities;
import com.manager.boats.rental.boats_rental.web.controller.dto.MarinDto;
import com.manager.boats.rental.boats_rental.web.controller.dto.MarinResponse;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.validation.BindingResult;


@RestController
@RequestMapping("${api.base.path}/marins")
@CrossOrigin(origins = "http://localhost:5173")
// @PreAuthorize("hasRole('ROLE_ADMIN')")
public class MarinController {

    @Autowired
    private IMarinServices marinServices;
    @Autowired
    private ValidationEntities validationEntities;


    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<ApiResponse> getAllMarins() {
        List<MarinResponse> marins = marinServices.getAllMarins();
        return ResponseEntity.ok().body(new ApiResponse("marins",marins));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getMarinById(@PathVariable Long id) {
        try {
            MarinResponse marin = marinServices.getMarinById(id);
            return ResponseEntity.ok().body(new ApiResponse("get marin",marin));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("errors",e.getMessage()));
        }

    }
    
    @PostMapping("/save")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> saveMarin(@Valid @RequestBody Marin entity,BindingResult result ) {
        if(result.hasFieldErrors()){
            return validationEntities.validation(result);
        }
        try {
            
            marinServices.saveMarin(entity);
            // Marin newMarin = marinServices.findByDni(entity.getDni());
            return ResponseEntity.ok().body(new ApiResponse("create",entity));
        }catch(NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("errors",e.getMessage()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("errors",e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> updateMarin(@Valid @RequestBody MarinDto entity,BindingResult result ,@PathVariable Long id) {
        if(result.hasFieldErrors()){
            return validationEntities.validation(result);
        }
        try {
            marinServices.updateMarin(entity, id);
            return ResponseEntity.ok().body(new ApiResponse("update marin",entity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> deleteMarin(@PathVariable Long id){
        try{
            marinServices.deleteMarinById(id);
            return ResponseEntity.ok().body(new ApiResponse("delete",null));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
