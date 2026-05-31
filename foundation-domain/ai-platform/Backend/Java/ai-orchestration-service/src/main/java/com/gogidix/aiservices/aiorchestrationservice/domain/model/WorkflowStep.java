package com.gogidix.aiservices.aiorchestrationservice.domain.model;

import java.util.Map;
import java.util.Objects;

public class WorkflowStep {
    private final String stepId;
    private final String service;
    private final String action;
    private final Map<String, Object> parameters;
    private final int estimatedDuration;
    private final int retryCount;
    private final int maxRetries;

    public WorkflowStep(String stepId, String service, String action, Map<String, Object> parameters, int estimatedDuration, int maxRetries) {
        this.stepId = Objects.requireNonNull(stepId, "stepId is required");
        this.service = Objects.requireNonNull(service, "service is required");
        this.action = Objects.requireNonNull(action, "action is required");
        this.parameters = parameters;
        this.estimatedDuration = estimatedDuration;
        this.maxRetries = maxRetries;
        this.retryCount = 0;
    }

    public String getStepId() { return stepId; }
    public String getService() { return service; }
    public String getAction() { return action; }
    public Map<String, Object> getParameters() { return parameters; }
    public int getEstimatedDuration() { return estimatedDuration; }
    public int getRetryCount() { return retryCount; }
    public int getMaxRetries() { return maxRetries; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String stepId;
        private String service;
        private String action;
        private Map<String, Object> parameters;
        private int estimatedDuration = 5;
        private int maxRetries = 3;

        public Builder stepId(String stepId) { this.stepId = stepId; return this; }
        public Builder service(String service) { this.service = service; return this; }
        public Builder action(String action) { this.action = action; return this; }
        public Builder parameters(Map<String, Object> parameters) { this.parameters = parameters; return this; }
        public Builder estimatedDuration(int estimatedDuration) { this.estimatedDuration = estimatedDuration; return this; }
        public Builder maxRetries(int maxRetries) { this.maxRetries = maxRetries; return this; }

        public WorkflowStep build() {
            return new WorkflowStep(
                stepId != null ? stepId : "step_" + System.currentTimeMillis(),
                service, action, parameters, estimatedDuration, maxRetries
            );
        }
    }
}
