package com.manager.boats.rental.boats_rental.services.implementation;

import java.lang.foreign.Linker.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manager.boats.rental.boats_rental.persistence.models.Users;
import com.manager.boats.rental.boats_rental.repositories.IUserRepository;
import com.manager.boats.rental.boats_rental.services.interfaces.IUserServices;
import com.manager.boats.rental.boats_rental.web.controller.dto.UserDto;
import com.manager.boats.rental.boats_rental.web.controller.dto.UserResponse;


@Service
public class UserService implements IUserServices{

    @Autowired
    private IUserRepository userRepository;

    @Transactional(readOnly=true)
    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
            .map(user -> 
            new UserResponse(
            user.getName(), 
            user.getLastname(), 
            user.getEmail(), 
            user.getPhoneNumber(), 
            user.getAddres()))
            .toList();
        
    }
    
    @Transactional(readOnly=true)
    @Override
    public Users getUsersById(Long id) {
        return userRepository.findById(id).get();
    }

    @Transactional
    @Override
    public void saveUser(UserDto userDto) {
        Users user = new Users();
        user.setName(userDto.getName());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAddres(userDto.getAddres());
        userRepository.save(user);
    }
    @Transactional
    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
    @Transactional(readOnly=true)
    @Override
    public void updateUser(UserDto usersdto, Long id) {
        Optional<Users> usersOptional = userRepository.findById(id);
        if(usersOptional.isPresent()){
            Users users = usersOptional.get();
            users.setName(usersdto.getName());
            users.setLastname(usersdto.getLastname());
            users.setEmail(usersdto.getEmail());
            users.setPassword(usersdto.getPassword());
            users.setPhoneNumber(usersdto.getPhoneNumber());
            users.setAddres(usersdto.getAddres());
            userRepository.save(users);
        }else{
            throw new RuntimeException("User not found");
        }
        
    }
    @Override
    public boolean existsUser(String email) {
        return userRepository.existsByEmail(email);
    }

    
}
