package com.manager.boats.rental.boats_rental.configuration.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.boats.rental.boats_rental.persistence.models.Users;
import com.manager.boats.rental.boats_rental.repositories.IUserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.lang.Arrays;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.manager.boats.rental.boats_rental.configuration.security.TokenJwtConfig.*;

public class JwtValidationFilter extends BasicAuthenticationFilter{

    
    private final IUserRepository userRepository;


    public JwtValidationFilter(AuthenticationManager authenticationManager,IUserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository; // Initialize in constructor

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

                String header = request.getHeader(HEADER_STRING);
                if(header == null || !header.startsWith(TOKEN_PREFIX)){
                    chain.doFilter(request, response);
                    return;
                }
                String token = header.replace(TOKEN_PREFIX, "");
                
                try {
                    // verificamos la firma del token
                    Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
                    String username = claims.getSubject();

                    Users user = userRepository.findByEmail(username)
                            .orElseThrow(() -> new JwtException("User not found in database"));
                    // user is enabled
                    if (!user.getEnabled()) {
                        System.out.println(user.getEnabled());
                        throw new JwtException("User is disabled");
                    }


                    Object authoritiesClaims = claims.get("authorities");

                    Collection<? extends GrantedAuthority> authorities = Arrays.asList(
                        new ObjectMapper()
                    .addMixIn(SimpleGrantedAuthority.class, 
                    SimpleGrentesAuthorityJson.class)
                    .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class)
                    );
    
                    // null porque el password solo se valida cuando creamos el token
                    
                    // INICIAMOS SESCION Y AUTHENTICAMOS
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken (user, null, authorities);
                    SecurityContextHolder .getContext().setAuthentication(authenticationToken);
                    
                    // continuar con los demas filtros
                    chain.doFilter(request, response);
                    return;

                } catch (JwtException e) {
                    
                    Map<String,String> body = new HashMap<>();
                    body.put("message", e.getMessage());
                    body.put("error", "token no es valido");
                    body.put("status", String.valueOf(HttpServletResponse.SC_UNAUTHORIZED));
                    body.put("request", request.getHeaderNames().toString());

                    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType(CONTENT_TYPE);
                }

    }

    
}
