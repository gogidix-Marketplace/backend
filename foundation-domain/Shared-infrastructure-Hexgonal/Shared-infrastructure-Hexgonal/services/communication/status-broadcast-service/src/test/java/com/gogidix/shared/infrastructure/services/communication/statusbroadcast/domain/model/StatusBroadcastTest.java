package com.gogidix.shared.infrastructure.services.communication.statusbroadcast.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StatusBroadcast domain model.
 */
@DisplayName("StatusBroadcast Domain Model Tests")
class StatusBroadcastTest {

    @Test
    @DisplayName("Should create status broadcast with constructor")
    void shouldCreateStatusBroadcastWithConstructor() {
        StatusBroadcast broadcast = new StatusBroadcast("tenant123", "SYSTEM", "System Update", "System maintenance scheduled");

        assertEquals("tenant123", broadcast.getTenantId());
        assertEquals("SYSTEM", broadcast.getBroadcastType());
        assertEquals("System Update", broadcast.getTitle());
        assertEquals("System maintenance scheduled", broadcast.getMessage());
        assertEquals("DRAFT", broadcast.getStatus());
        assertEquals("INFO", broadcast.getSeverity());
        assertEquals("ALL", broadcast.getTargetAudience());
        assertNotNull(broadcast.getCreatedAt());
        assertNotNull(broadcast.getUpdatedAt());
    }

    @Test
    @DisplayName("Should create status broadcast with no-args constructor")
    void shouldCreateStatusBroadcastWithNoArgsConstructor() {
        StatusBroadcast broadcast = new StatusBroadcast();

        assertNotNull(broadcast);
        assertNull(broadcast.getTenantId());
        assertNull(broadcast.getBroadcastType());
        assertNull(broadcast.getTitle());
        assertNull(broadcast.getMessage());
    }

    @Test
    @DisplayName("Should set and get all fields")
    void shouldSetAndGetAllFields() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, Object> metadata = Map.of("maintenanceId", "123", "duration", "2h");
        String[] targetTenantIds = {"tenant1", "tenant2", "tenant3"};

        StatusBroadcast broadcast = new StatusBroadcast();
        broadcast.setId("broadcast123");
        broadcast.setTenantId("tenant123");
        broadcast.setBroadcastType("MAINTENANCE");
        broadcast.setStatus("SCHEDULED");
        broadcast.setTitle("Scheduled Maintenance");
        broadcast.setMessage("System will be down for maintenance");
        broadcast.setSeverity("WARNING");
        broadcast.setScheduledAt(now.plusHours(2));
        broadcast.setExpiresAt(now.plusHours(4));
        broadcast.setSentAt(null);
        broadcast.setMetadata(metadata);
        broadcast.setTargetAudience("ADMINS");
        broadcast.setTargetTenantIds(targetTenantIds);
        broadcast.setCreatedAt(now);
        broadcast.setUpdatedAt(now);
        broadcast.setCreatedBy("admin");

