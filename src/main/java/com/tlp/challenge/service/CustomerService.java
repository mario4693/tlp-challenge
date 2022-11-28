package com.tlp.challenge.service;

import com.tlp.challenge.builder.CustomerBuilder;
import com.tlp.challenge.builder.DeviceBuilder;
import com.tlp.challenge.dto.*;
import com.tlp.challenge.entity.Customer;
import com.tlp.challenge.entity.Device;
import com.tlp.challenge.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

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
        return customerRepository.save(customer);
    }

    private Customer updateCustomerDevices(Customer customer, List<Device> newDevices){
        customer.setDevices(newDevices);
        customer.getDevices().forEach(device -> device.setCustomer(customer));
        return customerRepository.save(customer);
    }

    public Optional<CustomerDTO> updateCustomerDevices(EditCustomerDevicesDTO updateCustomerDevicesDTO) {
        var optionalCustomer = customerRepository.findById(updateCustomerDevicesDTO.customerId());
        return optionalCustomer
                .map(customer -> toCustomerDTO(updateCustomerDevices(customer, toListOfDevices(updateCustomerDevicesDTO.devices()))));
    }

    private List<Device> toListOfDevices(List<NewDeviceDTO> newDevicesDTO){
        return newDevicesDTO.stream()
                .map(newDeviceDTO -> new DeviceBuilder().withState(newDeviceDTO.state()).build())
                .collect(Collectors.toList());
    }
}