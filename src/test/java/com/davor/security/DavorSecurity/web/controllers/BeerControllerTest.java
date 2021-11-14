package com.davor.security.DavorSecurity.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
class BeerControllerTest extends BaseIT {

    @Test
    void authenticateUserSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/beer").with(httpBasic("user", "password")))
                .andExpect(status().isOk());

    }

    @Test
    void authenticateUserForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/beer").with(httpBasic("jorge", "password")))
                .andExpect(status().isUnauthorized());

    }

    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer"))
                .andExpect(status().isOk());
    }

//    @Test
//    void getBeerById() throws Exception {
//        mockMvc.perform(get("/api/v1/beer/0631234200036"))
//                .andExpect(status().isOk());
//    }

    @Test
    void getBeerByUpc() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
                .andExpect(status().isOk());
    }
}