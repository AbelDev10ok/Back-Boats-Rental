package com.manager.boats.rental.boats_rental.services.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manager.boats.rental.boats_rental.persistence.models.Marin;
import com.manager.boats.rental.boats_rental.repositories.IMarinRepository;
import com.manager.boats.rental.boats_rental.services.exception.NotFoundException;
import com.manager.boats.rental.boats_rental.services.interfaces.IMarinServices;

@Service
public class MarinServices implements IMarinServices {

    @Autowired
    private IMarinRepository marinRepository;

    @Transactional
    @Override
    public void deleteMarinById(Long id) {
        Optional<Marin> marinOptional = marinRepository.findById(id);
        if (marinOptional.isPresent()) {
            marinRepository.deleteById(id);
        } else {
            throw new NotFoundException("Marin not found"); 
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Marin> getAllMarins() {
            return marinRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Marin getMarinById(Long id) {
        Optional<Marin> marin = marinRepository.findById(id);
        if(marin.isPresent()){
            return marin.get();
        }else{
            throw new NotFoundException("Marin not found");
        }
    }

    @Transactional
    @Override
    public void saveMarin(Marin marin) {
        
        marinRepository.save(marin);
    }

    @Transactional
    @Override
    public void updateMarin(Marin marin, Long id) {
        Optional<Marin> marinOptional = marinRepository.findById(id);
        if(marinOptional.isPresent()){
            Marin marinDb = marinOptional.get();
            marinDb.setName(marin.getName());
            marinDb.setLastname(marin.getLastname());
            marinDb.setDni(marin.getDni());
            marinRepository.save(marinDb);
        }else{
            throw new NotFoundException("Marin not found");
        }
    }



    @Override
    public boolean existsMarin(String dni) {
        return marinRepository.existsByDni(dni);
    }
    
    

}
