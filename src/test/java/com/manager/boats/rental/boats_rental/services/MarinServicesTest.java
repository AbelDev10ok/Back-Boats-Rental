package com.manager.boats.rental.boats_rental.services;

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

import com.manager.boats.rental.boats_rental.persistence.models.Marin;
import com.manager.boats.rental.boats_rental.repositories.IMarinRepository;
import com.manager.boats.rental.boats_rental.services.exception.NotFoundException;
import com.manager.boats.rental.boats_rental.services.implementation.MarinServices;

import net.bytebuddy.description.type.TypeList.Empty;




// cargamos extensiones de mockito con junit5
@ExtendWith(MockitoExtension.class)
public class MarinServicesTest {

    @Mock
    private IMarinRepository  marinRepository;

    @InjectMocks
    private MarinServices marinService;

    private Marin marinInit;

    private List<Marin> marinsInit;
 
    @BeforeEach
    public void setUp(){
        marinInit = new Marin(1L,"NombreMarinero","ApellidoMarinero","12345678A");
    }


    @DisplayName("Test for save marin")
    @Test
    void testSaveMarin(){
        // given
        given(marinRepository.save(any(Marin.class))).willReturn(marinInit);
        // when
        marinService.saveMarin(marinInit);
        // then
        verify(marinRepository, times(1)).save(any(Marin.class));
    }

    @DisplayName("Test get all marins")
    @Test
    public void testGetAllMarins(){
        // given
        given(marinRepository.findAll()).willReturn(List.of(marinInit));

        // when
        List<Marin> marins = marinService.getAllMarins();
        
        // then
        assertNotNull(marins);
    }

    @DisplayName("Test get all marins empty")
    @Test
    public void testGetMarinsEmpty(){
        // given
        given(marinRepository.findAll()).willReturn(Collections.emptyList());
        
        // when
        List<Marin> marins = marinService.getAllMarins();

        // then
        assertEquals(marins.size(), 0);    
    }

    @DisplayName("Test get marin by id")
    @Test
    public void testGetMarinById(){
        // given
        given(marinRepository.findById(1L)).willReturn(Optional.of(marinInit));
        // when
        Marin marin = marinService.getMarinById(1L);
        // then
        assertEquals(marin.getId(), 1);
    }

    @DisplayName("Test get marin by id not found")
    @Test
    public void testGetMarinByIdNotFound(){
        // given
        given(marinRepository.findById(1L)).willThrow(NotFoundException.class);

        // when
        assertThrows(NotFoundException.class, 
            ()-> marinService.getMarinById(1L));

        // then
    }

    @DisplayName("Test delete marin")
    @Test
    public void testDeletMarinById(){
        // given
        given(marinRepository.findById(1L)).willReturn(Optional.of(marinInit));
        willDoNothing().given(marinRepository).deleteById(1L);

        // when
        marinService.deleteMarinById(1L);

        // then
        verify(marinRepository,times(1)).deleteById(1L);;
    }

    @DisplayName("Test delete marin (not found)")
    @Test
    public void testDeletMarinByIdNotFound() {
        // given
        given(marinRepository.findById(1L)).willThrow(NotFoundException.class);

        // when / then
        assertThrows(NotFoundException.class, () -> {
            marinService.deleteMarinById(1L); 
        });

        verify(marinRepository, never()).deleteById(any()); // Verificar que deleteById NO se llama
    }

    @DisplayName("Test update marin")
    @Test
    public void testUpdateMarin() {
        // Given
        Long marinId = 1L; 
        Marin marinActualizado = new Marin(marinId, "NombreActualizado", "ApellidoMarinero", "12345678A"); 

        given(marinRepository.findById(marinId)).willReturn(Optional.of(marinInit)); 
        given(marinRepository.save(any(Marin.class))).willReturn(marinActualizado); // Be specific here!

        // When
        marinService.updateMarin(marinActualizado, 1L); 

        // Then
        verify(marinRepository, times(1)).save(marinInit); 
    }

    @DisplayName("Test update marin (Not found)")
    @Test
    public void testUpdateMarinNotFound(){
        // Given
        Long marinId = 1L; 
        Marin marinActualizado = new Marin(marinId, "NombreActualizado", "ApellidoMarinero", "12345678A"); 

        given(marinRepository.findById(marinId)).willThrow(NotFoundException.class); 

        // When
        assertThrows(NotFoundException.class, () -> {
            marinService.updateMarin(marinActualizado, 1L);
        });

        // Then
        verify(marinRepository,never()).save(marinInit); 
    }
    
}
