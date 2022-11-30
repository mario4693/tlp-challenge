package com.tlp.challenge.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public record NewDevicesDTO(@NotNull List<DeviceDTO> devices) {}