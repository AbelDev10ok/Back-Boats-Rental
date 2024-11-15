package com.manager.boats.rental.boats_rental.services.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manager.boats.rental.boats_rental.persistence.models.Role;
import com.manager.boats.rental.boats_rental.persistence.models.Users;
import com.manager.boats.rental.boats_rental.repositories.IRoleRepository;
import com.manager.boats.rental.boats_rental.repositories.IUserRepository;
import com.manager.boats.rental.boats_rental.services.exception.AlreadyExistsException;
import com.manager.boats.rental.boats_rental.services.exception.NotFoundException;
import com.manager.boats.rental.boats_rental.services.interfaces.IUserServices;
import com.manager.boats.rental.boats_rental.web.controller.dto.UserResponse;


@Service
public class UserService implements IUserServices{

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired 
    private PasswordEncoder passwordEncoder;


    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly=true)
    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserResponse.class)) // Usa ModelMapper aquí
                .toList();
    }
    
    @Transactional(readOnly=true)
    @Override
    public Users getUsersById(Long id) {
        return userRepository.findById(id).get();
    }

    @Transactional
    @Override
    public void saveUser(Users user) {
        boolean exists = userRepository.existsByEmail(user.getEmail());
        if(exists){
            throw new AlreadyExistsException("email exists");
        }
        Optional<Role> roleUser = roleRepository.findByName("ROLE_USER");   
        List<Role> roles = new ArrayList<>();
        roleUser.ifPresent(roles::add);
        if(user.isAdmin()){
            System.out.println(user.isAdmin());
            Optional<Role> roleAdmin = roleRepository.findByName("ROLE_ADMIN");
            roleAdmin.ifPresent(roles::add);
        }
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        Optional<Users> user = userRepository.findById(id);
        if(!user.isPresent()){
            throw new NotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }


    @Transactional
    @Override
    public void updateUser(Users user, Long id) {
        Optional<Users> usersOptional = userRepository.findById(id);
        if(usersOptional.isPresent()){
            Users newUsers = usersOptional.get();

            newUsers.setEmail(user.getEmail());
            newUsers.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(newUsers);
        }else{
            throw new NotFoundException("User not found");
        }
        
    }

    @Override
    public boolean existsUser(String email) {
        return userRepository.existsByEmail(email);
    }

    
}
