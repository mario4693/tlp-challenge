package com.tlp.challenge.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;
@Setter
@Getter
@Entity
@Table(name = "device")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, length = 16)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private DeviceState state;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private Device() {
    }

    public Device(UUID id, DeviceState state) {
        this.id = id;
        this.state = state;
    }

    public enum DeviceState {
        ACTIVE,
        INACTIVE,
        LOST
    }
}