package com.tlp.challenge.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tlp.challenge.entity.Device;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * A DTO for the {@link com.tlp.challenge.entity.Device} entity
 */
//public record DeviceDTO(UUID id, Device.DeviceState state, CustomerDTO customer){}
@Getter
@Builder(setterPrefix = "with")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class DeviceDTO {
    private final UUID id;
    private final Device.DeviceState state;
    private final Long customerId;
}