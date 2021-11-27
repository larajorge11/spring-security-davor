package com.davor.security.davorsecurity.web.controllers;

import com.davor.security.davorsecurity.repositories.BeerInventoryRepository;
import com.davor.security.davorsecurity.repositories.BeerRepository;
import com.davor.security.davorsecurity.repositories.CustomerRepository;
import com.davor.security.davorsecurity.services.BeerService;
import com.davor.security.davorsecurity.services.BreweryService;
import com.davor.security.davorsecurity.services.security.JpaUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class IndexControllerTest extends BaseIT {

    @MockBean
    BeerRepository beerRepository;

    @MockBean
    BeerInventoryRepository beerInventoryRepository;

    @MockBean
    BreweryService breweryService;

    @MockBean
    CustomerRepository customerRepository;

    @MockBean
    BeerService beerService;

    @MockBean
    JpaUserDetailsService jpaUserDetailsService;

    @Test
    void testGetIndexSlash() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

}