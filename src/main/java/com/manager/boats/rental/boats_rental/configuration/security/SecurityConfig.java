package com.manager.boats.rental.boats_rental.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;

import com.manager.boats.rental.boats_rental.configuration.security.filter.JwtAuthentication;
import com.manager.boats.rental.boats_rental.configuration.security.filter.JwtValidationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    // encriptacion
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    DefaultSecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http.authorizeHttpRequests((auth) -> auth
                .requestMatchers(HttpMethod.POST ,"api/v1/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET ,"api/v1/boats/**").permitAll()
                .requestMatchers(HttpMethod.POST ,"api/v1/boats/save").hasRole("ADMIN")
                .anyRequest().authenticated())
                .addFilter(new JwtAuthentication(authenticationManager()))
                .addFilter(new JwtValidationFilter(authenticationManager()))
                .csrf(config -> config.disable())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}
