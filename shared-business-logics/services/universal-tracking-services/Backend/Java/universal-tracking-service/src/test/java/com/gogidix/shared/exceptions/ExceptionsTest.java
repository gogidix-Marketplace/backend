package com.gogidix.shared.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for custom exception classes.
 */
class ExceptionsTest {

    @Test
    void testConflictException_Creation() {
        String message = "Resource already exists";
        ConflictException exception = new ConflictException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConflictException_WithCause() {
        String message = "Database conflict";
        Throwable cause = new RuntimeException("Primary key violation");
        ConflictException exception = new ConflictException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testNotFoundException_Creation() {
        String message = "Resource not found";
        NotFoundException exception = new NotFoundException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testNotFoundException_WithCause() {
        String message = "User not found";
        Throwable cause = new RuntimeException("Database query returned no results");
        NotFoundException exception = new NotFoundException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testValidationException_Creation() {
        String message = "Invalid input data";
        ValidationException exception = new ValidationException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testValidationException_WithCause() {
        String message = "Validation failed";
        Throwable cause = new IllegalArgumentException("Invalid email format");
        ValidationException exception = new ValidationException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testExceptionsAreRuntime() {
        ConflictException conflictException = new ConflictException("test");
        NotFoundException notFoundException = new NotFoundException("test");
        ValidationException validationException = new ValidationException("test");

        assertTrue(conflictException instanceof RuntimeException);
        assertTrue(notFoundException instanceof RuntimeException);
        assertTrue(validationException instanceof RuntimeException);
    }
}
