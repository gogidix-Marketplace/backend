package com.gogidix.shared.messaging.domain.model;

/**
 * Message type enumeration for messaging system
 */
public enum MessageType {
    EMAIL("Email message"),
    SMS("SMS message"),
    PUSH_NOTIFICATION("Push notification"),
    IN_APP_MESSAGE("In-app message"),
    WEBHOOK("Webhook message"),
    SYSTEM_ALERT("System alert message"),
    MARKETING("Marketing message"),
    TRANSACTION("Transaction message"),
    AUDIT("Audit message"),
    EVENT("Event message");

    private final String description;

    MessageType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRealTime() {
        return this == PUSH_NOTIFICATION || 
               this == IN_APP_MESSAGE || 
               this == SYSTEM_ALERT ||
               this == WEBHOOK;
    }

    public boolean requiresDeliveryConfirmation() {
        return this == EMAIL || 
               this == SMS || 
               this == TRANSACTION ||
               this == AUDIT;
    }

    public int getMaxRetryAttempts() {
        return switch (this) {
            case SYSTEM_ALERT, TRANSACTION, AUDIT -> 5;
            case EMAIL, SMS -> 3;
            case PUSH_NOTIFICATION, WEBHOOK -> 2;
            default -> 1;
        };
    }
}