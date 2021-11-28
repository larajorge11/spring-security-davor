package com.davor.security.davorsecurity.web.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CustomerControllerTest extends BaseIT {

    @DisplayName(value = "List Customers")
    @Nested
    class ListCustomer {
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

    @DisplayName(value = "Add Customers")
    @Nested
    class AddCustomers {

        @Rollback
        @Test
        void processCreationForm() throws Exception {
            mockMvc.perform(post("/customers/new")
                    .param("customerName", "Foo Customer")
                    .with(httpBasic("jorge", "davor1989")))
                    .andExpect(status().is3xxRedirection());
        }

        @Rollback
        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource(value = "com.davor.security.davorsecurity.web.controllers.CustomerControllerTest#getStreamNotAdminUsers")
        void processCreationFormNoAuthorized(String user, String password) throws Exception {
            mockMvc.perform(post("/customers/new")
                            .param("customerName", "Foo Customer 2")
                            .with(httpBasic(user, password)))
                    .andExpect(status().isForbidden());
        }

        @Rollback
        @Test
        void processCreationFormNoLoggedIn() throws Exception {
            mockMvc.perform(post("/customers/new")
                            .param("customerName", "Foo Customer"))
                    .andExpect(status().isUnauthorized());
        }

    }

}