package com.tlp.challenge.dto;

import com.tlp.challenge.entity.Device;

import javax.validation.constraints.NotEmpty;

public record EditDeviceStateDTO(@NotEmpty(message = "device state cannot be null") Device.DeviceState state) {}