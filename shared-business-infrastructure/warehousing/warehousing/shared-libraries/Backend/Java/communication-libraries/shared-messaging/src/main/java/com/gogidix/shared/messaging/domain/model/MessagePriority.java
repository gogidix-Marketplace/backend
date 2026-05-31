package com.gogidix.shared.messaging.domain.model;

/**
 * Message priority levels for message processing and delivery
 */
public enum MessagePriority {
    URGENT(1, "Urgent - immediate delivery required"),
    HIGH(2, "High priority message"),
    NORMAL(3, "Normal priority message"),
    LOW(4, "Low priority message"),
    BULK(5, "Bulk message - can be delayed");

    private final int level;
    private final String description;

    MessagePriority(int level, String description) {
        this.level = level;
        this.description = description;
    }

    public int getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    public boolean isHighPriority() {
        return this == URGENT || this == HIGH;
    }

    public long getMaxDelaySeconds() {
        return switch (this) {
            case URGENT -> 0; // immediate
            case HIGH -> 30;
            case NORMAL -> 300; // 5 minutes
            case LOW -> 1800; // 30 minutes
            case BULK -> 3600; // 1 hour
        };
    }

    public int getRetryIntervalSeconds() {
        return switch (this) {
            case URGENT -> 5;
            case HIGH -> 30;
            case NORMAL -> 60;
            case LOW -> 300;
            case BULK -> 900;
        };
    }
}