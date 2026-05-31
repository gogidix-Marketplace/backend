package com.gogidix.shared.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ValidationException Tests")
class ValidationExceptionTest {

    @Test
    @DisplayName("Test constructor with message")
    void testConstructorWithMessage() {
        ValidationException exception = new ValidationException("Validation failed");
        assertEquals("Validation failed", exception.getMessage());
        assertEquals(400, exception.getHttpStatus());
    }

    @Test
    @DisplayName("Test constructor with message and validation errors")
    void testConstructorWithMessageAndErrors() {
        List<String> errors = Arrays.asList("Field 'name' is required", "Field 'email' is invalid");
        ValidationException exception = new ValidationException("Validation failed", errors);
        assertEquals("Validation failed", exception.getMessage());
        assertEquals(400, exception.getHttpStatus());
    }

    @Test
    @DisplayName("Test static factory of() with message")
    void testStaticFactoryOfMessage() {
        ValidationException exception = ValidationException.of("Invalid input");
        assertEquals("Invalid input", exception.getMessage());
    }

    @Test
    @DisplayName("Test static factory of() with message and errors")
    void testStaticFactoryOfMessageAndErrors() {
        List<String> errors = Collections.singletonList("Required field missing");
        ValidationException exception = ValidationException.of("Validation failed", errors);
        assertEquals("Validation failed", exception.getMessage());
    }

    @Test
    @DisplayName("Test validation exception is not critical (4xx)")
    void testIsNotCritical() {
        ValidationException exception = new ValidationException("Invalid data");
        assertFalse(exception.isCritical());
    }

    @Test
    @DisplayName("Test can be thrown and caught")
    void testCanBeThrownAndCaught() {
        ValidationException caught = assertThrows(ValidationException.class, () -> {
            throw new ValidationException("Bad request");
        });
        assertEquals("Bad request", caught.getMessage());
        assertEquals(400, caught.getHttpStatus());
    }

    @Test
    @DisplayName("Test with empty validation errors list")
    void testWithEmptyErrorsList() {
        ValidationException exception = new ValidationException("No errors", Collections.emptyList());
        assertEquals("No errors", exception.getMessage());
        assertEquals(400, exception.getHttpStatus());
    }

    @Test
    @DisplayName("Test with multiple validation errors")
    void testWithMultipleErrors() {
        List<String> errors = Arrays.asList("Field 'name' is required", "Field 'email' is invalid", "Field 'age' must be positive");
        ValidationException exception = new ValidationException("Multiple errors", errors);
        assertEquals("Multiple errors", exception.getMessage());
        assertEquals(400, exception.getHttpStatus());
    }
}
