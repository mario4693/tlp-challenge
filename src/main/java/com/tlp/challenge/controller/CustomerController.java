package com.tlp.challenge.controller;

import com.tlp.challenge.dto.*;
import com.tlp.challenge.service.CustomerService;
import com.tlp.challenge.service.DeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody SignupDTO signupDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.saveCustomer(signupDTO));
    }

    @GetMapping(value = "{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id) {
        Optional<CustomerDTO> optionalCustomer = customerService.getCustomerDTOFromId(id);
        return optionalCustomer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping(value = "{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDTO> editCustomerAddress(@PathVariable Long id, @Valid @RequestBody EditCustomerAddressDTO editCustomerAddressDTO) {
        Optional<CustomerDTO> optionalCustomer = customerService.editCustomerAddress(id, editCustomerAddressDTO.address());
        return optionalCustomer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping(value = "{id}/devices",consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDTO> addDevicesToCustomer(@Valid @RequestBody EditCustomerDevicesDTO editCustomerDevicesDTO) {
        Optional<CustomerDTO> optionalCustomer = customerService.updateCustomerDevices(editCustomerDevicesDTO);
        return optionalCustomer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}