package com.tlp.challenge.service;

import com.tlp.challenge.builder.CustomerBuilder;
import com.tlp.challenge.builder.DeviceBuilder;
import com.tlp.challenge.dto.*;
import com.tlp.challenge.entity.Customer;
import com.tlp.challenge.entity.Device;
import com.tlp.challenge.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.tlp.challenge.service.CustomerService.toListOfDevices;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    private CustomerService customerService;

//    private final static SignupDTO aSignupDTO = new SignupDTO("Mario", "Altamura", "ABCDEF93H01A123B", "My address 9, Milano");
    private final static DeviceDTO device1 = new DeviceDTO(UUID.randomUUID(), Device.DeviceState.INACTIVE);
    private final static DeviceDTO device2 = new DeviceDTO(UUID.randomUUID(), Device.DeviceState.LOST);
    private final static List<DeviceDTO> devices = List.of(device1, device2);
    private final static SignupDTO aSignupDTO = new SignupDTO("Mario", "Altamura", "ABCDEF93H01A123B", "My address 9, Milano", device1, device2);
    private final static CustomerDTO aCustomerDTOWithDevices = new CustomerDTO(1L,"Mario", "Altamura", "ABCDEF93H01A123B", "My address 9, Milano", devices);

    private final Customer aCustomer = new CustomerBuilder()
            .withId(1L)
            .withName("Mario")
            .withSurname("Altamura")
            .withFiscalCode("ABCDEF93H01A123B")
            .withAddress("My address 9, Milano")
            .withDevices(toListOfDevices(new DeviceDTO[]{device1, device2}))
            .build();

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(customerRepository);
    }

    @Test
    void saveCustomer_shouldReturnANewSavedCustomer() {
        when(customerRepository.save(any())).thenReturn(aCustomer);
        CustomerDTO customerDTO = customerService.saveCustomer(aSignupDTO);
        verify(customerRepository).save(any());
        verifyNoMoreInteractions(customerRepository);
        assertEquals(aCustomerDTOWithDevices, customerDTO);
        assertNotNull(customerDTO.id());
        assertFalse(customerDTO.devices().isEmpty());
    }

    @Test
    void getCustomerFromId_shouldReturnAPresentOptionalWithCustomerDTO() {
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(aCustomer));
        Optional<CustomerDTO> optionalCustomerDTO = customerService.getCustomerDTOFromId(customerId);
        verify(customerRepository).findById(customerId);
        verifyNoMoreInteractions(customerRepository);
        assertTrue(optionalCustomerDTO.isPresent());
    }

    @Test
    void getCustomerFromId_shouldReturnAnEmptyOptionalIfCustomerNotFound() {
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        Optional<CustomerDTO> optionalCustomerDTO = customerService.getCustomerDTOFromId(customerId);
        verify(customerRepository).findById(customerId);
        verifyNoMoreInteractions(customerRepository);
        assertTrue(optionalCustomerDTO.isEmpty());
    }

    @Test
    void editCustomerAddress_shouldReturnACustomerDTOWithNewAddress() {
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(aCustomer));
        String newCustomerAddress = "My address 1, Bergamo";
        Customer updatedCustomer = new CustomerBuilder()
                .withId(1L)
                .withName("Mario")
                .withSurname("Altamura")
                .withFiscalCode("ABCDEF93H01A123B")
                .withAddress(newCustomerAddress)
                .withDevices(toListOfDevices(new DeviceDTO[]{device1, device2}))
                .build();
        when(customerRepository.save(any())).thenReturn(updatedCustomer);
        Optional<CustomerDTO> updatedCustomerDTO = customerService.editCustomerAddress(customerId, newCustomerAddress);
        verify(customerRepository).findById(customerId);
        verify(customerRepository).save(any());
        assertTrue(updatedCustomerDTO.isPresent());
        assertFalse(updatedCustomerDTO.get().address().isEmpty());
        assertEquals(newCustomerAddress, updatedCustomerDTO.get().address());
        assertEquals(updatedCustomer.getAddress(), updatedCustomerDTO.get().address());
    }

    @Test
    void updateCustomerDevices_shouldReturnCustomerWithNewDevices(){
        var customerId = 1L;
        var newCustomerDeviceDTO = new NewDeviceDTO(Device.DeviceState.INACTIVE);
        var customerWithoutDevices = new CustomerBuilder()
                .withId(1L)
                .withName("Mario")
                .withSurname("Altamura")
                .withFiscalCode("ABCDEF93H01A123B")
                .withAddress("My address 9, Milano")
                .withDevices(emptyList())
                .build();
        var device1 = new DeviceBuilder()
                .withId(UUID.randomUUID())
                .withState(Device.DeviceState.INACTIVE).build();
        var device2 = new DeviceBuilder()
                .withId(UUID.randomUUID())
                .withState(Device.DeviceState.LOST).build();
        var aCustomerWithDevices = new CustomerBuilder()
                .withId(1L)
                .withName("Mario")
                .withSurname("Altamura")
                .withFiscalCode("ABCDEF93H01A123B")
                .withAddress("My address 9, Milano")
                .withDevices(List.of(device1, device2))
                .build();
        var updateCustomerDevicesDTO = new EditCustomerDevicesDTO(customerId, List.of(newCustomerDeviceDTO));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerWithoutDevices));
        when(customerRepository.save(any())).thenReturn(aCustomerWithDevices);
        var newCustomerDTO = customerService.updateCustomerDevices(updateCustomerDevicesDTO);
        verify(customerRepository).findById(customerId);
        verify(customerRepository).save(any());
        verifyNoMoreInteractions(customerRepository);
        assertTrue(newCustomerDTO.isPresent());
        assertFalse(newCustomerDTO.get().devices().isEmpty());
        assertNotNull(newCustomerDTO.get().devices().get(0).id());
        assertNotNull(newCustomerDTO.get().devices().get(1).id());
    }

    @Test
    void updateCustomerDevices_shouldReturnAnEmptyOptionalIfCustomerNotFound() {
        var customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        var updateCustomerDevicesDTO = new EditCustomerDevicesDTO(customerId, null);
        Optional<CustomerDTO> optionalCustomerDTO = customerService.updateCustomerDevices(updateCustomerDevicesDTO);
        verify(customerRepository).findById(customerId);
        verifyNoMoreInteractions(customerRepository);
        assertTrue(optionalCustomerDTO.isEmpty());
    }
}