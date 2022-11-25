package com.tlp.challenge.dto;

import com.tlp.challenge.entity.Device;

import java.util.UUID;

/**
 * A DTO for the {@link com.tlp.challenge.entity.Device} entity
 */
public record DeviceDTO(UUID id, Device.DeviceState state) {}