package com.manager.boats.rental.boats_rental.services.interfaces;

import java.util.List;

import com.manager.boats.rental.boats_rental.persistence.models.Marin;

public interface IMarinServices {
    List<Marin> getAllMarins();
    Marin getMarinById(Long id);
    void saveMarin(Marin marin);
    void deleteMarinById(Long id);
    void updateMarin(Marin marin,Long id);
    boolean existsMarin(String dni);
}
