package com.gogidix.aiservices.aiorchestrationservice.application.dto;

import com.gogidix.aiservices.aiorchestrationservice.domain.model.Workflow;
import com.gogidix.aiservices.aiorchestrationservice.domain.model.WorkflowStatus;
import com.gogidix.aiservices.aiorchestrationservice.domain.model.WorkflowStep;

import java.time.Instant;
import java.util.List;

public record WorkflowResponseDto(
    String id, String workflowId, String tenantId, String name, String description,
    List<WorkflowStep> steps, List<String> triggers, WorkflowStatus status,
    Instant createdAt, Instant updatedAt, Instant lastExecutedAt, Integer maxExecutionTime
) {
    public static WorkflowResponseDto from(Workflow workflow) {
        return new WorkflowResponseDto(
            workflow.getId(), workflow.getWorkflowId(), workflow.getTenantId(), workflow.getName(),
            workflow.getDescription(), workflow.getSteps(), workflow.getTriggers(),
            workflow.getStatus(), workflow.getCreatedAt(), workflow.getUpdatedAt(),
            workflow.getLastExecutedAt(), workflow.getMaxExecutionTime()
        );
    }
}
