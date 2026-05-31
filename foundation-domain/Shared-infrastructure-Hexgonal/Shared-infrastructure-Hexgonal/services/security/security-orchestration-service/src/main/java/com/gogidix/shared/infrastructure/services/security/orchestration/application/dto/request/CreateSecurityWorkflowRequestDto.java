package com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.request;
import jakarta.validation.constraints.NotBlank;
/**
 * Request DTO for creating a security workflow.
 */
public record CreateSecurityWorkflowRequestDto(
    @NotBlank(message = "Workflow name is required")
    String workflowName,
    String description,
    String triggerType
) {
}
