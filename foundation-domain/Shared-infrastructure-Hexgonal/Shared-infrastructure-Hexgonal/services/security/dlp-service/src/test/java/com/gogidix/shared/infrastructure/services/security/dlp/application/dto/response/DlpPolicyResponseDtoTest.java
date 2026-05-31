package com.gogidix.shared.infrastructure.services.security.dlp.application.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DlpPolicyResponseDto.
 */
@DisplayName("DlpPolicyResponseDto Tests")
class DlpPolicyResponseDtoTest {

    @Test
    @DisplayName("Should create response DTO with all values")
    void shouldCreateResponseDtoWithAllValues() {
        LocalDateTime now = LocalDateTime.now();
        List<String> patterns = List.of("\\d{3}-\\d{2}-\\d{4}", "\\d{9}");

        DlpPolicyResponseDto dto = new DlpPolicyResponseDto(
            "policy-123",
            "tenant-456",
            "policy-id-789",
            "SSN Detection Policy",
            "Detects Social Security Numbers",
            "ACTIVE",
            patterns,
            "BLOCK",
            now,
            now
        );

        assertEquals("policy-123", dto.id());
        assertEquals("tenant-456", dto.tenantId());
        assertEquals("policy-id-789", dto.policyId());
        assertEquals("SSN Detection Policy", dto.policyName());
        assertEquals("Detects Social Security Numbers", dto.description());
        assertEquals("ACTIVE", dto.status());
        assertEquals(2, dto.sensitiveDataPatterns().size());
        assertEquals("BLOCK", dto.action());
        assertEquals(now, dto.createdAt());
        assertEquals(now, dto.updatedAt());
    }

    @Test
    @DisplayName("Should create response DTO with null optional values")
    void shouldCreateResponseDtoWithNullOptionalValues() {
        DlpPolicyResponseDto dto = new DlpPolicyResponseDto(
            "policy-123",
            "tenant-456",
            "policy-id-789",
            "Minimal Policy",
            null,
            "DRAFT",
            null,
            null,
            null,
            null
        );

        assertEquals("policy-123", dto.id());
        assertEquals("tenant-456", dto.tenantId());
        assertEquals("policy-id-789", dto.policyId());
        assertEquals("Minimal Policy", dto.policyName());
        assertNull(dto.description());
        assertEquals("DRAFT", dto.status());
        assertNull(dto.sensitiveDataPatterns());
        assertNull(dto.action());
        assertNull(dto.createdAt());
        assertNull(dto.updatedAt());
    }

    @Test
    @DisplayName("Should create response DTO with ACTIVE status")
    void shouldCreateResponseDtoWithActiveStatus() {
        DlpPolicyResponseDto dto = new DlpPolicyResponseDto(
            "policy-123",
            "tenant-456",
            "policy-id-789",
            "Active Policy",
            null,
            "ACTIVE",
            null,
            null,
            null,
            null
        );

        assertEquals("ACTIVE", dto.status());
    }

    @Test
    @DisplayName("Should create response DTO with INACTIVE status")
    void shouldCreateResponseDtoWithInactiveStatus() {
        DlpPolicyResponseDto dto = new DlpPolicyResponseDto(
            "policy-123",
            "tenant-456",
            "policy-id-789",
            "Inactive Policy",
            null,
            "INACTIVE",
            null,
            null,
            null,
            null
        );

        assertEquals("INACTIVE", dto.status());
    }

    @Test
    @DisplayName("Should create response DTO with DRAFT status")
    void shouldCreateResponseDtoWithDraftStatus() {
        DlpPolicyResponseDto dto = new DlpPolicyResponseDto(
            "policy-123",
            "tenant-456",
            "policy-id-789",
            "Draft Policy",
            null,
            "DRAFT",
            null,
            null,
            null,
            null
        );

        assertEquals("DRAFT", dto.status());
    }

    @Test
    @DisplayName("Should create response DTO with BLOCK action")
    void shouldCreateResponseDtoWithBlockAction() {
        DlpPolicyResponseDto dto = new DlpPolicyResponseDto(
            "policy-123",
            "tenant-456",
            "policy-id-789",
            "Block Policy",
            null,
            "ACTIVE",
            null,
            "BLOCK",
            null,
            null
        );

        assertEquals("BLOCK", dto.action());
    }

