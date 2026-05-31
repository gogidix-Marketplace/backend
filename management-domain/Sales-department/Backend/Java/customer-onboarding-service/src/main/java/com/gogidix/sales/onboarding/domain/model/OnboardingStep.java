package com.gogidix.sales.onboarding.domain.model;

import com.gogidix.sales.onboarding.domain.valueobject.StepStatus;
import com.gogidix.sales.onboarding.domain.valueobject.StepType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Onboarding Step Domain Entity
 * Represents a single step in the onboarding process
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingStep {

    private String stepId;

    private String onboardingId;

    private String name;

    private String description;

    private StepType stepType;

    private StepStatus status;

    private Integer order;

    private Boolean optional;

    private Boolean autoComplete;

    private String assignedTo;

    private String assignedBy;

    private Instant startedAt;

    private Instant completedAt;

    private Long durationMinutes;

    private String completedBy;

    private String updatedBy;

    private Instant updatedAt;

    private List<String> dependencies;

    private String notes;

    private String templateStepId;

    private Integer estimatedDurationMinutes;

    private String helpUrl;

    private List<String> requiredFields;

    /**
     * Starts the step
     */
    public void start(String startedBy) {
        if (this.status != StepStatus.PENDING) {
            throw new IllegalStateException("Step is not in PENDING status");
        }
        this.status = StepStatus.IN_PROGRESS;
        this.startedAt = Instant.now();
        this.updatedBy = startedBy;
        this.updatedAt = Instant.now();
    }

    /**
     * Completes the step
     */
    public void complete(String completedBy, String notes) {
        if (this.status != StepStatus.IN_PROGRESS) {
            throw new IllegalStateException("Step is not in progress");
        }
        this.status = StepStatus.COMPLETED;
        this.completedAt = Instant.now();
        this.completedBy = completedBy;
        this.updatedBy = completedBy;
        this.updatedAt = Instant.now();
        this.notes = notes;

        if (this.startedAt != null) {
            this.durationMinutes = java.time.temporal.ChronoUnit.MINUTES.between(
                    this.startedAt, this.completedAt);
        }
    }

    /**
     * Fails the step
     */
    public void fail(String failedBy, String reason) {
        this.status = StepStatus.FAILED;
        this.updatedBy = failedBy;
        this.updatedAt = Instant.now();
        this.notes = reason;
    }

    /**
     * Checks if the step is ready to be started
     */
    public boolean isReadyToStart() {
        return this.status == StepStatus.PENDING;
    }

    /**
     * Checks if the step can be auto-completed
     */
    public boolean canAutoComplete() {
        return this.autoComplete != null && this.autoComplete;
    }
}
