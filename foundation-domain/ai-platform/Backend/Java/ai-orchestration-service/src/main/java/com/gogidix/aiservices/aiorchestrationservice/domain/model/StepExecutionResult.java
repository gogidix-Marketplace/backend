package com.gogidix.aiservices.aiorchestrationservice.domain.model;

import java.time.Instant;

public record StepExecutionResult(
    String stepId,
    String service,
    String action,
    boolean success,
    String output,
    String errorMessage,
    Instant startedAt,
    Instant completedAt,
    int attemptNumber
) {
    public long getDurationMs() {
        return completedAt.toEpochMilli() - startedAt.toEpochMilli();
    }
}
