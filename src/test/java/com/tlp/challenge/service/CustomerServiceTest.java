package com.tlp.challenge.service;

import com.tlp.challenge.builder.CustomerBuilder;
import com.tlp.challenge.dto.CustomerDTO;
import com.tlp.challenge.dto.DeviceDTO;
import com.tlp.challenge.dto.SignupDTO;
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
    private final static CustomerDTO aCustomerDTO = new CustomerDTO(1L,"Mario", "Altamura", "ABCDEF93H01A123B", "My address 9, Milano", devices);

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
        assertEquals(aCustomerDTO, customerDTO);
        assertNotNull(customerDTO.id());
        assertFalse(customerDTO.devices().isEmpty());
    }

    @Test
    void getCustomerFromId_shouldReturnAPresentOptionalWithCustomerDTO() {
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(aCustomer));
        Optional<CustomerDTO> optionalCustomerDTO = customerService.getCustomerFromId(customerId);
        verify(customerRepository).findById(customerId);
        verifyNoMoreInteractions(customerRepository);
        assertTrue(optionalCustomerDTO.isPresent());
    }

    @Test
    void getCustomerFromId_shouldReturnAnEmptyOptionalIfCustomerNotFound() {
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        Optional<CustomerDTO> optionalCustomerDTO = customerService.getCustomerFromId(customerId);
        verify(customerRepository).findById(customerId);
        verifyNoMoreInteractions(customerRepository);
        assertFalse(optionalCustomerDTO.isPresent());
    }
}