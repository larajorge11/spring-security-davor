package com.davor.security.davorsecurity.web.controllers.api;

import com.davor.security.davorsecurity.bootstrap.DefaultBreweryLoader;
import com.davor.security.davorsecurity.domain.Beer;
import com.davor.security.davorsecurity.domain.Customer;
import com.davor.security.davorsecurity.repositories.BeerOrderRepository;
import com.davor.security.davorsecurity.repositories.BeerRepository;
import com.davor.security.davorsecurity.repositories.CustomerRepository;
import com.davor.security.davorsecurity.web.controllers.BaseIT;
import com.davor.security.davorsecurity.web.model.BeerOrderDto;
import com.davor.security.davorsecurity.web.model.BeerOrderLineDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
class BeerOrderControllerTest extends BaseIT {
    public static final String API_ROOT = "/api/v1/customers/";

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

//cant use nested tests bug - https://github.com/spring-projects/spring-security/issues/8793
//    @DisplayName("Create Test")
//    @Nested
//    class createOrderTests {


    @Test
    void createOrderNotAuth() throws Exception {
        BeerOrderDto beerOrderDto = buildOrderDto(tecniCustomer, loadedBeers.get(0).getId());

        mockMvc.perform(post(API_ROOT + tecniCustomer.getId() + "/orders")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerOrderDto)))
                .andExpect(status().isUnauthorized());
    }

    @WithUserDetails("spring")
    @Test
    void createOrderUserAdmin() throws Exception {
        BeerOrderDto beerOrderDto = buildOrderDto(tecniCustomer, loadedBeers.get(0).getId());

        mockMvc.perform(post(API_ROOT + tecniCustomer.getId() + "/orders")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerOrderDto)))
                .andExpect(status().isCreated());
    }

    @WithUserDetails(DefaultBreweryLoader.USER_TECNI)
    @Test
    void createOrderUserAuthCustomer() throws Exception {
        BeerOrderDto beerOrderDto = buildOrderDto(tecniCustomer, loadedBeers.get(0).getId());

        mockMvc.perform(post(API_ROOT + tecniCustomer.getId() + "/orders")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerOrderDto)))
                .andExpect(status().isCreated());
    }

    @WithUserDetails(DefaultBreweryLoader.USER_PETS)
    @Test
    void createOrderUserNOTAuthCustomer() throws Exception {
        BeerOrderDto beerOrderDto = buildOrderDto(tecniCustomer, loadedBeers.get(0).getId());

        mockMvc.perform(post(API_ROOT + tecniCustomer.getId() + "/orders")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerOrderDto)))
                .andExpect(status().isForbidden());
    }

    // }
    @Test
    void listOrdersNotAuth() throws Exception {
        mockMvc.perform(get(API_ROOT + tecniCustomer.getId() + "/orders"))
                .andExpect(status().isUnauthorized());
    }

    @WithUserDetails(value = "spring")
    @Test
    void listOrdersAdminAuth() throws Exception {
        mockMvc.perform(get(API_ROOT + tecniCustomer.getId() + "/orders"))
                .andExpect(status().isOk());
    }

    @WithUserDetails(value = DefaultBreweryLoader.USER_TECNI)
    @Test
    void listOrdersCustomerAuth() throws Exception {
        mockMvc.perform(get(API_ROOT + tecniCustomer.getId() + "/orders"))
                .andExpect(status().isOk());
    }

    @WithUserDetails(value = DefaultBreweryLoader.USER_PETS)
    @Test
    void listOrdersCustomerNOTAuth() throws Exception {
        mockMvc.perform(get(API_ROOT + tecniCustomer.getId() + "/orders"))
                .andExpect(status().isForbidden());
    }

    @Test
    void listOrdersNoAuth() throws Exception {
        mockMvc.perform(get(API_ROOT + tecniCustomer.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Disabled
    @Test
    void pickUpOrderNotAuth() {
    }

    @Disabled
    @Test
    void pickUpOrderNotAdminUser() {
    }

    @Disabled
    @Test
    void pickUpOrderCustomerUserAUTH() {
    }

    @Disabled
    @Test
    void pickUpOrderCustomerUserNOT_AUTH() {
    }

    private BeerOrderDto buildOrderDto(Customer customer, UUID beerId) {
        List<BeerOrderLineDto> orderLines = Arrays.asList(BeerOrderLineDto.builder()
                .id(UUID.randomUUID())
                .beerId(beerId)
                .orderQuantity(5)
                .build());

        return BeerOrderDto.builder()
                .customerId(customer.getId())
                .customerRef("123")
                .orderStatusCallbackUrl("http://example.com")
                .beerOrderLines(orderLines)
                .build();
    }
}