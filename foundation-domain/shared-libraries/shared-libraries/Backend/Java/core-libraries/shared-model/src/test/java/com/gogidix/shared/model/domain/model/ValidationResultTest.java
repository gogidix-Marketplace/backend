package com.gogidix.shared.model.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ValidationResult Tests")
class ValidationResultTest {

    @Test
    void testSuccessFactory() {
        ValidationResult result = ValidationResult.success();
        assertTrue(result.isValid());
        assertFalse(result.isInvalid());
        assertFalse(result.hasErrors());
        assertEquals(0, result.getErrorCount());
    }

    @Test
    void testValidFactory() {
        ValidationResult result = ValidationResult.valid();
        assertTrue(result.isValid());
    }

    @Test
    void testFailureSingleError() {
        ValidationResult result = ValidationResult.failure("Something went wrong");
        assertFalse(result.isValid());
        assertTrue(result.isInvalid());
        assertTrue(result.hasErrors());
        assertEquals(1, result.getErrorCount());
        assertEquals("Something went wrong", result.getErrors().get(0));
    }

    @Test
    void testFailureMultipleErrors() {
        ValidationResult result = ValidationResult.failure(List.of("Error 1", "Error 2"));
        assertFalse(result.isValid());
        assertEquals(2, result.getErrorCount());
    }

    @Test
    void testInvalidFactory() {
        ValidationResult result = ValidationResult.invalid(List.of("Invalid field"));
        assertFalse(result.isValid());
        assertEquals(1, result.getErrorCount());
    }

    @Test
    void testWithWarnings() {
        ValidationResult result = ValidationResult.withWarnings(List.of("Warning 1"));
        assertTrue(result.isValid());
    }

    @Test
    void testAddError() {
        ValidationResult result = ValidationResult.success();
        result.addError("New error");
        assertFalse(result.isValid());
        assertEquals(1, result.getErrorCount());
    }

    @Test
    void testAddErrorNullIgnored() {
        ValidationResult result = ValidationResult.success();
        result.addError(null);
        assertTrue(result.isValid());
        assertEquals(0, result.getErrorCount());
    }

    @Test
    void testAddErrorBlankIgnored() {
        ValidationResult result = ValidationResult.success();
        result.addError("   ");
        assertTrue(result.isValid());
        assertEquals(0, result.getErrorCount());
    }

    @Test
    void testAddErrorsList() {
        ValidationResult result = ValidationResult.success();
        result.addErrors(List.of("E1", "E2", "E3"));
        assertFalse(result.isValid());
        assertEquals(3, result.getErrorCount());
    }

    @Test
    void testAddErrorsNullList() {
        ValidationResult result = ValidationResult.success();
        result.addErrors(null);
        assertTrue(result.isValid());
    }

    @Test
    void testGetErrorsAsString() {
        ValidationResult result = ValidationResult.failure(List.of("A", "B"));
        assertEquals("A, B", result.getErrorsAsString());
    }

    @Test
    void testConstructorWithValidAndErrors() {
        ValidationResult result = new ValidationResult(true, List.of("Warning"));
        assertTrue(result.isValid());
    }

    @Test
    void testConstructorWithNullErrors() {
        ValidationResult result = new ValidationResult(false, null);
        assertFalse(result.isValid());
        assertEquals(0, result.getErrorCount());
    }

    @Test
    void testToString() {
        ValidationResult result = ValidationResult.failure("Error");
        String str = result.toString();
        assertTrue(str.contains("false"));
        assertTrue(str.contains("Error"));
    }

    @Test
    void testGetErrorsReturnsCopy() {
        ValidationResult result = ValidationResult.failure("E1");
        result.getErrors().add("E2");
        assertEquals(1, result.getErrorCount());
    }
}
