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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print; 


import java.util.Arrays;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.manager.boats.rental.boats_rental.persistence.models.Rental;
import com.manager.boats.rental.boats_rental.services.implementation.RentalServices;
import com.manager.boats.rental.boats_rental.util.ValidationEntities;
import com.manager.boats.rental.boats_rental.web.controller.RentalController;
import com.manager.boats.rental.boats_rental.web.controller.dto.RentalDto;

@WebMvcTest(RentalController.class)
public class RentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RentalServices rentalServices;

    @MockBean
    private ValidationEntities  validationEntities;

    private RentalDto rentalDtoInit;


    @BeforeEach
    public void setUp(){
        rentalDtoInit = new RentalDto("2025-01-05","2026-01-06",4L);
    }

    @DisplayName("Test save rental")
    @Test
    public void testSaveRental() throws Exception {
        // given
        willDoNothing().given(rentalServices).save(any(RentalDto.class),any(Long.class),any(Long.class));


        ObjectMapper objectMapper = new ObjectMapper();
        String rentalDtoJson = objectMapper.writeValueAsString(rentalDtoInit);


        // when
        ResultActions response = mockMvc.perform(post("/api/v1/rentals/clientId/1/boatsId/1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(rentalDtoJson));
        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.messae", is("sucess")))
                .andDo(print());
    }

    @DisplayName("Test update rental")
    @Test
    public void testUpdateRental() throws Exception {
        // given
        willDoNothing().given(rentalServices).updateProduct(any(RentalDto.class),any(Long.class));

        ObjectMapper objectMapper = new ObjectMapper();
        String rentalDtoJson = objectMapper.writeValueAsString(rentalDtoInit);

        // when
        ResultActions response = mockMvc.perform(put("/api/v1/rentals/update/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(rentalDtoJson));
        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.messae", is("update")))
                .andDo(print())
                .andReturn();
    }

    @DisplayName("Test delete rental")
    @Test
    public void testDeleteRental() throws Exception{
        // given
        willDoNothing().given(rentalServices).delete(any(Long.class));
    
        // when
        ResultActions response = mockMvc.perform(delete("/api/v1/rentals/1"));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.messae", is("delete")))
                .andDo(print());
    }

    @DisplayName("Test get rentals")
    @Test
    public void testGetRentals() throws Exception{
        List<Rental> rentals = Arrays.asList(new Rental(),new Rental());
        // given
        given(rentalServices.getAll()).willReturn(rentals);

        // when
        ResultActions response = mockMvc.perform(get("/api/v1/rentals")
                                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.messae", is("suscces")))
                .andDo(print());
    }
}
