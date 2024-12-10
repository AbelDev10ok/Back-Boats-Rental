package com.manager.boats.rental.boats_rental.services.implementation;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manager.boats.rental.boats_rental.persistence.models.Boat;
import com.manager.boats.rental.boats_rental.persistence.models.EstateRental;
import com.manager.boats.rental.boats_rental.persistence.models.Rental;
import com.manager.boats.rental.boats_rental.persistence.models.Users;
import com.manager.boats.rental.boats_rental.repositories.IBoatRepository;
import com.manager.boats.rental.boats_rental.repositories.IRentalRepository;
import com.manager.boats.rental.boats_rental.repositories.IUserRepository;
import com.manager.boats.rental.boats_rental.services.exception.NotFoundException;
import com.manager.boats.rental.boats_rental.services.interfaces.IRentalServices;
import com.manager.boats.rental.boats_rental.web.controller.RentalController;
import com.manager.boats.rental.boats_rental.web.controller.dto.BoatDto;
import com.manager.boats.rental.boats_rental.web.controller.dto.RentalDto;
import com.manager.boats.rental.boats_rental.web.controller.dto.RentalResponse;
import com.manager.boats.rental.boats_rental.web.controller.dto.UserResponse;

@Service
public class RentalServices implements IRentalServices{

    @Autowired
    private IRentalRepository rentalRepository;
    @Autowired
    private IBoatRepository boatRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    private final Logger log = LoggerFactory.getLogger(RentalController.class);

    @Transactional
    public void cancelRental(Long id,Users user){
        Rental rental = rentalRepository.findById(id)
            .orElseThrow(()-> new NotFoundException("Rental not found"));

        if (!rental.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("You are not authorized to cancel this rental.");
        }

        Date today = new Date();
        log.debug("Fecha hoy: ",today);
        long diff = rental.getDateInit().getTime() - today.getTime();
        log.debug("Fecha inicio renta : ",rental.getDateInit().getTime());
        log.debug("Fecha find de renta : ",rental.getDateInit().getTime());
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        log.debug("Fecha hoy: ",today);
        
        //  verificamos si la renta comienza en el futuro
        if (days >= 0) {
            if (days < 2) {
                throw new IllegalStateException("La renta solo se puede cancelar con 2 días de anticipación.");
            }
        } 

        if(rental.getState() == EstateRental.CANCELADO){
            throw new IllegalStateException("Rental already cancelled");
        }
        rental.setState(EstateRental.CANCELADO);
        rentalRepository.save(rental);
    }

    
    @Transactional
    @Override
    public void delete(Long id) {    
        rentalRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Rental not found"));
        
        rentalRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Rental save(RentalDto rentalDto,Long userId,Long boatId) {
        Users  user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Boat boat = boatRepository.findByTuition(boatId).orElseThrow(() -> new NotFoundException("Boat not found"));;
        
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
        rental.setState(EstateRental.PENDIENTE); 
        rental.setUser(user);
        try {
            rentalRepository.save(rental);
            return rental;        
        } catch (DataIntegrityViolationException  e) {
            String databaseErrorMessage = e.getMostSpecificCause().getMessage();
            
            if (databaseErrorMessage.contains("No tenemos marins disponibles para esta reserva.")) {
                throw new NotFoundException(databaseErrorMessage);
            } else {

                throw new RuntimeException("Error al guardar la renta: " + databaseErrorMessage, e);
            }
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<RentalResponse> getAll() {
        List<Rental> rentals = rentalRepository.findAll();
        return rentals.stream()
                .map(rental -> {
                    RentalResponse rentalDTO = modelMapper.map(rental, RentalResponse.class); // Mapeo básico
                    BoatDto boatDTO = modelMapper.map(rental.getBoat(), BoatDto.class);
                    rentalDTO.setBoat(boatDTO);

                    UserResponse userDTO = modelMapper.map(rental.getUser(), UserResponse.class);
                    rentalDTO.setUser(userDTO);


                    return rentalDTO;
                })
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Rental getByUserId(Long id) {
        return rentalRepository.findByUserId(id).orElseThrow(()-> new NotFoundException("User not exists"));
    }

    @Transactional
    @Override
    public void updateRental(RentalDto rentalDto, Long id) {
        Optional<Rental> rentalOptional = rentalRepository.findById(id);    
        
        SimpleDateFormat stdDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateInit = rentalDto.getDateInit();
        String dateEnd = rentalDto.getDateEnd();
        
        try {
            Date dnInit = stdDateFormat.parse(dateInit);
            Date dnEnd = stdDateFormat.parse(dateEnd);

            if(rentalOptional.isPresent()){
                Rental existingRental = rentalOptional.get();
                existingRental.setDateInit(dnInit);
                existingRental.setDateEnd(dnEnd);
                existingRental.setState(rentalDto.getState());
                rentalRepository.save(existingRental);
            }else{
                throw new NotFoundException("Rental not found");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    
    }
 
    // una ves que ingrese al link de su gmail
    @Transactional
    @Override
    public void confirmRental(String token) {
        Rental rental = rentalRepository.findByConfirmationToken(token)
                .orElseThrow(() -> new NotFoundException("Invalid confirmation token"));

        if (rental.isConfirmed()) {
           throw new IllegalStateException("Rental already confirmed");
        }
        rental.setState(EstateRental.CONFIRMADO);
        rental.setConfirmed(true); // Marca la renta como confirmada
        rentalRepository.save(rental);
    }
    
}
