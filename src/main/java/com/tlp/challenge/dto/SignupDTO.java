package com.tlp.challenge.dto;

import com.tlp.challenge.entity.Device;

import javax.validation.constraints.Size;

public record SignupDTO(String name, String surname, String fiscalCode, String address, @Size(max = 2) Device... devices) {}