package com.gogidix.shared.courier.ecommerce.unit;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import com.gogidix.shared.courier.ecommerce.interfaces.rest.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleIllegalArgumentException() {
        ResponseEntity<Map<String, Object>> response = handler.handleIllegalArgument(
            new IllegalArgumentException("Not found"));

        assertEquals(404, response.getBody().get("status"));
        assertEquals("Not found", response.getBody().get("message"));
    }

    @Test
    void shouldHandleIllegalStateException() {
        ResponseEntity<Map<String, Object>> response = handler.handleIllegalState(
            new IllegalStateException("Conflict"));

        assertEquals(409, response.getBody().get("status"));
        assertEquals("Conflict", response.getBody().get("message"));
    }

    @Test
    void shouldHandleGenericException() {
        ResponseEntity<Map<String, Object>> response = handler.handleGeneric(
            new RuntimeException("Something broke"));

        assertEquals(500, response.getBody().get("status"));
        assertEquals("Something broke", response.getBody().get("message"));
    }
}
