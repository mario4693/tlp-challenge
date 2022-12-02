package com.tlp.challenge.service;

import com.tlp.challenge.dto.CustomerDTO;
import com.tlp.challenge.dto.SignupDTO;
import java.util.Optional;

public interface ICustomerService {
    CustomerDTO saveCustomer(SignupDTO signupDTO);
    Optional<CustomerDTO> getCustomerDTOFromId(Long customerId);
    Optional<CustomerDTO> editCustomerAddress(Long id, String newAddress);
}
