package com.gogidix.shared.utilities.domain.model;

/**
 * Enumeration of utility operation priorities
 */
public enum OperationPriority {
    
    /** Low priority operations */
    LOW("Low", 1),
    
    /** Normal priority operations */
    NORMAL("Normal", 2),
    
    /** High priority operations */
    HIGH("High", 3),
    
    /** Critical priority operations */
    CRITICAL("Critical", 4);
    
    private final String displayName;
    private final int level;
    
    OperationPriority(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getLevel() {
        return level;
    }
    
    /**
     * Determines if this priority is higher than another priority
     */
    public boolean isHigherThan(OperationPriority other) {
        return this.level > other.level;
    }
    
    /**
     * Determines if this priority requires immediate processing
     */
    public boolean requiresImmediateProcessing() {
        return this == HIGH || this == CRITICAL;
    }
    
    /**
     * Gets the processing delay in milliseconds for this priority
     */
    public long getProcessingDelayMs() {
        return switch (this) {
            case CRITICAL -> 0; // Process immediately
            case HIGH -> 100; // 100ms delay
            case NORMAL -> 1000; // 1 second delay
            case LOW -> 5000; // 5 second delay
        };
    }
    
    /**
     * Gets the queue position weight for this priority
     */
    public int getQueueWeight() {
        return switch (this) {
            case CRITICAL -> 1000;
            case HIGH -> 100;
            case NORMAL -> 10;
            case LOW -> 1;
        };
    }
    
    /**
     * Gets the timeout multiplier for this priority
     */
    public double getTimeoutMultiplier() {
        return switch (this) {
            case CRITICAL -> 2.0; // Double timeout for critical operations
            case HIGH -> 1.5; // 50% more timeout
            case NORMAL -> 1.0; // Standard timeout
            case LOW -> 0.8; // 20% less timeout
        };
    }
    
    /**
     * Gets the retry count for this priority
     */
    public int getRetryCount() {
        return switch (this) {
            case CRITICAL -> 5; // More retries for critical operations
            case HIGH -> 3; // Standard retries
            case NORMAL -> 2; // Fewer retries
            case LOW -> 1; // Minimal retries
        };
    }
    
    /**
     * Determines if this priority allows resource preemption
     */
    public boolean allowsPreemption() {
        return this == CRITICAL;
    }
    
    /**
     * Gets the resource allocation percentage for this priority
     */
    public int getResourceAllocationPercentage() {
        return switch (this) {
            case CRITICAL -> 80; // 80% of available resources
            case HIGH -> 60; // 60% of available resources
            case NORMAL -> 40; // 40% of available resources
            case LOW -> 20; // 20% of available resources
        };
    }
    
    /**
     * Determines if operations of this priority should be logged
     */
    public boolean shouldLog() {
        return true; // All priorities should be logged
    }
    
    /**
     * Gets the log level for operations of this priority
     */
    public String getLogLevel() {
        return switch (this) {
            case CRITICAL -> "ERROR"; // Critical operations logged as errors for visibility
            case HIGH -> "WARN"; // High priority operations as warnings
            case NORMAL, LOW -> "INFO"; // Normal operations as info
        };
    }
}