package com.gogidix.aiservices.supplychainoptimizationservice.domain.aggregate;

import com.gogidix.aiservices.supplychainoptimizationservice.domain.event.OptimizationEvent;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.event.OptimizationEventType;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.*;
import com.gogidix.aiservices.supplychainoptimizationservice.shared.exception.SupplyChainException;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class OptimizationRequest {
    private final UUID requestId;
    private final String tenantId;
    private final OptimizationType type;
    private final Map<String, Object> parameters;
    private OptimizationStatus status;
    private OptimizationResult result;
    private final List<OptimizationEvent> events;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant completedAt;
    private String errorMessage;
    private final int priority;

    private OptimizationRequest(Builder builder) {
        if (builder.tenantId == null || builder.tenantId.trim().isEmpty()) {
            throw new IllegalArgumentException("Tenant ID is required");
        }
        if (builder.type == null) {
            throw new IllegalArgumentException("Optimization type is required");
        }
        this.requestId = builder.requestId != null ? builder.requestId : UUID.randomUUID();
        this.tenantId = builder.tenantId;
        this.type = builder.type;
        this.parameters = builder.parameters != null ? new HashMap<>(builder.parameters) : new HashMap<>();
        this.status = OptimizationStatus.PENDING;
        this.result = null;
        this.events = new ArrayList<>();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.priority = builder.priority;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public UUID getRequestId() {
        return requestId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public OptimizationType getType() {
        return type;
    }

    public Map<String, Object> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    public OptimizationStatus getStatus() {
        return status;
    }

    public OptimizationResult getResult() {
        return result;
    }

    public List<OptimizationEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getPriority() {
        return priority;
    }

    // Business methods
    public void startProcessing() {
        if (status != OptimizationStatus.PENDING) {
            throw new SupplyChainException("Cannot start processing from status: " + status);
        }
        this.status = OptimizationStatus.IN_PROGRESS;
        addEvent(OptimizationEvent.builder()
                .eventType(OptimizationEventType.PROCESSING_STARTED)
                .description("Optimization processing started")
                .build());
        touch();
    }

    public void completeWithResult(OptimizationResult result) {
        if (status != OptimizationStatus.IN_PROGRESS) {
            throw new SupplyChainException("Cannot complete from status: " + status);
        }
        this.status = OptimizationStatus.COMPLETED;
        this.result = result;
        this.completedAt = Instant.now();
        addEvent(OptimizationEvent.builder()
                .eventType(OptimizationEventType.COMPLETED)
                .description("Optimization completed successfully")
                .build());
        touch();
    }

    public void failWithError(String errorMessage) {
        if (status.isTerminal()) {
            throw new SupplyChainException("Cannot fail from terminal status: " + status);
        }
        this.status = OptimizationStatus.FAILED;
        this.errorMessage = errorMessage;
        this.completedAt = Instant.now();
        addEvent(OptimizationEvent.builder()
                .eventType(OptimizationEventType.FAILED)
                .description("Optimization failed: " + errorMessage)
                .build());
        touch();
    }

    public void cancel() {
        if (status.isTerminal()) {
            throw new SupplyChainException("Cannot cancel from terminal status: " + status);
        }
        this.status = OptimizationStatus.CANCELLED;
        this.completedAt = Instant.now();
        addEvent(OptimizationEvent.builder()
                .eventType(OptimizationEventType.CANCELLED)
                .description("Optimization was cancelled")
                .build());
        touch();
    }

    public void addParameter(String key, Object value) {
        this.parameters.put(key, value);
        touch();
    }

    public <T> T getParameter(String key, Class<T> type) {
        Object value = parameters.get(key);
        if (value == null) {
            return null;
        }
        return type.cast(value);
    }

    public void addEvent(OptimizationEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        events.add(event);
        touch();
    }

    public long getDurationMs() {
        if (completedAt == null) {
            return Duration.between(createdAt, Instant.now()).toMillis();
        }
        return Duration.between(createdAt, completedAt).toMillis();
    }

    public boolean isCompleted() {
        return status == OptimizationStatus.COMPLETED;
    }

    public boolean isFailed() {
        return status == OptimizationStatus.FAILED;
    }

    public boolean isProcessing() {
        return status == OptimizationStatus.IN_PROGRESS;
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OptimizationRequest that = (OptimizationRequest) o;
        return Objects.equals(requestId, that.requestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId);
    }

    public static class Builder {
        private UUID requestId;
        private String tenantId;
        private OptimizationType type;
        private Map<String, Object> parameters;
        private int priority = 5;

        public Builder requestId(UUID requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder type(OptimizationType type) {
            this.type = type;
            return this;
        }

        public Builder parameters(Map<String, Object> parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public OptimizationRequest build() {
            return new OptimizationRequest(this);
        }
    }
}
