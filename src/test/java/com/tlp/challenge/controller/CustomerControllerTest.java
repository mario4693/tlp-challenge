package com.tlp.challenge.controller;

import com.tlp.challenge.dto.CustomerDTO;
import com.tlp.challenge.dto.SignupDTO;
import com.tlp.challenge.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {
    @Mock
    private CustomerService customerService;

    private CustomerController customerController;

    private final static SignupDTO aSignupDTO = new SignupDTO("Mario", "Altamura", "ABCDEF93H01A123B", "My address 9, Milano");
    private final static CustomerDTO aCustomerDTO = new CustomerDTO(1L,"Mario", "Altamura", "ABCDEF93H01A123B", "My address 9, Milano", emptyList());

    @BeforeEach
    void setUp() {
        customerController = new CustomerController(customerService);
    }

    @Test
    void createCustomer_shouldReturnANewCustomer(){
        when(customerService.saveCustomer(aSignupDTO)).thenReturn(aCustomerDTO);
        ResponseEntity<CustomerDTO> response = customerController.createCustomer(aSignupDTO);
        verify(customerService, only()).saveCustomer(aSignupDTO);
        assertEquals(aCustomerDTO, response.getBody());
    }
}