    @Test
    @DisplayName("Should create response DTO with ALERT action")
    void shouldCreateResponseDtoWithAlertAction() {
        DlpPolicyResponseDto dto = new DlpPolicyResponseDto(
            "policy-123",
            "tenant-456",
            "policy-id-789",
            "Alert Policy",
            null,
            "ACTIVE",
            null,
            "ALERT",
            null,
            null
        );

        assertEquals("ALERT", dto.action());
    }

    @Test
    @DisplayName("Should create response DTO with QUARANTINE action")
    void shouldCreateResponseDtoWithQuarantineAction() {
        DlpPolicyResponseDto dto = new DlpPolicyResponseDto(
            "policy-123",
            "tenant-456",
            "policy-id-789",
            "Quarantine Policy",
            null,
            "ACTIVE",
            null,
            "QUARANTINE",
            null,
            null
        );

        assertEquals("QUARANTINE", dto.action());
    }

    @Test
    @DisplayName("Should create response DTO with AUDIT action")
    void shouldCreateResponseDtoWithAuditAction() {
        DlpPolicyResponseDto dto = new DlpPolicyResponseDto(
            "policy-123",
            "tenant-456",
            "policy-id-789",
            "Audit Policy",
            null,
            "ACTIVE",
            null,
            "AUDIT",
            null,
            null
        );

        assertEquals("AUDIT", dto.action());
    }

    @Test
    @DisplayName("Should handle empty patterns list")
    void shouldHandleEmptyPatternsList() {
        DlpPolicyResponseDto dto = new DlpPolicyResponseDto(
            "policy-123",
            "tenant-456",
            "policy-id-789",
            "Empty Patterns Policy",
            null,
            "ACTIVE",
            List.of(),
            null,
            null,
            null
        );

        assertNotNull(dto.sensitiveDataPatterns());
        assertTrue(dto.sensitiveDataPatterns().isEmpty());
    }

    @Test
    @DisplayName("Should handle timestamps")
    void shouldHandleTimestamps() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 15, 14, 30, 0);

        DlpPolicyResponseDto dto = new DlpPolicyResponseDto(
            "policy-123",
            "tenant-456",
            "policy-id-789",
            "Timestamp Policy",
            null,
            "ACTIVE",
            null,
            null,
            createdAt,
            updatedAt
        );

        assertEquals(createdAt, dto.createdAt());
        assertEquals(updatedAt, dto.updatedAt());
        assertNotEquals(createdAt, updatedAt);
    }

    @Test
    @DisplayName("Should handle multiple sensitive data patterns")
    void shouldHandleMultipleSensitiveDataPatterns() {
        List<String> patterns = List.of(
            "\\d{3}-\\d{2}-\\d{4}",
            "\\d{9}",
            "\\b(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14})\\b",
            "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b"
        );

        DlpPolicyResponseDto dto = new DlpPolicyResponseDto(
            "policy-123",
            "tenant-456",
            "policy-id-789",
            "Multiple Patterns Policy",
            null,
            "ACTIVE",
            patterns,
            "BLOCK",
            null,
            null
        );

        assertEquals(4, dto.sensitiveDataPatterns().size());
        assertTrue(dto.sensitiveDataPatterns().contains("\\d{9}"));
    }

    @Test
    @DisplayName("Should maintain immutability")
    void shouldMaintainImmutability() {
        LocalDateTime now = LocalDateTime.now();
        List<String> patterns = List.of("\\d{9}");

        DlpPolicyResponseDto dto = new DlpPolicyResponseDto(
            "policy-123",
            "tenant-456",
            "policy-id-789",
            "Immutable Policy",
            "Test description",
            "ACTIVE",
            patterns,
            "BLOCK",
            now,
            now
        );

        // Record is immutable by design
        assertEquals("policy-123", dto.id());
        assertEquals("tenant-456", dto.tenantId());
        assertEquals(patterns, dto.sensitiveDataPatterns());
        assertEquals(now, dto.createdAt());
    }

    @Test
    @DisplayName("Should handle all required fields with minimal data")
    void shouldHandleAllRequiredFieldsWithMinimalData() {
        DlpPolicyResponseDto dto = new DlpPolicyResponseDto(
            "id",
            "tenantId",
            "policyId",
            "name",
            null,
            "ACTIVE",
            null,
            null,
            null,
            null
        );

        assertEquals("id", dto.id());
        assertEquals("tenantId", dto.tenantId());
        assertEquals("policyId", dto.policyId());
        assertEquals("name", dto.policyName());
        assertEquals("ACTIVE", dto.status());
    }
}
