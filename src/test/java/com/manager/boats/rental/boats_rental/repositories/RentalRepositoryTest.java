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
        userInit = new Users("johndoe@example.com", "password");
        userRepository.save(userInit); 

        boatInit = new Boat(222222L,"velero",40L,"jenny","xr","true",20L);
        boatRepository.save(boatInit);

        rentalInit = new Rental();
        rentalInit.setUser(userInit);
        rentalInit.setBoat(boatInit);
        rentalInit.setHours(10L);
        rentalInit.setTotalHours(200L);
        rentalInit.setDateInit(new Date()); 
        rentalInit.setDateEnd(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24))); 
    }

    @DisplayName(value = "Save rental")
    @Test
    public void saveRental(){
        // given 
        rentalRepository.save(rentalInit);
        
        // when
        Rental rental = rentalRepository.findByUserId(userInit.getId()).get();
        
        // then
        assertNotNull(rental);
        assertEquals(rental.getTotalHours(), 200L);
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

    @DisplayName(value = "Update rental")
    @Test
    public void testUpdateRental(){
        // given
        rentalRepository.save(rentalInit);
        Rental rental = rentalRepository.findByUserId(userInit.getId()).get();
        rental.setTotalHours(300L);
        rentalRepository.save(rental);

        // when
        Rental rentaldb = rentalRepository.findByUserId(userInit.getId()).get();

        // then
        assertEquals(rentaldb.getTotalHours(), 300L);
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
