package com.manager.boats.rental.boats_rental.controllers;

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

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.boats.rental.boats_rental.persistence.models.Boat;
import com.manager.boats.rental.boats_rental.services.exception.NotFoundException;
import com.manager.boats.rental.boats_rental.services.implementation.BoatServices;
import com.manager.boats.rental.boats_rental.util.ValidationEntities;
import com.manager.boats.rental.boats_rental.web.controller.BoatController;
import com.manager.boats.rental.boats_rental.web.controller.dto.BoatDto;


@WebMvcTest(BoatController.class)
public class BoatControllerTest {
        
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoatServices boatService;

    @MockBean
    private ValidationEntities validationEntities;
    
    private BoatDto boatDto;


    @BeforeEach
    public void setUp(){
        boatDto = new BoatDto();
        boatDto.setTuition(1234L);
        boatDto.setName("abel");
        boatDto.setType("velero");  
        boatDto.setAbility(50L);
        boatDto.setModel("rr");
        boatDto.setPriceHours(20L); 
    }


    @Test
    public void testGetBoats() throws Exception {
        // Given
        Boat boat1 = new Boat(1L, "velero", 40L, "jenny", "xr", "true", 20L);
        Boat boat2 = new Boat(2L, "motor", 25L, "Seafarer", "xx", "false", 10L);
        List<Boat> boats = Arrays.asList(boat1,boat2
        );
        given(boatService.getAll()).willReturn(boats);

        // When
        ResultActions response = mockMvc.perform(get("/api/v1/boats")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(boat1.getName())))
                .andExpect(jsonPath("$[1].name", is(boat2.getName())))
                // Add more assertions for other fields as needed
                ;
    }

    @Test
    public void testGetBoatById() throws Exception {
        // Given
        Boat boat = new Boat(1L, "velero", 40L, "jenny", "xr", "true", 2L);
        given(boatService.getById(1L)).willReturn(boat);

        // when
        ResultActions response = mockMvc.perform(get("/api/v1/boats/1"));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is(boat.getName())))
                .andExpect(jsonPath("$.data.ability", is(40)))
                .andExpect(jsonPath("$.data.model", is(boat.getModel())))
                .andExpect(jsonPath("$.data.priceHours", is(2)));
    }

    @Test
    public void testGetBoatByIdNotFound() throws Exception {
        // Given
        given(boatService.getById(1L)).willThrow(NotFoundException.class);

        // when
        ResultActions response = mockMvc.perform(get("/api/v1/boats/1"));

        // then
        response.andExpect(status().isNotFound());

    }


    @Test
    public void testCreateBoat() throws Exception {
        // Given
        BoatDto boatDto = new BoatDto();
        boatDto.setTuition(1234L);
        boatDto.setName("abel");
        boatDto.setType("velero");  
        boatDto.setAbility(50L);
        boatDto.setModel("rr");
        boatDto.setPriceHours(20L);

        willDoNothing().given(boatService).save(any(BoatDto.class)); 
        // Convert the DTO to JSON

        ObjectMapper objectMapper = new ObjectMapper();
        String boatDtoJson = objectMapper.writeValueAsString(boatDto);

        // When
        ResultActions response = mockMvc.perform(post("/api/v1/boats/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(boatDtoJson));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.messae", is("create")))
                .andExpect(jsonPath("$.data.name", is("abel")));
    }

    @Test
    public void testDeletBoat() throws Exception{
        // given
        willDoNothing().given(boatService).delete(boatDto.getTuition());

        // when
        ResultActions response = mockMvc.perform(delete("/api/v1/boats/1234"));

        // then

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.messae", is("delete")));
    }

    @Test
    public void testDeletBoatNotFound() throws Exception{
        // given
        willThrow(NotFoundException.class).given(boatService).delete(1L);
        // when
        ResultActions response = mockMvc.perform(delete("/api/v1/boats/1"));

        // then
        response.andExpect(status().isNotFound());
    }


	@Test
	public void testUpdateBoat() throws Exception {
		// given
		willDoNothing().given(boatService).updateProduct(any(BoatDto.class), any(Long.class));
		ObjectMapper objectMapper = new ObjectMapper();
		String boatDtoJson = objectMapper.writeValueAsString(boatDto);

		// when
		ResultActions response = mockMvc.perform(put("/api/v1/boats/1234") // Assuming this is your correct endpoint
				.contentType(MediaType.APPLICATION_JSON)
				.content(boatDtoJson));

		// then
		response.andExpect(status().isOk())
				.andExpect(jsonPath("$.messae", is("update boat"))); 

		// Verify that updateProduct was called
		verify(boatService, times(1)).updateProduct(any(BoatDto.class), any(Long.class));
	}


	@Test
	public void testUpdateBoatNotFound() throws Exception {
		// Given
		Long nonExistentBoatId = 1L; // Choose an ID that doesn't exist
		willThrow(NotFoundException.class).given(boatService)
										.updateProduct(any(BoatDto.class), eq(nonExistentBoatId));

		ObjectMapper objectMapper = new ObjectMapper();
		String boatDtoJson = objectMapper.writeValueAsString(boatDto);

		// When
		ResultActions response = mockMvc.perform(put("/api/v1/boats/" + nonExistentBoatId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(boatDtoJson));

		// Then
		response.andExpect(status().isNotFound()); 
	}



}

