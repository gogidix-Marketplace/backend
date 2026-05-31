package com.gogidix.aiservices.aiorchestrationservice.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class WorkflowExecution {
    private final String executionId;
    private final String workflowId;
    private final String tenantId;
    private final Instant startedAt;
    private Instant completedAt;
    private ExecutionStatus status;
    private final List<StepExecutionResult> stepResults;
    private String errorMessage;

    public enum ExecutionStatus {
        RUNNING, COMPLETED, FAILED, TIMEOUT
    }

    public WorkflowExecution(String workflowId, String tenantId) {
        this.executionId = "exec_" + java.util.UUID.randomUUID().toString().substring(0, 8);
        this.workflowId = Objects.requireNonNull(workflowId, "workflowId is required");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.startedAt = Instant.now();
        this.status = ExecutionStatus.RUNNING;
        this.stepResults = new ArrayList<>();
    }

    public void complete() {
        this.status = ExecutionStatus.COMPLETED;
        this.completedAt = Instant.now();
    }

    public void fail(String errorMessage) {
        this.status = ExecutionStatus.FAILED;
        this.errorMessage = errorMessage;
        this.completedAt = Instant.now();
    }

    public void timeout() {
        this.status = ExecutionStatus.TIMEOUT;
        this.completedAt = Instant.now();
    }

    public void addStepResult(StepExecutionResult result) {
        this.stepResults.add(result);
    }

    public String getExecutionId() { return executionId; }
    public String getWorkflowId() { return workflowId; }
    public String getTenantId() { return tenantId; }
    public Instant getStartedAt() { return startedAt; }
    public Instant getCompletedAt() { return completedAt; }
    public ExecutionStatus getStatus() { return status; }
    public List<StepExecutionResult> getStepResults() { return Collections.unmodifiableList(stepResults); }
    public String getErrorMessage() { return errorMessage; }

    public long getDurationSeconds() {
        if (completedAt == null) {
            return Instant.now().getEpochSecond() - startedAt.getEpochSecond();
        }
        return completedAt.getEpochSecond() - startedAt.getEpochSecond();
    }
}
