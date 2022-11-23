package com.tlp.challenge.controller;

import com.tlp.challenge.dto.CustomerDTO;
import com.tlp.challenge.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {
    @Mock
    private CustomerService customerService;

    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        customerController = new CustomerController(customerService);
    }

    @Test
    void createUser_shouldReturn201AndANewUser(){
        CustomerDTO aCustomer = new CustomerDTO(1L, "Mario", "Altamura", "ABCDEF93H01A123B", "My address 9, Milano");

        when(customerService.createCustomer()).thenReturn(aCustomer);
        ResponseEntity<CustomerDTO> response = customerController.createCustomer();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(aCustomer, response.getBody());
    }
}