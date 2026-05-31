package com.gogidix.shared.utilities.domain.valueobject;

/**
 * Enumeration of processing statuses for utility operations
 */
public enum ProcessingStatus {
    
    PENDING("Operation is pending execution"),
    PROCESSING("Operation is currently being processed"),
    IN_PROGRESS("Operation is currently being processed"),
    SUCCESS("Operation completed successfully"),
    SUCCESS_WITH_WARNINGS("Operation completed successfully but with warnings"),
    FAILED("Operation failed due to an error"),
    CANCELLED("Operation was cancelled before completion"),
    TIMEOUT("Operation timed out during processing"),
    PARTIAL_SUCCESS("Operation partially succeeded");
    
    private final String description;
    
    ProcessingStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Checks if the status represents a successful operation
     */
    public boolean isSuccessful() {
        return this == SUCCESS || this == SUCCESS_WITH_WARNINGS || this == PARTIAL_SUCCESS;
    }
    
    /**
     * Checks if the status represents a failed operation
     */
    public boolean isFailed() {
        return this == FAILED || this == CANCELLED || this == TIMEOUT;
    }
    
    /**
     * Checks if the status represents an ongoing operation
     */
    public boolean isOngoing() {
        return this == PENDING || this == IN_PROGRESS;
    }
    
    /**
     * Checks if the status represents a completed operation (success or failure)
     */
    public boolean isCompleted() {
        return !isOngoing();
    }
    
    /**
     * Gets the severity level of the status
     */
    public String getSeverity() {
        return switch (this) {
            case SUCCESS -> "info";
            case SUCCESS_WITH_WARNINGS, PARTIAL_SUCCESS -> "warning";
            case FAILED, TIMEOUT -> "error";
            case CANCELLED -> "warning";
            case PENDING, PROCESSING, IN_PROGRESS -> "info";
        };
    }
}