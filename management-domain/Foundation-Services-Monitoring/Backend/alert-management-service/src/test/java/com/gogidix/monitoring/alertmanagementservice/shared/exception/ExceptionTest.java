package com.gogidix.monitoring.alertmanagementservice.shared.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Exception Classes Tests")
class ExceptionTest {

    @Test
    @DisplayName("NotFoundException should create with message")
    void notFoundExceptionShouldCreateWithMessage() {
        // When
        NotFoundException exception = new NotFoundException("Resource not found");

        // Then
        assertEquals("Resource not found", exception.getMessage());
        assertEquals("NOT_FOUND", exception.getErrorCode());
    }

    @Test
    @DisplayName("NotFoundException should create with message and cause")
    void notFoundExceptionShouldCreateWithMessageAndCause() {
        // Given
        Throwable cause = new RuntimeException("Inner cause");

        // When
        NotFoundException exception = new NotFoundException("Resource not found", cause);

        // Then
        assertEquals("Resource not found", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals("NOT_FOUND", exception.getErrorCode());
    }

    @Test
    @DisplayName("ValidationException should create with message")
    void validationExceptionShouldCreateWithMessage() {
        // When
        ValidationException exception = new ValidationException("Validation failed");

        // Then
        assertEquals("Validation failed", exception.getMessage());
        assertEquals("VALIDATION", exception.getErrorCode());
    }

    @Test
    @DisplayName("ValidationException should create with message and cause")
    void validationExceptionShouldCreateWithMessageAndCause() {
        // Given
        Throwable cause = new RuntimeException("Inner cause");

        // When
        ValidationException exception = new ValidationException("Validation failed", cause);

        // Then
        assertEquals("Validation failed", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals("VALIDATION", exception.getErrorCode());
    }

    @Test
    @DisplayName("Exceptions should be RuntimeException")
    void exceptionsShouldBeRuntimeException() {
        NotFoundException notFound = new NotFoundException("Not found");
        ValidationException validation = new ValidationException("Invalid");

        assertInstanceOf(RuntimeException.class, notFound);
        assertInstanceOf(RuntimeException.class, validation);
    }
}
