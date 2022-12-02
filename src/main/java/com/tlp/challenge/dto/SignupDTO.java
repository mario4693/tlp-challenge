package com.tlp.challenge.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public record SignupDTO(@NotEmpty String name,@NotEmpty String surname,@NotEmpty String fiscalCode,@NotEmpty String address, @Size(max = 2) DeviceDTO... devices) {}