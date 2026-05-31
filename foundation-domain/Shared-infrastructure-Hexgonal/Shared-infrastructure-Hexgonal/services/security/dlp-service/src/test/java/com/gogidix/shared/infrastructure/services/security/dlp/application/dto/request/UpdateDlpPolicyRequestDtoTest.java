package com.gogidix.shared.infrastructure.services.security.dlp.application.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UpdateDlpPolicyRequestDto.
 */
@DisplayName("UpdateDlpPolicyRequestDto Tests")
class UpdateDlpPolicyRequestDtoTest {

    @Test
    @DisplayName("Should create DTO with all values")
    void shouldCreateDtoWithAllValues() {
        List<String> patterns = List.of("\\d{3}-\\d{2}-\\d{4}", "\\d{9}");
        UpdateDlpPolicyRequestDto dto = new UpdateDlpPolicyRequestDto(
            "Updated SSN Detection Policy",
            "Updated description",
            patterns,
            "QUARANTINE"
        );

        assertEquals("Updated SSN Detection Policy", dto.policyName());
        assertEquals("Updated description", dto.description());
        assertEquals(2, dto.sensitiveDataPatterns().size());
        assertEquals("QUARANTINE", dto.action());
    }

    @Test
    @DisplayName("Should create DTO with all null values")
    void shouldCreateDtoWithAllNullValues() {
        UpdateDlpPolicyRequestDto dto = new UpdateDlpPolicyRequestDto(
            null,
            null,
            null,
            null
        );

        assertNull(dto.policyName());
        assertNull(dto.description());
        assertNull(dto.sensitiveDataPatterns());
        assertNull(dto.action());
    }

    @Test
    @DisplayName("Should create DTO with partial updates")
    void shouldCreateDtoWithPartialUpdates() {
        UpdateDlpPolicyRequestDto dto = new UpdateDlpPolicyRequestDto(
            "New Name Only",
            null,
            null,
            null
        );

        assertEquals("New Name Only", dto.policyName());
        assertNull(dto.description());
        assertNull(dto.sensitiveDataPatterns());
        assertNull(dto.action());
    }

    @Test
    @DisplayName("Should create DTO with only action update")
    void shouldCreateDtoWithOnlyActionUpdate() {
        UpdateDlpPolicyRequestDto dto = new UpdateDlpPolicyRequestDto(
            null,
            null,
            null,
            "AUDIT"
        );

        assertNull(dto.policyName());
        assertNull(dto.description());
        assertNull(dto.sensitiveDataPatterns());
        assertEquals("AUDIT", dto.action());
    }

    @Test
    @DisplayName("Should create DTO with only patterns update")
    void shouldCreateDtoWithOnlyPatternsUpdate() {
        List<String> patterns = List.of("\\d{16}");
        UpdateDlpPolicyRequestDto dto = new UpdateDlpPolicyRequestDto(
            null,
            null,
            patterns,
            null
        );

        assertNull(dto.policyName());
        assertNull(dto.description());
        assertEquals(1, dto.sensitiveDataPatterns().size());
        assertNull(dto.action());
    }

    @Test
    @DisplayName("Should create DTO with only description update")
    void shouldCreateDtoWithOnlyDescriptionUpdate() {
        UpdateDlpPolicyRequestDto dto = new UpdateDlpPolicyRequestDto(
            null,
            "Updated description only",
            null,
            null
        );

        assertNull(dto.policyName());
        assertEquals("Updated description only", dto.description());
        assertNull(dto.sensitiveDataPatterns());
        assertNull(dto.action());
    }

    @Test
    @DisplayName("Should handle ALERT action")
    void shouldHandleAlertAction() {
        UpdateDlpPolicyRequestDto dto = new UpdateDlpPolicyRequestDto(
            null,
            null,
            null,
            "ALERT"
        );

        assertEquals("ALERT", dto.action());
    }

    @Test
    @DisplayName("Should handle BLOCK action")
    void shouldHandleBlockAction() {
        UpdateDlpPolicyRequestDto dto = new UpdateDlpPolicyRequestDto(
            null,
            null,
            null,
            "BLOCK"
        );

        assertEquals("BLOCK", dto.action());
    }

    @Test
    @DisplayName("Should handle QUARANTINE action")
    void shouldHandleQuarantineAction() {
        UpdateDlpPolicyRequestDto dto = new UpdateDlpPolicyRequestDto(
            null,
            null,
            null,
            "QUARANTINE"
        );

        assertEquals("QUARANTINE", dto.action());
    }

    @Test
    @DisplayName("Should handle AUDIT action")
    void shouldHandleAuditAction() {
        UpdateDlpPolicyRequestDto dto = new UpdateDlpPolicyRequestDto(
            null,
            null,
            null,
            "AUDIT"
        );

        assertEquals("AUDIT", dto.action());
    }

    @Test
    @DisplayName("Should handle empty patterns list")
    void shouldHandleEmptyPatternsList() {
        UpdateDlpPolicyRequestDto dto = new UpdateDlpPolicyRequestDto(
            null,
            null,
            List.of(),
            null
        );

        assertNotNull(dto.sensitiveDataPatterns());
        assertTrue(dto.sensitiveDataPatterns().isEmpty());
    }

    @Test
    @DisplayName("Should handle multiple patterns")
    void shouldHandleMultiplePatterns() {
        List<String> patterns = List.of(
            "\\d{3}-\\d{2}-\\d{4}",
            "\\d{9}",
            "\\b(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14})\\b"
        );
        UpdateDlpPolicyRequestDto dto = new UpdateDlpPolicyRequestDto(
            null,
            null,
            patterns,
            null
        );

        assertEquals(3, dto.sensitiveDataPatterns().size());
    }

    @Test
    @DisplayName("Should maintain immutability")
    void shouldMaintainImmutability() {
        List<String> patterns = List.of("\\d{9}");
        UpdateDlpPolicyRequestDto dto = new UpdateDlpPolicyRequestDto(
            "Immutable Policy",
            "Test",
            patterns,
            "BLOCK"
        );

        // Record is immutable by design
        assertEquals("Immutable Policy", dto.policyName());
        assertEquals(patterns, dto.sensitiveDataPatterns());
    }

    @Test
    @DisplayName("Should allow updating all fields to new values")
    void shouldAllowUpdatingAllFieldsToNewValues() {
        UpdateDlpPolicyRequestDto dto = new UpdateDlpPolicyRequestDto(
            "Completely New Policy Name",
            "Completely new description",
            List.of("\\d{10}"),
            "ALERT"
        );

        assertEquals("Completely New Policy Name", dto.policyName());
        assertEquals("Completely new description", dto.description());
        assertEquals(1, dto.sensitiveDataPatterns().size());
        assertEquals("\\d{10}", dto.sensitiveDataPatterns().get(0));
        assertEquals("ALERT", dto.action());
    }

    @Test
    @DisplayName("Should handle empty string as valid policy name")
    void shouldHandleEmptyStringAsValidPolicyName() {
        UpdateDlpPolicyRequestDto dto = new UpdateDlpPolicyRequestDto(
            "",
            null,
            null,
            null
        );

        assertEquals("", dto.policyName());
    }
}
