package com.gogidix.shared.exceptions.domain.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BusinessRuleException Tests")
class BusinessRuleExceptionTest {

    @Test
    @DisplayName("Test constructor with message")
    void testConstructorWithMessage() {
        BusinessRuleException exception = new BusinessRuleException("Order amount exceeds credit limit");
        assertEquals("Order amount exceeds credit limit", exception.getMessage());
        assertEquals("BUSINESS_RULE_VIOLATION", exception.getErrorCode());
        assertEquals(400, exception.getHttpStatus());
        assertEquals("BUSINESS", exception.getCategory());
    }

    @Test
    @DisplayName("Test constructor with message and custom error code")
    void testConstructorWithMessageAndErrorCode() {
        BusinessRuleException exception = new BusinessRuleException("Insufficient stock", "INSUFFICIENT_STOCK");
        assertEquals("Insufficient stock", exception.getMessage());
        assertEquals("INSUFFICIENT_STOCK", exception.getErrorCode());
        assertEquals(400, exception.getHttpStatus());
    }

    @Test
    @DisplayName("Test constructor with message and cause")
    void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException("Inventory check failed");
        BusinessRuleException exception = new BusinessRuleException("Stock validation failed", cause);
        assertEquals("Stock validation failed", exception.getMessage());
        assertEquals("BUSINESS_RULE_VIOLATION", exception.getErrorCode());
        assertEquals(cause, exception.getCause());
        assertEquals(400, exception.getHttpStatus());
    }

    @Test
    @DisplayName("Test business rule exception is not critical")
    void testIsNotCritical() {
        BusinessRuleException exception = new BusinessRuleException("Rule violated");
        assertFalse(exception.isCritical());
    }

    @Test
    @DisplayName("Test can be thrown and caught")
    void testCanBeThrownAndCaught() {
        BusinessRuleException caught = assertThrows(BusinessRuleException.class, () -> {
            throw new BusinessRuleException("Cannot delete active order");
        });
        assertEquals("Cannot delete active order", caught.getMessage());
    }

    @Test
    @DisplayName("Test can add context")
    void testCanAddContext() {
        BusinessRuleException exception = new BusinessRuleException("Limit exceeded");
        exception.addContext("limit", 10000);
        exception.addContext("requested", 15000);
        assertEquals(2, exception.getContext().size());
        assertEquals(10000, exception.getContext().get("limit"));
        assertEquals(15000, exception.getContext().get("requested"));
    }

    @Test
    @DisplayName("Test is not retryable")
    void testIsNotRetryable() {
        BusinessRuleException exception = new BusinessRuleException("Rule violated");
        assertFalse(exception.isRetryable());
    }
}
