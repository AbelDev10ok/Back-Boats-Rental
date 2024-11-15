package com.manager.boats.rental.boats_rental.services.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manager.boats.rental.boats_rental.persistence.models.Users;
import com.manager.boats.rental.boats_rental.repositories.IUserRepository;

// cada vez que hagamos login spring config utilizara esta clase ya que implementas UserDetailsServices
@Service
public class UserDetailsServices  implements UserDetailsService{

    @Autowired
    private IUserRepository userRepository;


    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = userRepository.findByEmail(username);
        if(user.isEmpty()){
            throw new UsernameNotFoundException(String.format("User %s not found", username));
        }
        Users userDb = user.orElseThrow();
        List<GrantedAuthority> roles = userDb.getRoles().stream()
        .map(role -> (GrantedAuthority) role::getName)
        .toList();

        // User de springSecurity
        return User.builder()
        .username(userDb.getEmail())
        .password(userDb.getPassword())
        .disabled(!userDb.getEnabled())
        .accountExpired(false)
        .accountLocked(false)
        .credentialsExpired(false)
        .authorities(roles)
        .build();
    }
    
}
