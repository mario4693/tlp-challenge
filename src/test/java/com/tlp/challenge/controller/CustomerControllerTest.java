package com.tlp.challenge.controller;

import com.tlp.challenge.dto.CustomerDTO;
import com.tlp.challenge.dto.EditCustomerAddressDTO;
import com.tlp.challenge.dto.SignupDTO;
import com.tlp.challenge.dto.NewDeviceDTO;
import com.tlp.challenge.dto.DeviceDTO;
import com.tlp.challenge.dto.EditCustomerDevicesDTO;
import com.tlp.challenge.service.CustomerService;
import com.tlp.challenge.entity.Device.DeviceState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        var response = customerController.createCustomer(aSignupDTO);
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
        ResponseEntity<CustomerDTO> response = customerController.getCustomer(customerId);
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
        String newCustomerAddress = "My address 1, Bergamo";
        when(customerService.editCustomerAddress(customerId, newCustomerAddress)).thenReturn(Optional.empty());
        var response = customerController.editCustomerAddress(customerId, new EditCustomerAddressDTO(newCustomerAddress));
        verify(customerService).editCustomerAddress(customerId, newCustomerAddress);
        verifyNoMoreInteractions(customerService);
        assertTrue(Objects.isNull(response.getBody()));
    }

    @Test
    void addDevicesToCustomer_shouldReturnACustomerWithTheNewDevice(){
        var customerId = 1L;
        var newCustomerDevice = new NewDeviceDTO(DeviceState.INACTIVE);
        var newAddedDevice = new DeviceDTO(UUID.randomUUID(), DeviceState.INACTIVE);
        var editCustomerDevices = new EditCustomerDevicesDTO(customerId, List.of(newCustomerDevice));
        var aCustomerDTOWithADevice = new CustomerDTO(customerId,"Mario", "Altamura", "ABCDEF93H01A123B", "My address 9, Milano", List.of(newAddedDevice));
        when(customerService.updateCustomerDevices(editCustomerDevices)).thenReturn(Optional.of(aCustomerDTOWithADevice));
        var response = customerController.addDevicesToCustomer(editCustomerDevices);
        verify(customerService, only()).updateCustomerDevices(editCustomerDevices);
        verifyNoMoreInteractions(customerService);
        assertNotNull(response.getBody());
        assertFalse(response.getBody().devices().isEmpty());
        assertNotNull(newAddedDevice.id());
    }

    @Test
    void addDevicesToCustomer_shouldReturnNullBodyIfCustomerNotFound(){
        var customerId = 1L;
        var newCustomerDevice = new NewDeviceDTO(DeviceState.INACTIVE);
        var editCustomerDevices = new EditCustomerDevicesDTO(customerId, List.of(newCustomerDevice));
        when(customerService.updateCustomerDevices(editCustomerDevices)).thenReturn(Optional.empty());
        var response = customerController.addDevicesToCustomer(editCustomerDevices);
        verify(customerService).updateCustomerDevices(editCustomerDevices);
        verifyNoMoreInteractions(customerService);
        assertNull(response.getBody());
    }
}