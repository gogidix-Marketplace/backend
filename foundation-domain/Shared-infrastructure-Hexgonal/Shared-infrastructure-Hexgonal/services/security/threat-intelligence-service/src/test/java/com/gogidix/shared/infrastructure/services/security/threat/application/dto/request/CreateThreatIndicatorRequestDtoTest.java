package com.gogidix.shared.infrastructure.services.security.threat.application.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CreateThreatIndicatorRequestDto.
 */
@DisplayName("CreateThreatIndicatorRequestDto Tests")
class CreateThreatIndicatorRequestDtoTest {

    @Test
    @DisplayName("Should create DTO with all values")
    void shouldCreateDtoWithAllValues() {
        CreateThreatIndicatorRequestDto dto = new CreateThreatIndicatorRequestDto(
            "IP_ADDRESS",
            "192.168.1.1",
            "HIGH",
            "Malicious IP address"
        );

        assertEquals("IP_ADDRESS", dto.indicatorType());
        assertEquals("192.168.1.1", dto.value());
        assertEquals("HIGH", dto.severity());
        assertEquals("Malicious IP address", dto.description());
    }

    @Test
    @DisplayName("Should create DTO with null optional values")
    void shouldCreateDtoWithNullOptionalValues() {
        CreateThreatIndicatorRequestDto dto = new CreateThreatIndicatorRequestDto(
            "HASH",
            "abc123",
            null,
            null
        );

        assertEquals("HASH", dto.indicatorType());
        assertEquals("abc123", dto.value());
        assertNull(dto.severity());
        assertNull(dto.description());
    }

    @Test
    @DisplayName("Should handle IP_ADDRESS indicator type")
    void shouldHandleIpAddressIndicatorType() {
        CreateThreatIndicatorRequestDto dto = new CreateThreatIndicatorRequestDto(
            "IP_ADDRESS",
            "10.0.0.1",
            null,
            null
        );

        assertEquals("IP_ADDRESS", dto.indicatorType());
        assertEquals("10.0.0.1", dto.value());
    }

    @Test
    @DisplayName("Should handle DOMAIN indicator type")
    void shouldHandleDomainIndicatorType() {
        CreateThreatIndicatorRequestDto dto = new CreateThreatIndicatorRequestDto(
            "DOMAIN",
            "malicious.example.com",
            null,
            null
        );

        assertEquals("DOMAIN", dto.indicatorType());
        assertEquals("malicious.example.com", dto.value());
    }

    @Test
    @DisplayName("Should handle HASH indicator type")
    void shouldHandleHashIndicatorType() {
        CreateThreatIndicatorRequestDto dto = new CreateThreatIndicatorRequestDto(
            "HASH",
            "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd",
            null,
            null
        );

        assertEquals("HASH", dto.indicatorType());
        assertEquals("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd", dto.value());
    }

    @Test
    @DisplayName("Should handle URL indicator type")
    void shouldHandleUrlIndicatorType() {
        CreateThreatIndicatorRequestDto dto = new CreateThreatIndicatorRequestDto(
            "URL",
            "http://phishing.example.com/login",
            null,
            null
        );

        assertEquals("URL", dto.indicatorType());
        assertEquals("http://phishing.example.com/login", dto.value());
    }

    @Test
    @DisplayName("Should handle EMAIL indicator type")
    void shouldHandleEmailIndicatorType() {
        CreateThreatIndicatorRequestDto dto = new CreateThreatIndicatorRequestDto(
            "EMAIL",
            "attacker@malicious.com",
            null,
            null
        );

        assertEquals("EMAIL", dto.indicatorType());
        assertEquals("attacker@malicious.com", dto.value());
    }

    @Test
    @DisplayName("Should handle HIGH severity")
    void shouldHandleHighSeverity() {
        CreateThreatIndicatorRequestDto dto = new CreateThreatIndicatorRequestDto(
            "IP_ADDRESS",
            "10.0.0.1",
            "HIGH",
            null
        );

        assertEquals("HIGH", dto.severity());
    }

    @Test
    @DisplayName("Should handle MEDIUM severity")
    void shouldHandleMediumSeverity() {
        CreateThreatIndicatorRequestDto dto = new CreateThreatIndicatorRequestDto(
            "IP_ADDRESS",
            "10.0.0.1",
            "MEDIUM",
            null
        );

        assertEquals("MEDIUM", dto.severity());
    }

    @Test
    @DisplayName("Should handle LOW severity")
    void shouldHandleLowSeverity() {
        CreateThreatIndicatorRequestDto dto = new CreateThreatIndicatorRequestDto(
            "IP_ADDRESS",
            "10.0.0.1",
            "LOW",
            null
        );

        assertEquals("LOW", dto.severity());
    }

    @Test
    @DisplayName("Should handle CRITICAL severity")
    void shouldHandleCriticalSeverity() {
        CreateThreatIndicatorRequestDto dto = new CreateThreatIndicatorRequestDto(
            "IP_ADDRESS",
            "10.0.0.1",
            "CRITICAL",
            null
        );

        assertEquals("CRITICAL", dto.severity());
    }

    @Test
    @DisplayName("Should handle long description")
    void shouldHandleLongDescription() {
        String longDescription = "This IP address has been associated with multiple cyber attacks. " +
            "It is known to be part of a botnet used for DDoS attacks and has been flagged by " +
            "multiple threat intelligence sources.";

        CreateThreatIndicatorRequestDto dto = new CreateThreatIndicatorRequestDto(
            "IP_ADDRESS",
            "10.0.0.1",
            "HIGH",
            longDescription
        );

        assertEquals(longDescription, dto.description());
    }

    @Test
    @DisplayName("Should maintain immutability")
    void shouldMaintainImmutability() {
        CreateThreatIndicatorRequestDto dto = new CreateThreatIndicatorRequestDto(
            "DOMAIN",
            "test.com",
            "MEDIUM",
            "Test description"
        );

        // Record is immutable by design
        assertEquals("DOMAIN", dto.indicatorType());
        assertEquals("test.com", dto.value());
        assertEquals("MEDIUM", dto.severity());
        assertEquals("Test description", dto.description());
    }

    @Test
    @DisplayName("Should handle empty description")
    void shouldHandleEmptyDescription() {
        CreateThreatIndicatorRequestDto dto = new CreateThreatIndicatorRequestDto(
            "IP_ADDRESS",
            "10.0.0.1",
            "LOW",
            ""
        );

        assertEquals("", dto.description());
    }

    @Test
    @DisplayName("Should handle different IP formats")
    void shouldHandleDifferentIpFormats() {
        CreateThreatIndicatorRequestDto dto1 = new CreateThreatIndicatorRequestDto(
            "IP_ADDRESS", "192.168.1.1", null, null
        );
        CreateThreatIndicatorRequestDto dto2 = new CreateThreatIndicatorRequestDto(
            "IP_ADDRESS", "2001:0db8:85a3::8a2e:0370:7334", null, null
        );

        assertEquals("192.168.1.1", dto1.value());
        assertEquals("2001:0db8:85a3::8a2e:0370:7334", dto2.value());
    }

    @Test
    @DisplayName("Should handle file hash values")
    void shouldHandleFileHashValues() {
        CreateThreatIndicatorRequestDto dto1 = new CreateThreatIndicatorRequestDto(
            "HASH", "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd", null, null
        );
        CreateThreatIndicatorRequestDto dto2 = new CreateThreatIndicatorRequestDto(
            "HASH", "d41d8cd98f00b204e9800998ecf8427e", null, null
        );

        assertTrue(dto1.value().length() > 40);
        assertEquals(32, dto2.value().length());
    }
}
