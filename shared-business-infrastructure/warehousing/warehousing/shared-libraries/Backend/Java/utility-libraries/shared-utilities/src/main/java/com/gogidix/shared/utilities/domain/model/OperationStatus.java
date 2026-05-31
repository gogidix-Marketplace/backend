package com.gogidix.shared.utilities.domain.model;

/**
 * Enumeration of utility operation statuses
 */
public enum OperationStatus {
    
    /** Operation is queued for processing */
    QUEUED("Queued"),
    
    /** Operation is currently running */
    RUNNING("Running"),
    
    /** Operation completed successfully */
    COMPLETED("Completed"),
    
    /** Operation failed */
    FAILED("Failed"),
    
    /** Operation encountered an error */
    ERROR("Error"),
    
    /** Operation was cancelled */
    CANCELLED("Cancelled"),
    
    /** Operation timed out */
    TIMEOUT("Timeout"),
    
    /** Operation is paused */
    PAUSED("Paused"),
    
    /** Operation is retrying after failure */
    RETRYING("Retrying");
    
    private final String displayName;
    
    OperationStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Determines if this status indicates the operation is active
     */
    public boolean isActive() {
        return this == RUNNING || this == RETRYING;
    }
    
    /**
     * Determines if this status indicates the operation is pending
     */
    public boolean isPending() {
        return this == QUEUED || this == PAUSED;
    }
    
    /**
     * Determines if this status indicates the operation is finished
     */
    public boolean isFinished() {
        return this == COMPLETED || this == FAILED || this == ERROR || 
               this == CANCELLED || this == TIMEOUT;
    }
    
    /**
     * Determines if this status indicates success
     */
    public boolean isSuccess() {
        return this == COMPLETED;
    }
    
    /**
     * Determines if this status indicates failure
     */
    public boolean isFailure() {
        return this == FAILED || this == ERROR || this == TIMEOUT;
    }
    
    /**
     * Determines if the operation can be retried from this status
     */
    public boolean canRetry() {
        return this == FAILED || this == ERROR || this == TIMEOUT;
    }
    
    /**
     * Determines if the operation can be cancelled from this status
     */
    public boolean canCancel() {
        return this == QUEUED || this == RUNNING || this == PAUSED || this == RETRYING;
    }
    
    /**
     * Determines if the operation can be paused from this status
     */
    public boolean canPause() {
        return this == RUNNING || this == QUEUED;
    }
    
    /**
     * Determines if the operation can be resumed from this status
     */
    public boolean canResume() {
        return this == PAUSED;
    }
    
    /**
     * Gets the next expected status in normal operation flow
     */
    public OperationStatus getNextExpectedStatus() {
        return switch (this) {
            case QUEUED -> RUNNING;
            case RUNNING -> COMPLETED;
            case PAUSED -> RUNNING;
            case RETRYING -> RUNNING;
            default -> this; // Terminal states remain unchanged
        };
    }
    
    /**
     * Determines if transition to the target status is allowed
     */
    public boolean canTransitionTo(OperationStatus targetStatus) {
        return switch (this) {
            case QUEUED -> targetStatus == RUNNING || 
                          targetStatus == CANCELLED ||
                          targetStatus == PAUSED;
            
            case RUNNING -> targetStatus == COMPLETED ||
                           targetStatus == FAILED ||
                           targetStatus == ERROR ||
                           targetStatus == CANCELLED ||
                           targetStatus == TIMEOUT ||
                           targetStatus == PAUSED;
            
            case PAUSED -> targetStatus == RUNNING ||
                          targetStatus == CANCELLED;
            
            case FAILED, ERROR, TIMEOUT -> targetStatus == RETRYING ||
                                          targetStatus == CANCELLED;
            
            case RETRYING -> targetStatus == RUNNING ||
                            targetStatus == CANCELLED;
            
            case COMPLETED, CANCELLED -> false; // Terminal states
        };
    }
    
    /**
     * Gets the log level for this status
     */
    public String getLogLevel() {
        return switch (this) {
            case QUEUED, RUNNING, PAUSED, RETRYING -> "INFO";
            case COMPLETED -> "INFO";
            case FAILED, ERROR, TIMEOUT -> "ERROR";
            case CANCELLED -> "WARN";
        };
    }
}