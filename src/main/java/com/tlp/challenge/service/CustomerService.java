package com.tlp.challenge.service;

import com.tlp.challenge.builder.CustomerBuilder;
import com.tlp.challenge.builder.DeviceBuilder;
import com.tlp.challenge.dto.*;
import com.tlp.challenge.entity.Customer;
import com.tlp.challenge.entity.Device;
import com.tlp.challenge.exception.CustomerDevicesNotUpdatable;
import com.tlp.challenge.repository.CustomerRepository;
import com.tlp.challenge.repository.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final DeviceRepository deviceRepository;

    public CustomerService(CustomerRepository customerRepository, DeviceRepository deviceRepository) {
        this.customerRepository = customerRepository;
        this.deviceRepository = deviceRepository;
    }

    public CustomerDTO saveCustomer(SignupDTO signupDTO) {
        var savedCustomer = customerRepository.save(toCustomer(signupDTO));
        CustomerDTO customerDTO;
        if (Objects.nonNull(signupDTO.devices()) && signupDTO.devices().length>0){
            var devices = toListOfDevices(signupDTO.devices());
            devices.forEach(device -> device.setCustomer(savedCustomer));
            var savedDevices = deviceRepository.saveAll(devices);
            customerDTO = toCustomerDTO(savedCustomer, savedDevices);
        } else customerDTO = toCustomerDTO(savedCustomer);
        return customerDTO;
    }

    Customer toCustomer(SignupDTO signupDTO){
        return new CustomerBuilder()
                .withName(signupDTO.name())
                .withSurname(signupDTO.surname())
                .withFiscalCode(signupDTO.fiscalCode())
                .withAddress(signupDTO.address())
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
                .build();
    }

    CustomerDTO toCustomerDTO(Customer customer, List<Device> devices){
        return CustomerDTO.builder()
                .withId(customer.getId())
                .withName(customer.getName())
                .withSurname(customer.getSurname())
                .withFiscalCode(customer.getFiscalCode())
                .withAddress(customer.getAddress())
                .withDevices(toListOfDevicesDTO(devices))
                .build();
    }

    public Optional<CustomerDTO> getCustomerDTOFromId(Long customerId) {
        var optionalCustomer = customerRepository.findById(customerId);
        var optionalCustomerDevices = deviceRepository.findByCustomerId(customerId);
        Optional<CustomerDTO> optionalCustomerDTO = Optional.empty();
        if (optionalCustomer.isPresent()){
            if (optionalCustomerDevices.isEmpty()) {
                optionalCustomerDTO = optionalCustomer.map(this::toCustomerDTO);
            } else {
                optionalCustomerDTO = Optional.of(toCustomerDTO(optionalCustomer.get(), optionalCustomerDevices.get()));
            }
        }
        return optionalCustomerDTO;
    }

    public Optional<CustomerDTO> editCustomerAddress(Long id, String newAddress) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        return optionalCustomer.map(customer -> toCustomerDTO(updateCustomerAddress(customer, newAddress)));
    }

    private Customer updateCustomerAddress(Customer customer, String newAddress){
        customer.setAddress(newAddress);
        return customerRepository.saveAndFlush(customer);
    }

    //    private void updateCustomerDevices(Customer customer, List<Device> newDevices){
    private void updateCustomerDevices(Customer customer, List<Device> newDevices){
        newDevices.forEach(device -> device.setCustomer(customer));
        deviceRepository.saveAll(newDevices);
//        return getCustomerDTOFromId(customer.getId());
    }

    public Optional<CustomerDTO> updateCustomerDevices(Long customerId, EditCustomerDevicesDTO updateCustomerDevicesDTO) throws CustomerDevicesNotUpdatable {
        var optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            var optionalCustomerDevices = deviceRepository.findByCustomerId(customerId);
            var customer = optionalCustomer.get();
//            if (optionalCustomerDevices.isPresent() && optionalCustomerDevices.get().isEmpty()){
            if (optionalCustomerDevices.isPresent() && optionalCustomerDevices.get().isEmpty()){
                System.out.println("NO CUSTOMER DEVICES");
                var newDevices = toListOfDevices(customer, updateCustomerDevicesDTO.devices());
                updateCustomerDevices(customer, newDevices);
            } else {
                if(optionalCustomerDevices.isPresent()) {
                    System.out.println("FOUND CUSTOMER DEVICES");
                    //TODO check and improve
                    var customerDevices = optionalCustomerDevices.get();
                    var nOfCustomerDevices = customerDevices.size();
                    //sum(nOfCustomerDevices, updateCustomerDevicesDTO.devices.size())<=2
                    if (nOfCustomerDevices == 2) {
                        //NO
                        System.out.println("ALREADY 2 DEVICES");
                        throw new CustomerDevicesNotUpdatable();
                    } else if (nOfCustomerDevices == 1 && updateCustomerDevicesDTO.devices().size() == 1) {
                        System.out.println("1 DEVICE, COULD ACCEPT ANOTHER 1");
                        //PROCEED
                        var newDevices = toListOfDevices(customer, updateCustomerDevicesDTO.devices());
                        updateCustomerDevices(customer, newDevices);
                    } else if (nOfCustomerDevices == 1 && updateCustomerDevicesDTO.devices().size() == 2) {
                        System.out.println("1 DEVICE, COULD NOT ACCEPT ANOTHER 2");
                        throw new CustomerDevicesNotUpdatable();
                    }
                }
            }
        } else return Optional.empty();
//        return optionalCustomer
//                .map(customer -> toCustomerDTO(updateCustomerDevices(customer, toListOfDevices(customer, updateCustomerDevicesDTO.devices()))));
        return getCustomerDTOFromId(customerId);
//        return optionalCustomer.map(customer -> toCustomerDTO(customer, null));
    }

    private List<Device> toListOfDevices(Customer customer, List<NewDeviceDTO> newDevicesDTO){
        return newDevicesDTO.stream()
                .map(newDeviceDTO -> new DeviceBuilder()
                        .withState(newDeviceDTO.state())
                        .withCustomer(customer)
                        .build())
                .collect(Collectors.toList());
    }
}