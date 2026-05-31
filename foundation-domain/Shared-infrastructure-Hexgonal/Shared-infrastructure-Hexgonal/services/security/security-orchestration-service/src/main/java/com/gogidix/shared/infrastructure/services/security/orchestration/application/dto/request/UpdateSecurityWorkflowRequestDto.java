package com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.request;
/**
 * Request DTO for updating a security workflow.
 */
public record UpdateSecurityWorkflowRequestDto(
    String workflowName,
    String description,
    String triggerType
) {
}
