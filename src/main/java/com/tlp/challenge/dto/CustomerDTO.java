package com.tlp.challenge.dto;

import lombok.Builder;

import java.util.List;

/**
 * A DTO for the {@link com.tlp.challenge.entity.Customer} entity
 */
@Builder(setterPrefix = "with")
public record CustomerDTO(Long id, String name, String surname, String fiscalCode,
                          String address, List<DeviceDTO> devices) {
}