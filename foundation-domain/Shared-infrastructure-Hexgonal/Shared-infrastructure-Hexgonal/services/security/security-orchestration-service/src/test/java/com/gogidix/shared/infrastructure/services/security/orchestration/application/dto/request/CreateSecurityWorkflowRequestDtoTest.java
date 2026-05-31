package com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CreateSecurityWorkflowRequestDto.
 */
@DisplayName("CreateSecurityWorkflowRequestDto Tests")
class CreateSecurityWorkflowRequestDtoTest {

    @Test
    @DisplayName("Should create DTO with all values")
    void shouldCreateDtoWithAllValues() {
        CreateSecurityWorkflowRequestDto dto = new CreateSecurityWorkflowRequestDto(
            "Incident Response Workflow",
            "Automated response to security incidents",
            "THREAT_DETECTED"
        );

        assertEquals("Incident Response Workflow", dto.workflowName());
        assertEquals("Automated response to security incidents", dto.description());
        assertEquals("THREAT_DETECTED", dto.triggerType());
    }

    @Test
    @DisplayName("Should create DTO with null optional values")
    void shouldCreateDtoWithNullOptionalValues() {
        CreateSecurityWorkflowRequestDto dto = new CreateSecurityWorkflowRequestDto(
            "Minimal Workflow",
            null,
            null
        );

        assertEquals("Minimal Workflow", dto.workflowName());
        assertNull(dto.description());
        assertNull(dto.triggerType());
    }

    @Test
    @DisplayName("Should handle THREAT_DETECTED trigger type")
    void shouldHandleThreatDetectedTriggerType() {
        CreateSecurityWorkflowRequestDto dto = new CreateSecurityWorkflowRequestDto(
            "Threat Response",
            null,
            "THREAT_DETECTED"
        );

        assertEquals("THREAT_DETECTED", dto.triggerType());
    }

    @Test
    @DisplayName("Should handle SCHEDULED trigger type")
    void shouldHandleScheduledTriggerType() {
        CreateSecurityWorkflowRequestDto dto = new CreateSecurityWorkflowRequestDto(
            "Scheduled Task",
            null,
            "SCHEDULED"
        );

        assertEquals("SCHEDULED", dto.triggerType());
    }

    @Test
    @DisplayName("Should handle MANUAL trigger type")
    void shouldHandleManualTriggerType() {
        CreateSecurityWorkflowRequestDto dto = new CreateSecurityWorkflowRequestDto(
            "Manual Task",
            null,
            "MANUAL"
        );

        assertEquals("MANUAL", dto.triggerType());
    }

    @Test
    @DisplayName("Should handle EVENT_BASED trigger type")
    void shouldHandleEventBasedTriggerType() {
        CreateSecurityWorkflowRequestDto dto = new CreateSecurityWorkflowRequestDto(
            "Event Based Task",
            null,
            "EVENT_BASED"
        );

        assertEquals("EVENT_BASED", dto.triggerType());
    }

    @Test
    @DisplayName("Should handle long description")
    void shouldHandleLongDescription() {
        String longDescription = "This is a comprehensive workflow for handling security incidents. " +
            "It includes steps for detection, containment, eradication, and recovery. " +
            "The workflow orchestrates multiple security tools and processes.";

        CreateSecurityWorkflowRequestDto dto = new CreateSecurityWorkflowRequestDto(
            "Complex Workflow",
            longDescription,
            null
        );

        assertEquals(longDescription, dto.description());
    }

    @Test
    @DisplayName("Should maintain immutability")
    void shouldMaintainImmutability() {
        CreateSecurityWorkflowRequestDto dto = new CreateSecurityWorkflowRequestDto(
            "Test Workflow",
            "Test description",
            "MANUAL"
        );

        // Record is immutable by design
        assertEquals("Test Workflow", dto.workflowName());
        assertEquals("Test description", dto.description());
        assertEquals("MANUAL", dto.triggerType());
    }

    @Test
    @DisplayName("Should handle empty description")
    void shouldHandleEmptyDescription() {
        CreateSecurityWorkflowRequestDto dto = new CreateSecurityWorkflowRequestDto(
            "Empty Description Workflow",
            "",
            null
        );

        assertEquals("", dto.description());
    }

    @Test
    @DisplayName("Should handle special characters in workflow name")
    void shouldHandleSpecialCharactersInWorkflowName() {
        CreateSecurityWorkflowRequestDto dto = new CreateSecurityWorkflowRequestDto(
            "Workflow-1: Incident Response (2024)",
            null,
            null
        );

        assertEquals("Workflow-1: Incident Response (2024)", dto.workflowName());
    }
}
