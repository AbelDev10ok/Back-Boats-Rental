package com.manager.boats.rental.boats_rental.services.implementation;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional
    @Override
    public void delete(Long id) {
        Optional<Boat> boatOptional = boatRepository.findById(id);
        if(boatOptional.isPresent()){
            boatRepository.deleteById(id);
        }else{
            throw new NotFoundException("Not found");
        }
        
    }

    @Transactional(readOnly = true)
    @Override
    public List<Boat> getAll() {
        List<Boat> boats = boatRepository.findAll();
        if(!boats.isEmpty()){
            return boats;
        }
        return null;
    }

    @Transactional(readOnly = true)
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
    public void save(BoatDto boatDto) {
        Boat boat = new Boat();
        boat.setAbility(boatDto.getAbility());
        boat.setModel(boatDto.getModel());
        boat.setName(boatDto.getName());
        boat.setType(boatDto.getType());
        boat.setTuition(boatDto.getTuition());
        boat.setState("aviable");
        boat.setPriceHours(boatDto.getPriceHours());
        boatRepository.save(boat);
    }
    
    @Transactional
    @Override
    public void updateProduct(BoatDto boat, Long id) {
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
    

    @Transactional(readOnly = true)
    @Override
    public boolean existsByTuition(Long tuition) {
        System.out.println(tuition);
        return boatRepository.existsById(tuition);
    }

    //mapp Boat to BoatDto
    public  Boat mapToDto(BoatDto boatDto){
        Boat boat = modelMapper.map(boatDto, Boat.class);
        return boat;
    }
}
