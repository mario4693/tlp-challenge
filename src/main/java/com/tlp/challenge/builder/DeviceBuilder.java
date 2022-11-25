package com.tlp.challenge.builder;

import com.tlp.challenge.entity.Device;

import java.util.UUID;

public class DeviceBuilder {
    private UUID id;
    private Device.DeviceState state;

    public DeviceBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public DeviceBuilder withState(Device.DeviceState state) {
        this.state = state;
        return this;
    }

    public Device build() {
        return new Device(id, state);
    }
}