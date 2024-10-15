package com.manager.boats.rental.boats_rental.services.interfaces;

import java.util.List;

import com.manager.boats.rental.boats_rental.persistence.models.Boat;
import com.manager.boats.rental.boats_rental.web.controller.dto.BoatDto;

public interface IBoatServices {
    List<Boat> getAll();
    Boat getById(Long id);
    void save(BoatDto boat);
    void delete(Long id);
    void updateProduct(BoatDto boat, Long id);
    boolean existsByTuition(Long tuition);
}
