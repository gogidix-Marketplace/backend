package com.gogidix.aiservices.aidocumentprocessingservice.domain.model;

public enum ProcessingStatus {
    PENDING(false, false),
    PROCESSING(true, false),
    VALIDATING(true, false),
    COMPLETED(false, true),
    FAILED(false, true);

    private final boolean active;
    private final boolean terminal;

    ProcessingStatus(boolean active, boolean terminal) {
        this.active = active;
        this.terminal = terminal;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public boolean isFinal() {
        return this == COMPLETED || this == FAILED;
    }

    public boolean canTransitionTo(ProcessingStatus newStatus) {
        return switch (this) {
            case PENDING -> newStatus == PROCESSING;
            case PROCESSING -> newStatus == COMPLETED || newStatus == FAILED || newStatus == VALIDATING;
            case VALIDATING -> newStatus == COMPLETED || newStatus == FAILED;
            case COMPLETED, FAILED -> false;
        };
    }

    public static ProcessingStatus fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return PENDING;
        }

        try {
            return ProcessingStatus.valueOf(value.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return PENDING;
        }
    }
}
