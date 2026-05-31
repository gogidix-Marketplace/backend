package com.gogidix.dashboard.gateway.chart.interfaces.rest;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HealthControllerTest {

    private final HealthController healthController = new HealthController();

    @Test
    void health_returnsUpStatus() {
        ResponseEntity<Map<String, Object>> response = healthController.health();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("UP", response.getBody().get("status"));
        assertEquals("chart-service", response.getBody().get("service"));
        assertNotNull(response.getBody().get("timestamp"));
    }

    @Test
    void info_returnsServiceInformation() {
        ResponseEntity<Map<String, Object>> response = healthController.info();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Chart Service", response.getBody().get("name"));
        assertEquals("1.0.0", response.getBody().get("version"));
        assertEquals(8909, response.getBody().get("port"));
        assertNotNull(response.getBody().get("description"));
    }
}
