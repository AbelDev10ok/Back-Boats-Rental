package com.manager.boats.rental.boats_rental.services.implementation;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manager.boats.rental.boats_rental.persistence.models.Boat;
import com.manager.boats.rental.boats_rental.persistence.models.Marin;
import com.manager.boats.rental.boats_rental.repositories.IMarinRepository;
import com.manager.boats.rental.boats_rental.services.exception.NotFoundException;
import com.manager.boats.rental.boats_rental.services.interfaces.IBoatServices;
import com.manager.boats.rental.boats_rental.services.interfaces.IMarinServices;
import com.manager.boats.rental.boats_rental.web.controller.dto.MarinDto;
import com.manager.boats.rental.boats_rental.web.controller.dto.MarinResponse;

@Service
public class MarinServices implements IMarinServices {

    @Autowired
    private IMarinRepository marinRepository;

    @Autowired
    private IBoatServices  boatServices;

    @Autowired
    private ModelMapper modelMapper;

    

    @Override
    public Marin findByDni(String dni) {
        return marinRepository.findByDni(dni);
    }

    @Transactional
    @Override
    public void deleteMarinById(Long id) {
        Optional<Marin> marinOptional = marinRepository.findById(id);
        if (marinOptional.isPresent()) {
            for(Boat boat: marinOptional.get().getBoats()){
                boatServices.delete(boat.getTuition());
            }
            marinRepository.deleteById(id);
        } else {
            throw new NotFoundException("Marin not found"); 
        }
        // delete boats for not have errors of restriction of key between relations
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<MarinResponse> getAllMarins() {
            return marinRepository.findAll().stream().map(marin->modelMapper.map(marin, MarinResponse.class)).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public MarinResponse getMarinById(Long id) {
        Optional<Marin> marin = marinRepository.findById(id);
        if(marin.isPresent()){
            return modelMapper.map(marin.get(), MarinResponse.class);
        }else{
            throw new NotFoundException("Marin not found");
        }
    }

    @Transactional
    @Override
    public void saveMarin(Marin marin) {
        Marin marinDb = marinRepository.findByDni(marin.getDni());
        if(marinDb != null){
            throw new NotFoundException("Marin already exists");
        }
        
        marinRepository.save(marin);
    }

    @Transactional
    @Override
    public void updateMarin(MarinDto marindto, Long id) {
        Optional<Marin> marinOptional = marinRepository.findById(id);
        if(marinOptional.isPresent()){
            Marin marinDb = marinOptional.get();
            marinDb.setName(marindto.getName());
            marinDb.setLastname(marindto.getLastname());
            marinDb.setDni(marindto.getDni());
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
