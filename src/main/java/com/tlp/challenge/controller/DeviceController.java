package com.tlp.challenge.controller;

import com.tlp.challenge.service.DeviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "devices")
public class DeviceController {
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping(value = "{uuid}")
    public ResponseEntity<Void> isDevicePresent(@PathVariable UUID uuid) {
        return deviceService.isDevicePresent(uuid) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "uuid")
    public ResponseEntity<Void> deleteDevice(UUID uuid) {
        return deviceService.deleteDeviceById(uuid) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
