package com.tlp.challenge.service;

import com.tlp.challenge.dto.CustomerDTO;
import com.tlp.challenge.dto.SignupDTO;
import com.tlp.challenge.entity.Customer;
import com.tlp.challenge.repository.CustomerRepository;
import com.tlp.challenge.util.Utils;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.tlp.challenge.util.Utils.toCustomer;
import static com.tlp.challenge.util.Utils.toCustomerDTO;

@Service
public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerDTO saveCustomer(SignupDTO signupDTO) {
        var aCustomer = toCustomer(signupDTO);
        aCustomer.getDevices().forEach(device -> device.setCustomer(aCustomer));
        Customer savedCustomer = customerRepository.save(aCustomer);
        return toCustomerDTO(savedCustomer);
    }

    @Override
    public Optional<CustomerDTO> getCustomerDTOFromId(Long customerId) {
        return customerRepository.findById(customerId)
                .map(Utils::toCustomerDTO);
    }

    @Override
    public Optional<CustomerDTO> editCustomerAddress(Long id, String newAddress) {
        return customerRepository.findById(id)
                .map(customer -> toCustomerDTO(updateCustomerAddress(customer, newAddress)));
    }

    @Override
    public boolean deleteCustomerById(Long customerId) {
        var isDeleted = false;
        if (customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
            isDeleted=true;
        }
        return isDeleted;
    }

    private Customer updateCustomerAddress(Customer customer, String newAddress){
        customer.setAddress(newAddress);
        return customerRepository.save(customer);
    }
}