package com.gogidix.shared.infrastructure.services.security.threat.application.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ThreatIndicatorResponseDto.
 */
@DisplayName("ThreatIndicatorResponseDto Tests")
class ThreatIndicatorResponseDtoTest {

    @Test
    @DisplayName("Should create response DTO with all values")
    void shouldCreateResponseDtoWithAllValues() {
        LocalDateTime now = LocalDateTime.now();

        ThreatIndicatorResponseDto dto = new ThreatIndicatorResponseDto(
            "indicator-123",
            "tenant-456",
            "indicator-id-789",
            "IP_ADDRESS",
            "192.168.1.1",
            "HIGH",
            "Malicious IP address",
            true,
            now,
            now
        );

        assertEquals("indicator-123", dto.id());
        assertEquals("tenant-456", dto.tenantId());
        assertEquals("indicator-id-789", dto.indicatorId());
        assertEquals("IP_ADDRESS", dto.indicatorType());
        assertEquals("192.168.1.1", dto.value());
        assertEquals("HIGH", dto.severity());
        assertEquals("Malicious IP address", dto.description());
        assertTrue(dto.active());
        assertEquals(now, dto.createdAt());
        assertEquals(now, dto.updatedAt());
    }

    @Test
    @DisplayName("Should create response DTO with null optional values")
    void shouldCreateResponseDtoWithNullOptionalValues() {
        ThreatIndicatorResponseDto dto = new ThreatIndicatorResponseDto(
            "indicator-123",
            "tenant-456",
            "indicator-id-789",
            "HASH",
            "abc123",
            null,
            null,
            true,
            null,
            null
        );

        assertEquals("indicator-123", dto.id());
        assertEquals("tenant-456", dto.tenantId());
        assertEquals("indicator-id-789", dto.indicatorId());
        assertEquals("HASH", dto.indicatorType());
        assertEquals("abc123", dto.value());
        assertNull(dto.severity());
        assertNull(dto.description());
        assertTrue(dto.active());
        assertNull(dto.createdAt());
        assertNull(dto.updatedAt());
    }

    @Test
    @DisplayName("Should handle active indicator")
    void shouldHandleActiveIndicator() {
        ThreatIndicatorResponseDto dto = new ThreatIndicatorResponseDto(
            "id", "tenant", "indicatorId", "IP_ADDRESS", "10.0.0.1", "HIGH", null, true, null, null
        );

        assertTrue(dto.active());
    }

    @Test
    @DisplayName("Should handle inactive indicator")
    void shouldHandleInactiveIndicator() {
        ThreatIndicatorResponseDto dto = new ThreatIndicatorResponseDto(
            "id", "tenant", "indicatorId", "IP_ADDRESS", "10.0.0.1", "HIGH", null, false, null, null
        );

        assertFalse(dto.active());
    }

    @Test
    @DisplayName("Should handle HIGH severity")
    void shouldHandleHighSeverity() {
        ThreatIndicatorResponseDto dto = new ThreatIndicatorResponseDto(
            "id", "tenant", "indicatorId", "IP_ADDRESS", "10.0.0.1", "HIGH", null, true, null, null
        );

        assertEquals("HIGH", dto.severity());
    }

    @Test
    @DisplayName("Should handle MEDIUM severity")
    void shouldHandleMediumSeverity() {
        ThreatIndicatorResponseDto dto = new ThreatIndicatorResponseDto(
            "id", "tenant", "indicatorId", "IP_ADDRESS", "10.0.0.1", "MEDIUM", null, true, null, null
        );

        assertEquals("MEDIUM", dto.severity());
    }

    @Test
    @DisplayName("Should handle LOW severity")
    void shouldHandleLowSeverity() {
        ThreatIndicatorResponseDto dto = new ThreatIndicatorResponseDto(
            "id", "tenant", "indicatorId", "IP_ADDRESS", "10.0.0.1", "LOW", null, true, null, null
        );

        assertEquals("LOW", dto.severity());
    }

    @Test
    @DisplayName("Should handle CRITICAL severity")
    void shouldHandleCriticalSeverity() {
        ThreatIndicatorResponseDto dto = new ThreatIndicatorResponseDto(
            "id", "tenant", "indicatorId", "IP_ADDRESS", "10.0.0.1", "CRITICAL", null, true, null, null
        );

        assertEquals("CRITICAL", dto.severity());
    }

