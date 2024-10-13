package com.manager.boats.rental.boats_rental.services.interfaces;

import java.util.List;

import com.manager.boats.rental.boats_rental.persistence.models.Rental;
import com.manager.boats.rental.boats_rental.web.controller.dto.RentalDto;

public interface IRentalServices {
    List<Rental> getAll();
    Rental getByUserId(Long id);
    void save(RentalDto rental,Long userId,Long boatId);
    void delete(Long id);
    void updateProduct(Rental rental, Long id);
}
