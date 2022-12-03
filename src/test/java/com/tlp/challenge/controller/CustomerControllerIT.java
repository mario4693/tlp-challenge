package com.tlp.challenge.controller;

import com.tlp.challenge.builder.CustomerBuilder;
import com.tlp.challenge.entity.Customer;
import com.tlp.challenge.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class CustomerControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    private final static String BASE_PATH = "http://localhost:8080/api/v1";
    private final static Customer aCustomer = new CustomerBuilder().withName("Mario").build();

    @AfterEach
    void clearCustomerRepo(){
        customerRepository.deleteAll();
    }

    @Test
    public void createCustomer_shouldReturn201WhenCustomerIsCreated() throws Exception {
        mockMvc.perform(post(BASE_PATH + "/customers")
                        .content("""
                                {
                                    "name":"Mario",
                                    "surname":"Altamura",
                                    "fiscalCode":"ABCDEF",
                                    "address":"My Address 1, Milan"
                                }""")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());

    }

    @Test
    public void createCustomer_shouldReturn400IfRequestWrongFormatted() throws Exception {
        mockMvc.perform(post(BASE_PATH + "/customers")
                        .content("""
                                {
                                    "surname":"Altamura",
                                    "fiscalCode":"ABCDEF",
                                    "address":"My Address 1, Milan"
                                }""")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").doesNotExist());

    }

    @Test
    public void getCustomer_shouldReturn200WithCustomerDTO() throws Exception {
        var customerId = customerRepository.save(aCustomer).getId();
        mockMvc.perform(get(BASE_PATH + "/customers/"+customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId));

    }

    @Test
    public void getCustomer_shouldReturn404IfCustomerIsNotPresent() throws Exception {
        var customerId = 1;
        mockMvc.perform(get(BASE_PATH + "/customers/"+customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void editCustomerAddress_shouldReturn200WithNewCustomerDTO() throws Exception {
        var customerId = customerRepository.save(aCustomer).getId();
        var newAddressRequest = """
                                {"address":"My new Address, 1"}
                                """;
        mockMvc.perform(patch(BASE_PATH + "/customers/"+customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newAddressRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.address").value("My new Address, 1"));
    }

    @Test
    public void editCustomerAddress_shouldReturn400IfNewAddressIsMissing() throws Exception {
        var customerId = 1;
        mockMvc.perform(patch(BASE_PATH + "/customers/"+customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"address":null}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editCustomerAddress_shouldReturn404IfCustomerToUpdateNotFound() throws Exception {
        var customerId = 1;
        mockMvc.perform(patch(BASE_PATH + "/customers/"+customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"address":"My new Address, 1"}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteCustomer_shouldReturn204IfCustomerCorrectlyDeleted() throws Exception {
        var customerId = customerRepository.save(aCustomer).getId();
        mockMvc.perform(delete(BASE_PATH + "/customers/"+customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteCustomer_shouldReturn404IfCustomerToDeleteNotFound() throws Exception {
        var customerId = 1;
        mockMvc.perform(delete(BASE_PATH + "/customers/"+customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}