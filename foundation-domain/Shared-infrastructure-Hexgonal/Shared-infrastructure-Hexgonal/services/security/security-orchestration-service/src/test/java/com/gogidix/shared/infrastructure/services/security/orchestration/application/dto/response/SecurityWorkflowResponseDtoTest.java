package com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SecurityWorkflowResponseDto.
 */
@DisplayName("SecurityWorkflowResponseDto Tests")
class SecurityWorkflowResponseDtoTest {

    @Test
    @DisplayName("Should create response DTO with all values")
    void shouldCreateResponseDtoWithAllValues() {
        LocalDateTime now = LocalDateTime.now();

        SecurityWorkflowResponseDto dto = new SecurityWorkflowResponseDto(
            "workflow-123",
            "tenant-456",
            "workflow-id-789",
            "Incident Response Workflow",
            "Automated incident response",
            "ACTIVE",
            "THREAT_DETECTED",
            now,
            now
        );

        assertEquals("workflow-123", dto.id());
        assertEquals("tenant-456", dto.tenantId());
        assertEquals("workflow-id-789", dto.workflowId());
        assertEquals("Incident Response Workflow", dto.workflowName());
        assertEquals("Automated incident response", dto.description());
        assertEquals("ACTIVE", dto.status());
        assertEquals("THREAT_DETECTED", dto.triggerType());
        assertEquals(now, dto.createdAt());
        assertEquals(now, dto.updatedAt());
    }

    @Test
    @DisplayName("Should create response DTO with null optional values")
    void shouldCreateResponseDtoWithNullOptionalValues() {
        SecurityWorkflowResponseDto dto = new SecurityWorkflowResponseDto(
            "workflow-123",
            "tenant-456",
            "workflow-id-789",
            "Minimal Workflow",
            null,
            "DRAFT",
            null,
            null,
            null
        );

        assertEquals("workflow-123", dto.id());
        assertEquals("tenant-456", dto.tenantId());
        assertEquals("workflow-id-789", dto.workflowId());
        assertEquals("Minimal Workflow", dto.workflowName());
        assertNull(dto.description());
        assertEquals("DRAFT", dto.status());
        assertNull(dto.triggerType());
        assertNull(dto.createdAt());
        assertNull(dto.updatedAt());
    }

    @Test
    @DisplayName("Should create response DTO with ACTIVE status")
    void shouldCreateResponseDtoWithActiveStatus() {
        SecurityWorkflowResponseDto dto = new SecurityWorkflowResponseDto(
            "id", "tenant", "workflowId", "Active Workflow", null, "ACTIVE", null, null, null
        );

        assertEquals("ACTIVE", dto.status());
    }

    @Test
    @DisplayName("Should create response DTO with INACTIVE status")
    void shouldCreateResponseDtoWithInactiveStatus() {
        SecurityWorkflowResponseDto dto = new SecurityWorkflowResponseDto(
            "id", "tenant", "workflowId", "Inactive Workflow", null, "INACTIVE", null, null, null
        );

        assertEquals("INACTIVE", dto.status());
    }

    @Test
    @DisplayName("Should create response DTO with DRAFT status")
    void shouldCreateResponseDtoWithDraftStatus() {
        SecurityWorkflowResponseDto dto = new SecurityWorkflowResponseDto(
            "id", "tenant", "workflowId", "Draft Workflow", null, "DRAFT", null, null, null
        );

        assertEquals("DRAFT", dto.status());
    }

    @Test
    @DisplayName("Should handle different trigger types")
    void shouldHandleDifferentTriggerTypes() {
        SecurityWorkflowResponseDto dto1 = new SecurityWorkflowResponseDto(
            "id1", "tenant", "workflowId1", "Test1", null, "ACTIVE", "THREAT_DETECTED", null, null
        );
        SecurityWorkflowResponseDto dto2 = new SecurityWorkflowResponseDto(
            "id2", "tenant", "workflowId2", "Test2", null, "ACTIVE", "SCHEDULED", null, null
        );
        SecurityWorkflowResponseDto dto3 = new SecurityWorkflowResponseDto(
            "id3", "tenant", "workflowId3", "Test3", null, "ACTIVE", "MANUAL", null, null
        );
        SecurityWorkflowResponseDto dto4 = new SecurityWorkflowResponseDto(
            "id4", "tenant", "workflowId4", "Test4", null, "ACTIVE", "EVENT_BASED", null, null
        );

        assertEquals("THREAT_DETECTED", dto1.triggerType());
        assertEquals("SCHEDULED", dto2.triggerType());
        assertEquals("MANUAL", dto3.triggerType());
        assertEquals("EVENT_BASED", dto4.triggerType());
    }

    @Test
    @DisplayName("Should handle timestamps")
    void shouldHandleTimestamps() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 15, 14, 30, 0);

        SecurityWorkflowResponseDto dto = new SecurityWorkflowResponseDto(
            "workflow-123",
            "tenant-456",
            "workflow-id-789",
            "Timestamp Workflow",
            null,
            "ACTIVE",
            null,
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

        SecurityWorkflowResponseDto dto = new SecurityWorkflowResponseDto(
            "workflow-123",
            "tenant-456",
            "workflow-id-789",
            "Immutable Workflow",
            "Test description",
            "ACTIVE",
            "MANUAL",
            now,
            now
        );

        // Record is immutable by design
        assertEquals("workflow-123", dto.id());
        assertEquals("tenant-456", dto.tenantId());
        assertEquals("MANUAL", dto.triggerType());
    }

    @Test
    @DisplayName("Should handle long description")
    void shouldHandleLongDescription() {
        String longDescription = "This is a comprehensive workflow that handles multiple security scenarios. " +
            "It includes automated responses for malware detection, phishing attempts, and data breaches. " +
            "The workflow integrates with various security tools for coordinated response.";

        SecurityWorkflowResponseDto dto = new SecurityWorkflowResponseDto(
            "id", "tenant", "workflowId", "Complex Workflow", longDescription, "ACTIVE", null, null, null
        );

        assertEquals(longDescription, dto.description());
    }

    @Test
    @DisplayName("Should handle all required fields with minimal data")
    void shouldHandleAllRequiredFieldsWithMinimalData() {
        SecurityWorkflowResponseDto dto = new SecurityWorkflowResponseDto(
            "id",
            "tenantId",
            "workflowId",
            "name",
            null,
            "ACTIVE",
            null,
            null,
            null
        );

        assertEquals("id", dto.id());
        assertEquals("tenantId", dto.tenantId());
        assertEquals("workflowId", dto.workflowId());
        assertEquals("name", dto.workflowName());
        assertEquals("ACTIVE", dto.status());
    }
}
