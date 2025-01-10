package com.manager.boats.rental.boats_rental.services.interfaces;

import java.util.List;

import com.manager.boats.rental.boats_rental.persistence.models.Users;
import com.manager.boats.rental.boats_rental.web.controller.dto.UserAuth;
import com.manager.boats.rental.boats_rental.web.controller.dto.UserDtoEmail;
import com.manager.boats.rental.boats_rental.web.controller.dto.UserResponse;

public interface IUserServices {
    List<UserResponse> getAllUsers();
    Users getUsersById(Long id);
    void saveUser(UserAuth user);
    void deleteUserById(Long id);
    void updateUser(Users usersdto,Long id);
    boolean existsUser(String email);
    void disableUser(String email, boolean enabled) ;
    Users findByEmail(String email);
    Users updateUserEmail(UserDtoEmail user, Long id);
    Users updateUserPassword(Long id, String newPassword);
}
