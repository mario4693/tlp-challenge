package com.tlp.challenge.dto;

import javax.validation.constraints.NotEmpty;

public record EditCustomerAddressDTO(@NotEmpty(message = "address cannot be empty") String address) {}