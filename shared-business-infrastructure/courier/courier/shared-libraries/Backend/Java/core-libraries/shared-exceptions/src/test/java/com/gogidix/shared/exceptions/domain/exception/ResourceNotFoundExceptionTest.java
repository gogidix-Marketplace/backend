package com.gogidix.shared.exceptions.domain.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for ResourceNotFoundException
 */
@DisplayName("ResourceNotFoundException Tests")
class ResourceNotFoundExceptionTest {

    @Test
    @DisplayName("Test constructor with resource type and id")
    void testConstructorWithResourceTypeAndId() {
        ResourceNotFoundException exception = new ResourceNotFoundException("User", "12345");

        assertEquals("User not found with id: 12345", exception.getMessage());
        assertEquals("RESOURCE_NOT_FOUND", exception.getErrorCode());
        assertEquals(404, exception.getHttpStatus());
        assertEquals("User", exception.getResourceType());
        assertEquals("12345", exception.getResourceId());
    }

    @Test
    @DisplayName("Test context is populated with resource info")
    void testContextIsPopulated() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Product", "PROD-001");

        assertEquals("Product", exception.getContext().get("resourceType"));
        assertEquals("PROD-001", exception.getContext().get("resourceId"));
    }

    @Test
    @DisplayName("Test constructor with message only")
    void testConstructorWithMessageOnly() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Custom not found message");

        assertEquals("Custom not found message", exception.getMessage());
        assertEquals("RESOURCE_NOT_FOUND", exception.getErrorCode());
        assertEquals(404, exception.getHttpStatus());
        assertEquals("Unknown", exception.getResourceType());
        assertEquals("Unknown", exception.getResourceId());
    }

    @ParameterizedTest
    @ValueSource(strings = {"User", "Product", "Order", "Payment", "Shipment", "Category"})
    @DisplayName("Test with different resource types")
    void testWithDifferentResourceTypes(String resourceType) {
        ResourceNotFoundException exception = new ResourceNotFoundException(resourceType, "ID-123");

        assertEquals(resourceType, exception.getResourceType());
        assertTrue(exception.getMessage().contains(resourceType));
    }

    @Test
    @DisplayName("Test message format for different resources")
    void testMessageFormat() {
        ResourceNotFoundException userEx = new ResourceNotFoundException("User", "123");
        ResourceNotFoundException productEx = new ResourceNotFoundException("Product", "SKU-001");
        ResourceNotFoundException orderEx = new ResourceNotFoundException("Order", "ORD-999");

        assertTrue(userEx.getMessage().matches("User not found with id: 123"));
        assertTrue(productEx.getMessage().matches("Product not found with id: SKU-001"));
        assertTrue(orderEx.getMessage().matches("Order not found with id: ORD-999"));
    }

    @Test
    @DisplayName("Test resource not found inherits from BusinessException")
    void testInheritsFromBusinessException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Test", "123");

        assertTrue(exception instanceof BusinessException);
        assertTrue(exception instanceof BaseException);
    }

    @Test
    @DisplayName("Test can add additional context")
    void testCanAddAdditionalContext() {
        ResourceNotFoundException exception = new ResourceNotFoundException("User", "123");
        exception.addContext("searchCriteria", "email:test@example.com");
        exception.addContext("timestamp", "2024-01-01T10:00:00");

        assertEquals(4, exception.getContext().size()); // resourceType, resourceId, plus 2 more
        assertTrue(exception.getContext().containsKey("searchCriteria"));
    }

    @Test
    @DisplayName("Test is not critical (4xx error)")
    void testIsNotCritical() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Test", "123");
        assertFalse(exception.isCritical());
    }

    @Test
    @DisplayName("Test is not retryable")
    void testIsNotRetryable() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Test", "123");
        assertFalse(exception.isRetryable());
    }

    @Test
    @DisplayName("Test can be thrown and caught")
    void testCanBeThrownAndCaught() {
        ResourceNotFoundException caught = assertThrows(ResourceNotFoundException.class, () -> {
            throw new ResourceNotFoundException("Account", "ACC-001");
        });

        assertEquals("Account not found with id: ACC-001", caught.getMessage());
        assertEquals("Account", caught.getResourceType());
    }

    @Test
    @DisplayName("Test with empty resource id")
    void testWithEmptyResourceId() {
        ResourceNotFoundException exception = new ResourceNotFoundException("User", "");

        assertEquals("", exception.getResourceId());
        assertTrue(exception.getMessage().contains("User not found with id: "));
    }

    @Test
    @DisplayName("Test with special characters in resource id")
    void testWithSpecialCharactersInResourceId() {
        String specialId = "ID-123_456.test@example.com";
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource", specialId);

        assertEquals(specialId, exception.getResourceId());
        assertTrue(exception.getMessage().contains(specialId));
    }

    @Test
    @DisplayName("Test multiple resource not found exceptions have independent state")
    void testIndependentState() {
        ResourceNotFoundException ex1 = new ResourceNotFoundException("User", "1");
        ResourceNotFoundException ex2 = new ResourceNotFoundException("Product", "2");

        assertEquals("User", ex1.getResourceType());
        assertEquals("Product", ex2.getResourceType());

        ex1.addContext("extra", "info1");
        ex2.addContext("extra", "info2");

        assertEquals("info1", ex1.getContext().get("extra"));
        assertEquals("info2", ex2.getContext().get("extra"));
    }

    @Test
    @DisplayName("Test timestamp is generated correctly")
    void testTimestampGenerated() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Test", "123");
        assertNotNull(exception.getTimestamp());
    }

    @Test
    @DisplayName("Test category is BUSINESS")
    void testCategoryIsBusiness() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Test", "123");
        assertEquals("BUSINESS", exception.getCategory());
    }
}
