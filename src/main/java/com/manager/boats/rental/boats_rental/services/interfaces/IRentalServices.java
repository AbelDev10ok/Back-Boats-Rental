package com.manager.boats.rental.boats_rental.services.interfaces;

import java.util.List;

import com.manager.boats.rental.boats_rental.persistence.models.Rental;
import com.manager.boats.rental.boats_rental.persistence.models.Users;
import com.manager.boats.rental.boats_rental.web.controller.dto.RentalDto;
import com.manager.boats.rental.boats_rental.web.controller.dto.RentalResponse;

public interface IRentalServices {
    List<RentalResponse> getAll();
    Rental getByUserId(Long id);
    Rental save(RentalDto rental,Long userId,Long boatId);
    void delete(Long id);
    void updateRental(RentalDto rental, Long id);
    void confirmRental(String token);
    void cancelRental(Long id,Users user);
}
