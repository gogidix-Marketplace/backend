package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.interfaces.rest.controller;

import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.exception.ServiceLevelAgreementNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
    }

    @Test
    @DisplayName("Should handle ServiceLevelAgreementNotFoundException")
    void shouldHandleSlaNotFoundException() {
        ServiceLevelAgreementNotFoundException ex = new ServiceLevelAgreementNotFoundException("sla-123");

        ResponseEntity<Object> response = handler.handleSlaNotFoundException(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.NOT_FOUND.value(), body.get("status"));
        assertEquals("NOT_FOUND", body.get("error"));
        assertTrue(((String) body.get("message")).contains("sla-123"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    @DisplayName("Should handle IllegalArgumentException")
    void shouldHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Bad request data");

        ResponseEntity<Object> response = handler.handleIllegalArgumentException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.get("status"));
        assertEquals("BAD_REQUEST", body.get("error"));
        assertEquals("Bad request data", body.get("message"));
    }

    @Test
    @DisplayName("Should handle generic exception")
    void shouldHandleGenericException() {
        Exception ex = new Exception("Something went wrong");

        ResponseEntity<Object> response = handler.handleGenericException(ex, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), body.get("status"));
        assertEquals("INTERNAL_SERVER_ERROR", body.get("error"));
        assertEquals("An unexpected error occurred", body.get("message"));
    }
}
