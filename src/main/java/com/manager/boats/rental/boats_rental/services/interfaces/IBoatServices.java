package com.manager.boats.rental.boats_rental.services.interfaces;

import java.util.List;

import com.manager.boats.rental.boats_rental.persistence.models.Boat;

public interface IBoatServices {
    List<Boat> getAll();
    Boat getById(Long id);
    void save(Boat boat);
    void delete(Long id);
    void updateProduct(Boat boat, Long id);
}
