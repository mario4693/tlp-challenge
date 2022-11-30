package com.tlp.challenge.service;

import com.tlp.challenge.dto.CustomerDTO;
import com.tlp.challenge.dto.SignupDTO;
import com.tlp.challenge.entity.Customer;
import com.tlp.challenge.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.tlp.challenge.util.Utils.toCustomer;
import static com.tlp.challenge.util.Utils.toListOfDevicesDTO;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDTO saveCustomer(SignupDTO signupDTO) {
        Customer aCustomer = toCustomer(signupDTO);
        aCustomer.getDevices().forEach(device -> device.setCustomer(aCustomer));
        Customer savedCustomer = customerRepository.save(aCustomer);
        return toCustomerDTO(savedCustomer);
    }

    CustomerDTO toCustomerDTO(Customer customer){
        return CustomerDTO.builder()
                .withId(customer.getId())
                .withName(customer.getName())
                .withSurname(customer.getSurname())
                .withFiscalCode(customer.getFiscalCode())
                .withAddress(customer.getAddress())
                .withDevices(toListOfDevicesDTO(customer.getDevices()))
                .build();
    }

    public Optional<CustomerDTO> getCustomerDTOFromId(Long customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        return optionalCustomer.map(this::toCustomerDTO);
    }

    public Optional<CustomerDTO> editCustomerAddress(Long id, String newAddress) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        return optionalCustomer.map(customer -> toCustomerDTO(updateCustomerAddress(customer, newAddress)));
    }

    private Customer updateCustomerAddress(Customer customer, String newAddress){
        customer.setAddress(newAddress);
        customerRepository.save(customer);
        return customer;
    }
}