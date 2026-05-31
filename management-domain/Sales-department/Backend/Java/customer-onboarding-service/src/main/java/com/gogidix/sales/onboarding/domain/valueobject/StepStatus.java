package com.gogidix.sales.onboarding.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Step Status Value Object
 * Defines the current status of an onboarding step
 */
public enum StepStatus {
    PENDING("Pending", "Step is pending"),
    IN_PROGRESS("In Progress", "Step is in progress"),
    COMPLETED("Completed", "Step is completed"),
    SKIPPED("Skipped", "Step was skipped"),
    FAILED("Failed", "Step failed");

    private final String value;
    private final String description;

    StepStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static StepStatus fromValue(String value) {
        for (StepStatus status : StepStatus.values()) {
            if (status.value.equalsIgnoreCase(value) || status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown StepStatus: " + value);
    }

    public boolean isTerminal() {
        return this == COMPLETED || this == SKIPPED || this == FAILED;
    }

    public boolean isActive() {
        return this == IN_PROGRESS;
    }
}
