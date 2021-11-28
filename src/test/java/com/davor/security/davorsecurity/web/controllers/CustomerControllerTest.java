package com.davor.security.davorsecurity.web.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CustomerControllerTest extends BaseIT {

    @ParameterizedTest(name = "#{index} with [{arguments}]")
    @MethodSource(value = "com.davor.security.davorsecurity.web.controllers.CustomerControllerTest#getStreamAdminCustomerUsers")
    void testListAuthorizedRoles(String user, String password) throws Exception {
        mockMvc.perform(get("/customers").with(httpBasic(user, password)))
                .andExpect(status().isOk());
    }

    @Test
    void testListNoLoggedIn() throws Exception {
        mockMvc.perform(get("/customers"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testListNoAuthorizedRole() throws Exception {
        mockMvc.perform(get("/customers")
                        .with(httpBasic("jacobo", "davor1989")))
                .andExpect(status().isForbidden());
    }
}