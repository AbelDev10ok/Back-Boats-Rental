package com.manager.boats.rental.boats_rental.controllers;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.mockito.BDDMockito;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; 
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.boats.rental.boats_rental.persistence.models.Marin;
import com.manager.boats.rental.boats_rental.services.implementation.MarinServices;
import com.manager.boats.rental.boats_rental.util.ValidationEntities;
import com.manager.boats.rental.boats_rental.web.controller.MarinController;

@WebMvcTest(MarinController.class)
public class MarinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MarinServices marinServices;

    
    @MockBean
    private ValidationEntities validationEntities;


    private Marin marinInit;

    private List<Marin> marins = new ArrayList<>();

    @BeforeEach
    public void setUp(){
        marinInit = new Marin(1L,"NombreMarinero","ApellidoMarinero","12345678A");
        marins = List.of(marinInit, new Marin(2L,"NombreMarinero2","ApellidoMarinero2","12345678B"), new Marin(3L,"NombreMarinero3","ApellidoMarinero3","12345678C"));
    }

    @DisplayName("Test get All marin")
    @Test
    public void testGetAllMarins() throws Exception {
        // given
        given(marinServices.getAllMarins()).willReturn(marins);

        // when
        ResultActions response = mockMvc.perform(get("/api/v1/marins")
                                .contentType(MediaType.APPLICATION_JSON));


        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.messae", is("marins")))
                .andExpect(jsonPath("$.data[0].name", is(marins.get(0).getName())));
    }

    @DisplayName("Test get marin by id")
    @Test
    public void testGetMarinById() throws Exception {
        // given
        given(marinServices.getMarinById(1L)).willReturn(marinInit);

        // when
        ResultActions response = mockMvc.perform(get("/api/v1/marins/1")
                                    .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.messae", is("get marin")))
                .andExpect(jsonPath("$.data.name", is(marinInit.getName())));
    }

    @DisplayName("Test save marin")
    @Test
    public void testSaveMarin() throws Exception {
        // given
        willDoNothing().given(marinServices).saveMarin(any(Marin.class));


        ObjectMapper objectMapper = new ObjectMapper();
        String marinJson = objectMapper.writeValueAsString(marinInit);


        // when
        ResultActions response = mockMvc.perform(post("/api/v1/marins/save")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(marinJson));

        // then
            response.andExpect(status().isOk())
                    .andDo(print());;
    }

    @DisplayName("Test update Marin")
    @Test
    public void updateMarin() throws Exception{
        // given
        willDoNothing().given(marinServices).updateMarin(any(Marin.class), any(Long.class));
        
        ObjectMapper objectMapper = new ObjectMapper();
        String marinJson = objectMapper.writeValueAsString(marinInit);

        // when
        ResultActions response = mockMvc.perform(put("/api/v1/marins/" + marinInit.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(marinJson));

        // then
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
