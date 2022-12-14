package com.tlp.challenge.controller;

import com.tlp.challenge.dto.CustomerDTO;
import com.tlp.challenge.dto.EditCustomerAddressDTO;
import com.tlp.challenge.dto.SignupDTO;
import com.tlp.challenge.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
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
        verifyNoMoreInteractions(customerService);
        assertEquals(aCustomerDTO, response.getBody());
    }

    @Test
    void getCustomer_shouldReturnAnExistentCustomer(){
        var customerId = 1L;
        when(customerService.getCustomerDTOFromId(customerId)).thenReturn(Optional.of(aCustomerDTO));
        var response = customerController.getCustomer(customerId);
        verify(customerService).getCustomerDTOFromId(customerId);
        verifyNoMoreInteractions(customerService);
        assertEquals(aCustomerDTO, response.getBody());
    }

    @Test
    void getCustomer_shouldReturnNullBody(){
        var customerId = 1L;
        when(customerService.getCustomerDTOFromId(customerId)).thenReturn(Optional.empty());
        var response = customerController.getCustomer(customerId);
        verify(customerService).getCustomerDTOFromId(customerId);
        verifyNoMoreInteractions(customerService);
        assertTrue(Objects.isNull(response.getBody()));
    }

    @Test
    void editCustomerAddress_shouldReturnABodyWithUpdatedCustomer(){
        var customerId = 1L;
        var newCustomerAddress = "My address 1, Bergamo";
        var updatedCustomerDTO = new CustomerDTO(1L,"Mario", "Altamura","ABCDEF93H01A123B", "My address 1, Bergamo", emptyList());
        when(customerService.editCustomerAddress(customerId, newCustomerAddress)).thenReturn(Optional.of(updatedCustomerDTO));
        var response = customerController.editCustomerAddress(customerId, new EditCustomerAddressDTO(newCustomerAddress));
        verify(customerService).editCustomerAddress(customerId, newCustomerAddress);
        verifyNoMoreInteractions(customerService);
        assertTrue(Objects.nonNull(response.getBody()));
        assertEquals(newCustomerAddress, response.getBody().address());
    }

    @Test
    void editCustomerAddress_shouldReturnNullBodyIfCustomerNotFound(){
        var customerId = 1L;
        var newCustomerAddress = "My address 1, Bergamo";
        when(customerService.editCustomerAddress(customerId, newCustomerAddress)).thenReturn(Optional.empty());
        var response = customerController.editCustomerAddress(customerId, new EditCustomerAddressDTO(newCustomerAddress));
        verify(customerService).editCustomerAddress(customerId, newCustomerAddress);
        verifyNoMoreInteractions(customerService);
        assertTrue(Objects.isNull(response.getBody()));
    }

    @Test
    void deleteCustomer_shouldReturnNoContentIfCustomerCorrectlyDeleted() {
        var customerId = 1L;
        when(customerService.deleteCustomerById(customerId)).thenReturn(true);
        var response = customerController.deleteCustomer(customerId);
        verify(customerService).deleteCustomerById(customerId);
        verifyNoMoreInteractions(customerService);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteCustomer_shouldReturnNotFoundIfCustomerToDeleteDoesNotExists(){
        var customerId = 1L;
        when(customerService.deleteCustomerById(customerId)).thenReturn(false);
        var response = customerController.deleteCustomer(customerId);
        verify(customerService).deleteCustomerById(customerId);
        verifyNoMoreInteractions(customerService);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}