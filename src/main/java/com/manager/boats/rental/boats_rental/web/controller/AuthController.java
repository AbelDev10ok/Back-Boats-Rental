package com.manager.boats.rental.boats_rental.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manager.boats.rental.boats_rental.configuration.security.JwtUtil;
import com.manager.boats.rental.boats_rental.persistence.models.Users;
import com.manager.boats.rental.boats_rental.repositories.IUserRepository;
import com.manager.boats.rental.boats_rental.services.interfaces.IUserServices;
import com.manager.boats.rental.boats_rental.util.ApiResponse;
import com.manager.boats.rental.boats_rental.util.ValidationEntities;

import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;

import static com.manager.boats.rental.boats_rental.configuration.security.TokenJwtConfig.SECRET_KEY;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
    private JwtUtil jwtUtil;


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
    public ResponseEntity<ApiResponse> loginUser(@RequestBody Users entity) {
        try {
            Users user = userRepository.findByEmail(entity.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (!passwordEncoder.matches(entity.getPassword(), user.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            }

            // Use your JwtUtil class to generate the token
            String token = jwtUtil.generateToken(user);  //  Use the overload for User objects.

            return ResponseEntity.ok(new ApiResponse("success", Map.of("token", token)));
        } catch (UsernameNotFoundException | BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(ex.getMessage(), null));
        }
    }
}

    
