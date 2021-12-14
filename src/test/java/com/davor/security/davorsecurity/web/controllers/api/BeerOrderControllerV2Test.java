package com.davor.security.davorsecurity.web.controllers.api;

import com.davor.security.davorsecurity.bootstrap.DefaultBreweryLoader;
import com.davor.security.davorsecurity.domain.Beer;
import com.davor.security.davorsecurity.domain.BeerOrder;
import com.davor.security.davorsecurity.domain.Customer;
import com.davor.security.davorsecurity.repositories.BeerOrderRepository;
import com.davor.security.davorsecurity.repositories.BeerRepository;
import com.davor.security.davorsecurity.repositories.CustomerRepository;
import com.davor.security.davorsecurity.web.controllers.BaseIT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BeerOrderControllerV2Test extends BaseIT {
    public static final String API_ROOT = "/api/v2/orders/";

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    ObjectMapper objectMapper;

    Customer tecniCustomer;
    Customer petsCustomer;
    Customer brandCustomer;
    List<Beer> loadedBeers;

    @BeforeEach
    void setUp() {
        tecniCustomer = customerRepository.findAllByCustomerName(DefaultBreweryLoader.TECNI_RODAMIENTOS).orElseThrow();
        petsCustomer = customerRepository.findAllByCustomerName(DefaultBreweryLoader.PETS_GROOMING).orElseThrow();
        brandCustomer = customerRepository.findAllByCustomerName(DefaultBreweryLoader.BRANDIO).orElseThrow();
        loadedBeers = beerRepository.findAll();
    }

    @Test
    void listOrdersNotAuth() throws Exception {
        mockMvc.perform(get(API_ROOT))
                .andExpect(status().isUnauthorized());
    }

    @WithUserDetails(value = "jorge")
    @Test
    void listOrdersAdminAuth() throws Exception {
        mockMvc.perform(get(API_ROOT))
                .andExpect(status().isOk());
    }

    @WithUserDetails(value = DefaultBreweryLoader.USER_TECNI)
    @Test
    void listOrdersCustomerAuth() throws Exception {
        mockMvc.perform(get(API_ROOT))
                .andExpect(status().isOk());
    }

    @WithUserDetails(value = DefaultBreweryLoader.USER_PETS)
    @Test
    void listOrdersCustomerPetsAuth() throws Exception {
        mockMvc.perform(get(API_ROOT))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    void getByOrderIdNotAuth() throws Exception {
        BeerOrder beerOrder = tecniCustomer.getBeerOrders().stream().findFirst().orElseThrow();

        mockMvc.perform(get(API_ROOT + beerOrder.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Transactional
    @WithUserDetails("jorge")
    @Test
    void getByOrderIdADMIN() throws Exception {
        BeerOrder beerOrder = tecniCustomer.getBeerOrders().stream().findFirst().orElseThrow();

        mockMvc.perform(get(API_ROOT + beerOrder.getId()))
                .andExpect(status().is2xxSuccessful());
    }

    @Transactional
    @WithUserDetails(DefaultBreweryLoader.USER_TECNI)
    @Test
    void getByOrderIdCustomerAuth() throws Exception {
        BeerOrder beerOrder = tecniCustomer.getBeerOrders().stream().findFirst().orElseThrow();

        mockMvc.perform(get(API_ROOT + beerOrder.getId()))
                .andExpect(status().is2xxSuccessful());
    }

    @Transactional
    @WithUserDetails(DefaultBreweryLoader.USER_PETS)
    @Test
    void getByOrderIdCustomerNOTAuth() throws Exception {
        BeerOrder beerOrder = tecniCustomer.getBeerOrders().stream().findFirst().orElseThrow();

        mockMvc.perform(get(API_ROOT + beerOrder.getId()))
                .andExpect(status().isNotFound());
    }
}
