package com.tlp.challenge.service;

import com.tlp.challenge.builder.CustomerBuilder;
import com.tlp.challenge.builder.DeviceBuilder;
import com.tlp.challenge.dto.CustomerDTO;
import com.tlp.challenge.dto.DeviceDTO;
import com.tlp.challenge.dto.SignupDTO;
import com.tlp.challenge.entity.Customer;
import com.tlp.challenge.entity.Device;
import com.tlp.challenge.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyList;

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

        List<Device> devices = toListOfDevices(signupDTO.devices());
        return new CustomerBuilder()
                .withName(signupDTO.name())
                .withSurname(signupDTO.surname())
                .withFiscalCode(signupDTO.fiscalCode())
                .withAddress(signupDTO.address())
                .withDevices(devices)
                .build();
    }

    public static List<Device> toListOfDevices(DeviceDTO[] devices) {
        return Objects.nonNull(devices) ?
                Arrays.stream(devices).map(device -> new DeviceBuilder().withId(device.id()).withState(device.state()).build()).toList()
                : emptyList();
    }

    private List<DeviceDTO> toListOfDevicesDTO(List<Device> devices) {
        return devices.stream().map(device -> new DeviceDTO(device.getId(), device.getState())).toList();
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

    public Optional<DeviceDTO> getDeviceFromId(Long customerId, UUID deviceId) {
        return null;
    }
}