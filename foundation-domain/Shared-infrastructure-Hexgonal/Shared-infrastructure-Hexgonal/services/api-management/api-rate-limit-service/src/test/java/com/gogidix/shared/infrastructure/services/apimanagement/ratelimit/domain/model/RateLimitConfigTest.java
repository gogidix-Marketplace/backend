package com.gogidix.shared.infrastructure.services.apimanagement.ratelimit.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RateLimitConfig domain model (API Management).
 */
@DisplayName("API Rate Limit Config Domain Model Tests")
class RateLimitConfigTest {

    @Test
    @DisplayName("Should create config with default constructor")
    void shouldCreateConfigWithDefaultConstructor() {
        RateLimitConfig config = new RateLimitConfig();

        assertNull(config.getTenantId());
        assertNull(config.getId());
        assertNull(config.getApiKey());
        assertNull(config.getEndpoint());
        assertEquals(0, config.getRequestsPerMinute());
        assertEquals(0, config.getRequestsPerHour());
        assertEquals(0, config.getRequestsPerDay());
        assertFalse(config.isActive());
        assertNull(config.getDescription());
        assertNull(config.getCreatedAt());
        assertNull(config.getUpdatedAt());
    }

    @Test
    @DisplayName("Should create config with parameterized constructor")
    void shouldCreateConfigWithParameterizedConstructor() {
        TenantId tenantId = TenantId.of("tenant-001");

        RateLimitConfig config = new RateLimitConfig(tenantId, "api-key-123", "/api/v1/users");

        assertEquals(tenantId, config.getTenantId());
        assertEquals("api-key-123", config.getApiKey());
        assertEquals("/api/v1/users", config.getEndpoint());
        assertTrue(config.isActive());
        assertEquals(60, config.getRequestsPerMinute());
        assertEquals(1000, config.getRequestsPerHour());
        assertEquals(10000, config.getRequestsPerDay());
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void shouldSetAndGetAllProperties() {
        RateLimitConfig config = new RateLimitConfig();
        LocalDateTime now = LocalDateTime.now();
        TenantId tenantId = TenantId.of("tenant-456");

        config.setTenantId(tenantId);
        config.setId("config-123");
        config.setApiKey("new-api-key");
        config.setEndpoint("/api/v1/products");
        config.setRequestsPerMinute(120);
        config.setRequestsPerHour(2000);
        config.setRequestsPerDay(20000);
        config.setActive(true);
        config.setDescription("Product API rate limit");
        config.setCreatedAt(now);
        config.setUpdatedAt(now);

        assertEquals(tenantId, config.getTenantId());
        assertEquals("config-123", config.getId());
        assertEquals("new-api-key", config.getApiKey());
        assertEquals("/api/v1/products", config.getEndpoint());
        assertEquals(120, config.getRequestsPerMinute());
        assertEquals(2000, config.getRequestsPerHour());
        assertEquals(20000, config.getRequestsPerDay());
        assertTrue(config.isActive());
        assertEquals("Product API rate limit", config.getDescription());
        assertEquals(now, config.getCreatedAt());
        assertEquals(now, config.getUpdatedAt());
    }

    @Test
    @DisplayName("Should toggle active status")
    void shouldToggleActiveStatus() {
        RateLimitConfig config = new RateLimitConfig();

        assertFalse(config.isActive());

        config.setActive(true);
        assertTrue(config.isActive());

        config.setActive(false);
        assertFalse(config.isActive());
    }

    @Test
    @DisplayName("Should set individual rate limits")
    void shouldSetIndividualRateLimits() {
        RateLimitConfig config = new RateLimitConfig();

        config.setRequestsPerMinute(30);
        config.setRequestsPerHour(500);
        config.setRequestsPerDay(5000);

        assertEquals(30, config.getRequestsPerMinute());
        assertEquals(500, config.getRequestsPerHour());
        assertEquals(5000, config.getRequestsPerDay());
    }

    @Test
    @DisplayName("Should handle null tenant ID")
    void shouldHandleNullTenantId() {
        RateLimitConfig config = new RateLimitConfig();

        assertNull(config.getTenantId());

        config.setTenantId(null);
        assertNull(config.getTenantId());
    }

    @Test
    @DisplayName("Should set null for optional string properties")
    void shouldSetNullForOptionalStringProperties() {
        RateLimitConfig config = new RateLimitConfig();
        config.setApiKey(null);
        config.setEndpoint(null);
        config.setDescription(null);

        assertNull(config.getApiKey());
        assertNull(config.getEndpoint());
        assertNull(config.getDescription());
    }

    @Test
    @DisplayName("Should handle zero rate limits")
    void shouldHandleZeroRateLimits() {
        RateLimitConfig config = new RateLimitConfig();

        config.setRequestsPerMinute(0);
        config.setRequestsPerHour(0);
        config.setRequestsPerDay(0);

        assertEquals(0, config.getRequestsPerMinute());
        assertEquals(0, config.getRequestsPerHour());
        assertEquals(0, config.getRequestsPerDay());
    }

    @Test
    @DisplayName("Should handle large rate limit values")
    void shouldHandleLargeRateLimitValues() {
        RateLimitConfig config = new RateLimitConfig();

        config.setRequestsPerMinute(10000);
        config.setRequestsPerHour(600000);
        config.setRequestsPerDay(10000000);

        assertEquals(10000, config.getRequestsPerMinute());
        assertEquals(600000, config.getRequestsPerHour());
        assertEquals(10000000, config.getRequestsPerDay());
    }

    @Test
    @DisplayName("Should maintain tenant ID reference")
    void shouldMaintainTenantIdReference() {
        TenantId tenantId = TenantId.of("tenant-789");
        RateLimitConfig config = new RateLimitConfig(tenantId, "key", "endpoint");

        // Same reference
        assertSame(tenantId, config.getTenantId());
    }

    @Test
    @DisplayName("Should create multiple configs independently")
    void shouldCreateMultipleConfigsIndependently() {
        TenantId tenant1 = TenantId.of("tenant-001");
        TenantId tenant2 = TenantId.of("tenant-002");

        RateLimitConfig config1 = new RateLimitConfig(tenant1, "key-1", "/api/1");
        RateLimitConfig config2 = new RateLimitConfig(tenant2, "key-2", "/api/2");

        assertEquals("tenant-001", config1.getTenantId().getValue());
        assertEquals("tenant-002", config2.getTenantId().getValue());
        assertEquals("key-1", config1.getApiKey());
        assertEquals("key-2", config2.getApiKey());
        assertEquals("/api/1", config1.getEndpoint());
        assertEquals("/api/2", config2.getEndpoint());
    }

    @Test
    @DisplayName("Should handle update of timestamp fields")
    void shouldHandleUpdateOfTimestampFields() {
        RateLimitConfig config = new RateLimitConfig();
        LocalDateTime initialTime = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedTime = LocalDateTime.now();

        config.setCreatedAt(initialTime);
        config.setUpdatedAt(updatedTime);

        assertEquals(initialTime, config.getCreatedAt());
        assertEquals(updatedTime, config.getUpdatedAt());

        // Update again
        LocalDateTime newTime = LocalDateTime.now().plusHours(1);
        config.setUpdatedAt(newTime);
        assertEquals(newTime, config.getUpdatedAt());
        // Created at should remain unchanged
        assertEquals(initialTime, config.getCreatedAt());
    }
}
