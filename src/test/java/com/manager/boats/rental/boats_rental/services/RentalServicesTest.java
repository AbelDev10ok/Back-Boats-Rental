package com.manager.boats.rental.boats_rental.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.hibernate.annotations.NotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import com.manager.boats.rental.boats_rental.persistence.models.Boat;
import com.manager.boats.rental.boats_rental.persistence.models.Rental;
import com.manager.boats.rental.boats_rental.persistence.models.Users;
import com.manager.boats.rental.boats_rental.repositories.IBoatRepository;
import com.manager.boats.rental.boats_rental.repositories.IRentalRepository;
import com.manager.boats.rental.boats_rental.repositories.IUserRepository;
import com.manager.boats.rental.boats_rental.services.implementation.RentalServices;
import com.manager.boats.rental.boats_rental.web.controller.dto.RentalDto;

// cargamos extensiones de mockito con junit5
@ExtendWith(MockitoExtension.class)
public class RentalServicesTest {

    @Mock
    private IRentalRepository rentalRepository;

    @Mock
    private IBoatRepository boatRepository;

    @Mock
    private IUserRepository userRepository;


    @InjectMocks
    private RentalServices rentalServices;

    private RentalDto rentalDtoInit;


    @BeforeEach
    public void setUp(){
        rentalDtoInit = new RentalDto("2025-01-05","2026-01-06",4L);
    }

    @Test
    void testSaveRental() {
        // Given
        Long userId = 48L;
        Long boatId = 3L;
        Users user = new Users(); // Crea una instancia de Users con la información necesaria
        user.setId(userId);
        Boat boat = new Boat(); // Crea una instancia de Boat con la información necesaria
        boat.setTuition(boatId);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(boatRepository.findById(boatId)).willReturn(Optional.of(boat));
        given(rentalRepository.save(any(Rental.class))).willReturn(new Rental());

        // When
        rentalServices.save(rentalDtoInit, userId, boatId);

        // Then
        verify(rentalRepository, times(1)).save(any(Rental.class));
    }

    @DisplayName("Test get all rentals")
    @Test
    public void testGetAllRentals(){
        // given
        given(rentalRepository.findAll()).willReturn(List.of(new Rental()));

        // when
        List<Rental> rentals = rentalServices.getAll();
        
        // then
        assertNotNull(rentals);
    }


    @DisplayName("Test get all rentals empty")
    @Test
    public void testGetRentalEmpty(){
        // given
        given(rentalRepository.findAll()).willReturn(Collections.emptyList());

        // when
        List<Rental> rentals = rentalServices.getAll();

        // then
        assertEquals(rentals.size(), 0);    
    }

    @Test
    void testUpdateRental() throws ParseException {
        // Given
        Long userId = 48L;
        Rental existingRental = new Rental(); // Crea una instancia de Rental con datos existentes
        existingRental.setUser(new Users("abel", "alarcon", "abel@gmial.com", "11111", "12345","casacuberta")); // Asocia un usuario al rental
        given(rentalRepository.findByUserId(userId)).willReturn(Optional.of(existingRental));
    
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Define el formato de fecha
        // When
        rentalServices.updateProduct(rentalDtoInit, userId);
    
        // Then
        Rental updatedRental = rentalRepository.findByUserId(userId).orElse(null); // Obtén el rental actualizado
        assertNotNull(updatedRental);
        
        // Convierte las fechas del DTO a Date antes de comparar
        assertEquals(sdf.parse(rentalDtoInit.getDateInit()), updatedRental.getDateInit()); 
        assertEquals(sdf.parse(rentalDtoInit.getDateEnd()), updatedRental.getDateEnd()); 
        assertEquals(rentalDtoInit.getHours(), updatedRental.getHours()); 
    }
    

    
}
