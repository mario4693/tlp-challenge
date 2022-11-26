package com.tlp.challenge.dto;

import com.tlp.challenge.entity.Device;

import javax.validation.constraints.NotNull;

public record EditDeviceStateDTO(@NotNull(message = "device state cannot be null") Device.DeviceState state) {}