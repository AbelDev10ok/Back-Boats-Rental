package com.manager.boats.rental.boats_rental.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manager.boats.rental.boats_rental.configuration.security.JwtUtil;
import com.manager.boats.rental.boats_rental.persistence.models.Users;
import com.manager.boats.rental.boats_rental.repositories.IUserRepository;
import com.manager.boats.rental.boats_rental.services.interfaces.IUserServices;
import com.manager.boats.rental.boats_rental.util.ApiResponse;
import com.manager.boats.rental.boats_rental.util.ValidationEntities;

import jakarta.validation.Valid;


import java.util.Map;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private IUserServices userServices;
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    // @Autowired
    // Dotenv dotenv;



    @Autowired
    private ValidationEntities validationEntities;

    // coloco admin en false ya que no debe ser administrador cualquier usuario
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody Users entity,BindingResult result ) {   
        if(result.hasFieldErrors()){
            return validationEntities.validation(result);
        }
        try {
            entity.setAdmin(false);
            userServices.saveUser(entity);     
            return ResponseEntity.ok().body(new ApiResponse("success",entity));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Users entity) {
        try {
            userRepository.findByEmail(entity.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));                   

            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(entity.getEmail(), entity.getPassword())
            );

            String email = ((User) authentication.getPrincipal()).getUsername();  // Obtener el email

            Users user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found")); // Obtener el usuario de la base de datos


            String token = jwtUtil.generateToken(user);

            return ResponseEntity.ok(new ApiResponse("success", Map.of("id",user.getId(),"email",user.getEmail(),"token", token)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("error",e.getMessage())); // Encapsula la excepción
        }
    }
}

    
