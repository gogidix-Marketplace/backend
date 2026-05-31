package com.gogidix.shared.infrastructure.services.security.dlp.application.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CreateDlpPolicyRequestDto.
 */
@DisplayName("CreateDlpPolicyRequestDto Tests")
class CreateDlpPolicyRequestDtoTest {

    @Test
    @DisplayName("Should create DTO with all values")
    void shouldCreateDtoWithAllValues() {
        List<String> patterns = List.of("\\d{3}-\\d{2}-\\d{4}", "\\d{9}");
        CreateDlpPolicyRequestDto dto = new CreateDlpPolicyRequestDto(
            "SSN Detection Policy",
            "Detects Social Security Numbers in text",
            patterns,
            "BLOCK"
        );

        assertEquals("SSN Detection Policy", dto.policyName());
        assertEquals("Detects Social Security Numbers in text", dto.description());
        assertEquals(2, dto.sensitiveDataPatterns().size());
        assertEquals("BLOCK", dto.action());
    }

    @Test
    @DisplayName("Should create DTO with null optional values")
    void shouldCreateDtoWithNullOptionalValues() {
        CreateDlpPolicyRequestDto dto = new CreateDlpPolicyRequestDto(
            "Minimal Policy",
            null,
            null,
            null
        );

        assertEquals("Minimal Policy", dto.policyName());
        assertNull(dto.description());
        assertNull(dto.sensitiveDataPatterns());
        assertNull(dto.action());
    }

    @Test
    @DisplayName("Should create DTO with empty patterns list")
    void shouldCreateDtoWithEmptyPatternsList() {
        CreateDlpPolicyRequestDto dto = new CreateDlpPolicyRequestDto(
            "Empty Patterns Policy",
            "No patterns defined",
            List.of(),
            "ALERT"
        );

        assertEquals("Empty Patterns Policy", dto.policyName());
        assertNotNull(dto.sensitiveDataPatterns());
        assertTrue(dto.sensitiveDataPatterns().isEmpty());
    }

    @Test
    @DisplayName("Should handle ALERT action")
    void shouldHandleAlertAction() {
        CreateDlpPolicyRequestDto dto = new CreateDlpPolicyRequestDto(
            "Alert Policy",
            null,
            null,
            "ALERT"
        );

        assertEquals("ALERT", dto.action());
    }

    @Test
    @DisplayName("Should handle BLOCK action")
    void shouldHandleBlockAction() {
        CreateDlpPolicyRequestDto dto = new CreateDlpPolicyRequestDto(
            "Block Policy",
            null,
            null,
            "BLOCK"
        );

        assertEquals("BLOCK", dto.action());
    }

    @Test
    @DisplayName("Should handle QUARANTINE action")
    void shouldHandleQuarantineAction() {
        CreateDlpPolicyRequestDto dto = new CreateDlpPolicyRequestDto(
            "Quarantine Policy",
            null,
            null,
            "QUARANTINE"
        );

        assertEquals("QUARANTINE", dto.action());
    }

    @Test
    @DisplayName("Should handle AUDIT action")
    void shouldHandleAuditAction() {
        CreateDlpPolicyRequestDto dto = new CreateDlpPolicyRequestDto(
            "Audit Policy",
            null,
            null,
            "AUDIT"
        );

        assertEquals("AUDIT", dto.action());
    }

    @Test
    @DisplayName("Should handle complex regex patterns")
    void shouldHandleComplexRegexPatterns() {
        List<String> patterns = List.of(
            "\\b\\d{3}[-.]?\\d{2}[-.]?\\d{4}\\b",
            "\\b\\d{3}[-.]?\\d{2}[-.]?\\d{4}\\b",
            "\\b(?:\\d[ -]*?){13,16}\\b"
        );
        CreateDlpPolicyRequestDto dto = new CreateDlpPolicyRequestDto(
            "Complex Patterns Policy",
            "Multiple complex patterns",
            patterns,
            "BLOCK"
        );

        assertEquals(3, dto.sensitiveDataPatterns().size());
        assertTrue(dto.sensitiveDataPatterns().get(0).contains("\\b"));
    }

    @Test
    @DisplayName("Should handle single pattern")
    void shouldHandleSinglePattern() {
        List<String> patterns = List.of("\\d{3}-\\d{2}-\\d{4}");
        CreateDlpPolicyRequestDto dto = new CreateDlpPolicyRequestDto(
            "Single Pattern Policy",
            "Single pattern",
            patterns,
            "BLOCK"
        );

        assertEquals(1, dto.sensitiveDataPatterns().size());
        assertEquals("\\d{3}-\\d{2}-\\d{4}", dto.sensitiveDataPatterns().get(0));
    }

    @Test
    @DisplayName("Should handle long description")
    void shouldHandleLongDescription() {
        String longDescription = "This is a very long description that contains detailed information about the DLP policy. " +
            "It explains what data patterns to look for and what actions to take when such patterns are detected. " +
            "The policy is designed to protect sensitive information from being leaked outside the organization.";

        CreateDlpPolicyRequestDto dto = new CreateDlpPolicyRequestDto(
            "Long Description Policy",
            longDescription,
            null,
            "ALERT"
        );

        assertEquals(longDescription, dto.description());
    }

    @Test
    @DisplayName("Should handle special characters in policy name")
    void shouldHandleSpecialCharactersInPolicyName() {
        CreateDlpPolicyRequestDto dto = new CreateDlpPolicyRequestDto(
            "Policy-1: SSN & Credit Card (2024)",
            "Test description",
            null,
            "BLOCK"
        );

        assertEquals("Policy-1: SSN & Credit Card (2024)", dto.policyName());
    }

    @Test
    @DisplayName("Should maintain immutability")
    void shouldMaintainImmutability() {
        List<String> patterns = List.of("\\d{9}");
        CreateDlpPolicyRequestDto dto = new CreateDlpPolicyRequestDto(
            "Immutable Policy",
            "Test",
            patterns,
            "BLOCK"
        );

        // Record is immutable by design
        assertEquals("Immutable Policy", dto.policyName());
        assertEquals(patterns, dto.sensitiveDataPatterns());
    }
}
