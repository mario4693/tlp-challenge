package com.tlp.challenge.controller;

import com.tlp.challenge.builder.CustomerBuilder;
import com.tlp.challenge.builder.DeviceBuilder;
import com.tlp.challenge.entity.Device;
import com.tlp.challenge.repository.CustomerRepository;
import com.tlp.challenge.repository.DeviceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class DeviceControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private final static String BASE_PATH = "http://localhost:8080/api/v1";

    private final static UUID DEVICE_ID = UUID.randomUUID();
    private final static Device aDevice = new DeviceBuilder().withState(Device.DeviceState.INACTIVE).build();

    @AfterEach
    void clearDeviceRepo(){
        deviceRepository.deleteAll();
    }

    @Test
    public void createDevice_shouldReturn201WhenDeviceIsCreated() throws Exception {
        var aCustomer = new CustomerBuilder().withName("Mario").build();
        var customerId = customerRepository.save(aCustomer).getId();
        mockMvc.perform(post(BASE_PATH + "/devices")
                        .content("""
                                {
                                    "state":"INACTIVE",
                                    "customerId":"""+customerId+"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void createDevice_shouldReturn404WhenCustomerToPairWithDeviceIsNotPresent() throws Exception {
        mockMvc.perform(post(BASE_PATH + "/devices")
                        .content("""
                                {
                                    "state":"INACTIVE",
                                    "customerId":1}""")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createDevice_shouldReturn400IfRequestWrongFormatted() throws Exception {
        mockMvc.perform(post(BASE_PATH + "/devices")
                        .content("""
                                {
                                    "state":"INACTIVE"
                                }""")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    public void isDevicePresent_shouldReturn204WithoutBody() throws Exception {
        var deviceId = deviceRepository.save(aDevice).getId();
        var response = mockMvc.perform(get(BASE_PATH + "/devices/"+deviceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn().getResponse();
                assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void isDevicePresent_shouldReturn404IfDeviceIsNotPresent() throws Exception {
        mockMvc.perform(get(BASE_PATH + "/devices/"+DEVICE_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void editDeviceState_shouldReturn200WithNewDeviceDTO() throws Exception {
        var deviceId = deviceRepository.save(aDevice).getId();
        var newAddressRequest = """
                                {"state":"LOST"}
                                """;
        mockMvc.perform(patch(BASE_PATH + "/devices/"+deviceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newAddressRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(deviceId.toString()))
                .andExpect(jsonPath("$.state").value("LOST"));
    }

    @Test
    public void editDeviceState_shouldReturn400IfNewStateIsMissing() throws Exception {
        mockMvc.perform(patch(BASE_PATH + "/devices/"+DEVICE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"state":null}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editDeviceState_shouldReturn404IfDeviceToUpdateNotFound() throws Exception {
        mockMvc.perform(patch(BASE_PATH + "/devices/"+DEVICE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"state":"LOST"}
                                """))
                .andExpect(status().isNotFound());
    }
}