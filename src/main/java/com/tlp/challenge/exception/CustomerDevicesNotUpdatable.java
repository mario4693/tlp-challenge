package com.tlp.challenge.exception;

public class CustomerDevicesNotUpdatable extends Exception{
    public CustomerDevicesNotUpdatable() {
        super("Customer has already too many devices");
    }
}
