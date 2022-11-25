package com.tlp.challenge.controller;

import com.tlp.challenge.service.DeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceControllerTest {

    @Mock
    private DeviceService deviceService;

    private DeviceController deviceController;

    @BeforeEach
    void setUp() {
        deviceController = new DeviceController(deviceService);
    }

    @Test
    void getDevice_shouldReturnAnExistentDeviceById(){
        UUID deviceId = UUID.randomUUID();
        when(deviceService.isDevicePresent(deviceId)).thenReturn(true);
        ResponseEntity<Void> response = deviceController.isDevicePresent(deviceId);
        verify(deviceService).isDevicePresent(deviceId);
        verifyNoMoreInteractions(deviceService);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getDevice_shouldReturnEmptyBodyWhenDeviceIdNotFound(){
        UUID deviceId = UUID.randomUUID();
        when(deviceService.isDevicePresent(deviceId)).thenReturn(false);
        ResponseEntity<Void> response = deviceController.isDevicePresent(deviceId);
        verify(deviceService).isDevicePresent(deviceId);
        verifyNoMoreInteractions(deviceService);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}