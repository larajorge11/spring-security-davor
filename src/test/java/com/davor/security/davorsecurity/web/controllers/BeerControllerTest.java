package com.davor.security.davorsecurity.web.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
class BeerControllerTest extends BaseIT {

    private static final String PASSWORD = "davor1989";

//    @DisplayName(value = "Init New Form")
//    @Nested
//    class InitNewForm {
//
//        @Test
//        void initCreationFormAuth() throws Exception {
//            mockMvc.perform(get("/beers/new")
//                    .with(httpBasic("jorge", PASSWORD)))
//                    .andExpect(status().isOk())
//                    //.andExpect(view().name("beers/createBeer"))
//                    .andExpect(model().attributeExists("beer"));
//        }
//
//        void initCreationFormNoAuth() throws Exception {
//            mockMvc.perform(get("/beers/new"))
//                    .andExpect(status().isUnauthorized());
//        }
//    }

    @DisplayName(value = "Init Find Beer Form")
    @Nested
    class FindForm{

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("com.davor.security.davorsecurity.web.controllers.BeerControllerTest#getStreamAllUsers")
        void findBeersFormAuth(String user, String pwd) throws Exception {
            mockMvc.perform(get("/beers/find").with(httpBasic(user, pwd)))
                    .andExpect(status().isOk())
                    .andExpect(view().name("beers/findBeers"))
                    .andExpect(model().attributeExists("beer"));
        }

        @Test
        void findBeersFormNoAuth() throws Exception {
            mockMvc.perform(get("/beers/find").with(anonymous()))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Test
    void testFindBeerFormAdminRole() throws Exception {
        mockMvc.perform(get("/beers").param("beerName", "")
                        .with(httpBasic("jorge", PASSWORD)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteBeer() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/9b867954-a78a-4aa6-9ea7-4fb973c2fa13")
                .header("Api-Key", "jorge")
                .header("Api-Secret", "davor1989"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteBeerCustomerRole() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/9b867954-a78a-4aa6-9ea7-4fb973c2fa13")
                        .with(httpBasic("davor", "davor1989")))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteBeerUserRole() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/9b867954-a78a-4aa6-9ea7-4fb973c2fa13")
                        .with(httpBasic("jacobo", "davor1989")))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteBeerBadCredentials() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/9b867954-a78a-4aa6-9ea7-4fb973c2fa13")
                        .header("Api-Key", "test")
                        .header("Api-Secret", "test1989"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDeleteBeerCredentialsFromUrlSuccess() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/9b867954-a78a-4aa6-9ea7-4fb973c2fa13")
                        .param("Api-Key", "jacobo")
                        .param("Api-Secret", "davor1989"))
                .andExpect(status().isOk());
    }

    @Test
    void authenticateUserSHA516Success() throws Exception {
        mockMvc.perform(get("/api/v1/beer").with(httpBasic("davor", PASSWORD)))
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

//    @Test
//    void getBeerById() throws Exception {
//        mockMvc.perform(get("/api/v1/beer/0631234200036"))
//                .andExpect(status().isOk());
//    }

}