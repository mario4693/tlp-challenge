package com.tlp.challenge.dto;

import javax.validation.constraints.Size;

public record SignupDTO(String name, String surname, String fiscalCode, String address, @Size(max = 2) DeviceDTO... devices) {}