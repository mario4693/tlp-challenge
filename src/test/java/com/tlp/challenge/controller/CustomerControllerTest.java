package com.tlp.challenge.controller;

import com.tlp.challenge.dto.CustomerDTO;
import com.tlp.challenge.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {
    @Mock
    private CustomerService customerService;

    private CustomerController customerController;

    private final static CustomerDTO aCustomerDTO = new CustomerDTO(1L, "Mario", "Altamura", "ABCDEF93H01A123B", "My address 9, Milano");

    @BeforeEach
    void setUp() {
        customerController = new CustomerController(customerService);
    }

    @Test
    void createCustomer_shouldCreateANewCustomer(){
        when(customerService.saveCustomer(aCustomerDTO)).thenReturn(aCustomerDTO);
        ResponseEntity<CustomerDTO> response = customerController.createCustomer(aCustomerDTO);
        verify(customerService, only()).saveCustomer(aCustomerDTO);
        assertEquals(aCustomerDTO, response.getBody());
    }
}