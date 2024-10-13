package com.manager.boats.rental.boats_rental.services.interfaces;

import java.util.List;

import com.manager.boats.rental.boats_rental.persistence.models.Users;
import com.manager.boats.rental.boats_rental.web.controller.dto.UserDto;

public interface IUserServices {
    List<Users> getAllUsers();
    Users getUsersById(Long id);
    void saveUser(UserDto user);
    void deleteUserById(Long id);
    void updateUser(UserDto usersdto,Long id);
}