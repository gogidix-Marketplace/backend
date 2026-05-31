package com.gogidix.monitoring.alertmanagementservice.interfaces.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("HealthController Tests")
class HealthControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new HealthController()).build();
    }

    @Test
    @DisplayName("GET /health - Should return health status")
    void shouldReturnHealthStatus() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("alert-management-service"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("GET /health/liveness - Should return liveness status")
    void shouldReturnLivenessStatus() throws Exception {
        mockMvc.perform(get("/health/liveness"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("alive"));
    }

    @Test
    @DisplayName("GET /health/readiness - Should return readiness status")
    void shouldReturnReadinessStatus() throws Exception {
        mockMvc.perform(get("/health/readiness"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ready"));
    }
}
