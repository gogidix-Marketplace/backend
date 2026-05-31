package com.gogidix.shared.messaging.domain.valueobject;

/**
 * Value Object representing message priority levels
 * Part of proper hexagonal architecture value object layer
 */
public enum MessagePriority {
    
    /** Low priority messages */
    LOW("Low", 1, 10),
    
    /** Normal priority messages */
    NORMAL("Normal", 2, 25),
    
    /** High priority messages */
    HIGH("High", 3, 50),
    
    /** Urgent messages requiring immediate attention */
    URGENT("Urgent", 4, 80),
    
    /** Critical system messages */
    CRITICAL("Critical", 5, 95);
    
    private final String displayName;
    private final int level;
    private final int score;
    
    MessagePriority(String displayName, int level, int score) {
        this.displayName = displayName;
        this.level = level;
        this.score = score;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getLevel() {
        return level;
    }
    
    public int getScore() {
        return score;
    }
    
    /**
     * Determines if this priority requires immediate processing
     */
    public boolean requiresImmediateProcessing() {
        return this == URGENT || this == CRITICAL;
    }
    
    /**
     * Gets the processing delay in milliseconds
     */
    public long getProcessingDelayMs() {
        return switch (this) {
            case CRITICAL -> 0; // Process immediately
            case URGENT -> 0; // Process immediately
            case HIGH -> 1000; // 1 second delay
            case NORMAL -> 5000; // 5 second delay
            case LOW -> 30000; // 30 second delay
        };
    }
    
    /**
     * Gets the retry interval in milliseconds
     */
    public long getRetryIntervalMs() {
        return switch (this) {
            case CRITICAL -> 15000; // 15 seconds
            case URGENT -> 30000; // 30 seconds
            case HIGH -> 60000; // 1 minute
            case NORMAL -> 300000; // 5 minutes
            case LOW -> 900000; // 15 minutes
        };
    }
    
    /**
     * Determines if this priority should bypass rate limiting
     */
    public boolean bypassRateLimit() {
        return this == URGENT || this == CRITICAL;
    }
    
    /**
     * Compares priority levels
     */
    public boolean isHigherThan(MessagePriority other) {
        return this.level > other.level;
    }
}
