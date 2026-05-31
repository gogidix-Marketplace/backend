package com.gogidix.shared.exceptions.domain.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for ValidationException
 */
@DisplayName("ValidationException Tests")
class ValidationExceptionTest {

    @Test
    @DisplayName("Test constructor with message only")
    void testConstructorWithMessageOnly() {
        ValidationException exception = new ValidationException("Validation failed");

        assertEquals("Validation failed", exception.getMessage());
        assertEquals("VALIDATION_ERROR", exception.getErrorCode());
        assertEquals(400, exception.getHttpStatus());
        assertNotNull(exception.getValidationErrors());
        assertTrue(exception.getValidationErrors().isEmpty());
        assertFalse(exception.hasValidationErrors());
    }

    @Test
    @DisplayName("Test constructor with message and validation errors")
    void testConstructorWithMessageAndErrors() {
        List<String> errors = Arrays.asList("Email is required", "Password too short");
        ValidationException exception = new ValidationException("Validation failed", errors);

        assertEquals("Validation failed", exception.getMessage());
        assertEquals(2, exception.getValidationErrors().size());
        assertTrue(exception.getValidationErrors().contains("Email is required"));
        assertTrue(exception.getValidationErrors().contains("Password too short"));
        assertTrue(exception.hasValidationErrors());
    }

    @Test
    @DisplayName("Test addValidationError")
    void testAddValidationError() {
        ValidationException exception = new ValidationException("Validation failed");

        assertTrue(exception.getValidationErrors().isEmpty());

        exception.addValidationError("Name is required");
        exception.addValidationError("Email format invalid");

        assertEquals(2, exception.getValidationErrors().size());
        assertTrue(exception.hasValidationErrors());
    }

    @Test
    @DisplayName("Test getValidationErrors returns a copy")
    void testGetValidationErrorsReturnsCopy() {
        List<String> errors = Arrays.asList("Error 1", "Error 2");
        ValidationException exception = new ValidationException("Validation failed", errors);

        List<String> retrievedErrors = exception.getValidationErrors();
        retrievedErrors.add("Error 3");

        // Original should not be modified
        assertEquals(2, exception.getValidationErrors().size());
        assertEquals(2, errors.size());
    }

    @Test
    @DisplayName("Test hasValidationErrors returns correct state")
    void testHasValidationErrors() {
        ValidationException emptyException = new ValidationException("No errors");
        ValidationException exceptionWithErrors = new ValidationException(
            "Errors",
            Arrays.asList("Error 1")
        );

        assertFalse(emptyException.hasValidationErrors());
        assertTrue(exceptionWithErrors.hasValidationErrors());
    }

    @Test
    @DisplayName("Test can add validation errors after creation")
    void testCanAddValidationErrorsAfterCreation() {
        ValidationException exception = new ValidationException("Initial message");

        assertFalse(exception.hasValidationErrors());

        exception.addValidationError("First error");
        assertTrue(exception.hasValidationErrors());
        assertEquals(1, exception.getValidationErrors().size());

        exception.addValidationError("Second error");
        assertEquals(2, exception.getValidationErrors().size());
    }

    @Test
    @DisplayName("Test validation exception inherits from BusinessException")
    void testInheritsFromBusinessException() {
        ValidationException exception = new ValidationException("Validation failed");

        assertTrue(exception instanceof BusinessException);
        assertTrue(exception instanceof BaseException);
    }

    @Test
    @DisplayName("Test can add context to validation exception")
    void testCanAddContext() {
        ValidationException exception = new ValidationException("Validation failed");
        exception.addContext("field", "email");
        exception.addContext("value", "invalid-email");

        assertEquals(2, exception.getContext().size());
        assertEquals("email", exception.getContext().get("field"));
    }

    @Test
    @DisplayName("Test is not critical")
    void testIsNotCritical() {
        ValidationException exception = new ValidationException("Validation failed");
        assertFalse(exception.isCritical());
    }

    @Test
    @DisplayName("Test is not retryable")
    void testIsNotRetryable() {
        ValidationException exception = new ValidationException("Validation failed");
        assertFalse(exception.isRetryable());
    }

    @Test
    @DisplayName("Test can be thrown and caught")
    void testCanBeThrownAndCaught() {
        ValidationException caught = assertThrows(ValidationException.class, () -> {
            throw new ValidationException("Invalid input");
        });

        assertEquals("Invalid input", caught.getMessage());
    }

    @Test
    @DisplayName("Test with empty validation errors list")
    void testWithEmptyValidationErrorsList() {
        ValidationException exception = new ValidationException(
            "No errors",
            List.of()
        );

        assertFalse(exception.hasValidationErrors());
        assertTrue(exception.getValidationErrors().isEmpty());
    }

    @Test
    @DisplayName("Test with multiple validation errors")
    void testWithMultipleValidationErrors() {
        List<String> errors = Arrays.asList(
            "Name required",
            "Email invalid",
            "Age must be positive",
            "Phone number format invalid"
        );

        ValidationException exception = new ValidationException("Multiple errors", errors);

        assertEquals(4, exception.getValidationErrors().size());
        assertTrue(exception.hasValidationErrors());
    }

    @Test
    @DisplayName("Test validation errors list is independent of original")
    void testValidationErrorsListIsIndependent() {
        List<String> originalErrors = new ArrayList<>(Arrays.asList("Error 1", "Error 2"));
        ValidationException exception = new ValidationException("Test", originalErrors);

        // Modify original list
        originalErrors.add("Error 3");

        // Exception's list should be unchanged
        assertEquals(2, exception.getValidationErrors().size());
    }

    @Test
    @DisplayName("Test category is BUSINESS")
    void testCategoryIsBusiness() {
        ValidationException exception = new ValidationException("Validation failed");
        assertEquals("BUSINESS", exception.getCategory());
    }

    @Test
    @DisplayName("Test common validation scenarios")
    void testCommonValidationScenarios() {
        // Email validation
        ValidationException emailEx = new ValidationException("Email validation failed");
        emailEx.addValidationError("Email is required");
        emailEx.addValidationError("Email format is invalid");

        // Password validation
        ValidationException passwordEx = new ValidationException("Password validation failed");
        passwordEx.addValidationError("Password must be at least 8 characters");
        passwordEx.addValidationError("Password must contain uppercase letter");
        passwordEx.addValidationError("Password must contain number");

        assertTrue(emailEx.hasValidationErrors());
        assertTrue(passwordEx.hasValidationErrors());
        assertEquals(2, emailEx.getValidationErrors().size());
        assertEquals(3, passwordEx.getValidationErrors().size());
    }

    @Test
    @DisplayName("Test adding duplicate validation errors")
    void testAddingDuplicateValidationErrors() {
        ValidationException exception = new ValidationException("Validation failed");

        exception.addValidationError("Duplicate error");
        exception.addValidationError("Duplicate error");

        assertEquals(2, exception.getValidationErrors().size());
    }

    @Test
    @DisplayName("Test adding null validation error")
    void testAddingNullValidationError() {
        ValidationException exception = new ValidationException("Validation failed");

        exception.addValidationError(null);

        assertEquals(1, exception.getValidationErrors().size());
        assertNull(exception.getValidationErrors().get(0));
    }

    @Test
    @DisplayName("Test validation error with context and errors")
    void testValidationErrorWithContextAndErrors() {
        List<String> errors = Arrays.asList("Error 1", "Error 2");
        ValidationException exception = new ValidationException("Validation failed", errors);

        exception.addContext("entity", "User");
        exception.addContext("operation", "CREATE");

        assertEquals(2, exception.getValidationErrors().size());
        assertEquals(2, exception.getContext().size());
    }
}