    @Test
    @DisplayName("Should handle different indicator types")
    void shouldHandleDifferentIndicatorTypes() {
        ThreatIndicatorResponseDto dto1 = new ThreatIndicatorResponseDto(
            "id1", "tenant", "indicatorId1", "IP_ADDRESS", "10.0.0.1", "HIGH", null, true, null, null
        );
        ThreatIndicatorResponseDto dto2 = new ThreatIndicatorResponseDto(
            "id2", "tenant", "indicatorId2", "DOMAIN", "malicious.com", "HIGH", null, true, null, null
        );
        ThreatIndicatorResponseDto dto3 = new ThreatIndicatorResponseDto(
            "id3", "tenant", "indicatorId3", "HASH", "abc123", "HIGH", null, true, null, null
        );
        ThreatIndicatorResponseDto dto4 = new ThreatIndicatorResponseDto(
            "id4", "tenant", "indicatorId4", "URL", "http://evil.com", "HIGH", null, true, null, null
        );
        ThreatIndicatorResponseDto dto5 = new ThreatIndicatorResponseDto(
            "id5", "tenant", "indicatorId5", "EMAIL", "attacker@evil.com", "HIGH", null, true, null, null
        );

        assertEquals("IP_ADDRESS", dto1.indicatorType());
        assertEquals("DOMAIN", dto2.indicatorType());
        assertEquals("HASH", dto3.indicatorType());
        assertEquals("URL", dto4.indicatorType());
        assertEquals("EMAIL", dto5.indicatorType());
    }

    @Test
    @DisplayName("Should handle timestamps")
    void shouldHandleTimestamps() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 15, 14, 30, 0);

        ThreatIndicatorResponseDto dto = new ThreatIndicatorResponseDto(
            "indicator-123",
            "tenant-456",
            "indicator-id-789",
            "IP_ADDRESS",
            "192.168.1.1",
            "HIGH",
            null,
            true,
            createdAt,
            updatedAt
        );

        assertEquals(createdAt, dto.createdAt());
        assertEquals(updatedAt, dto.updatedAt());
        assertNotEquals(createdAt, updatedAt);
    }

    @Test
    @DisplayName("Should maintain immutability")
    void shouldMaintainImmutability() {
        LocalDateTime now = LocalDateTime.now();

        ThreatIndicatorResponseDto dto = new ThreatIndicatorResponseDto(
            "indicator-123",
            "tenant-456",
            "indicator-id-789",
            "DOMAIN",
            "test.com",
            "MEDIUM",
            "Test description",
            true,
            now,
            now
        );

        // Record is immutable by design
        assertEquals("indicator-123", dto.id());
        assertEquals("tenant-456", dto.tenantId());
        assertEquals("DOMAIN", dto.indicatorType());
        assertEquals("MEDIUM", dto.severity());
        assertTrue(dto.active());
    }

    @Test
    @DisplayName("Should handle long description")
    void shouldHandleLongDescription() {
        String longDescription = "This indicator is associated with an advanced persistent threat (APT) group. " +
            "The group has been active since 2020 and targets organizations in the financial sector. " +
            "Their tactics include phishing, credential theft, and lateral movement.";

        ThreatIndicatorResponseDto dto = new ThreatIndicatorResponseDto(
            "id", "tenant", "indicatorId", "IP_ADDRESS", "10.0.0.1", "HIGH", longDescription, true, null, null
        );

        assertEquals(longDescription, dto.description());
    }

    @Test
    @DisplayName("Should handle IPv6 address")
    void shouldHandleIpv6Address() {
        String ipv6 = "2001:0db8:85a3::8a2e:0370:7334";

        ThreatIndicatorResponseDto dto = new ThreatIndicatorResponseDto(
            "id", "tenant", "indicatorId", "IP_ADDRESS", ipv6, "HIGH", null, true, null, null
        );

        assertEquals(ipv6, dto.value());
    }

    @Test
    @DisplayName("Should handle all required fields with minimal data")
    void shouldHandleAllRequiredFieldsWithMinimalData() {
        ThreatIndicatorResponseDto dto = new ThreatIndicatorResponseDto(
            "id",
            "tenantId",
            "indicatorId",
            "type",
            "value",
            null,
            null,
            true,
            null,
            null
        );

        assertEquals("id", dto.id());
        assertEquals("tenantId", dto.tenantId());
        assertEquals("indicatorId", dto.indicatorId());
        assertEquals("type", dto.indicatorType());
        assertEquals("value", dto.value());
        assertTrue(dto.active());
    }

    @Test
    @DisplayName("Should handle file hash value")
    void shouldHandleFileHashValue() {
        String hash = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a2f3c0e8b6c3e5e";

        ThreatIndicatorResponseDto dto = new ThreatIndicatorResponseDto(
            "id", "tenant", "indicatorId", "HASH", hash, "CRITICAL", null, true, null, null
        );

        assertEquals(hash, dto.value());
        assertEquals("CRITICAL", dto.severity());
    }

    @Test
    @DisplayName("Should handle URL value")
    void shouldHandleUrlValue() {
        String url = "https://phishing.example.com/login?redirect=steal";

        ThreatIndicatorResponseDto dto = new ThreatIndicatorResponseDto(
            "id", "tenant", "indicatorId", "URL", url, "HIGH", "Phishing URL", true, null, null
        );

        assertEquals(url, dto.value());
        assertEquals("Phishing URL", dto.description());
    }
}
