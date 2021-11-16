package com.davor.security.DavorSecurity.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
class BeerControllerTest extends BaseIT {

    private static final String PASSWORD = "davor1989";

    @Test
    void testDeleteBeer() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/d2509314-7cc3-4515-951f-e08edb21e2d2")
                .header("Api-Key", "davor-api-key")
                .header("Api-Secret", "davor-secret"))
                .andExpect(status().isOk());
    }

    @Test
    void authenticateUserSHA516Success() throws Exception {
        mockMvc.perform(get("/api/v1/beer").with(httpBasic("user", PASSWORD)))
                .andExpect(status().isOk());

    }

    @Test
    void authenticateJacoboBcryptSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/beer").with(httpBasic("jacobo", PASSWORD)))
                .andExpect(status().isOk());

    }

    @Test
    void authenticateJorgeLdapSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/beer").with(httpBasic("jorge", PASSWORD)))
                .andExpect(status().isOk());

    }

    @Test
    void authenticateUserSHA516Forbidden() throws Exception {
        mockMvc.perform(get("/api/v1/beer").with(httpBasic("user", "password")))
                .andExpect(status().isUnauthorized());

    }

    @Test
    void authenticateJacoboBcryptForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/beer").with(httpBasic("jacobo", "password")))
                .andExpect(status().isUnauthorized());

    }

    @Test
    void authenticateJorgeLdpaForbidden() throws Exception {
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