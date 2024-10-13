package com.manager.boats.rental.boats_rental.services.implementation;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.stereotype.Service;

import com.manager.boats.rental.boats_rental.persistence.models.Boat;
import com.manager.boats.rental.boats_rental.repositories.IBoatRepository;
import com.manager.boats.rental.boats_rental.services.exception.NotFoundException;
import com.manager.boats.rental.boats_rental.services.interfaces.IBoatServices;
import com.manager.boats.rental.boats_rental.web.controller.dto.BoatDto;

import org.springframework.transaction.annotation.Transactional;

@Service
public class BoatServices implements IBoatServices{
    @Autowired
    private IBoatRepository boatRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void delete(Long id) {
        Optional boatOptional = boatRepository.findById(id);
        if(boatOptional.isPresent()){
            boatRepository.deleteById(id);
        }else{
            throw new NotFoundException("Not found");
        }
        
    }

    @Override
    public List<Boat> getAll() {
        List<Boat> boats = boatRepository.findAll();
        if(!boats.isEmpty()){
            return boats;
        }
        return null;
    }

    @Override
    public Boat getById(Long id) {
        Optional<Boat> boat = boatRepository.findById(id);
        if(boat.isPresent()){
            return boat.get();
        }else{
            throw new NotFoundException("Not found");
        }

    }

    @Transactional
    @Override
    public void save(Boat boat) {
        boatRepository.save(boat);
    }
    

    @Override
    public void updateProduct(Boat boat, Long id) {
            Optional<Boat> existingBoatOptional = boatRepository.findById(id);
        
            if (existingBoatOptional.isPresent()) {
                Boat existingBoat = existingBoatOptional.get();
                existingBoat.setAbility(boat.getAbility());
                existingBoat.setModel(boat.getModel());
                existingBoat.setName(boat.getName());
                existingBoat.setType(boat.getType());
        
                boatRepository.save(existingBoat); // Save the updated boat
            } else {
                // Handle the case where the boat is not found
                throw new NotFoundException("Boat with ID " + id + " not found.");
            }
    }

    //mapp Boat to BoatDto
    public  Boat mapToDto(BoatDto boatDto){
        Boat boat = modelMapper.map(boatDto, Boat.class);
        return boat;
    }
}
