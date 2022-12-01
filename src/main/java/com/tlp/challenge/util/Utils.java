package com.tlp.challenge.util;

import com.tlp.challenge.builder.CustomerBuilder;
import com.tlp.challenge.builder.DeviceBuilder;
import com.tlp.challenge.dto.CustomerDTO;
import com.tlp.challenge.dto.DeviceDTO;
import com.tlp.challenge.dto.NewDeviceDTO;
import com.tlp.challenge.dto.SignupDTO;
import com.tlp.challenge.entity.Customer;
import com.tlp.challenge.entity.Device;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;

public class Utils {
    public static List<Device> toListOfDevices(List<NewDeviceDTO> devices) {
        return devices.stream().map(device -> new DeviceBuilder().withState(device.state()).build()).toList();
    }

    public static List<DeviceDTO> toListOfDevicesDTO(List<Device> devices) {
//        return devices.stream().map(device -> new DeviceDTO(device.getId(), device.getState(), toCustomerDTO(device.getCustomer()))).toList();
//        return devices.stream().map(device -> DeviceDTO.builder().withId(device.getId()).withState(device.getState()).withCustomerId(device.getCustomer().getId()).build()).toList();
        return devices.stream().map(device -> DeviceDTO.builder().withId(device.getId()).withState(device.getState()).build()).toList();
    }

    public static List<Device> toListOfDevices(DeviceDTO[] devices) {
        return Objects.nonNull(devices) ?
                Arrays.stream(devices).map(device -> new DeviceBuilder().withId(device.getId()).withState(device.getState()).build()).toList()
                : emptyList();
    }

    public static Customer toCustomer(SignupDTO signupDTO){

        List<Device> devices = toListOfDevices(signupDTO.devices());
        return new CustomerBuilder()
                .withName(signupDTO.name())
                .withSurname(signupDTO.surname())
                .withFiscalCode(signupDTO.fiscalCode())
                .withAddress(signupDTO.address())
                .withDevices(devices)
                .build();
    }

    public static CustomerDTO toCustomerDTO(Customer customer){
        return CustomerDTO.builder()
                .withId(customer.getId())
                .withName(customer.getName())
                .withSurname(customer.getSurname())
                .withFiscalCode(customer.getFiscalCode())
                .withAddress(customer.getAddress())
                .withDevices(toListOfDevicesDTO(customer.getDevices()))
                .build();
    }

    public static Device toDevice(NewDeviceDTO newDeviceDTO, Customer customer) {
        return new DeviceBuilder().withState(newDeviceDTO.state()).withCustomer(customer).build();
    }
}
