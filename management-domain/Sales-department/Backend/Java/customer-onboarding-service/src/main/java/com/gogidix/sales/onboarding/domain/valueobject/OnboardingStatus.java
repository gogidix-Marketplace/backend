package com.gogidix.sales.onboarding.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Onboarding Status Value Object
 * Defines the current status of the onboarding process
 */
public enum OnboardingStatus {
    NOT_STARTED("Not Started", "Onboarding process has not started"),
    IN_PROGRESS("In Progress", "Onboarding process is in progress"),
    PENDING_REVIEW("Pending Review", "Onboarding is pending review"),
    COMPLETED("Completed", "Onboarding process is completed"),
    ON_HOLD("On Hold", "Onboarding process is on hold"),
    CANCELLED("Cancelled", "Onboarding process was cancelled");

    private final String value;
    private final String description;

    OnboardingStatus(String value, String description) {
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
    public static OnboardingStatus fromValue(String value) {
        for (OnboardingStatus status : OnboardingStatus.values()) {
            if (status.value.equalsIgnoreCase(value) || status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown OnboardingStatus: " + value);
    }

    public boolean isTerminal() {
        return this == COMPLETED || this == CANCELLED;
    }

    public boolean isActive() {
        return this == IN_PROGRESS || this == PENDING_REVIEW;
    }
}
