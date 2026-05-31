package com.gogidix.shared.infrastructure.services.security.threat.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ThreatIndicator domain model.
 */
@DisplayName("ThreatIndicator Domain Model Tests")
class ThreatIndicatorTest {

    @Test
    @DisplayName("Should create ThreatIndicator with default values")
    void shouldCreateThreatIndicatorWithDefaults() {
        ThreatIndicator indicator = new ThreatIndicator();

        assertNull(indicator.getId());
        assertNull(indicator.getTenantId());
        assertNull(indicator.getIndicatorId());
        assertNull(indicator.getIndicatorType());
        assertNull(indicator.getValue());
        assertNull(indicator.getSeverity());
        assertNull(indicator.getDescription());
        assertTrue(indicator.getActive());
        assertNull(indicator.getCreatedAt());
        assertNull(indicator.getUpdatedAt());
    }

    @Test
    @DisplayName("Should create ThreatIndicator with constructor")
    void shouldCreateThreatIndicatorWithConstructor() {
        TenantId tenantId = TenantId.of("tenant-123");
        ThreatIndicator indicator = new ThreatIndicator(tenantId, "IP_ADDRESS", "192.168.1.1");

        assertEquals(tenantId, indicator.getTenantId());
        assertEquals("IP_ADDRESS", indicator.getIndicatorType());
        assertEquals("192.168.1.1", indicator.getValue());
        assertNotNull(indicator.getIndicatorId());
        assertTrue(indicator.getActive());
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void shouldSetAndGetAllProperties() {
        ThreatIndicator indicator = new ThreatIndicator();
        TenantId tenantId = TenantId.of("tenant-123");
        LocalDateTime now = LocalDateTime.now();

        indicator.setId("indicator-123");
        indicator.setTenantId(tenantId);
        indicator.setIndicatorId("indicator-id-456");
        indicator.setIndicatorType("DOMAIN");
        indicator.setValue("malicious.example.com");
        indicator.setSeverity(ThreatIndicator.ThreatSeverity.HIGH);
        indicator.setDescription("Known malicious domain");
        indicator.setActive(true);
        indicator.setCreatedAt(now);
        indicator.setUpdatedAt(now);

        assertEquals("indicator-123", indicator.getId());
        assertEquals(tenantId, indicator.getTenantId());
        assertEquals("indicator-id-456", indicator.getIndicatorId());
        assertEquals("DOMAIN", indicator.getIndicatorType());
        assertEquals("malicious.example.com", indicator.getValue());
        assertEquals(ThreatIndicator.ThreatSeverity.HIGH, indicator.getSeverity());
        assertEquals("Known malicious domain", indicator.getDescription());
        assertTrue(indicator.getActive());
        assertEquals(now, indicator.getCreatedAt());
        assertEquals(now, indicator.getUpdatedAt());
    }

    @Test
    @DisplayName("Should generate unique indicator IDs")
    void shouldGenerateUniqueIndicatorIds() {
        TenantId tenantId = TenantId.of("tenant-123");

        ThreatIndicator indicator1 = new ThreatIndicator(tenantId, "IP_ADDRESS", "10.0.0.1");
        ThreatIndicator indicator2 = new ThreatIndicator(tenantId, "IP_ADDRESS", "10.0.0.2");

        assertNotEquals(indicator1.getIndicatorId(), indicator2.getIndicatorId());
    }

    @Test
    @DisplayName("Should set active to true by default")
    void shouldSetActiveToTrueByDefault() {
        TenantId tenantId = TenantId.of("tenant-123");
        ThreatIndicator indicator = new ThreatIndicator(tenantId, "HASH", "abc123");

        assertTrue(indicator.getActive());
    }

    @Test
    @DisplayName("Should handle all ThreatSeverity enum values")
    void shouldHandleAllThreatSeverityEnums() {
        assertEquals(4, ThreatIndicator.ThreatSeverity.values().length);
        assertEquals(ThreatIndicator.ThreatSeverity.LOW, ThreatIndicator.ThreatSeverity.valueOf("LOW"));
        assertEquals(ThreatIndicator.ThreatSeverity.MEDIUM, ThreatIndicator.ThreatSeverity.valueOf("MEDIUM"));
        assertEquals(ThreatIndicator.ThreatSeverity.HIGH, ThreatIndicator.ThreatSeverity.valueOf("HIGH"));
        assertEquals(ThreatIndicator.ThreatSeverity.CRITICAL, ThreatIndicator.ThreatSeverity.valueOf("CRITICAL"));
    }

    @Test
    @DisplayName("Should handle different indicator types")
    void shouldHandleDifferentIndicatorTypes() {
        ThreatIndicator indicator = new ThreatIndicator();

        indicator.setIndicatorType("IP_ADDRESS");
        assertEquals("IP_ADDRESS", indicator.getIndicatorType());

        indicator.setIndicatorType("DOMAIN");
        assertEquals("DOMAIN", indicator.getIndicatorType());

        indicator.setIndicatorType("HASH");
        assertEquals("HASH", indicator.getIndicatorType());

        indicator.setIndicatorType("URL");
        assertEquals("URL", indicator.getIndicatorType());

        indicator.setIndicatorType("EMAIL");
        assertEquals("EMAIL", indicator.getIndicatorType());
    }

    @Test
    @DisplayName("Should handle different severity levels")
    void shouldHandleDifferentSeverityLevels() {
        ThreatIndicator indicator = new ThreatIndicator();

        indicator.setSeverity(ThreatIndicator.ThreatSeverity.LOW);
        assertEquals(ThreatIndicator.ThreatSeverity.LOW, indicator.getSeverity());

        indicator.setSeverity(ThreatIndicator.ThreatSeverity.MEDIUM);
        assertEquals(ThreatIndicator.ThreatSeverity.MEDIUM, indicator.getSeverity());

        indicator.setSeverity(ThreatIndicator.ThreatSeverity.HIGH);
        assertEquals(ThreatIndicator.ThreatSeverity.HIGH, indicator.getSeverity());

        indicator.setSeverity(ThreatIndicator.ThreatSeverity.CRITICAL);
        assertEquals(ThreatIndicator.ThreatSeverity.CRITICAL, indicator.getSeverity());
    }

    @Test
    @DisplayName("Should handle active status changes")
    void shouldHandleActiveStatusChanges() {
        ThreatIndicator indicator = new ThreatIndicator();

        indicator.setActive(true);
        assertTrue(indicator.getActive());

        indicator.setActive(false);
        assertFalse(indicator.getActive());

        indicator.setActive(true);
        assertTrue(indicator.getActive());
    }

    @Test
    @DisplayName("Should handle null description")
    void shouldHandleNullDescription() {
        ThreatIndicator indicator = new ThreatIndicator();
        indicator.setDescription(null);

        assertNull(indicator.getDescription());
    }

    @Test
    @DisplayName("Should handle non-null description")
    void shouldHandleNonNullDescription() {
        ThreatIndicator indicator = new ThreatIndicator();
        indicator.setDescription("Suspicious IP address");

        assertEquals("Suspicious IP address", indicator.getDescription());
    }

    @Test
    @DisplayName("Should handle different value formats")
    void shouldHandleDifferentValueFormats() {
        ThreatIndicator indicator = new ThreatIndicator();

        indicator.setValue("192.168.1.1");
        assertEquals("192.168.1.1", indicator.getValue());

        indicator.setValue("malicious.example.com");
        assertEquals("malicious.example.com", indicator.getValue());

        indicator.setValue("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd");
        assertEquals("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd", indicator.getValue());

        indicator.setValue("http://phishing.example.com/login");
        assertEquals("http://phishing.example.com/login", indicator.getValue());
    }

    @Test
    @DisplayName("Should handle timestamps")
    void shouldHandleTimestamps() {
        ThreatIndicator indicator = new ThreatIndicator();
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 15, 14, 30, 0);

        indicator.setCreatedAt(createdAt);
        indicator.setUpdatedAt(updatedAt);

        assertEquals(createdAt, indicator.getCreatedAt());
        assertEquals(updatedAt, indicator.getUpdatedAt());
    }

    @Test
    @DisplayName("Should handle long description")
    void shouldHandleLongDescription() {
        String longDescription = "This is a known malicious IP address associated with multiple " +
            "advanced persistent threat (APT) groups. It has been used in various cyber campaigns " +
            "targeting financial institutions and government agencies worldwide.";

        ThreatIndicator indicator = new ThreatIndicator();
        indicator.setDescription(longDescription);

        assertEquals(longDescription, indicator.getDescription());
    }
}
