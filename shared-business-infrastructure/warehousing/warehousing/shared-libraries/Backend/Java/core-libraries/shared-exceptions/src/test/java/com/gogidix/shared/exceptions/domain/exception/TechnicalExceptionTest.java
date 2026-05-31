package com.gogidix.shared.exceptions.domain.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for TechnicalException
 */
@DisplayName("TechnicalException Tests")
class TechnicalExceptionTest {

    @Test
    @DisplayName("Test constructor with message only")
    void testConstructorWithMessage() {
        TechnicalException exception = new TechnicalException("System error");

        assertEquals("System error", exception.getMessage());
        assertEquals("TECHNICAL_ERROR", exception.getErrorCode());
        assertEquals(500, exception.getHttpStatus());
        assertEquals("TECHNICAL", exception.getCategory());
    }

    @Test
    @DisplayName("Test constructor with message and error code")
    void testConstructorWithMessageAndErrorCode() {
        TechnicalException exception = new TechnicalException(
            "Database connection failed",
            "DATABASE_ERROR"
        );

        assertEquals("Database connection failed", exception.getMessage());
        assertEquals("DATABASE_ERROR", exception.getErrorCode());
        assertEquals(500, exception.getHttpStatus());
    }

    @Test
    @DisplayName("Test constructor with message and cause")
    void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException("Null pointer exception");
        TechnicalException exception = new TechnicalException(
            "Processing failed",
            cause
        );

        assertEquals("Processing failed", exception.getMessage());
        assertEquals("TECHNICAL_ERROR", exception.getErrorCode());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("Test is critical for 500 status")
    void testIsCritical() {
        TechnicalException exception = new TechnicalException("Server error");
        assertTrue(exception.isCritical());
    }

    @Test
    @DisplayName("Test is not retryable by default")
    void testIsNotRetryable() {
        TechnicalException exception = new TechnicalException("Server error");
        assertFalse(exception.isRetryable());
    }

    @ParameterizedTest
    @ValueSource(strings = {"DATABASE_ERROR", "NETWORK_ERROR", "FILE_IO_ERROR", "SERIALIZATION_ERROR"})
    @DisplayName("Test with different technical error codes")
    void testWithDifferentErrorCodes(String errorCode) {
        TechnicalException exception = new TechnicalException("Error", errorCode);
        assertEquals(errorCode, exception.getErrorCode());
    }

    @Test
    @DisplayName("Test can add context")
    void testCanAddContext() {
        TechnicalException exception = new TechnicalException("System error");
        exception.addContext("component", "PaymentService");
        exception.addContext("operation", "processPayment");

        assertEquals(2, exception.getContext().size());
    }

    @Test
    @DisplayName("Test can be thrown and caught")
    void testCanBeThrownAndCaught() {
        TechnicalException caught = assertThrows(TechnicalException.class, () -> {
            throw new TechnicalException("System failure");
        });

        assertEquals("System failure", caught.getMessage());
        assertEquals(500, caught.getHttpStatus());
    }

    @Test
    @DisplayName("Test inherits from BaseException")
    void testInheritsFromBaseException() {
        TechnicalException exception = new TechnicalException("Error");
        assertTrue(exception instanceof BaseException);
    }
}
