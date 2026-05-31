package com.gogidix.shared.warehousing.zone.interfaces.rest;

import com.gogidix.shared.warehousing.zone.application.service.ZoneService;
import com.gogidix.shared.warehousing.zone.domain.entity.Zone;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ZoneControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ZoneService zoneService;

    @InjectMocks
    private ZoneController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getWarehousesForZone_returns200() throws Exception {
        Zone zone = Zone.builder().id("z-1").zoneId("zone-lagos").zoneName("Lagos").build();
        when(zoneService.getWarehousesForZone("zone-lagos")).thenReturn(List.of(zone));

        mockMvc.perform(get("/api/v1/zones/zone-lagos/warehouses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].zoneId").value("zone-lagos"));
    }

    @Test
    void resolveZone_returns200() throws Exception {
        Zone zone = Zone.builder().id("z-1").zoneId("zone-lagos").zoneName("Lagos").build();
        when(zoneService.resolveZone(6.5, 3.4)).thenReturn(zone);

        mockMvc.perform(get("/api/v1/zones/resolve")
                        .param("lat", "6.5")
                        .param("lng", "3.4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.zoneId").value("zone-lagos"));
    }

    @Test
    void findNearbyZones_returns200() throws Exception {
        Zone zone = Zone.builder().id("z-1").zoneId("zone-lagos").build();
        when(zoneService.findNearbyZones(6.5, 3.4, 50)).thenReturn(List.of(zone));

        mockMvc.perform(get("/api/v1/zones/nearby")
                        .param("lat", "6.5")
                        .param("lng", "3.4")
                        .param("radius", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].zoneId").value("zone-lagos"));
    }

    @Test
    void getAllZones_returns200() throws Exception {
        when(zoneService.getAllZones()).thenReturn(List.of(
                Zone.builder().id("z-1").zoneId("zone-lagos").build(),
                Zone.builder().id("z-2").zoneId("zone-abuja").build()
        ));

        mockMvc.perform(get("/api/v1/zones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getZoneById_returns200() throws Exception {
        when(zoneService.getZoneById("z-1")).thenReturn(
                Zone.builder().id("z-1").zoneId("zone-lagos").zoneName("Lagos").build());

        mockMvc.perform(get("/api/v1/zones/z-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.zoneName").value("Lagos"));
    }

    @Test
    void createZone_returns201() throws Exception {
        Zone zone = Zone.builder().zoneName("Test Zone").zoneId("zone-test").build();
        when(zoneService.createZone(any(Zone.class))).thenReturn(
                Zone.builder().id("new-1").zoneName("Test Zone").zoneId("zone-test").build());

        mockMvc.perform(post("/api/v1/zones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(zone)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("new-1"));
    }
}
