package com.tlp.challenge.dto;

import com.tlp.challenge.entity.Device;

public record NewDeviceDTO(Device.DeviceState state) {}