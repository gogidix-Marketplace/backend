package com.gogidix.aiservices.aiorchestrationservice.application.dto;

import com.gogidix.aiservices.aiorchestrationservice.domain.model.WorkflowExecution;

import java.time.Instant;

public record WorkflowExecutionResponseDto(
    String executionId, String workflowId, String tenantId,
    WorkflowExecution.ExecutionStatus status, Instant startedAt, Instant completedAt,
    String errorMessage, long durationSeconds
) {
    public static WorkflowExecutionResponseDto from(WorkflowExecution execution) {
        return new WorkflowExecutionResponseDto(
            execution.getExecutionId(), execution.getWorkflowId(), execution.getTenantId(),
            execution.getStatus(), execution.getStartedAt(), execution.getCompletedAt(),
            execution.getErrorMessage(), execution.getDurationSeconds()
        );
    }
}
