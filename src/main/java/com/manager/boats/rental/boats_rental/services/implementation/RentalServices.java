package com.manager.boats.rental.boats_rental.services.implementation;

import java.util.List;
import java.util.Optional;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;  


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manager.boats.rental.boats_rental.persistence.models.Boat;
import com.manager.boats.rental.boats_rental.persistence.models.Rental;
import com.manager.boats.rental.boats_rental.persistence.models.Users;
import com.manager.boats.rental.boats_rental.repositories.IBoatRepository;
import com.manager.boats.rental.boats_rental.repositories.IRentalRepository;
import com.manager.boats.rental.boats_rental.repositories.IUserRepository;
import com.manager.boats.rental.boats_rental.services.exception.NotFoundException;
import com.manager.boats.rental.boats_rental.services.interfaces.IRentalServices;
import com.manager.boats.rental.boats_rental.web.controller.dto.RentalDto;

@Service
public class RentalServices implements IRentalServices{

    @Autowired
    private IRentalRepository rentalRepository;
    @Autowired
    private IBoatRepository boatRepository;
    @Autowired
    private IUserRepository userRepository;

    @Transactional
    @Override
    public void delete(Long id) {        
        rentalRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Rental> getAll() {
        return rentalRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Rental getByUserId(Long id) {
        return rentalRepository.findByUserId(id).get();
    }

    @Transactional
    @Override
    public void save(RentalDto rentalDto,Long userId,Long boatId) {
        Users  user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Boat boat = boatRepository.findById(boatId).orElseThrow(() -> new NotFoundException("Boat not found"));;

        SimpleDateFormat stdDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateInit = rentalDto.getDateInit();
        String dateEnd = rentalDto.getDateEnd();
        Rental rental = new Rental();

        try {
            Date dnInit = stdDateFormat.parse(dateInit);
            Date dnEnd = stdDateFormat.parse(dateEnd);
            rental.setDateInit(dnInit);
            rental.setDateEnd(dnEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        rental.setBoat(boat);
        rental.setHours(rentalDto.getHours());
        rental.setState("pendiente"); 
        rental.setTotalHours(boat.getPriceHours()*rentalDto.getHours());
        rental.setUser(user);
        rentalRepository.save(rental);        
    }

    @Transactional
    @Override
    public void updateProduct(Rental rental, Long id) {
        Optional<Rental> rentalOptional = rentalRepository.findByUserId(id);        
        if(rentalOptional.isPresent()){
            Rental existingRental = rentalOptional.get();
            existingRental.setDateInit(rental.getDateInit());
            existingRental.setDateEnd(rental.getDateEnd());
            existingRental.setState(rental.getState());
            existingRental.setHours(rental.getHours());
            existingRental.setTotalHours(rental.getTotalHours());
            rentalRepository.save(existingRental);
        }else{
            throw new NotFoundException("Rental not found");
        }
    }
    
}
