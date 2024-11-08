package com.manager.boats.rental.boats_rental.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.manager.boats.rental.boats_rental.persistence.models.Boat;
import com.manager.boats.rental.boats_rental.persistence.models.Marin;


@DataJpaTest // SOLO PRUEBA CAPA DE PERSISTENCIA (ENTIDADES Y REPOSITORIOS)
@AutoConfigureTestDatabase(replace = Replace.NONE)//para que ejecute a baseses de datos real
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class MarinRepositoryTest {

    @Autowired
    private IMarinRepository marinRepository;


    private Marin marinInit;

    @BeforeEach
    public void setUp(){
        marinInit = new Marin(1L,"NombreMarinero","ApellidoMarinero","12345678A");
    }

    @DisplayName(value = "Save marin")
    @Test
    public void saveMarin(){
        // BDD - estrategia a aplciar (usuario y comportamiento del sistema)

        // given
        marinRepository.save(marinInit);
        
        // when
        Marin marin = marinRepository.findById(1L).get();
        
        // then
        assertNotNull(marin);
        assertEquals(marin.getName(), "NombreMarinero");
    }

    @DisplayName(value = "List all marin")
    @Test
    public void getListAllMarin(){
        
        // given
        Iterable<Marin> marin = marinRepository.findAll();
        
        // then
        assertNotNull(marin);
    }

    @DisplayName(value = "get marin by id")
    @Test
    public void testGetMarinById(){
        // given
        marinRepository.save(marinInit);

        // when
        Marin marin = marinRepository.findById(1L).get();
        
        // then
        assertNotNull(marin);
        assertEquals(marin.getName(), "NombreMarinero");
    }
    

    @DisplayName(value = "List no empty")
    @Test
    public void testListNoEmpty(){
        List<Marin> marin = marinRepository.findAll();
        if(marin.isEmpty()){
            fail();
        }
    }   

    @DisplayName(value = "Update marin")
    @Test
    public void testUpdateMarin(){
        // given

        marinRepository.save(marinInit);
        Marin marin = marinRepository.findById(1L).get();
        marin.setName("Roberto");
        marinRepository.save(marin);
        
        // when
        Marin marindb = marinRepository.findById(1L).get();
        
        // then
        assertEquals(marindb.getName(), "Roberto");
    }

    @DisplayName(value = "Delete marin")
    @Test
    public void testDeleteMarin(){
        // given
        marinRepository.save(marinInit);
        boolean marin = marinRepository.findById(1L).isPresent();
        if(!marin){
            fail();
        }
        // when
        marinRepository.deleteById(1L);
        boolean noExisteDespuesDeEliminar = marinRepository.findById(1L).isPresent();

        // then
        assertTrue(marin);
        assertFalse(noExisteDespuesDeEliminar);
    }
}
