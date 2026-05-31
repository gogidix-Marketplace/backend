package com.gogidix.shared.infrastructure.services.observability.ratelimit.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RateLimitConfig domain model.
 */
@DisplayName("RateLimitConfig Domain Model Tests")
class RateLimitConfigTest {

    @Test
    @DisplayName("Should create rate limit config with builder")
    void shouldCreateRateLimitConfigWithBuilder() {
        RateLimitConfig config = RateLimitConfig.builder()
                .id("config-123")
                .tenantId(TenantId.of("tenant-001"))
                .apiKey("api-key-123")
                .route("/api/v1/users")
                .type(RateLimitConfig.RateLimitType.API_KEY)
                .capacity(100)
                .refillTokens(10)
                .refillPeriod(60)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assertEquals("config-123", config.getId());
        assertEquals("tenant-001", config.getTenantId().getValue());
        assertEquals("api-key-123", config.getApiKey());
        assertEquals("/api/v1/users", config.getRoute());
        assertEquals(RateLimitConfig.RateLimitType.API_KEY, config.getType());
        assertEquals(100, config.getCapacity());
        assertEquals(10, config.getRefillTokens());
        assertEquals(60, config.getRefillPeriod());
        assertTrue(config.isEnabled());
        assertNotNull(config.getCreatedAt());
        assertNotNull(config.getUpdatedAt());
    }

    @Test
    @DisplayName("Should create rate limit config with default values")
    void shouldCreateRateLimitConfigWithDefaults() {
        RateLimitConfig config = new RateLimitConfig();

        assertNull(config.getId());
        assertNull(config.getTenantId());
        assertNull(config.getApiKey());
        assertNull(config.getRoute());
        assertNull(config.getType());
        assertEquals(0, config.getCapacity());
        assertEquals(0, config.getRefillTokens());
        assertEquals(0, config.getRefillPeriod());
        assertFalse(config.isEnabled()); // default for boolean is false
        assertNull(config.getCreatedAt());
        assertNull(config.getUpdatedAt());
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void shouldSetAndGetAllProperties() {
        RateLimitConfig config = new RateLimitConfig();

        config.setId("id-456");
        config.setTenantId(TenantId.of("tenant-789"));
        config.setApiKey("new-api-key");
        config.setRoute("/api/v1/products");
        config.setType(RateLimitConfig.RateLimitType.USER);
        config.setCapacity(200);
        config.setRefillTokens(20);
        config.setRefillPeriod(120);
        config.setEnabled(true);
        config.setCreatedAt(LocalDateTime.now());
        config.setUpdatedAt(LocalDateTime.now());

        assertEquals("id-456", config.getId());
        assertEquals("tenant-789", config.getTenantId().getValue());
        assertEquals("new-api-key", config.getApiKey());
        assertEquals("/api/v1/products", config.getRoute());
        assertEquals(RateLimitConfig.RateLimitType.USER, config.getType());
        assertEquals(200, config.getCapacity());
        assertEquals(20, config.getRefillTokens());
        assertEquals(120, config.getRefillPeriod());
        assertTrue(config.isEnabled());
        assertNotNull(config.getCreatedAt());
        assertNotNull(config.getUpdatedAt());
    }

    @Test
    @DisplayName("Should calculate refill rate per second correctly")
    void shouldCalculateRefillRatePerSecondCorrectly() {
        RateLimitConfig config = RateLimitConfig.builder()
                .refillTokens(60)
                .refillPeriod(60)
                .build();

        assertEquals(1.0, config.getRefillRatePerSecond(), 0.001);
    }

    @Test
    @DisplayName("Should calculate refill rate for fractional values")
    void shouldCalculateRefillRateForFractionalValues() {
        RateLimitConfig config = RateLimitConfig.builder()
                .refillTokens(10)
                .refillPeriod(30)
                .build();

        assertEquals(0.333, config.getRefillRatePerSecond(), 0.001);
    }

    @Test
    @DisplayName("Should handle all RateLimitType enum values")
    void shouldHandleAllRateLimitTypeEnums() {
        assertEquals(5, RateLimitConfig.RateLimitType.values().length);
        assertEquals(RateLimitConfig.RateLimitType.API_KEY, RateLimitConfig.RateLimitType.valueOf("API_KEY"));
        assertEquals(RateLimitConfig.RateLimitType.USER, RateLimitConfig.RateLimitType.valueOf("USER"));
        assertEquals(RateLimitConfig.RateLimitType.TENANT, RateLimitConfig.RateLimitType.valueOf("TENANT"));
        assertEquals(RateLimitConfig.RateLimitType.IP_ADDRESS, RateLimitConfig.RateLimitType.valueOf("IP_ADDRESS"));
        assertEquals(RateLimitConfig.RateLimitType.GLOBAL, RateLimitConfig.RateLimitType.valueOf("GLOBAL"));
    }

    @Test
    @DisplayName("Should create rate limit config with all args constructor")
    void shouldCreateRateLimitConfigWithAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();

        RateLimitConfig config = new RateLimitConfig(
                "config-123",
                TenantId.of("tenant-001"),
                "api-key-123",
                "/api/v1",
                RateLimitConfig.RateLimitType.TENANT,
                100,
                10,
                60,
                true,
                now,
                now
        );

        assertEquals("config-123", config.getId());
        assertEquals(RateLimitConfig.RateLimitType.TENANT, config.getType());
        assertEquals(100, config.getCapacity());
        assertEquals(10, config.getRefillTokens());
        assertEquals(60, config.getRefillPeriod());
    }

