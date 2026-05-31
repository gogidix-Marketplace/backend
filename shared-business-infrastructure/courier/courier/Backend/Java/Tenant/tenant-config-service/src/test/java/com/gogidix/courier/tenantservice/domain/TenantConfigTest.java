package com.gogidix.courier.tenantservice.domain;

import com.gogidix.courier.tenantservice.domain.entity.TenantConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TenantConfig value object.
 */
@DisplayName("TenantConfig Value Object Tests")
class TenantConfigTest {

    @Test
    @DisplayName("Should create default config")
    void shouldCreateDefaultConfig() {
        // When
        TenantConfig config = new TenantConfig();

        // Then
        assertEquals(100, config.getMaxDrivers());
        assertEquals(1000, config.getMaxDailyOrders());
        assertEquals(1000, config.getMaxDailyDeliveries());
        assertEquals(50.0, config.getServiceRadiusKm());
        assertEquals("USD", config.getDefaultCurrency());
        assertEquals("UTC", config.getTimezone());
        assertTrue(config.getDeliveryFeeEnabled());
        assertEquals(5.0, config.getDeliveryFeeAmount());
        assertEquals(0.0, config.getTaxRate());
        assertFalse(config.getAutoAcceptOrders());
        assertTrue(config.getRequireDriverVerification());
    }

    @Test
    @DisplayName("Should create config using builder")
    void shouldCreateConfigUsingBuilder() {
        // Given
        Map<String, Object> customSettings = new HashMap<>();
        customSettings.put("key1", "value1");

        // When
        TenantConfig config = TenantConfig.Builder.builder()
                .maxDrivers(200)
                .maxDailyOrders(2000)
                .defaultCurrency("EUR")
                .customSettings(customSettings)
                .build();

        // Then
        assertEquals(200, config.getMaxDrivers());
        assertEquals(2000, config.getMaxDailyOrders());
        assertEquals("EUR", config.getDefaultCurrency());
        assertEquals("value1", config.getCustomSettings().get("key1"));
    }

    @Test
    @DisplayName("Should set custom setting")
    void shouldSetCustomSetting() {
        // Given
        TenantConfig config = new TenantConfig();

        // When
        config.setSetting("customKey", "customValue");

        // Then
        assertEquals("customValue", config.getSetting("customKey"));
    }

    @Test
    @DisplayName("Should get null for non-existent setting")
    void shouldGetNullForNonExistentSetting() {
        // Given
        TenantConfig config = new TenantConfig();

        // When
        Object value = config.getSetting("nonExistent");

        // Then
        assertNull(value);
    }

    @Test
    @DisplayName("Should remove custom setting")
    void shouldRemoveCustomSetting() {
        // Given
        TenantConfig config = new TenantConfig();
        config.setSetting("key", "value");

        // When
        Object removed = config.removeSetting("key");

        // Then
        assertEquals("value", removed);
        assertNull(config.getSetting("key"));
    }

    @Test
    @DisplayName("Should create copy via constructor")
    void shouldCreateCopy() {
        // Given
        TenantConfig original = new TenantConfig();
        original.setMaxDrivers(500);
        original.setSetting("key", "value");

        // When
        TenantConfig copy = new TenantConfig(original);

        // Then
        assertEquals(original.getMaxDrivers(), copy.getMaxDrivers());
        assertEquals(original.getSetting("key"), copy.getSetting("key"));
        assertNotSame(original.getCustomSettings(), copy.getCustomSettings());
    }
}
