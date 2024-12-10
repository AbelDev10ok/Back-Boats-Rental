package com.manager.boats.rental.boats_rental.services.interfaces;

import java.util.List;

import com.manager.boats.rental.boats_rental.persistence.models.Marin;
import com.manager.boats.rental.boats_rental.web.controller.dto.MarinDto;
import com.manager.boats.rental.boats_rental.web.controller.dto.MarinResponse;

public interface IMarinServices {
    List<MarinResponse> getAllMarins();
    MarinResponse getMarinById(Long id);
    void saveMarin(Marin marin);
    void deleteMarinById(Long id);
    void updateMarin(MarinDto marin,Long id);
    boolean existsMarin(String dni);
    Marin findByDni(String dni);
}
