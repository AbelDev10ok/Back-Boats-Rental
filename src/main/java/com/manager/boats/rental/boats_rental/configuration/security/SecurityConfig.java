package com.manager.boats.rental.boats_rental.configuration.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.manager.boats.rental.boats_rental.configuration.security.filter.JwtAuthentication;
import com.manager.boats.rental.boats_rental.configuration.security.filter.JwtValidationFilter;
import com.manager.boats.rental.boats_rental.repositories.IUserRepository;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private IUserRepository userRepository; // Inject the repository into SecurityConfig


    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    AuthenticationManager authenticationManager() throws Exception{
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
                .requestMatchers(HttpMethod.POST ,"/api/v1/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/rentals/confirm/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Permite acceso a Swagger UI

                .requestMatchers(HttpMethod.PUT ,"/api/v1/boats/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE ,"/api/v1/boats/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST ,"/api/v1/boats/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST ,"/api/v1/marins/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/{idUser}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/users/{idUser}/enabled").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST ,"/api/v1/rentals/**").hasRole("USER")
                .requestMatchers(HttpMethod.PUT ,"/api/v1/rentals/**").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE ,"/api/v1/rentals/**").hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/api/v1/users/email").hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/api/v1/users/password").hasRole("USER" )
                .requestMatchers(HttpMethod.GET ,"/api/v1/boats/**").hasRole("USER")

                .requestMatchers(HttpMethod.GET, "/api/v1/marins").hasAnyRole("ADMIN", "USER")  //  <-- Aquí el cambio

                
                .anyRequest().authenticated())
                .addFilter(new JwtAuthentication(authenticationManager()))
                .addFilter(new JwtValidationFilter(authenticationManager(),userRepository))
                .csrf(config -> config.disable())
                .cors(cors->cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization","Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
