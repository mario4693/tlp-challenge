package com.tlp.challenge.service;

import com.tlp.challenge.builder.CustomerBuilder;
import com.tlp.challenge.dto.CustomerDTO;
import com.tlp.challenge.dto.SignupDTO;
import com.tlp.challenge.entity.Customer;
import com.tlp.challenge.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDTO saveCustomer(SignupDTO signupDTO) {
        Customer aCustomer = toCustomer(signupDTO);
        Customer savedCustomer = customerRepository.save(aCustomer);
        return toCustomerDTO(savedCustomer);
    }

    Customer toCustomer(SignupDTO signupDTO){
        return new CustomerBuilder()
                .withName(signupDTO.name())
                .withSurname(signupDTO.surname())
                .withFiscalCode(signupDTO.fiscalCode())
                .withAddress(signupDTO.address())
                .build();
    }

    CustomerDTO toCustomerDTO(Customer customer){
        return CustomerDTO.builder()
                .withId(customer.getId())
                .withName(customer.getName())
                .withSurname(customer.getSurname())
                .withFiscalCode(customer.getFiscalCode())
                .withAddress(customer.getAddress())
                .build();
    }
}