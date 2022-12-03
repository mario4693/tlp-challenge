package com.tlp.challenge.service;

import com.tlp.challenge.dto.DeviceDTO;
import com.tlp.challenge.dto.NewDeviceDTO;
import com.tlp.challenge.entity.Device;
import com.tlp.challenge.exception.CustomerDevicesFullException;
import com.tlp.challenge.exception.CustomerNotFoundException;

import java.util.Optional;
import java.util.UUID;

public interface IDeviceService {
    DeviceDTO saveDevice(NewDeviceDTO newDeviceDTO) throws CustomerNotFoundException, CustomerDevicesFullException;
    boolean isDevicePresent(UUID deviceId);
    Optional<DeviceDTO> editDeviceState(UUID deviceId, Device.DeviceState newDeviceState);
    boolean deleteDeviceById(UUID deviceId);
}