    @Test
    @DisplayName("Should create config for each rate limit type")
    void shouldCreateConfigForEachRateLimitType() {
        RateLimitConfig apiKeyConfig = RateLimitConfig.builder()
                .type(RateLimitConfig.RateLimitType.API_KEY)
                .apiKey("key-123")
                .build();

        RateLimitConfig userConfig = RateLimitConfig.builder()
                .type(RateLimitConfig.RateLimitType.USER)
                .build();

        RateLimitConfig tenantConfig = RateLimitConfig.builder()
                .type(RateLimitConfig.RateLimitType.TENANT)
                .build();

        RateLimitConfig ipConfig = RateLimitConfig.builder()
                .type(RateLimitConfig.RateLimitType.IP_ADDRESS)
                .build();

        RateLimitConfig globalConfig = RateLimitConfig.builder()
                .type(RateLimitConfig.RateLimitType.GLOBAL)
                .build();

        assertEquals(RateLimitConfig.RateLimitType.API_KEY, apiKeyConfig.getType());
        assertEquals(RateLimitConfig.RateLimitType.USER, userConfig.getType());
        assertEquals(RateLimitConfig.RateLimitType.TENANT, tenantConfig.getType());
        assertEquals(RateLimitConfig.RateLimitType.IP_ADDRESS, ipConfig.getType());
        assertEquals(RateLimitConfig.RateLimitType.GLOBAL, globalConfig.getType());
    }

    @Test
    @DisplayName("Should handle zero refill period gracefully")
    void shouldHandleZeroRefillPeriodGracefully() {
        RateLimitConfig config = RateLimitConfig.builder()
                .refillTokens(10)
                .refillPeriod(0)
                .build();

        // Division by zero would result in Infinity
        assertTrue(Double.isInfinite(config.getRefillRatePerSecond()));
    }

    @Test
    @DisplayName("Should handle disabled rate limit config")
    void shouldHandleDisabledRateLimitConfig() {
        RateLimitConfig config = RateLimitConfig.builder()
                .enabled(false)
                .capacity(100)
                .refillTokens(10)
                .refillPeriod(60)
                .build();

        assertFalse(config.isEnabled());
        assertEquals(100, config.getCapacity());
        assertEquals(0.167, config.getRefillRatePerSecond(), 0.001);
    }

    @Test
    @DisplayName("Should create config with all null optional fields")
    void shouldCreateConfigWithAllNullOptionalFields() {
        RateLimitConfig config = RateLimitConfig.builder()
                .id("config-123")
                .tenantId(TenantId.of("tenant-001"))
                .build();

        assertEquals("config-123", config.getId());
        assertEquals("tenant-001", config.getTenantId().getValue());
        assertNull(config.getApiKey());
        assertNull(config.getRoute());
        assertNull(config.getType());
    }
}
