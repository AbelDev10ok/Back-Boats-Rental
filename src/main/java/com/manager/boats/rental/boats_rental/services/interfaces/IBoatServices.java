package com.manager.boats.rental.boats_rental.services.interfaces;

import java.util.List;
import java.text.ParseException;


import com.manager.boats.rental.boats_rental.persistence.models.Boat;
import com.manager.boats.rental.boats_rental.web.controller.dto.BoatResponse;

public interface IBoatServices {
    List<BoatResponse> getAll();
    BoatResponse getByTuition(Long id);
    void save(Boat boat);
    void delete(Long tuition);
    void updateBoat(Boat boat, Long id);
    boolean existsByTuition(Long tuition);
    void insertMarinInBoat(Long marinI, Long boatId);
    List<BoatResponse> getBoatsAvaiable(String dateInit,String dateEnd) throws ParseException;
}
