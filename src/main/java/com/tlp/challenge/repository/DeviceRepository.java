package com.tlp.challenge.repository;

import com.tlp.challenge.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
    Optional<List<Device>> findByCustomerId(Long id);
}