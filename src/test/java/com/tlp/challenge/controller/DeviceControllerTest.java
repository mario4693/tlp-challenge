package com.tlp.challenge.controller;

import com.tlp.challenge.dto.DeviceDTO;
import com.tlp.challenge.dto.EditDeviceStateDTO;
import com.tlp.challenge.dto.NewDeviceDTO;
import com.tlp.challenge.entity.Device;
import com.tlp.challenge.exception.CustomerNotFoundException;
import com.tlp.challenge.service.DeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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
        var deviceId = UUID.randomUUID();
        when(deviceService.isDevicePresent(deviceId)).thenReturn(true);
        var response = deviceController.isDevicePresent(deviceId);
        verify(deviceService).isDevicePresent(deviceId);
        verifyNoMoreInteractions(deviceService);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void getDevice_shouldReturnEmptyBodyWhenDeviceIdNotFound(){
        var deviceId = UUID.randomUUID();
        when(deviceService.isDevicePresent(deviceId)).thenReturn(false);
        var response = deviceController.isDevicePresent(deviceId);
        verify(deviceService).isDevicePresent(deviceId);
        verifyNoMoreInteractions(deviceService);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteDevice_shouldReturnNoContentIfDeviceCorrectlyDeleted(){
        var deviceId = UUID.randomUUID();
        when(deviceService.deleteDeviceById(deviceId)).thenReturn(true);
        var response = deviceController.deleteDevice(deviceId);
        verify(deviceService).deleteDeviceById(deviceId);
        verifyNoMoreInteractions(deviceService);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteDevice_shouldReturnNotFoundIfDeviceToDeleteDoesNotExists(){
        var deviceId = UUID.randomUUID();
        when(deviceService.deleteDeviceById(deviceId)).thenReturn(false);
        var response = deviceController.deleteDevice(deviceId);
        verify(deviceService).deleteDeviceById(deviceId);
        verifyNoMoreInteractions(deviceService);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void editDeviceState_shouldReturnABodyWithUpdatedDevice(){
        var deviceId = UUID.randomUUID();
        var newDeviceState = Device.DeviceState.LOST;
        var editDeviceStateDTO = new EditDeviceStateDTO(newDeviceState);
        var updatedDevice = DeviceDTO.builder().withId(deviceId).withState(editDeviceStateDTO.state()).build();
        when(deviceService.editDeviceState(deviceId, editDeviceStateDTO.state())).thenReturn(Optional.of(updatedDevice));
        var response = deviceController.editDeviceState(deviceId, editDeviceStateDTO);
        verify(deviceService).editDeviceState(deviceId, newDeviceState);
        verifyNoMoreInteractions(deviceService);
        assertTrue(Objects.nonNull(response.getBody()));
        assertEquals(editDeviceStateDTO.state(), response.getBody().getState());
    }

    @Test
    void editDeviceState_shouldReturnAnEmptyBodyWhenDeviceToUpdateDoesNotExists(){
        var deviceId = UUID.randomUUID();
        var newDeviceState = Device.DeviceState.LOST;
        var editDeviceStateDTO = new EditDeviceStateDTO(newDeviceState);
        when(deviceService.editDeviceState(deviceId, editDeviceStateDTO.state())).thenReturn(Optional.empty());
        var response = deviceController.editDeviceState(deviceId, editDeviceStateDTO);
        verify(deviceService).editDeviceState(deviceId, newDeviceState);
        verifyNoMoreInteractions(deviceService);
        assertTrue(Objects.isNull(response.getBody()));
    }

    @Test
    void createDevices_shouldReturnNewDevice() throws CustomerNotFoundException {
        var UUID_1 = UUID.randomUUID();
        var newDeviceDTO = new NewDeviceDTO(Device.DeviceState.INACTIVE, 1L);
        var deviceDTO = DeviceDTO.builder()
                .withId(UUID_1)
                .withState(Device.DeviceState.INACTIVE)
                .withCustomerId(1L).build();
        when(deviceService.saveDevice(newDeviceDTO)).thenReturn(deviceDTO);
        var response = deviceController.createDevice(newDeviceDTO);
        verify(deviceService).saveDevice(newDeviceDTO);
        verifyNoMoreInteractions(deviceService);
        assertEquals(deviceDTO, response.getBody());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertNotNull(response.getBody().getCustomerId());
    }
}