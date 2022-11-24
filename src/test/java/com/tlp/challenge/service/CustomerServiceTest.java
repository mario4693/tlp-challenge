package com.tlp.challenge.service;

import com.tlp.challenge.builder.CustomerBuilder;
import com.tlp.challenge.dto.CustomerDTO;
import com.tlp.challenge.dto.SignupDTO;
import com.tlp.challenge.entity.Customer;
import com.tlp.challenge.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    private CustomerService customerService;

    private final static SignupDTO aSignupDTO = new SignupDTO("Mario", "Altamura", "ABCDEF93H01A123B", "My address 9, Milano");

    private final Customer aCustomer = new CustomerBuilder()
            .withId(1L)
            .withName("Mario")
            .withSurname("Altamura")
            .withFiscalCode("ABCDEF93H01A123B")
            .withAddress("My address 9, Milano")
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
        assertNotNull(customerDTO);
        assertNotNull(customerDTO.id());
    }
}