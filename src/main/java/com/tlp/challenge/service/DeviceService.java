package com.tlp.challenge.service;

import com.tlp.challenge.dto.NewDeviceDTO;
import com.tlp.challenge.dto.DeviceDTO;
import com.tlp.challenge.entity.Device;
import com.tlp.challenge.exception.CustomerNotFoundException;
import com.tlp.challenge.repository.CustomerRepository;
import com.tlp.challenge.repository.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.tlp.challenge.util.Utils.toDevice;

@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final CustomerRepository customerRepository;

    public DeviceService(DeviceRepository deviceRepository, CustomerRepository customerRepository) {
        this.deviceRepository = deviceRepository;
        this.customerRepository = customerRepository;
    }

    public boolean isDevicePresent(UUID deviceId) {
        return deviceRepository.findById(deviceId).isPresent();
    }

    public boolean deleteDeviceById(UUID deviceId) {
        boolean isDeleted = false;
        if(deviceRepository.existsById(deviceId)){
            deviceRepository.deleteById(deviceId);
            isDeleted = true;
        }
        return isDeleted;
    }

    public Optional<DeviceDTO> editDeviceState(UUID deviceId, Device.DeviceState newDeviceState) {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
        return optionalDevice.map(device -> toDeviceDTO(updateDeviceState(device, newDeviceState)));
    }

    private Device updateDeviceState(Device device, Device.DeviceState newDeviceState){
        device.setState(newDeviceState);
        return deviceRepository.save(device);
    }

    private DeviceDTO toDeviceDTO(Device device){
        return Objects.nonNull(device.getCustomer()) ?
                DeviceDTO.builder().withId(device.getId()).withState(device.getState()).withCustomerId(device.getCustomer().getId()).build()
                : DeviceDTO.builder().withId(device.getId()).withState(device.getState()).build();
    }

    public DeviceDTO saveDevice(NewDeviceDTO newDeviceDTO) throws CustomerNotFoundException {
        var customer = customerRepository.findById(newDeviceDTO.customerId()).orElseThrow(CustomerNotFoundException::new);
        var savedDevice = deviceRepository.save(toDevice(newDeviceDTO, customer));
        return toDeviceDTO(savedDevice);
    }
}