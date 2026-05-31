package com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.response;
import java.time.LocalDateTime;
/**
 * Response DTO for security workflow operations.
 */
public record SecurityWorkflowResponseDto(
    String id,
    String tenantId,
    String workflowId,
    String workflowName,
    String description,
    String status,
    String triggerType,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
