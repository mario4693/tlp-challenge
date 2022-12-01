package com.tlp.challenge.service;

import com.tlp.challenge.builder.DeviceBuilder;
import com.tlp.challenge.dto.DeviceDTO;
import com.tlp.challenge.dto.NewDeviceDTO;
import com.tlp.challenge.dto.NewDevicesDTO;
import com.tlp.challenge.entity.Device;
import com.tlp.challenge.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
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
    void isDevicePresent_shouldReturnTrue() {
        UUID deviceId = UUID.randomUUID();
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(new DeviceBuilder().build()));
        boolean isPresent = deviceService.isDevicePresent(deviceId);
        verify(deviceRepository).findById(deviceId);
        verifyNoMoreInteractions(deviceRepository);
        assertTrue(isPresent);
    }

    @Test
    void isDevicePresent_shouldReturnFalse() {
        UUID deviceId = UUID.randomUUID();
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.empty());
        boolean isPresent = deviceService.isDevicePresent(deviceId);
        verify(deviceRepository).findById(deviceId);
        verifyNoMoreInteractions(deviceRepository);
        assertFalse(isPresent);
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

    @Test
    void editDeviceState_shouldReturnADeviceDTOWithNewAddress(){
        UUID deviceId = UUID.randomUUID();
        Device.DeviceState oldState = Device.DeviceState.INACTIVE;
        Device.DeviceState newDeviceState = Device.DeviceState.LOST;
        Device oldDevice = new DeviceBuilder().withId(deviceId).withState(oldState).build();
        Device newDevice = new DeviceBuilder().withId(deviceId).withState(newDeviceState).build();
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(oldDevice));
        when(deviceRepository.save(any())).thenReturn(newDevice);

        Optional<DeviceDTO> optionalDeviceDTO = deviceService.editDeviceState(deviceId, newDeviceState);
        verify(deviceRepository).findById(deviceId);
        verify(deviceRepository).save(any());
        assertTrue(optionalDeviceDTO.isPresent());
        assertEquals(newDeviceState, optionalDeviceDTO.get().getState());
    }

    @Test
    void editDeviceState_shouldReturnAnEmptyOptionalIfDeviceNotFound(){
        UUID deviceId = UUID.randomUUID();
        Device.DeviceState newDeviceState = Device.DeviceState.LOST;
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.empty());

        Optional<DeviceDTO> optionalDeviceDTO = deviceService.editDeviceState(deviceId, newDeviceState);
        verify(deviceRepository).findById(deviceId);
        verifyNoMoreInteractions(deviceRepository);
        assertFalse(optionalDeviceDTO.isPresent());
    }

    @Test
    void saveDevices_shouldReturnNewSavedDevices() {
        var UUID_1 = UUID.randomUUID();
        var UUID_2 = UUID.randomUUID();
//        var deviceDTO1 = DeviceDTO.builder().withId(UUID_1).withState(Device.DeviceState.INACTIVE).build();
//        var deviceDTO2 = DeviceDTO.builder().withId(UUID_1).withState(Device.DeviceState.LOST).build();
        var deviceDTO1 = new NewDeviceDTO(Device.DeviceState.INACTIVE);
        var deviceDTO2 = new NewDeviceDTO(Device.DeviceState.LOST);
        var device1 = new DeviceBuilder().withId(UUID_1).withState(Device.DeviceState.INACTIVE).build();
        var device2 = new DeviceBuilder().withId(UUID_2).withState(Device.DeviceState.LOST).build();
        var devices = List.of(device1, device2);
        when(deviceRepository.saveAll(anyList())).thenReturn(devices);
        var newDevicesDTO = new NewDevicesDTO(List.of(deviceDTO1, deviceDTO2));
        var savedDevicesDTO = deviceService.saveDevices(newDevicesDTO);
        verify(deviceRepository).saveAll(anyList());
        verifyNoMoreInteractions(deviceRepository);
        assertFalse(savedDevicesDTO.isEmpty());
        assertEquals(2, savedDevicesDTO.size());
    }
}