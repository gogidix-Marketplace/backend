package com.gogidix.aiservices.supplychainoptimizationservice.domain.model;

import lombok.Getter;

@Getter
public enum OptimizationStatus {
    PENDING("pending"),
    IN_PROGRESS("in_progress"),
    COMPLETED("completed"),
    FAILED("failed"),
    CANCELLED("cancelled");

    private final String value;

    OptimizationStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static OptimizationStatus fromString(String value) {
        for (OptimizationStatus status : OptimizationStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown optimization status: " + value);
    }

    public boolean isTerminal() {
        return this == COMPLETED || this == FAILED || this == CANCELLED;
    }
}
