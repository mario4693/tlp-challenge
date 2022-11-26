package com.tlp.challenge.service;

import com.tlp.challenge.repository.DeviceRepository;
import org.springframework.stereotype.Service;

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
}
