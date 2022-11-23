package com.tlp.challenge.dto;

/**
 * A DTO for the {@link com.tlp.challenge.entity.Customer} entity
 */
public record CustomerDTO(Long id, String name, String surname, String fiscalCode,
                          String address) {
}