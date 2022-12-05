package com.tlp.challenge.controller;

import com.tlp.challenge.dto.DeviceDTO;
import com.tlp.challenge.dto.EditDeviceStateDTO;
import com.tlp.challenge.dto.NewDeviceDTO;
import com.tlp.challenge.exception.CustomerDevicesFullException;
import com.tlp.challenge.exception.CustomerNotFoundException;
import com.tlp.challenge.service.DeviceService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "api/v1/devices")
public class DeviceController {
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New created device"),
            @ApiResponse(responseCode = "404", description = "Customer to pair with new device not found", content = @Content),
            @ApiResponse(responseCode = "422", description = "Server can't add also this new device", content = @Content)
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<DeviceDTO> createDevice(@Valid @RequestBody NewDeviceDTO newDeviceDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(deviceService.saveDevice(newDeviceDTO));
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomerDevicesFullException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Device specified is present"),
            @ApiResponse(responseCode = "404", description = "Device not found", content = @Content),
    })
    @GetMapping(value = "{uuid}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> isDevicePresent(@PathVariable UUID uuid) {
        return deviceService.isDevicePresent(uuid) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Device specified has been deleted"),
            @ApiResponse(responseCode = "404", description = "Device not found", content = @Content),
    })
    @DeleteMapping(value = "{uuid}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteDevice(@PathVariable UUID uuid) {
        return deviceService.deleteDeviceById(uuid) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New edited device"),
            @ApiResponse(responseCode = "404", description = "Device to updated not found", content = @Content),
    })
    @PatchMapping(value = "{uuid}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<DeviceDTO> editDeviceState(@PathVariable UUID uuid, @Valid @RequestBody EditDeviceStateDTO editDeviceStateDTO) {
        var optionalDeviceDTO = deviceService.editDeviceState(uuid, editDeviceStateDTO.state());
        return optionalDeviceDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
