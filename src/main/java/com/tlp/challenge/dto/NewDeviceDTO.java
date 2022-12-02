package com.tlp.challenge.dto;

import com.tlp.challenge.entity.Device;

import javax.validation.constraints.NotNull;

public record NewDeviceDTO(@NotNull Device.DeviceState state, @NotNull Long customerId) {}