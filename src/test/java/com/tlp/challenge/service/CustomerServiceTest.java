package com.tlp.challenge.service;

import com.tlp.challenge.builder.CustomerBuilder;
import com.tlp.challenge.builder.DeviceBuilder;
import com.tlp.challenge.dto.*;
import com.tlp.challenge.entity.Customer;
import com.tlp.challenge.entity.Device;
import com.tlp.challenge.exception.CustomerDevicesNotUpdatable;
import com.tlp.challenge.repository.CustomerRepository;
import com.tlp.challenge.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private DeviceRepository deviceRepository;
    private CustomerService customerService;

    private final static UUID UUID_1 = UUID.randomUUID();
    private final static UUID UUID_2 = UUID.randomUUID();
    private final static Device device1 = new DeviceBuilder()
        .withId(UUID_1)
        .withState(Device.DeviceState.INACTIVE)
        .build();
    private final static Device device2 = new DeviceBuilder()
            .withId(UUID_2)
            .withState(Device.DeviceState.LOST)
            .build();

    private final static DeviceDTO device1DTO = new DeviceDTO(UUID_1, Device.DeviceState.INACTIVE);
    private final static DeviceDTO device2DTO = new DeviceDTO(UUID_2, Device.DeviceState.LOST);
    private final static SignupDTO aSignupDTOWithDevices = new SignupDTO("Mario", "Altamura", "ABCDEF93H01A123B", "My address 9, Milano", device1DTO, device2DTO);

    private final static List<Device> devices = List.of(device1, device2);
    private final static Customer aCustomer = new CustomerBuilder()
            .withId(1L)
            .withName("Mario")
            .withSurname("Altamura")
            .withFiscalCode("ABCDEF93H01A123B")
            .withAddress("My address 9, Milano")
            .build();

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(customerRepository, deviceRepository);
    }

    @Test
    void saveCustomer_shouldReturnANewSavedCustomerWithoutDevices() {
        SignupDTO aSignupDTOWithoutDevices = new SignupDTO("Mario", "Altamura", "ABCDEF93H01A123B", "My address 9, Milano");
        when(customerRepository.save(any())).thenReturn(aCustomer);
        CustomerDTO customerDTO = customerService.saveCustomer(aSignupDTOWithoutDevices);
        verify(customerRepository).save(any());
        verifyNoMoreInteractions(customerRepository);
        verifyNoInteractions(deviceRepository);
        assertNotNull(customerDTO.id());
        assertNull(customerDTO.devices());
    }

    @Test
    void saveCustomer_shouldReturnANewSavedCustomerWithDevices() {
        when(customerRepository.save(any())).thenReturn(aCustomer);
        CustomerDTO customerDTO = customerService.saveCustomer(aSignupDTOWithDevices);
        verify(customerRepository).save(any());
        verify(deviceRepository).saveAll(any());
        verifyNoMoreInteractions(customerRepository);
        verifyNoMoreInteractions(deviceRepository);
        assertNotNull(customerDTO.id());
        assertNotNull(customerDTO.devices());
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
        String newCustomerAddress = "My address 1, Bergamo";
        Customer updatedCustomer = new CustomerBuilder()
                .withId(1L)
                .withName("Mario")
                .withSurname("Altamura")
                .withFiscalCode("ABCDEF93H01A123B")
                .withAddress(newCustomerAddress)
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(aCustomer));
        when(customerRepository.saveAndFlush(aCustomer)).thenReturn(updatedCustomer);
        Optional<CustomerDTO> updatedCustomerDTO = customerService.editCustomerAddress(customerId, newCustomerAddress);
        verify(customerRepository).findById(customerId);
        verify(customerRepository).saveAndFlush(aCustomer);

        verifyNoMoreInteractions(customerRepository);
        assertTrue(updatedCustomerDTO.isPresent());
        assertFalse(updatedCustomerDTO.get().address().isEmpty());
        assertEquals(newCustomerAddress, updatedCustomerDTO.get().address());
//        assertEquals(updatedCustomer.getAddress(), updatedCustomerDTO.get().address());
    }

    //TODO fix test updateCustomerDevices_shouldReturnCustomerWithNewDevices
//    @Test
    void updateCustomerDevices_shouldReturnCustomerWithNewDevices() throws CustomerDevicesNotUpdatable {
        var customerId = 1L;
        var newCustomerDeviceDTO = new NewDeviceDTO(Device.DeviceState.INACTIVE);
        var aCustomer = new CustomerBuilder()
                .withId(1L)
                .withName("Mario")
                .withSurname("Altamura")
                .withFiscalCode("ABCDEF93H01A123B")
                .withAddress("My address 9, Milano")
                .build();

        var updateCustomerDevicesDTO = new EditCustomerDevicesDTO(customerId, List.of(newCustomerDeviceDTO));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(aCustomer));
        when(deviceRepository.findByCustomerId(customerId)).thenReturn(Optional.of(Collections.emptyList()));

        var newCustomerDTO = customerService.updateCustomerDevices(updateCustomerDevicesDTO);
        verify(customerRepository, times(2)).findById(customerId);
        verify(deviceRepository, times(2)).findByCustomerId(customerId);
        verify(deviceRepository).saveAll(any());
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(aCustomer));
        when(deviceRepository.findByCustomerId(customerId)).thenReturn(Optional.of(devices));
        verifyNoMoreInteractions(customerRepository);
        verifyNoMoreInteractions(deviceRepository);
        assertTrue(newCustomerDTO.isPresent());
        assertFalse(newCustomerDTO.get().devices().isEmpty());
        assertEquals(2, newCustomerDTO.get().devices().size());
//        assertNotNull(newCustomerDTO.get().devices().get(0).id());
//        assertNotNull(newCustomerDTO.get().devices().get(1).id());
    }

    @Test
    void updateCustomerDevices_shouldReturnAnEmptyOptionalIfCustomerNotFound() throws CustomerDevicesNotUpdatable {
        var customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        var updateCustomerDevicesDTO = new EditCustomerDevicesDTO(customerId, null);
        Optional<CustomerDTO> optionalCustomerDTO = customerService.updateCustomerDevices(updateCustomerDevicesDTO);
        verify(customerRepository).findById(customerId);
        assertTrue(optionalCustomerDTO.isEmpty());
    }
}