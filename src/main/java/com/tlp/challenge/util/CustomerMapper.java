package com.tlp.challenge.util;

import com.tlp.challenge.builder.CustomerBuilder;
import com.tlp.challenge.builder.DeviceBuilder;
import com.tlp.challenge.dto.DeviceDTO;
import com.tlp.challenge.dto.SignupDTO;
import com.tlp.challenge.entity.Customer;
import com.tlp.challenge.entity.Device;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;

@Component
public class CustomerMapper {
    public Customer toCustomer(SignupDTO signupDTO){

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
                Arrays.stream(devices).map(device -> new DeviceBuilder().withId(device.getId()).withState(device.getState()).build()).toList()
                : emptyList();
    }
}