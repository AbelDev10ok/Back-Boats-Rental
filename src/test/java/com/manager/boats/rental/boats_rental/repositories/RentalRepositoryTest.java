package com.manager.boats.rental.boats_rental.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.manager.boats.rental.boats_rental.persistence.models.Boat;
import com.manager.boats.rental.boats_rental.persistence.models.Rental;
import com.manager.boats.rental.boats_rental.persistence.models.Users;

@DataJpaTest // SOLO PRUEBA CAPA DE PERSISTENCIA (ENTIDADES Y REPOSITORIOS)
@AutoConfigureTestDatabase(replace = Replace.NONE)//para que ejecute a baseses de datos real
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class RentalRepositoryTest {

    @Autowired
    private IRentalRepository rentalRepository;

    @Autowired
    private IBoatRepository boatRepository;

    @Autowired
    private IUserRepository userRepository;

    private Rental rentalInit;
    private Boat boatInit;
    private Users userInit;

    @BeforeEach
    public void setUp(){
        
        rentalRepository.deleteAll(); // Or clear only the boats table if that's enough.
        boatRepository.deleteAll();
        userRepository.deleteAll();
        userInit = new Users("johndoe@example.com", "password");
        userRepository.save(userInit); 

        long tuition = System.currentTimeMillis();
        boatInit = new Boat(tuition,"velero",40L,"jenny","xr","true",20L);
        boatRepository.save(boatInit);

        rentalInit = new Rental();
        rentalInit.setUser(userInit);
        rentalInit.setBoat(boatInit);
        rentalInit.setHours(10L);
        rentalInit.setTotalHours(200L);
        rentalInit.setDateInit(new Date()); 
        rentalInit.setDateEnd(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24))); 
        rentalRepository.save(rentalInit); // <-- Guardar rentalInit aquí

    }

    @Test
    @DisplayName("Save a new rental")
    void saveRentalTest() {
        // Create User and Boat within the test
        Users user = new Users("test@example.com", "password");
        userRepository.save(user);
    
        Boat boat = new Boat(System.currentTimeMillis(), "velero", 40L, "jenny", "xr", "true", 20L);
        boatRepository.save(boat);
    
        Rental rentalToSave = new Rental(); // New rental instance for this test
        rentalToSave.setUser(user);
        rentalToSave.setBoat(boat); // Use the boat created in this test
        rentalToSave.setHours(10L);
        rentalToSave.setTotalHours(200L);
        rentalToSave.setDateInit(new Date());
        rentalToSave.setDateEnd(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24)));
    
        // When: Saving the rental
        Rental savedRental = rentalRepository.save(rentalToSave);
    
        // ... (rest of your assertions)
        assertNotNull(savedRental);
        assertNotNull(savedRental.getId());
    }

    @DisplayName(value = "List all rentals")
    @Test
    public void testGetListAllRentals(){
        
        // given
        Iterable<Rental> rental = rentalRepository.findAll();
        
        // then
        assertNotNull(rental);
    }

    @DisplayName(value = "get rental by User id")
    @Test
    public void testGetRentalByUserId(){
        rentalRepository.save(rentalInit);

        Rental rental = rentalRepository.findByUserId(userInit.getId()).get();
        assertNotNull(rental);
    }

    @DisplayName(value = "List no empty")
    @Test
    public void testListNoEmpty(){
        List<Rental> rental = rentalRepository.findAll();
        if(rental.isEmpty()){
            fail();
        }
    }

    @DisplayName("Update rental")
    @Test
    @Transactional
    public void testUpdateRental() {
        // Given (Setup data within the test method for clearer demonstration)
        Users user = new Users("testuser@example.com", "password");
        user = userRepository.save(user);
    
        Boat boat = new Boat(System.currentTimeMillis(), "velero", 40L, "testboat", "model", "true", 20L);
        boat = boatRepository.save(boat);
    
    
        Rental rental = new Rental();
        rental.setUser(user);
        rental.setBoat(boat);
        rental.setHours(5L);  // Initial hours
        rental.setTotalHours(100L); // Initial total hours
        rental.setDateInit(new Date());
        rental.setDateEnd(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24)));
        rental = rentalRepository.save(rental);
    
        Long rentalId = rental.getId(); // Get the ID of the saved rental
    
        // When (Retrieve, modify, and save)
        Rental rentalToUpdate = rentalRepository.findById(rentalId).orElseThrow();  // Retrieve the saved entity
        Long newTotalHours = 300L;
        rentalToUpdate.setTotalHours(newTotalHours); // Modify
    
        Date newDateEnd = new Date(rentalToUpdate.getDateEnd().getTime() + (1000 * 60 * 60 * 24));
        rentalToUpdate.setDateEnd(newDateEnd);
        rentalRepository.save(rentalToUpdate); // Save the changes
    
    
        // Then (Refresh to avoid stale data)
        Rental updatedRental = rentalRepository.findById(rentalId).orElseThrow();
        assertEquals(newTotalHours, updatedRental.getTotalHours());
        assertEquals(newDateEnd, updatedRental.getDateEnd());
    }

    @DisplayName(value = "Delete rental")
    @Test
    public void testDeleteRental(){
        // given
        rentalRepository.save(rentalInit);
        boolean rental = rentalRepository.findByUserId(userInit.getId()).isPresent();
        if(!rental){
            fail();
        }

        // when
        rentalRepository.delete(rentalInit);
        boolean afterDelete = rentalRepository.findByUserId(userInit.getId()).isPresent();

        // then
        assertTrue(rental);
        assertFalse(afterDelete);

    }


}
