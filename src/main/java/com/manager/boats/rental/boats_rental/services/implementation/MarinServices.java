package com.manager.boats.rental.boats_rental.services.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manager.boats.rental.boats_rental.persistence.models.Marin;
import com.manager.boats.rental.boats_rental.repositories.IMarinRepository;
import com.manager.boats.rental.boats_rental.services.interfaces.IMarinServices;

@Service
public class MarinServices implements IMarinServices {

    @Autowired
    private IMarinRepository marinRepository;

    @Override
    public void deleteMarinById(Long id) {
        marinRepository.deleteById(id);
        
    }

    @Override
    public List<Marin> getAllMarins() {
        return marinRepository.findAll();
    }

    @Override
    public Marin getMarinById(Long id) {
        return marinRepository.findById(id).get();
    }

    @Override
    public void saveMarin(Marin marin) {
        marinRepository.save(marin);
    }

    @Override
    public void updateMarin(Marin marin, Long id) {
        Optional<Marin> marinOptional = marinRepository.findById(id);
        if(marinOptional.isPresent()){
            Marin marinDb = marinOptional.get();
            marinDb.setName(marinDb.getName());
            marinDb.setLastname(marinDb.getLastname());
            marinRepository.save(marinDb);
        }else{
            throw new RuntimeException("Marin not found");
        }
    }
    
}
