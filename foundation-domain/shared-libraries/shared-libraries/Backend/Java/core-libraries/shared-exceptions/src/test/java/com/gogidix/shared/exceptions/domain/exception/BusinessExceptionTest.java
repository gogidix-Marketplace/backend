package com.gogidix.shared.exceptions.domain.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for BusinessException
 */
@DisplayName("BusinessException Tests")
class BusinessExceptionTest {

    @Test
    @DisplayName("Test constructor with message only")
    void testConstructorWithMessage() {
        BusinessException exception = new BusinessException("Business rule violated");

        assertEquals("Business rule violated", exception.getMessage());
        assertEquals("BUSINESS_ERROR", exception.getErrorCode());
        assertEquals(400, exception.getHttpStatus());
        assertEquals("BUSINESS", exception.getCategory());
    }

    @Test
    @DisplayName("Test constructor with message and error code")
    void testConstructorWithMessageAndErrorCode() {
        BusinessException exception = new BusinessException("Invalid operation", "INVALID_OPERATION");

        assertEquals("Invalid operation", exception.getMessage());
        assertEquals("INVALID_OPERATION", exception.getErrorCode());
        assertEquals(400, exception.getHttpStatus());
    }

    @Test
    @DisplayName("Test constructor with message and cause")
    void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException("Root cause");
        BusinessException exception = new BusinessException("Business error", cause);

        assertEquals("Business error", exception.getMessage());
        assertEquals("BUSINESS_ERROR", exception.getErrorCode());
        assertEquals(cause, exception.getCause());
        assertEquals(400, exception.getHttpStatus());
    }

    @Test
    @DisplayName("Test constructor with message, error code, and custom HTTP status")
    void testConstructorWithMessageErrorCodeAndHttpStatus() {
        BusinessException exception = new BusinessException("Payment required", "PAYMENT_REQUIRED", 402);

        assertEquals("Payment required", exception.getMessage());
        assertEquals("PAYMENT_REQUIRED", exception.getErrorCode());
        assertEquals(402, exception.getHttpStatus());
    }

    @Test
    @DisplayName("Test constructor with all parameters")
    void testConstructorWithAllParameters() {
        Throwable cause = new RuntimeException("Database error");
        BusinessException exception = new BusinessException(
            "Constraint violation", cause, "CONSTRAINT_VIOLATION", 422
        );

        assertEquals("Constraint violation", exception.getMessage());
        assertEquals("CONSTRAINT_VIOLATION", exception.getErrorCode());
        assertEquals(422, exception.getHttpStatus());
        assertEquals("BUSINESS", exception.getCategory());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("Test business exception is not critical")
    void testIsNotCritical() {
        BusinessException exception = new BusinessException("Business error");
        assertFalse(exception.isCritical());
    }

    @Test
    @DisplayName("Test business exception with 4xx status is not critical")
    void test4xxStatusIsNotCritical() {
        BusinessException exception = new BusinessException("Error", "ERROR", 422);
        assertFalse(exception.isCritical());
    }

    @Test
    @DisplayName("Test business exception can have context added")
    void testCanAddContext() {
        BusinessException exception = new BusinessException("Business error");
        exception.addContext("field", "amount");
        exception.addContext("value", -100);

        assertEquals(2, exception.getContext().size());
    }

    @Test
    @DisplayName("Test business exception can be thrown and caught")
    void testCanBeThrownAndCaught() {
        BusinessException caught = assertThrows(BusinessException.class, () -> {
            throw new BusinessException("Invalid business operation");
        });

        assertEquals("Invalid business operation", caught.getMessage());
    }

    @Test
    @DisplayName("Test business exception with different error codes")
    void testDifferentErrorCodes() {
        BusinessException ex1 = new BusinessException("Error", "INVENTORY_LOW");
        BusinessException ex2 = new BusinessException("Error", "PAYMENT_DECLINED");
        BusinessException ex3 = new BusinessException("Error", "SHIPMENT_UNAVAILABLE");

        assertEquals("INVENTORY_LOW", ex1.getErrorCode());
        assertEquals("PAYMENT_DECLINED", ex2.getErrorCode());
        assertEquals("SHIPMENT_UNAVAILABLE", ex3.getErrorCode());
    }
}
