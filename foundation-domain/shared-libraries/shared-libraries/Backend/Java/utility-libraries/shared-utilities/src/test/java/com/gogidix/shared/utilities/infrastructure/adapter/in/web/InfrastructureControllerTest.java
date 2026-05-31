package com.gogidix.shared.utilities.infrastructure.adapter.in.web;

import com.gogidix.shared.utilities.application.port.in.ProcessUtilityPort;
import com.gogidix.shared.utilities.domain.model.ProcessingRequest;
import com.gogidix.shared.utilities.domain.model.UtilityResult;
import com.gogidix.shared.utilities.domain.valueobject.UtilityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

class InfrastructureControllerTest {

    private UtilityController controller;
    private ProcessUtilityPort port;

    @BeforeEach
    void setUp() {
        port = new ProcessUtilityPort() {
            public UtilityResult<?> processUtilityRequest(ProcessingRequest req) {
                return UtilityResult.success("op-1", req.getUtilityType(), "ok");
            }
            public CompletableFuture<UtilityResult<?>> processUtilityRequestAsync(ProcessingRequest req) {
                return CompletableFuture.completedFuture(UtilityResult.success("op-1", req.getUtilityType(), "async-ok"));
            }
        };
        controller = new UtilityController(port);
    }

    @Test
    void formatDateTime_success() {
        String dt = LocalDateTime.now().toString();
        var response = controller.formatDateTime(Map.of("dateTime", dt));
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void formatDateTime_invalidInput() {
        var response = controller.formatDateTime(Map.of("dateTime", "invalid"));
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void parseDateTime_success() {
        String iso = LocalDateTime.now().toString();
        var response = controller.parseDateTime(Map.of("isoString", iso));
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void parseDateTime_missingKey() {
        var response = controller.parseDateTime(Map.of());
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void isDateTimeExpired_success() {
        String dt = LocalDateTime.now().toString();
        var response = controller.isDateTimeExpired(Map.of("dateTime", dt));
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void isDateTimeExpired_invalidInput() {
        var response = controller.isDateTimeExpired(Map.of("dateTime", "not-a-date"));
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void serializeToJson_success() {
        var response = controller.serializeToJson(Map.of("key", "value"));
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void validateJson_success() {
        var response = controller.validateJson(Map.of("json", "{\"valid\":true}"));
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void validateJson_invalidInput() {
        var response = controller.validateJson(Map.of());
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void prettyPrintJson_success() {
        var response = controller.prettyPrintJson(Map.of("json", "{\"a\":1}"));
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void processAsync_success() {
        var response = controller.processAsync(Map.of("utilityType", "DATETIME_FORMATTING", "data", "test"));
        assertEquals(202, response.getStatusCode().value());
    }

    @Test
    void processAsync_invalidType() {
        var response = controller.processAsync(Map.of("utilityType", "INVALID", "data", "test"));
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void getHealth() {
        var response = controller.getHealth();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().get("status"));
    }

    @Test
    void getUtilityTypes() {
        var response = controller.getUtilityTypes();
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().length > 0);
    }
}
