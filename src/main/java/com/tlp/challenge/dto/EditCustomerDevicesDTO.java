package com.tlp.challenge.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public record EditCustomerDevicesDTO(@NotNull(message = "should provide at least one new device") @Size(min = 1, max = 2) List<NewDeviceDTO> devices) {}