package com.gogidix.shared.infrastructure.services.security.analytics.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SecurityEvent domain model.
 */
@DisplayName("SecurityEvent Domain Model Tests")
class SecurityEventTest {

    @Test
    @DisplayName("Should create SecurityEvent with default values")
    void shouldCreateSecurityEventWithDefaults() {
        SecurityEvent event = new SecurityEvent();

        assertNull(event.getId());
        assertNull(event.getTenantId());
        assertNull(event.getEventId());
        assertNull(event.getEventType());
        assertNull(event.getSeverity());
        assertNull(event.getSource());
        assertNull(event.getDescription());
        assertNull(event.getTimestamp());
        assertNull(event.getCreatedAt());
    }

    @Test
    @DisplayName("Should create SecurityEvent with constructor")
    void shouldCreateSecurityEventWithConstructor() {
        TenantId tenantId = TenantId.of("tenant-123");
        SecurityEvent event = new SecurityEvent(tenantId, "LOGIN_ATTEMPT", "HIGH");

        assertEquals(tenantId, event.getTenantId());
        assertEquals("LOGIN_ATTEMPT", event.getEventType());
        assertEquals("HIGH", event.getSeverity());
        assertNotNull(event.getEventId());
        assertNotNull(event.getTimestamp());
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void shouldSetAndGetAllProperties() {
        SecurityEvent event = new SecurityEvent();
        TenantId tenantId = TenantId.of("tenant-123");
        LocalDateTime now = LocalDateTime.now();

        event.setId("event-123");
        event.setTenantId(tenantId);
        event.setEventId("event-id-456");
        event.setEventType("DATA_BREACH");
        event.setSeverity("CRITICAL");
        event.setSource("external-api");
        event.setDescription("Potential data breach detected");
        event.setTimestamp(now);
        event.setCreatedAt(now);

        assertEquals("event-123", event.getId());
        assertEquals(tenantId, event.getTenantId());
        assertEquals("event-id-456", event.getEventId());
        assertEquals("DATA_BREACH", event.getEventType());
        assertEquals("CRITICAL", event.getSeverity());
        assertEquals("external-api", event.getSource());
        assertEquals("Potential data breach detected", event.getDescription());
        assertEquals(now, event.getTimestamp());
        assertEquals(now, event.getCreatedAt());
    }

    @Test
    @DisplayName("Should generate unique event IDs")
    void shouldGenerateUniqueEventIds() {
        TenantId tenantId = TenantId.of("tenant-123");

        SecurityEvent event1 = new SecurityEvent(tenantId, "LOGIN_ATTEMPT", "LOW");
        SecurityEvent event2 = new SecurityEvent(tenantId, "LOGOUT", "LOW");

        assertNotEquals(event1.getEventId(), event2.getEventId());
    }

    @Test
    @DisplayName("Should set timestamp to current time by default")
    void shouldSetTimestampToCurrentTimeByDefault() {
        TenantId tenantId = TenantId.of("tenant-123");
        LocalDateTime before = LocalDateTime.now();

        SecurityEvent event = new SecurityEvent(tenantId, "TEST", "MEDIUM");

        LocalDateTime after = LocalDateTime.now();

        assertNotNull(event.getTimestamp());
        assertTrue(event.getTimestamp().isAfter(before.minusSeconds(1)));
        assertTrue(event.getTimestamp().isBefore(after.plusSeconds(1)));
    }

    @Test
    @DisplayName("Should handle different severity levels")
    void shouldHandleDifferentSeverityLevels() {
        SecurityEvent event = new SecurityEvent();

        event.setSeverity("LOW");
        assertEquals("LOW", event.getSeverity());

        event.setSeverity("MEDIUM");
        assertEquals("MEDIUM", event.getSeverity());

        event.setSeverity("HIGH");
        assertEquals("HIGH", event.getSeverity());

        event.setSeverity("CRITICAL");
        assertEquals("CRITICAL", event.getSeverity());
    }

    @Test
    @DisplayName("Should handle different event types")
    void shouldHandleDifferentEventTypes() {
        SecurityEvent event = new SecurityEvent();

        event.setEventType("LOGIN_ATTEMPT");
        assertEquals("LOGIN_ATTEMPT", event.getEventType());

        event.setEventType("DATA_BREACH");
        assertEquals("DATA_BREACH", event.getEventType());

        event.setEventType("MALWARE_DETECTED");
        assertEquals("MALWARE_DETECTED", event.getEventType());

        event.setEventType("PHISHING_ATTEMPT");
        assertEquals("PHISHING_ATTEMPT", event.getEventType());
    }

    @Test
    @DisplayName("Should handle null source")
    void shouldHandleNullSource() {
        SecurityEvent event = new SecurityEvent();
        event.setSource(null);

        assertNull(event.getSource());
    }

    @Test
    @DisplayName("Should handle non-null source")
    void shouldHandleNonNullSource() {
        SecurityEvent event = new SecurityEvent();
        event.setSource("internal-monitoring");

        assertEquals("internal-monitoring", event.getSource());
    }

    @Test
    @DisplayName("Should handle null description")
    void shouldHandleNullDescription() {
        SecurityEvent event = new SecurityEvent();
        event.setDescription(null);

        assertNull(event.getDescription());
    }

    @Test
    @DisplayName("Should handle non-null description")
    void shouldHandleNonNullDescription() {
        SecurityEvent event = new SecurityEvent();
        event.setDescription("Security event detected");

        assertEquals("Security event detected", event.getDescription());
    }

    @Test
    @DisplayName("Should handle custom timestamp")
    void shouldHandleCustomTimestamp() {
        SecurityEvent event = new SecurityEvent();
        LocalDateTime customTime = LocalDateTime.of(2024, 1, 1, 12, 0, 0);

        event.setTimestamp(customTime);

        assertEquals(customTime, event.getTimestamp());
    }

    @Test
    @DisplayName("Should handle INFO severity")
    void shouldHandleInfoSeverity() {
        TenantId tenantId = TenantId.of("tenant-123");
        SecurityEvent event = new SecurityEvent(tenantId, "SYSTEM_LOG", "INFO");

        assertEquals("INFO", event.getSeverity());
    }

    @Test
    @DisplayName("Should handle WARNING severity")
    void shouldHandleWarningSeverity() {
        TenantId tenantId = TenantId.of("tenant-123");
        SecurityEvent event = new SecurityEvent(tenantId, "UNUSUAL_ACTIVITY", "WARNING");

        assertEquals("WARNING", event.getSeverity());
    }
}
