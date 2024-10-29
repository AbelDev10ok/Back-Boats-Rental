package com.manager.boats.rental.boats_rental.services.implementation;

import java.util.List;
import java.util.Date;
import java.util.Optional;
import java.text.ParseException;
import java.text.SimpleDateFormat;  

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manager.boats.rental.boats_rental.persistence.models.Boat;
import com.manager.boats.rental.boats_rental.persistence.models.Marin;
import com.manager.boats.rental.boats_rental.repositories.IBoatRepository;
import com.manager.boats.rental.boats_rental.repositories.IMarinRepository;
import com.manager.boats.rental.boats_rental.services.exception.NotFoundException;
import com.manager.boats.rental.boats_rental.services.interfaces.IBoatServices;
import com.manager.boats.rental.boats_rental.web.controller.dto.BoatDto;

import org.springframework.transaction.annotation.Transactional;

@Service
public class BoatServices implements IBoatServices{
    @Autowired
    private IBoatRepository boatRepository;

    @Autowired
    private IMarinRepository marinRepository;

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
        Optional<Boat> btDb = boatRepository.findById(boatDto.getTuition());
        if(btDb.isPresent()){
            throw new NotFoundException("exists");
        }

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


    // public void save(BoatDto boatDto) {
    //     // Check if a boat with the given tuition already exists
    //     Optional<Boat> existingBoat = boatRepository.findById(boatDto.getTuition());

    //     if (existingBoat.isPresent()) {
    //         // Update the existing boat
    //         Boat boatToUpdate = existingBoat.get();
    //         boatToUpdate.setAbility(boatDto.getAbility());
    //         boatToUpdate.setModel(boatDto.getModel());
    //         boatToUpdate.setName(boatDto.getName());
    //         boatToUpdate.setType(boatDto.getType());
    //         boatToUpdate.setPriceHours(boatDto.getPriceHours()); 
    //         // ... update other fields as needed

    //         boatRepository.save(boatToUpdate); // Save the updated boat
    //     } else {
    //         // Create a new boat
    //         Boat newBoat = new Boat();
    //         newBoat.setAbility(boatDto.getAbility());
    //         newBoat.setModel(boatDto.getModel());
    //         newBoat.setName(boatDto.getName());
    //         newBoat.setType(boatDto.getType());
    //         newBoat.setTuition(boatDto.getTuition());
    //         newBoat.setState("available"); // Assuming "available" is the default state
    //         newBoat.setPriceHours(boatDto.getPriceHours());
    //         boatRepository.save(newBoat);
    //     }
    // }




    
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

    


    @Transactional
    @Override
    public void insertMarinInBoat(Long marinId, Long boatId) {
        Optional<Boat> boat = boatRepository.findById(boatId); 
        Optional<Marin> marin = marinRepository.findById(marinId);
        if(marin.isPresent()){
            Marin marinDb = marin.get();
            marinDb.getBoats().add(boat.get());
            marinRepository.save(marinDb);
        }else{
            new NotFoundException("Marin not found");
        }
        if(boat.isPresent()){
            Boat boatDb = boat.get();
            boatDb.setMarin(marin.get());
            boatRepository.save(boatDb);
        }
    }

    

    @Override
    public List<Boat> getBoatsAvaiable(String dateInit,String dateEnd) throws ParseException{
        SimpleDateFormat stdDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date dnInit = stdDateFormat.parse(dateInit);
        Date dnEnd = stdDateFormat.parse(dateEnd);
        return boatRepository.getBoatsAvaliable(dnInit,dnEnd);

    }

    //mapp Boat to BoatDto
    public  Boat mapToDto(BoatDto boatDto){
        Boat boat = modelMapper.map(boatDto, Boat.class);
        return boat;
    }
}
