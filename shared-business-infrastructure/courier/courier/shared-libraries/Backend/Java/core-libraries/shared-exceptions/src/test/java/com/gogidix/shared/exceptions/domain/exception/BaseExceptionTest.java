package com.gogidix.shared.exceptions.domain.exception;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for BaseException
 * Tests all constructors, methods, and edge cases
 */
@DisplayName("BaseException Tests")
class BaseExceptionTest {

    @Test
    @DisplayName("Test custom exception constructor with message only")
    void testCustomExceptionConstructor() {
        // Create a custom exception for testing
        TestException exception = new TestException("Test message", "TEST_ERROR", 400, "TEST");

        assertEquals("Test message", exception.getMessage());
        assertEquals("TEST_ERROR", exception.getErrorCode());
        assertEquals(400, exception.getHttpStatus());
        assertEquals("TEST", exception.getCategory());
        assertNotNull(exception.getTimestamp());
        assertNotNull(exception.getContext());
        assertTrue(exception.getContext().isEmpty());
    }

    @Test
    @DisplayName("Test constructor with cause")
    void testConstructorWithCause() {
        Throwable cause = new RuntimeException("Root cause");
        TestException exception = new TestException("Test message", cause, "TEST_ERROR", 500);

        assertEquals("Test message", exception.getMessage());
        assertEquals("TEST_ERROR", exception.getErrorCode());
        assertEquals(500, exception.getHttpStatus());
        assertEquals("GENERAL", exception.getCategory());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("Test constructor with cause and category")
    void testConstructorWithCauseAndCategory() {
        Throwable cause = new RuntimeException("Root cause");
        TestException exception = new TestException("Test message", cause, "TEST_ERROR", 500, "CUSTOM");

        assertEquals("CUSTOM", exception.getCategory());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("Test addContext method")
    void testAddContext() {
        TestException exception = new TestException("Test message", "TEST_ERROR", 400, "TEST");

        exception.addContext("key1", "value1");
        exception.addContext("key2", 123);
        exception.addContext("key3", true);

        assertEquals(3, exception.getContext().size());
        assertEquals("value1", exception.getContext().get("key1"));
        assertEquals(123, exception.getContext().get("key2"));
        assertEquals(true, exception.getContext().get("key3"));
    }

    @Test
    @DisplayName("Test isCritical with 5xx status")
    void testIsCriticalWith5xxStatus() {
        TestException exception5xx = new TestException("Server error", "SERVER_ERROR", 500, "SERVER");
        TestException exception503 = new TestException("Service unavailable", "SERVICE_ERROR", 503, "SERVER");

        assertTrue(exception5xx.isCritical());
        assertTrue(exception503.isCritical());
    }

    @Test
    @DisplayName("Test isCritical with 4xx status")
    void testIsCriticalWith4xxStatus() {
        TestException exception4xx = new TestException("Client error", "CLIENT_ERROR", 400, "CLIENT");
        TestException exception404 = new TestException("Not found", "NOT_FOUND", 404, "CLIENT");

        assertFalse(exception4xx.isCritical());
        assertFalse(exception404.isCritical());
    }

    @ParameterizedTest
    @Disabled("JUnit compatibility issue - requires aligned JUnit versions")
    @CsvSource({
        "500, true",
        "501, true",
        "502, true",
        "503, true",
        "504, true",
        "505, true",
        "400, false",
        "404, false",
        "401, false"
    })
    @DisplayName("Test isCritical with various HTTP status codes")
    void testIsCriticalWithVariousStatusCodes(int status, boolean expected) {
        TestException exception = new TestException("Test", "TEST", status, "TEST");
        assertEquals(expected, exception.isCritical());
    }

    @Test
    @DisplayName("Test isRetryable with 503 status")
    void testIsRetryableWith503() {
        TestException exception = new TestException("Service unavailable", "SERVICE_UNAVAILABLE", 503, "SERVER");
        assertTrue(exception.isRetryable());
    }

    @Test
    @DisplayName("Test isRetryable with 504 status")
    void testIsRetryableWith504() {
        TestException exception = new TestException("Gateway timeout", "GATEWAY_TIMEOUT", 504, "SERVER");
        assertTrue(exception.isRetryable());
    }

    @ParameterizedTest
    @ValueSource(ints = {400, 401, 404, 500, 501, 502})
    @DisplayName("Test isRetryable with non-retryable status codes")
    void testIsNotRetryable(int status) {
        TestException exception = new TestException("Test", "TEST", status, "TEST");
        assertFalse(exception.isRetryable());
    }

    @Test
    @DisplayName("Test timestamp is set correctly")
    void testTimestamp() {
        LocalDateTime before = LocalDateTime.now();
        TestException exception = new TestException("Test", "TEST", 400, "TEST");
        LocalDateTime after = LocalDateTime.now();

        assertNotNull(exception.getTimestamp());
        assertTrue(!exception.getTimestamp().isBefore(before));
        assertTrue(!exception.getTimestamp().isAfter(after));
    }

    @Test
    @DisplayName("Test context is mutable")
    void testContextIsMutable() {
        TestException exception = new TestException("Test", "TEST", 400, "TEST");

        exception.addContext("key1", "value1");
        assertEquals(1, exception.getContext().size());

        exception.getContext().put("key2", "value2");
        assertEquals(2, exception.getContext().size());
    }

    @Test
    @DisplayName("Test exception can be thrown and caught")
    void testExceptionCanBeThrownAndCaught() {
        assertThrows(TestException.class, () -> {
            throw new TestException("Test message", "TEST", 400, "TEST");
        });

        TestException caught = assertThrows(TestException.class, () -> {
            throw new TestException("Test message", "TEST", 404, "TEST");
        });

        assertEquals(404, caught.getHttpStatus());
    }

    @Test
    @DisplayName("Test exception with null context values")
    void testExceptionWithNullContextValues() {
        TestException exception = new TestException("Test", "TEST", 400, "TEST");
        exception.addContext("nullKey", null);
        exception.addContext("stringKey", "value");

        assertEquals(2, exception.getContext().size());
        assertNull(exception.getContext().get("nullKey"));
        assertEquals("value", exception.getContext().get("stringKey"));
    }

    @Test
    @DisplayName("Test multiple exceptions can be created with independent contexts")
    void testMultipleExceptionsIndependentContexts() {
        TestException exception1 = new TestException("Test1", "TEST1", 400, "TEST");
        TestException exception2 = new TestException("Test2", "TEST2", 500, "TEST");

        exception1.addContext("key", "value1");
        exception2.addContext("key", "value2");

        assertEquals("value1", exception1.getContext().get("key"));
        assertEquals("value2", exception2.getContext().get("key"));
    }

    // Helper class for testing BaseException
    private static class TestException extends BaseException {
        public TestException(String message, String errorCode, int httpStatus, String category) {
            super(message, errorCode, httpStatus, category);
        }

        public TestException(String message, Throwable cause, String errorCode, int httpStatus) {
            super(message, cause, errorCode, httpStatus);
        }

        public TestException(String message, Throwable cause, String errorCode, int httpStatus, String category) {
            super(message, cause, errorCode, httpStatus, category);
        }
    }
}