        assertEquals("broadcast123", broadcast.getId());
        assertEquals("tenant123", broadcast.getTenantId());
        assertEquals("MAINTENANCE", broadcast.getBroadcastType());
        assertEquals("SCHEDULED", broadcast.getStatus());
        assertEquals("Scheduled Maintenance", broadcast.getTitle());
        assertEquals("System will be down for maintenance", broadcast.getMessage());
        assertEquals("WARNING", broadcast.getSeverity());
        assertEquals(now.plusHours(2), broadcast.getScheduledAt());
        assertEquals(now.plusHours(4), broadcast.getExpiresAt());
        assertEquals(metadata, broadcast.getMetadata());
        assertEquals("ADMINS", broadcast.getTargetAudience());
        assertEquals(targetTenantIds, broadcast.getTargetTenantIds());
        assertEquals("admin", broadcast.getCreatedBy());
    }

    @Test
    @DisplayName("Should handle all broadcast types")
    void shouldHandleAllBroadcastTypes() {
        String[] types = {"SYSTEM", "SERVICE", "INCIDENT", "MAINTENANCE"};

        for (String type : types) {
            StatusBroadcast broadcast = new StatusBroadcast();
            broadcast.setBroadcastType(type);

            assertEquals(type, broadcast.getBroadcastType());
        }
    }

    @Test
    @DisplayName("Should handle all statuses")
    void shouldHandleAllStatuses() {
        String[] statuses = {"DRAFT", "SCHEDULED", "SENT", "EXPIRED"};

        for (String status : statuses) {
            StatusBroadcast broadcast = new StatusBroadcast();
            broadcast.setStatus(status);

            assertEquals(status, broadcast.getStatus());
        }
    }

    @Test
    @DisplayName("Should handle all severities")
    void shouldHandleAllSeverities() {
        String[] severities = {"INFO", "WARNING", "ERROR", "CRITICAL"};

        for (String severity : severities) {
            StatusBroadcast broadcast = new StatusBroadcast();
            broadcast.setSeverity(severity);

            assertEquals(severity, broadcast.getSeverity());
        }
    }

    @Test
    @DisplayName("Should handle all target audiences")
    void shouldHandleAllTargetAudiences() {
        String[] audiences = {"ALL", "ADMINS", "USERS", "TENANTS"};

        for (String audience : audiences) {
            StatusBroadcast broadcast = new StatusBroadcast();
            broadcast.setTargetAudience(audience);

            assertEquals(audience, broadcast.getTargetAudience());
        }
    }

    @Test
    @DisplayName("Should mark broadcast as sent")
    void shouldMarkBroadcastAsSent() {
        StatusBroadcast broadcast = new StatusBroadcast();
        broadcast.setStatus("SCHEDULED");

        broadcast.send();

        assertEquals("SENT", broadcast.getStatus());
        assertNotNull(broadcast.getSentAt());
        assertNotNull(broadcast.getUpdatedAt());
    }

    @Test
    @DisplayName("Should schedule broadcast")
    void shouldScheduleBroadcast() {
        StatusBroadcast broadcast = new StatusBroadcast();
        LocalDateTime scheduledTime = LocalDateTime.now().plusHours(2);

        broadcast.schedule(scheduledTime);

        assertEquals("SCHEDULED", broadcast.getStatus());
        assertEquals(scheduledTime, broadcast.getScheduledAt());
        assertNotNull(broadcast.getUpdatedAt());
    }

    @Test
    @DisplayName("Should expire broadcast")
    void shouldExpireBroadcast() {
        StatusBroadcast broadcast = new StatusBroadcast();
        broadcast.setStatus("SENT");

        broadcast.expire();

        assertEquals("EXPIRED", broadcast.getStatus());
        assertNotNull(broadcast.getUpdatedAt());
    }

    @Test
    @DisplayName("Should check if broadcast is sent")
    void shouldCheckIfBroadcastIsSent() {
        StatusBroadcast broadcast = new StatusBroadcast();
        broadcast.setStatus("SENT");

        assertTrue(broadcast.isSent());

        broadcast.setStatus("DRAFT");
        assertFalse(broadcast.isSent());
    }

    @Test
    @DisplayName("Should check if broadcast is scheduled")
    void shouldCheckIfBroadcastIsScheduled() {
        StatusBroadcast broadcast = new StatusBroadcast();
        broadcast.setStatus("SCHEDULED");
        broadcast.setScheduledAt(LocalDateTime.now().plusHours(1));

        assertTrue(broadcast.isScheduled());

        broadcast.setStatus("DRAFT");
        assertFalse(broadcast.isScheduled());
    }

    @Test
    @DisplayName("Should check if broadcast is expired")
    void shouldCheckIfBroadcastIsExpired() {
        StatusBroadcast broadcast = new StatusBroadcast();
        broadcast.setExpiresAt(LocalDateTime.now().minusMinutes(1));

        assertTrue(broadcast.isExpired());

        broadcast.setExpiresAt(LocalDateTime.now().plusHours(1));
        assertFalse(broadcast.isExpired());

        broadcast.setExpiresAt(null);
        assertFalse(broadcast.isExpired());
    }

    @Test
    @DisplayName("Should handle metadata")
    void shouldHandleMetadata() {
        StatusBroadcast broadcast = new StatusBroadcast();
        Map<String, Object> metadata = Map.of(
                "incidentId", "INC-123",
                "affectedServices", java.util.List.of("api", "database"),
                "estimatedDowntime", "30min"
        );

        broadcast.setMetadata(metadata);

        assertEquals(metadata, broadcast.getMetadata());
        assertEquals("INC-123", broadcast.getMetadata().get("incidentId"));
    }

    @Test
    @DisplayName("Should handle target tenant IDs")
    void shouldHandleTargetTenantIds() {
        StatusBroadcast broadcast = new StatusBroadcast();
        String[] tenantIds = {"tenant1", "tenant2", "tenant3"};

        broadcast.setTargetTenantIds(tenantIds);

        assertEquals(3, broadcast.getTargetTenantIds().length);
        assertEquals("tenant1", broadcast.getTargetTenantIds()[0]);
        assertEquals("tenant2", broadcast.getTargetTenantIds()[1]);
        assertEquals("tenant3", broadcast.getTargetTenantIds()[2]);
    }

    @Test
    @DisplayName("Should handle timestamps")
    void shouldHandleTimestamps() {
        LocalDateTime now = LocalDateTime.now();

        StatusBroadcast broadcast = new StatusBroadcast();
        broadcast.setScheduledAt(now.plusHours(1));
        broadcast.setExpiresAt(now.plusHours(3));
        broadcast.setSentAt(now);

        assertEquals(now.plusHours(1), broadcast.getScheduledAt());
        assertEquals(now.plusHours(3), broadcast.getExpiresAt());
        assertEquals(now, broadcast.getSentAt());
    }

    @Test
    @DisplayName("Should handle created by field")
    void shouldHandleCreatedByField() {
        StatusBroadcast broadcast = new StatusBroadcast();
        broadcast.setCreatedBy("admin-user");

        assertEquals("admin-user", broadcast.getCreatedBy());
    }
}
