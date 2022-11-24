package com.tlp.challenge.builder;

import com.tlp.challenge.entity.Customer;
import com.tlp.challenge.entity.Device;

import java.util.Collections;
import java.util.List;

public class CustomerBuilder {
    private Long id;
    private String name;
    private String surname;
    private String fiscalCode;
    private String address;
    private List<Device> devices;

    public CustomerBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CustomerBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CustomerBuilder withSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public CustomerBuilder withFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
        return this;
    }

    public CustomerBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public CustomerBuilder withDevices(Device... devices) {
        this.devices = devices!=null ? List.of(devices) : Collections.emptyList();
        return this;
    }

    public Customer build() {
        return new Customer(id, name, surname, fiscalCode, address, devices);
    }
}