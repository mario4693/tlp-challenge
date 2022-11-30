package com.tlp.challenge.controller;

import com.tlp.challenge.dto.*;
import com.tlp.challenge.service.DeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "api/v1/devices")
public class DeviceController {
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeviceDTO>> createDevices(@Valid @RequestBody NewDevicesDTO newDevicesDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deviceService.saveDevices(newDevicesDTO));
    }

    @GetMapping(value = "{uuid}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> isDevicePresent(@PathVariable UUID uuid) {
        return deviceService.isDevicePresent(uuid) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "{uuid}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteDevice(@PathVariable UUID uuid) {
        return deviceService.deleteDeviceById(uuid) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping(value = "{uuid}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<DeviceDTO> editDeviceState(@PathVariable UUID uuid, @Valid @RequestBody EditDeviceStateDTO editDeviceStateDTO) {
        Optional<DeviceDTO> optionalDeviceDTO = deviceService.editDeviceState(uuid, editDeviceStateDTO.state());
        return optionalDeviceDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
