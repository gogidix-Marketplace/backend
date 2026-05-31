package com.gogidix.shared.messaging.domain.valueobject;

/**
 * Classification of messages for routing and processing
 */
public enum MessageClassification {
    
    /** Standard messages */
    STANDARD("Standard"),
    
    /** High security messages requiring encryption */
    HIGH_SECURITY("High Security"),
    
    /** Broadcast messages to multiple recipients */
    BROADCAST("Broadcast"),
    
    /** Reply messages in a thread */
    REPLY("Reply"),
    
    /** Domain events */
    DOMAIN_EVENT("Domain Event"),
    
    /** Command messages */
    COMMAND("Command"),
    
    /** Query messages */
    QUERY("Query");
    
    private final String displayName;
    
    MessageClassification(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Determines if this classification requires special handling
     */
    public boolean requiresSpecialHandling() {
        return this == HIGH_SECURITY ||
               this == DOMAIN_EVENT ||
               this == COMMAND;
    }
    
    /**
     * Determines priority weight for this classification
     */
    public int getPriorityWeight() {
        return switch (this) {
            case HIGH_SECURITY, COMMAND -> 10;
            case DOMAIN_EVENT -> 8;
            case QUERY -> 5;
            case REPLY -> 3;
            default -> 1;
        };
    }
}
