package com.gogidix.shared.infrastructure.services.security.analytics.application.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CreateSecurityEventRequestDto.
 */
@DisplayName("CreateSecurityEventRequestDto Tests")
class CreateSecurityEventRequestDtoTest {

    @Test
    @DisplayName("Should create DTO with all values")
    void shouldCreateDtoWithAllValues() {
        LocalDateTime timestamp = LocalDateTime.now();
        CreateSecurityEventRequestDto dto = new CreateSecurityEventRequestDto(
            "LOGIN_ATTEMPT",
            "HIGH",
            "web-app",
            "Failed login attempt",
            timestamp
        );

        assertEquals("LOGIN_ATTEMPT", dto.eventType());
        assertEquals("HIGH", dto.severity());
        assertEquals("web-app", dto.source());
        assertEquals("Failed login attempt", dto.description());
        assertEquals(timestamp, dto.timestamp());
    }

    @Test
    @DisplayName("Should create DTO with null optional values")
    void shouldCreateDtoWithNullOptionalValues() {
        CreateSecurityEventRequestDto dto = new CreateSecurityEventRequestDto(
            "DATA_BREACH",
            "CRITICAL",
            null,
            null,
            null
        );

        assertEquals("DATA_BREACH", dto.eventType());
        assertEquals("CRITICAL", dto.severity());
        assertNull(dto.source());
        assertNull(dto.description());
        assertNull(dto.timestamp());
    }

    @Test
    @DisplayName("Should handle HIGH severity")
    void shouldHandleHighSeverity() {
        CreateSecurityEventRequestDto dto = new CreateSecurityEventRequestDto(
            "INTRUSION",
            "HIGH",
            null,
            null,
            null
        );

        assertEquals("HIGH", dto.severity());
    }

    @Test
    @DisplayName("Should handle MEDIUM severity")
    void shouldHandleMediumSeverity() {
        CreateSecurityEventRequestDto dto = new CreateSecurityEventRequestDto(
            "UNUSUAL_ACTIVITY",
            "MEDIUM",
            null,
            null,
            null
        );

        assertEquals("MEDIUM", dto.severity());
    }

    @Test
    @DisplayName("Should handle LOW severity")
    void shouldHandleLowSeverity() {
        CreateSecurityEventRequestDto dto = new CreateSecurityEventRequestDto(
            "SUCCESSFUL_LOGIN",
            "LOW",
            null,
            null,
            null
        );

        assertEquals("LOW", dto.severity());
    }

    @Test
    @DisplayName("Should handle CRITICAL severity")
    void shouldHandleCriticalSeverity() {
        CreateSecurityEventRequestDto dto = new CreateSecurityEventRequestDto(
            "RANSOMWARE_DETECTED",
            "CRITICAL",
            null,
            null,
            null
        );

        assertEquals("CRITICAL", dto.severity());
    }

    @Test
    @DisplayName("Should handle INFO severity")
    void shouldHandleInfoSeverity() {
        CreateSecurityEventRequestDto dto = new CreateSecurityEventRequestDto(
            "SYSTEM_LOG",
            "INFO",
            null,
            null,
            null
        );

        assertEquals("INFO", dto.severity());
    }

    @Test
    @DisplayName("Should handle different event types")
    void shouldHandleDifferentEventTypes() {
        CreateSecurityEventRequestDto dto1 = new CreateSecurityEventRequestDto(
            "LOGIN_ATTEMPT", "HIGH", null, null, null);
        CreateSecurityEventRequestDto dto2 = new CreateSecurityEventRequestDto(
            "DATA_BREACH", "CRITICAL", null, null, null);
        CreateSecurityEventRequestDto dto3 = new CreateSecurityEventRequestDto(
            "MALWARE_DETECTED", "HIGH", null, null, null);
        CreateSecurityEventRequestDto dto4 = new CreateSecurityEventRequestDto(
            "PHISHING_ATTEMPT", "MEDIUM", null, null, null);

        assertEquals("LOGIN_ATTEMPT", dto1.eventType());
        assertEquals("DATA_BREACH", dto2.eventType());
        assertEquals("MALWARE_DETECTED", dto3.eventType());
        assertEquals("PHISHING_ATTEMPT", dto4.eventType());
    }

    @Test
    @DisplayName("Should handle custom timestamp")
    void shouldHandleCustomTimestamp() {
        LocalDateTime customTime = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        CreateSecurityEventRequestDto dto = new CreateSecurityEventRequestDto(
            "TEST_EVENT",
            "LOW",
            null,
            null,
            customTime
        );

        assertEquals(customTime, dto.timestamp());
    }

    @Test
    @DisplayName("Should handle source field")
    void shouldHandleSourceField() {
        CreateSecurityEventRequestDto dto = new CreateSecurityEventRequestDto(
            "TEST_EVENT",
            "LOW",
            "internal-monitoring",
            null,
            null
        );

        assertEquals("internal-monitoring", dto.source());
    }

    @Test
    @DisplayName("Should handle description field")
    void shouldHandleDescriptionField() {
        CreateSecurityEventRequestDto dto = new CreateSecurityEventRequestDto(
            "TEST_EVENT",
            "LOW",
            null,
            "Detailed description of the security event",
            null
        );

        assertEquals("Detailed description of the security event", dto.description());
    }

    @Test
    @DisplayName("Should handle long description")
    void shouldHandleLongDescription() {
        String longDescription = "This is a very long description that contains detailed information about " +
            "the security event. It may include multiple sentences and paragraphs to provide comprehensive " +
            "context about what happened, when it happened, and what actions were taken.";

        CreateSecurityEventRequestDto dto = new CreateSecurityEventRequestDto(
            "TEST_EVENT",
            "LOW",
            null,
            longDescription,
            null
        );

        assertEquals(longDescription, dto.description());
    }

    @Test
    @DisplayName("Should handle WARNING severity")
    void shouldHandleWarningSeverity() {
        CreateSecurityEventRequestDto dto = new CreateSecurityEventRequestDto(
            "SUSPICIOUS_ACTIVITY",
            "WARNING",
            null,
            null,
            null
        );

        assertEquals("WARNING", dto.severity());
    }

    @Test
    @DisplayName("Should maintain immutability")
    void shouldMaintainImmutability() {
        LocalDateTime timestamp = LocalDateTime.now();
        CreateSecurityEventRequestDto dto = new CreateSecurityEventRequestDto(
            "TEST_EVENT",
            "LOW",
            "test-source",
            "test-description",
            timestamp
        );

        // Record is immutable by design
        assertEquals("TEST_EVENT", dto.eventType());
        assertEquals("LOW", dto.severity());
        assertEquals("test-source", dto.source());
        assertEquals("test-description", dto.description());
        assertEquals(timestamp, dto.timestamp());
    }

    @Test
    @DisplayName("Should handle external source")
    void shouldHandleExternalSource() {
        CreateSecurityEventRequestDto dto = new CreateSecurityEventRequestDto(
            "EXTERNAL_THREAT",
            "HIGH",
            "external-threat-intelligence",
            null,
            null
        );

        assertEquals("external-threat-intelligence", dto.source());
    }
}
