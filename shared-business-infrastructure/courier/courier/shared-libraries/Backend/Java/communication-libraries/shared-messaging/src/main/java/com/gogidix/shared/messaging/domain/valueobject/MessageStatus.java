package com.gogidix.shared.messaging.domain.valueobject;

/**
 * Enumeration of message status values
 */
public enum MessageStatus {
    
    /** Message created but not yet sent */
    PENDING("Pending"),
    
    /** Message is being processed for sending */
    PROCESSING("Processing"),
    
    /** Message has been sent successfully */
    SENT("Sent"),
    
    /** Message has been delivered to recipient */
    DELIVERED("Delivered"),
    
    /** Message has been read by recipient */
    READ("Read"),
    
    /** Message sending failed */
    FAILED("Failed"),
    
    /** Message has been cancelled */
    CANCELLED("Cancelled"),
    
    /** Message has been archived */
    ARCHIVED("Archived"),
    
    /** Message has expired */
    EXPIRED("Expired");
    
    private final String displayName;
    
    MessageStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Determines if this status indicates a final state
     */
    public boolean isFinalState() {
        return this == READ ||
               this == FAILED ||
               this == CANCELLED ||
               this == ARCHIVED ||
               this == EXPIRED;
    }
    
    /**
     * Determines if this status indicates success
     */
    public boolean isSuccess() {
        return this == SENT ||
               this == DELIVERED ||
               this == READ;
    }
    
    /**
     * Determines if this status indicates failure
     */
    public boolean isFailure() {
        return this == FAILED ||
               this == CANCELLED ||
               this == EXPIRED;
    }
    
    /**
     * Determines if message can be retried from this status
     */
    public boolean canRetry() {
        return this == FAILED;
    }
    
    /**
     * Gets the next expected status in the message lifecycle
     */
    public MessageStatus getNextStatus() {
        return switch (this) {
            case PENDING -> PROCESSING;
            case PROCESSING -> SENT;
            case SENT -> DELIVERED;
            case DELIVERED -> READ;
            default -> this; // Final states remain unchanged
        };
    }
}