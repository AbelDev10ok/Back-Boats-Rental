package com.manager.boats.rental.boats_rental.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import org.springframework.test.annotation.Rollback;

import com.manager.boats.rental.boats_rental.persistence.models.Boat;

@DataJpaTest // SOLO PRUEBA CAPA DE PERSISTENCIA (ENTIDADES Y REPOSITORIOS)
@AutoConfigureTestDatabase(replace = Replace.NONE)//para que ejecute a baseses de datos real
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class BoatRepository {

    @Autowired
    private IBoatRepository boatRepository;

    private Boat boatInit;


    @BeforeEach
    public void setUp(){
        boatInit = new Boat(222222L,"velero",40L,"jenny","xr","true",20L);

    }

    @Test
    // @Rollback(false)//para que me guarde en db real
    @Order(1)
    @DisplayName("Guardar nuevo Bote")
    public void guardarBote(){
        // BDD - estrategia a aplciar (usuario y comportamiento del sistema)

        // given - dado o condicion
        Boat boat = new Boat(1231231231L,"velero",40L,"jenny","xr","true",20L);

        // when - accion o el comportamiento
        Boat boatdb = boatRepository.save(boat);

        // then - verificar la salida
        assertNotNull(boatdb);
    }

    @Test
    @DisplayName("Listar todos los marin")
    public void listarMarines(){
        Iterable<Boat> boat = boatRepository.findAll();
        assertNotNull(boat);
    }

    @Test
    @DisplayName("Listar por Boat by id")
    public void listarPorId(){
        Boat newBoat = new Boat(123312323414L,"velero",40L,"jenny","xr","true",20L);    
        boatRepository.save(newBoat);
        Boat boat = boatRepository.findByTuition(123312323414L).get();
        assertNotNull(boat);
    }

    @Test
    public void listadoNoVacio(){
        List<Boat> boat = boatRepository.findAll();
        for(Boat b : boat){
            System.out.println(b.getName());
        }
        if(boat.isEmpty()){
            fail();
        }
    }

    @Test
    // interactua con la base de datos
    // @Rollback(false)
    @DisplayName("Actualizar boat")
    public void actualizarBoat(){
        Boat newBoat = new Boat(111111111L,"velero",40L,"jenny","xr","true",20L);    

        boatRepository.save(newBoat);
        Boat boat = boatRepository.findByTuition(newBoat.getTuition() ).get();
        boat.setName("Roberto");
        boatRepository.save(boat);
        Boat boatdb = boatRepository.findByTuition(boatInit.getTuition()).get();
        assertEquals(boatdb.getName(),"Roberto");
    }

    @Test
    // @Rollback(false)
    @DisplayName("Eliminar boat")
    public void eliminarBoat(){
        Boat newBoat = new Boat(2323234L,"velero",40L,"jenny","xr","true",20L);    
        boatRepository.save(newBoat);
        boolean boat = boatRepository.findByTuition(2323234L).isPresent();
        if(!boat){
            fail();
        }
        boatRepository.deleteByTuition(2323234L);
        boolean noExisteDespuesDeEliminar = boatRepository.findByTuition(2323234L).isPresent();
        
        assertTrue(boat);
        assertFalse(noExisteDespuesDeEliminar);
    }
}
