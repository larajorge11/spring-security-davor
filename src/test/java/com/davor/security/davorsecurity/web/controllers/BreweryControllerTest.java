package com.davor.security.davorsecurity.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class BreweryControllerTest extends BaseIT {

    private static final String PASSWORD = "davor1989";

    @Test
    void testUserWithCustomerRole() throws Exception {
        mockMvc.perform(get("/brewery/breweries")
                .with(httpBasic("davor",PASSWORD)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testUserWithUserRole() throws Exception {
        mockMvc.perform(get("/brewery/breweries")
                        .with(httpBasic("jacobo",PASSWORD)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUserWithAdminRole() throws Exception {
        mockMvc.perform(get("/brewery/breweries")
                        .with(httpBasic("jorge",PASSWORD)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testListBreweriesNoAuth() throws Exception {
        mockMvc.perform(get("/brewery/breweries"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetBreweriesCustomer() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
                        .with(httpBasic("davor", PASSWORD)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testGetBreweriesAdmin() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
                        .with(httpBasic("jorge", PASSWORD)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testGetBreweriesUser() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
                        .with(httpBasic("jacobo", PASSWORD)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetBreweriesNoAuth() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries"))
                .andExpect(status().isUnauthorized());
    }
}