package com.tlp.challenge.service;

import com.tlp.challenge.builder.CustomerBuilder;
import com.tlp.challenge.builder.DeviceBuilder;
import com.tlp.challenge.dto.NewDeviceDTO;
import com.tlp.challenge.entity.Device;
import com.tlp.challenge.exception.CustomerNotFoundException;
import com.tlp.challenge.repository.CustomerRepository;
import com.tlp.challenge.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private CustomerRepository customerRepository;

    private DeviceService deviceService;

    @BeforeEach
    void setUp() {
        deviceService = new DeviceService(deviceRepository, customerRepository);
    }

    @Test
    void isDevicePresent_shouldReturnTrue() {
        var deviceId = UUID.randomUUID();
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(new DeviceBuilder().build()));
        var isPresent = deviceService.isDevicePresent(deviceId);
        verify(deviceRepository).findById(deviceId);
        verifyNoMoreInteractions(deviceRepository);
        assertTrue(isPresent);
    }

    @Test
    void isDevicePresent_shouldReturnFalse() {
        var deviceId = UUID.randomUUID();
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.empty());
        var isPresent = deviceService.isDevicePresent(deviceId);
        verify(deviceRepository).findById(deviceId);
        verifyNoMoreInteractions(deviceRepository);
        assertFalse(isPresent);
    }

    @Test
    void deleteDeviceById_shouldReturnTrueIfDeleteCorrectlyPerformed() {
        var deviceId = UUID.randomUUID();
        when(deviceRepository.existsById(deviceId)).thenReturn(true);
        boolean isDeleted = deviceService.deleteDeviceById(deviceId);
        verify(deviceRepository).existsById(deviceId);
        verify(deviceRepository).deleteById(deviceId);
        verifyNoMoreInteractions(deviceRepository);
        assertTrue(isDeleted);
    }

    @Test
    void deleteDeviceById_shouldReturnFalseIfDeviceHasNotBeenDeleted() {
        var deviceId = UUID.randomUUID();
        when(deviceRepository.existsById(deviceId)).thenReturn(false);
        var isDeleted = deviceService.deleteDeviceById(deviceId);
        verify(deviceRepository).existsById(deviceId);
        verifyNoMoreInteractions(deviceRepository);
        assertFalse(isDeleted);
    }

    @Test
    void editDeviceState_shouldReturnADeviceDTOWithNewAddress(){
        var deviceId = UUID.randomUUID();
        var oldState = Device.DeviceState.INACTIVE;
        var newDeviceState = Device.DeviceState.LOST;
        var oldDevice = new DeviceBuilder().withId(deviceId).withState(oldState).build();
        var newDevice = new DeviceBuilder().withId(deviceId).withState(newDeviceState).build();
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(oldDevice));
        when(deviceRepository.save(any())).thenReturn(newDevice);

        var optionalDeviceDTO = deviceService.editDeviceState(deviceId, newDeviceState);
        verify(deviceRepository).findById(deviceId);
        verify(deviceRepository).save(any());
        assertTrue(optionalDeviceDTO.isPresent());
        assertEquals(newDeviceState, optionalDeviceDTO.get().getState());
    }

    @Test
    void editDeviceState_shouldReturnAnEmptyOptionalIfDeviceNotFound(){
        var deviceId = UUID.randomUUID();
        var newDeviceState = Device.DeviceState.LOST;
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.empty());

        var optionalDeviceDTO = deviceService.editDeviceState(deviceId, newDeviceState);
        verify(deviceRepository).findById(deviceId);
        verifyNoMoreInteractions(deviceRepository);
        assertFalse(optionalDeviceDTO.isPresent());
    }

    @Test
    void saveDevices_shouldReturnNewSavedDevices() throws CustomerNotFoundException {
        var UUID_1 = UUID.randomUUID();
        var customerId = 1L;
        var newDeviceDTO = new NewDeviceDTO(Device.DeviceState.INACTIVE, customerId);
        var aCustomer = new CustomerBuilder().withId(customerId).build();
        var device = new DeviceBuilder().withId(UUID_1).withState(Device.DeviceState.INACTIVE).withCustomer(aCustomer).build();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(aCustomer));
        when(deviceRepository.save(any())).thenReturn(device);
        var savedDeviceDTO = deviceService.saveDevice(newDeviceDTO);
        verify(customerRepository).findById(customerId);
        verify(deviceRepository).save(any());
        verifyNoMoreInteractions(deviceRepository);
        assertNotNull(savedDeviceDTO.getId());
        assertNotNull(savedDeviceDTO.getState());
        assertNotNull(savedDeviceDTO.getCustomerId());
    }
}