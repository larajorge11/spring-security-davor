package com.davor.security.davorsecurity.web.controllers.api;

import com.davor.security.davorsecurity.domain.Beer;
import com.davor.security.davorsecurity.repositories.BeerRepository;
import com.davor.security.davorsecurity.web.controllers.BaseIT;
import com.davor.security.davorsecurity.web.model.BeerStyleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class BeerRestControllerTest extends BaseIT {

    @Autowired
    private BeerRepository beerRepository;

    private static final String PASSWORD = "davor1989";

    @DisplayName(value = "Delete Tests")
    @Nested
    class DeleteTests {
        public Beer beerToDelete() {
            Random random = new Random();

            return beerRepository.saveAndFlush(Beer.builder()
                            .beerName("Delete Beer")
                            .beerStyle(BeerStyleEnum.IPA)
                            .minOnHand(12)
                            .quantityToBrew(200)
                            .upc(String.valueOf(random.nextInt(999999999)))
                    .build());
        }

        @Test
        void deleteBeerHttpBasic() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .with(httpBasic("jorge", PASSWORD)))
                    .andExpect(status().is2xxSuccessful());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource(value = "com.davor.security.davorsecurity.web.controllers.api.BeerRestControllerTest#getStreamNotAdminUsers")
        void deleteBeerNoAuthorizedRole(String user, String password) throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                            .with(httpBasic(user, password)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerHttpBasicNoAuth() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId()))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName(value = "List Beers")
    @Nested
    class ListBeers {

        @Test
        void findBeers() throws Exception {
            mockMvc.perform(get("/api/v1/beer/"))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource(value = "com.davor.security.davorsecurity.web.controllers.api.BeerRestControllerTest#getStreamAllUsers")
        void findBeerAuthenticated(String user, String password) throws Exception {
            mockMvc.perform(get("/api/v1/beer/")
                    .with(httpBasic(user, password)))
                    .andExpect(status().isOk());
        }
    }

    @DisplayName(value = "Get Beer By ID")
    @Nested
    class GetBeerById {

        @Test
        void findBeerById() throws Exception {
            Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(get("/api/v1/beer/" + beer.getId()))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource(value = "com.davor.security.davorsecurity.web.controllers.api.BeerRestControllerTest#getStreamAllUsers")
        void findBeerByIdAuthenticated(String user, String password) throws Exception {
            Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(get("/api/v1/beer/" + beer.getId())
                            .with(httpBasic(user, password)))
                    .andExpect(status().isOk());
        }
    }

    @DisplayName(value = "Get Beer By UPC")
    @Nested
    class GetBeerByUpc {

        @Test
        void getBeerByUpc() throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource(value = "com.davor.security.davorsecurity.web.controllers.api.BeerRestControllerTest#getStreamAllUsers")
        void findBeerByIdAuthenticated(String user, String password) throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/0631234200036")
                            .with(httpBasic(user, password)))
                    .andExpect(status().isOk());
        }
    }

}