package com.tlp.challenge.controller;

import com.tlp.challenge.dto.CustomerDTO;
import com.tlp.challenge.dto.EditCustomerAddressDTO;
import com.tlp.challenge.dto.SignupDTO;
import com.tlp.challenge.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        var optionalCustomer = customerService.getCustomerDTOFromId(id);
        return optionalCustomer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping(value = "{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDTO> editCustomerAddress(@PathVariable Long id, @Valid @RequestBody EditCustomerAddressDTO editCustomerAddressDTO) {
        var optionalCustomer = customerService.editCustomerAddress(id, editCustomerAddressDTO.address());
        return optionalCustomer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        return customerService.deleteCustomerById(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}