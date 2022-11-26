package com.tlp.challenge.service;

import com.tlp.challenge.dto.DeviceDTO;
import com.tlp.challenge.entity.Device;
import com.tlp.challenge.repository.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public boolean isDevicePresent(UUID deviceId) {
        return deviceRepository.findById(deviceId).isPresent();
    }

    public boolean deleteDeviceById(UUID deviceId) {
        boolean isDeleted = false;
        if(deviceRepository.existsById(deviceId)){
            deviceRepository.deleteById(deviceId);
            isDeleted = true;
        }
        return isDeleted;
    }

    public Optional<DeviceDTO> editDeviceState(UUID deviceId, Device.DeviceState newDeviceState) {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
        return optionalDevice.map(device -> toDeviceDTO(updateDeviceState(device, newDeviceState)));
    }

    private Device updateDeviceState(Device device, Device.DeviceState newDeviceState){
        device.setState(newDeviceState);
        return deviceRepository.save(device);
    }

    private DeviceDTO toDeviceDTO(Device device){
        return new DeviceDTO(device.getId(), device.getState());
    }
}
