package com.tlp.challenge.controller;

import com.tlp.challenge.service.DeviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "devices")
public class DeviceController {
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping(value = {"{uuid}"})
    public ResponseEntity<Void> isDevicePresent(@PathVariable UUID uuid) {
        return deviceService.isDevicePresent(uuid) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
