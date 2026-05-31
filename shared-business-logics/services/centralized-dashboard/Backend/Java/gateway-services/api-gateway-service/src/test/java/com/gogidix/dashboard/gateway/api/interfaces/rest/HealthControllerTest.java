package com.gogidix.dashboard.gateway.api.interfaces.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HealthControllerTest {

    private final HealthController controller = new HealthController();

    @Nested
    @DisplayName("health tests")
    class HealthTests {
        @Test
        void health_returnsUp() {
            ResponseEntity<Map<String, Object>> response = controller.health();
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("UP", response.getBody().get("status"));
            assertEquals("api-gateway-service", response.getBody().get("service"));
            assertNotNull(response.getBody().get("timestamp"));
        }
    }

    @Nested
    @DisplayName("info tests")
    class InfoTests {
        @Test
        void info_returnsServiceInfo() {
            ResponseEntity<Map<String, Object>> response = controller.info();
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("API Gateway Service", response.getBody().get("name"));
            assertEquals("1.0.0", response.getBody().get("version"));
            assertEquals(8907, response.getBody().get("port"));
        }
    }
}
