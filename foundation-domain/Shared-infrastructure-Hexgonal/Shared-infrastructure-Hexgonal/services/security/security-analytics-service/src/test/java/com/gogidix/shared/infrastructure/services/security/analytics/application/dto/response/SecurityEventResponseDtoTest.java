package com.gogidix.shared.infrastructure.services.security.analytics.application.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SecurityEventResponseDto.
 */
@DisplayName("SecurityEventResponseDto Tests")
class SecurityEventResponseDtoTest {

    @Test
    @DisplayName("Should create response DTO with all values")
    void shouldCreateResponseDtoWithAllValues() {
        LocalDateTime timestamp = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 12, 0, 0);

        SecurityEventResponseDto dto = new SecurityEventResponseDto(
            "event-123",
            "tenant-456",
            "event-id-789",
            "LOGIN_ATTEMPT",
            "HIGH",
            "web-app",
            "Failed login attempt",
            timestamp,
            createdAt
        );

        assertEquals("event-123", dto.id());
        assertEquals("tenant-456", dto.tenantId());
        assertEquals("event-id-789", dto.eventId());
        assertEquals("LOGIN_ATTEMPT", dto.eventType());
        assertEquals("HIGH", dto.severity());
        assertEquals("web-app", dto.source());
        assertEquals("Failed login attempt", dto.description());
        assertEquals(timestamp, dto.timestamp());
        assertEquals(createdAt, dto.createdAt());
    }

    @Test
    @DisplayName("Should create response DTO with null optional values")
    void shouldCreateResponseDtoWithNullOptionalValues() {
        SecurityEventResponseDto dto = new SecurityEventResponseDto(
            "event-123",
            "tenant-456",
            "event-id-789",
            "DATA_BREACH",
            "CRITICAL",
            null,
            null,
            null,
            null
        );

        assertEquals("event-123", dto.id());
        assertEquals("tenant-456", dto.tenantId());
        assertEquals("event-id-789", dto.eventId());
        assertEquals("DATA_BREACH", dto.eventType());
        assertEquals("CRITICAL", dto.severity());
        assertNull(dto.source());
        assertNull(dto.description());
        assertNull(dto.timestamp());
        assertNull(dto.createdAt());
    }

    @Test
    @DisplayName("Should handle HIGH severity")
    void shouldHandleHighSeverity() {
        SecurityEventResponseDto dto = new SecurityEventResponseDto(
            "id", "tenant", "eventId", "TEST", "HIGH", null, null, null, null
        );

        assertEquals("HIGH", dto.severity());
    }

    @Test
    @DisplayName("Should handle MEDIUM severity")
    void shouldHandleMediumSeverity() {
        SecurityEventResponseDto dto = new SecurityEventResponseDto(
            "id", "tenant", "eventId", "TEST", "MEDIUM", null, null, null, null
        );

        assertEquals("MEDIUM", dto.severity());
    }

    @Test
    @DisplayName("Should handle LOW severity")
    void shouldHandleLowSeverity() {
        SecurityEventResponseDto dto = new SecurityEventResponseDto(
            "id", "tenant", "eventId", "TEST", "LOW", null, null, null, null
        );

        assertEquals("LOW", dto.severity());
    }

    @Test
    @DisplayName("Should handle CRITICAL severity")
    void shouldHandleCriticalSeverity() {
        SecurityEventResponseDto dto = new SecurityEventResponseDto(
            "id", "tenant", "eventId", "TEST", "CRITICAL", null, null, null, null
        );

        assertEquals("CRITICAL", dto.severity());
    }

    @Test
    @DisplayName("Should handle INFO severity")
    void shouldHandleInfoSeverity() {
        SecurityEventResponseDto dto = new SecurityEventResponseDto(
            "id", "tenant", "eventId", "TEST", "INFO", null, null, null, null
        );

        assertEquals("INFO", dto.severity());
    }

    @Test
    @DisplayName("Should handle different event types")
    void shouldHandleDifferentEventTypes() {
        SecurityEventResponseDto dto1 = new SecurityEventResponseDto(
            "id1", "tenant", "eventId1", "LOGIN_ATTEMPT", "HIGH", null, null, null, null
        );
        SecurityEventResponseDto dto2 = new SecurityEventResponseDto(
            "id2", "tenant", "eventId2", "DATA_BREACH", "CRITICAL", null, null, null, null
        );
        SecurityEventResponseDto dto3 = new SecurityEventResponseDto(
            "id3", "tenant", "eventId3", "MALWARE_DETECTED", "HIGH", null, null, null, null
        );

        assertEquals("LOGIN_ATTEMPT", dto1.eventType());
        assertEquals("DATA_BREACH", dto2.eventType());
        assertEquals("MALWARE_DETECTED", dto3.eventType());
    }

    @Test
    @DisplayName("Should handle timestamps")
    void shouldHandleTimestamps() {
        LocalDateTime timestamp = LocalDateTime.of(2024, 1, 1, 10, 0, 0);
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 30, 0);

        SecurityEventResponseDto dto = new SecurityEventResponseDto(
            "event-123",
            "tenant-456",
            "event-id-789",
            "TEST_EVENT",
            "LOW",
            null,
            null,
            timestamp,
            createdAt
        );

        assertEquals(timestamp, dto.timestamp());
        assertEquals(createdAt, dto.createdAt());
    }

    @Test
    @DisplayName("Should handle source field")
    void shouldHandleSourceField() {
        SecurityEventResponseDto dto = new SecurityEventResponseDto(
            "id", "tenant", "eventId", "TEST", "LOW", "internal-monitoring", null, null, null
        );

        assertEquals("internal-monitoring", dto.source());
    }

    @Test
    @DisplayName("Should handle description field")
    void shouldHandleDescriptionField() {
        SecurityEventResponseDto dto = new SecurityEventResponseDto(
            "id", "tenant", "eventId", "TEST", "LOW", null, "Test description", null, null
        );

        assertEquals("Test description", dto.description());
    }

    @Test
    @DisplayName("Should handle long description")
    void shouldHandleLongDescription() {
        String longDescription = "This is a comprehensive description of the security event. " +
            "It includes all relevant details such as the source of the event, the timeline, " +
            "the affected systems, and the remediation steps taken.";

        SecurityEventResponseDto dto = new SecurityEventResponseDto(
            "id", "tenant", "eventId", "TEST", "LOW", null, longDescription, null, null
        );

        assertEquals(longDescription, dto.description());
    }

    @Test
    @DisplayName("Should maintain immutability")
    void shouldMaintainImmutability() {
        LocalDateTime timestamp = LocalDateTime.now();
        LocalDateTime createdAt = LocalDateTime.now();

        SecurityEventResponseDto dto = new SecurityEventResponseDto(
            "event-123",
            "tenant-456",
            "event-id-789",
            "TEST_EVENT",
            "LOW",
            "test-source",
            "test-description",
            timestamp,
            createdAt
        );

        // Record is immutable by design
        assertEquals("event-123", dto.id());
        assertEquals("tenant-456", dto.tenantId());
        assertEquals("TEST_EVENT", dto.eventType());
        assertEquals("LOW", dto.severity());
    }

    @Test
    @DisplayName("Should handle WARNING severity")
    void shouldHandleWarningSeverity() {
        SecurityEventResponseDto dto = new SecurityEventResponseDto(
            "id", "tenant", "eventId", "TEST", "WARNING", null, null, null, null
        );

        assertEquals("WARNING", dto.severity());
    }

    @Test
    @DisplayName("Should handle external source")
    void shouldHandleExternalSource() {
        SecurityEventResponseDto dto = new SecurityEventResponseDto(
            "id", "tenant", "eventId", "TEST", "HIGH", "external-threat-intelligence", null, null, null
        );

        assertEquals("external-threat-intelligence", dto.source());
    }

    @Test
    @DisplayName("Should handle null timestamp")
    void shouldHandleNullTimestamp() {
        SecurityEventResponseDto dto = new SecurityEventResponseDto(
            "id", "tenant", "eventId", "TEST", "LOW", null, null, null, LocalDateTime.now()
        );

        assertNull(dto.timestamp());
    }

    @Test
    @DisplayName("Should handle null createdAt")
    void shouldHandleNullCreatedAt() {
        SecurityEventResponseDto dto = new SecurityEventResponseDto(
            "id", "tenant", "eventId", "TEST", "LOW", null, null, LocalDateTime.now(), null
        );

        assertNull(dto.createdAt());
    }
}
