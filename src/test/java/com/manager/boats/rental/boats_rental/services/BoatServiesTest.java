package com.manager.boats.rental.boats_rental.services;

import java.beans.Transient;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.manager.boats.rental.boats_rental.persistence.models.Boat;
import com.manager.boats.rental.boats_rental.persistence.models.Marin;
import com.manager.boats.rental.boats_rental.repositories.IBoatRepository;
import com.manager.boats.rental.boats_rental.repositories.IMarinRepository;
import com.manager.boats.rental.boats_rental.services.exception.NotFoundException;
import com.manager.boats.rental.boats_rental.services.implementation.BoatServices;
import com.manager.boats.rental.boats_rental.web.controller.dto.BoatDto;

// cargamos extensiones de mockito con junit5
@ExtendWith(MockitoExtension.class)
public class BoatServiesTest {

    // creamos un simlulacro
    @Mock
    private IBoatRepository boatRepository;
    @Mock
    private IMarinRepository marinRepository;
    
    // se encarga de injectarlo dentro del mock
    @InjectMocks
    private BoatServices boatService;

    private Boat boatInit;
    

    private BoatDto boatDtoInit;

       @BeforeEach
    public void setUp(){
        boatInit = new Boat(222222L,"velero",40L,"jenny","xr","true",20L);

        boatDtoInit = new BoatDto();
        boatDtoInit.setTuition(boatInit.getTuition());
        boatDtoInit.setAbility(boatInit.getAbility());
        boatDtoInit.setModel(boatInit.getModel());
        boatDtoInit.setName(boatInit.getName());
        boatDtoInit.setPriceHours(boatInit.getPriceHours());
        boatDtoInit.setType(boatInit.getType());
    }


    @DisplayName("Test for save boat")
    @Test
    void testSaveBoat(){
        // given
        given(boatRepository.save(boatInit)).willReturn(boatInit);
        // when
        boatService.save(boatDtoInit);
        // then
    }

    @DisplayName("Test with exception for save boat")
    @Test
    void testSaveBoatExceptions(){
        // given
        given(boatRepository.findById(boatInit.getTuition())).willReturn(Optional.of(boatInit));
        // when
        assertThrows(NotFoundException.class, () ->{
            boatService.save(boatDtoInit);

        });
        // then
        verify(boatRepository,never()).save(any(Boat.class));
    }

    @DisplayName("Test for get all boats")
    @Test
    void testGetAllBoats(){
        // given
        given(boatRepository.findAll()).willReturn(List.of(boatInit));
        // when
        List<Boat> boats = boatService.getAll();
        // then
        assertNotNull(boats);
        assertEquals(1,boats.size());
    }

    @Test
    void getListEmpty(){
        // given
        given(boatRepository.findAll()).willReturn(Collections.emptyList());
        // when
        List<Boat> boats = boatService.getAll();
        // then
        assertNull(boats);
    }


    @DisplayName("Test for get boat")
    @Test
    void testGetBoat(){
        // given
        given(boatRepository.findById(222222L)).willReturn(Optional.of(boatInit));
        // when
        Boat boat = boatService.getById(boatInit.getTuition());
        // then
        assertEquals(boatInit.getTuition(), 222222L);
        assertNotNull(boat);
    }

    @DisplayName("Test for update boat")
    @Test
    void testUpdateBoat(){
    // given
    given(boatRepository.findById(boatInit.getTuition())).willReturn(Optional.of(boatInit));
    boatDtoInit.setType("titanic"); 

    // when
    boatService.updateProduct(boatDtoInit, boatInit.getTuition()); 
    Boat boat = boatService.getById(boatInit.getTuition());

    // then
    assertEquals(boat.getType(), "titanic");
    }

    @DisplayName("Test for delete boat (boat not found)")
    @Test
    void deleteBoatNotFound() {
        // Given: Boat does NOT exist
        given(boatRepository.findById(boatInit.getTuition())).willReturn(Optional.empty());
    
        // When/Then: Expect an exception (or handle gracefully)
        assertThrows(NotFoundException.class, () -> {
            boatService.delete(boatInit.getTuition());
        });
    
        // Also verify that delete is NEVER called on the repository
        verify(boatRepository, never()).delete(any()); 
    }
    
    @DisplayName("Test for delete boat (successful deletion)")
    @Test
    void deleteBoatSuccess() {
        // Given: Boat exists in the repository
        given(boatRepository.findById(boatInit.getTuition())).willReturn(Optional.of(boatInit));
        willDoNothing().given(boatRepository).deleteById(boatInit.getTuition());

        // When: delete method is called
        boatService.delete(boatInit.getTuition());

        // Then: Verify deleteById is called on the repository exactly once
        verify(boatRepository, times(1)).deleteById(boatInit.getTuition());
    }

    @DisplayName("Test for insert marin into boat")
    @Test
    void testInsertMarinIntoBoat(){
        // Given: Boat and Marin exist
        Long boatId = 222222L; // Usando el ID de tu boatInit
        Long marinId = 1L; // Puedes usar cualquier ID válido para el marinero

        Boat boat = new Boat(boatId, "velero", 40L, "jenny", "xr", "true", 20L);
        Marin marin = new Marin(marinId, "NombreMarinero", "ApellidoMarinero", "12345678A"); // Crea un mock de Marin

        given(boatRepository.findById(boatId)).willReturn(Optional.of(boat));
        given(marinRepository.findById(marinId)).willReturn(Optional.of(marin));

        // When: insertMarinInBoat is called
        boatService.insertMarinInBoat(marinId, boatId);

        // Then: Verify the Marin is added to the Boat and saved
        verify(marinRepository, times(1)).save(marin);
        assertTrue(marin.getBoats().contains(boat));

        // Then: Verify the Boat is updated with the Marin and saved
        verify(boatRepository, times(1)).save(boat);
        assertEquals(marin, boat.getMarin()); 
    }


}
