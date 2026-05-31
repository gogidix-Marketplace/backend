package com.gogidix.shared.exceptions.domain.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("IntegrationException Tests")
class IntegrationExceptionTest {

    @Test
    @DisplayName("Test constructor with service name and message")
    void testConstructorWithServiceNameAndMessage() {
        IntegrationException exception = new IntegrationException("PaymentGateway", "Connection timeout");
        assertTrue(exception.getMessage().contains("PaymentGateway"));
        assertTrue(exception.getMessage().contains("Connection timeout"));
        assertEquals("INTEGRATION_ERROR", exception.getErrorCode());
        assertEquals(503, exception.getHttpStatus());
        assertEquals("PaymentGateway", exception.getServiceName());
    }

    @Test
    @DisplayName("Test constructor with service name, message, and cause")
    void testConstructorWithCause() {
        Throwable cause = new java.net.ConnectException("Connection refused");
        IntegrationException exception = new IntegrationException("InventoryService", "Unable to connect", cause);
        assertTrue(exception.getMessage().contains("InventoryService"));
        assertEquals("INTEGRATION_ERROR", exception.getErrorCode());
        assertEquals(503, exception.getHttpStatus());
        assertEquals("InventoryService", exception.getServiceName());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("Test service name is in context")
    void testServiceNameInContext() {
        IntegrationException exception = new IntegrationException("ERPService", "Timeout");
        assertEquals("ERPService", exception.getContext().get("serviceName"));
    }

    @Test
    @DisplayName("Test integration exception is critical (5xx)")
    void testIsCritical() {
        IntegrationException exception = new IntegrationException("Service", "Error");
        assertTrue(exception.isCritical());
    }

    @Test
    @DisplayName("Test integration exception is not retryable by default")
    void testIsNotRetryable() {
        IntegrationException exception = new IntegrationException("Service", "Error");
        assertFalse(exception.isRetryable());
    }

    @Test
    @DisplayName("Test can be thrown and caught")
    void testCanBeThrownAndCaught() {
        IntegrationException caught = assertThrows(IntegrationException.class, () -> {
            throw new IntegrationException("ExternalAPI", "Rate limited");
        });
        assertEquals("ExternalAPI", caught.getServiceName());
        assertEquals(503, caught.getHttpStatus());
    }
}
