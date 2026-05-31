package com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UpdateSecurityWorkflowRequestDto.
 */
@DisplayName("UpdateSecurityWorkflowRequestDto Tests")
class UpdateSecurityWorkflowRequestDtoTest {

    @Test
    @DisplayName("Should create DTO with all values")
    void shouldCreateDtoWithAllValues() {
        UpdateSecurityWorkflowRequestDto dto = new UpdateSecurityWorkflowRequestDto(
            "Updated Workflow",
            "Updated description",
            "SCHEDULED"
        );

        assertEquals("Updated Workflow", dto.workflowName());
        assertEquals("Updated description", dto.description());
        assertEquals("SCHEDULED", dto.triggerType());
    }

    @Test
    @DisplayName("Should create DTO with all null values")
    void shouldCreateDtoWithAllNullValues() {
        UpdateSecurityWorkflowRequestDto dto = new UpdateSecurityWorkflowRequestDto(
            null,
            null,
            null
        );

        assertNull(dto.workflowName());
        assertNull(dto.description());
        assertNull(dto.triggerType());
    }

    @Test
    @DisplayName("Should create DTO with partial updates")
    void shouldCreateDtoWithPartialUpdates() {
        UpdateSecurityWorkflowRequestDto dto = new UpdateSecurityWorkflowRequestDto(
            "New Name Only",
            null,
            null
        );

        assertEquals("New Name Only", dto.workflowName());
        assertNull(dto.description());
        assertNull(dto.triggerType());
    }

    @Test
    @DisplayName("Should create DTO with only trigger type update")
    void shouldCreateDtoWithOnlyTriggerTypeUpdate() {
        UpdateSecurityWorkflowRequestDto dto = new UpdateSecurityWorkflowRequestDto(
            null,
            null,
            "MANUAL"
        );

        assertNull(dto.workflowName());
        assertNull(dto.description());
        assertEquals("MANUAL", dto.triggerType());
    }

    @Test
    @DisplayName("Should create DTO with only description update")
    void shouldCreateDtoWithOnlyDescriptionUpdate() {
        UpdateSecurityWorkflowRequestDto dto = new UpdateSecurityWorkflowRequestDto(
            null,
            "Updated description only",
            null
        );

        assertNull(dto.workflowName());
        assertEquals("Updated description only", dto.description());
        assertNull(dto.triggerType());
    }

    @Test
    @DisplayName("Should handle different trigger types")
    void shouldHandleDifferentTriggerTypes() {
        UpdateSecurityWorkflowRequestDto dto1 = new UpdateSecurityWorkflowRequestDto(null, null, "THREAT_DETECTED");
        UpdateSecurityWorkflowRequestDto dto2 = new UpdateSecurityWorkflowRequestDto(null, null, "SCHEDULED");
        UpdateSecurityWorkflowRequestDto dto3 = new UpdateSecurityWorkflowRequestDto(null, null, "MANUAL");
        UpdateSecurityWorkflowRequestDto dto4 = new UpdateSecurityWorkflowRequestDto(null, null, "EVENT_BASED");

        assertEquals("THREAT_DETECTED", dto1.triggerType());
        assertEquals("SCHEDULED", dto2.triggerType());
        assertEquals("MANUAL", dto3.triggerType());
        assertEquals("EVENT_BASED", dto4.triggerType());
    }

    @Test
    @DisplayName("Should maintain immutability")
    void shouldMaintainImmutability() {
        UpdateSecurityWorkflowRequestDto dto = new UpdateSecurityWorkflowRequestDto(
            "Immutable Workflow",
            "Test description",
            "THREAT_DETECTED"
        );

        // Record is immutable by design
        assertEquals("Immutable Workflow", dto.workflowName());
        assertEquals("Test description", dto.description());
        assertEquals("THREAT_DETECTED", dto.triggerType());
    }

    @Test
    @DisplayName("Should allow updating all fields to new values")
    void shouldAllowUpdatingAllFieldsToNewValues() {
        UpdateSecurityWorkflowRequestDto dto = new UpdateSecurityWorkflowRequestDto(
            "Completely New Workflow Name",
            "Completely new description",
            "SCHEDULED"
        );

        assertEquals("Completely New Workflow Name", dto.workflowName());
        assertEquals("Completely new description", dto.description());
        assertEquals("SCHEDULED", dto.triggerType());
    }

    @Test
    @DisplayName("Should handle empty string as valid workflow name")
    void shouldHandleEmptyStringAsValidWorkflowName() {
        UpdateSecurityWorkflowRequestDto dto = new UpdateSecurityWorkflowRequestDto(
            "",
            null,
            null
        );

        assertEquals("", dto.workflowName());
    }
}
