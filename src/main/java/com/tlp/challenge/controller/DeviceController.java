package com.tlp.challenge.controller;

import com.tlp.challenge.dto.DeviceDTO;
import com.tlp.challenge.dto.EditDeviceStateDTO;
import com.tlp.challenge.service.DeviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
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

    @DeleteMapping(value = "{uuid}")
    public ResponseEntity<Void> deleteDevice(@PathVariable UUID uuid) {
        return deviceService.deleteDeviceById(uuid) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping("{uuid}")
    public ResponseEntity<DeviceDTO> editDeviceState(@PathVariable UUID uuid, @Valid @RequestBody EditDeviceStateDTO editDeviceStateDTO) {
        Optional<DeviceDTO> optionalDeviceDTO = deviceService.editDeviceState(uuid, editDeviceStateDTO.state());
        return optionalDeviceDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
