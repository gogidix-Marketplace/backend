package com.gogidix.aiservices.supplychainoptimizationservice.domain.event;

import lombok.Getter;

@Getter
public enum OptimizationEventType {
    REQUEST_CREATED("request_created"),
    PROCESSING_STARTED("processing_started"),
    ANALYSIS_IN_PROGRESS("analysis_in_progress"),
    MODEL_EXECUTION("model_execution"),
    COMPLETED("completed"),
    FAILED("failed"),
    CANCELLED("cancelled"),
    RESULT_GENERATED("result_generated");

    private final String value;

    OptimizationEventType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
