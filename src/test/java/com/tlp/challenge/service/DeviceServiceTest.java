package com.tlp.challenge.service;

import com.tlp.challenge.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    private DeviceService deviceService;

    @BeforeEach
    void setUp() {
        deviceService = new DeviceService(deviceRepository);
    }

    @Test
    void isDevicePresent() {
    }

    @Test
    void deleteDeviceById_shouldReturnTrueIfDeleteCorrectlyPerformed() {
        UUID deviceId = UUID.randomUUID();
        when(deviceRepository.existsById(deviceId)).thenReturn(true);
        boolean isDeleted = deviceService.deleteDeviceById(deviceId);
        verify(deviceRepository).existsById(deviceId);
        verify(deviceRepository).deleteById(deviceId);
        verifyNoMoreInteractions(deviceRepository);
        assertTrue(isDeleted);
    }

    @Test
    void deleteDeviceById_shouldReturnFalseIfDeviceHasNotBeenDeleted() {
        UUID deviceId = UUID.randomUUID();
        when(deviceRepository.existsById(deviceId)).thenReturn(false);
        boolean isDeleted = deviceService.deleteDeviceById(deviceId);
        verify(deviceRepository).existsById(deviceId);
        verifyNoMoreInteractions(deviceRepository);
        assertFalse(isDeleted);
    }
}