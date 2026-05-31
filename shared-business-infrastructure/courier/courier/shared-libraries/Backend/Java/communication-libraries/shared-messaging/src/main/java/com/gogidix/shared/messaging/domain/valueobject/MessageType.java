package com.gogidix.shared.messaging.domain.valueobject;

/**
 * Enumeration of message types in the system
 */
public enum MessageType {
    
    /** Email messages */
    EMAIL("Email"),
    
    /** SMS text messages */
    SMS("SMS"),
    
    /** Push notifications */
    PUSH_NOTIFICATION("Push Notification"),
    
    /** In-app notifications */
    IN_APP_NOTIFICATION("In-App Notification"),
    
    /** System notifications */
    SYSTEM_NOTIFICATION("System Notification"),
    
    /** Security alerts */
    SECURITY_NOTIFICATION("Security Alert"),
    
    /** System alerts */
    SYSTEM_ALERT("System Alert"),
    
    /** Broadcast messages */
    BROADCAST("Broadcast"),
    
    /** Private messages between users */
    PRIVATE_MESSAGE("Private Message"),
    
    /** User-generated messages */
    USER_MESSAGE("User Message"),
    
    /** Domain events */
    DOMAIN_EVENT("Domain Event"),
    
    /** Confidential messages */
    CONFIDENTIAL("Confidential"),
    
    /** Legal documents */
    LEGAL_DOCUMENT("Legal Document"),
    
    /** Financial messages */
    FINANCIAL_MESSAGE("Financial Message"),
    
    /** Order notifications */
    ORDER_NOTIFICATION("Order Notification"),
    
    /** Payment notifications */
    PAYMENT_NOTIFICATION("Payment Notification"),
    
    /** Shipping notifications */
    SHIPPING_NOTIFICATION("Shipping Notification"),
    
    /** Promotional messages */
    PROMOTIONAL("Promotional"),
    
    /** Transient messages that shouldn't be persisted */
    TRANSIENT("Transient"),
    
    /** Webhook notifications */
    WEBHOOK("Webhook"),
    
    /** API notifications */
    API_NOTIFICATION("API Notification"),
    
    /** Error notifications */
    ERROR_NOTIFICATION("Error Notification"),
    
    /** Command messages */
    COMMAND("Command"),
    
    /** Query messages */
    QUERY("Query");
    
    private final String displayName;
    
    MessageType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Determines if this message type requires user interaction
     */
    public boolean requiresUserInteraction() {
        return this == SYSTEM_ALERT ||
               this == SECURITY_NOTIFICATION ||
               this == ORDER_NOTIFICATION ||
               this == PAYMENT_NOTIFICATION;
    }
    
    /**
     * Determines if this message type should be persisted
     */
    public boolean shouldPersist() {
        return this != TRANSIENT;
    }
    
    /**
     * Determines if this message type requires persistence (alias for shouldPersist)
     */
    public boolean requiresPersistence() {
        return shouldPersist();
    }
    
    /**
     * Determines if this message type is a notification
     */
    public boolean isNotification() {
        return this == PUSH_NOTIFICATION ||
               this == IN_APP_NOTIFICATION ||
               this == SYSTEM_NOTIFICATION ||
               this == SECURITY_NOTIFICATION ||
               this == ORDER_NOTIFICATION ||
               this == PAYMENT_NOTIFICATION ||
               this == SHIPPING_NOTIFICATION ||
               this == API_NOTIFICATION;
    }
    
    /**
     * Determines if this message type is system-generated
     */
    public boolean isSystemGenerated() {
        return this == SYSTEM_NOTIFICATION ||
               this == SYSTEM_ALERT ||
               this == SECURITY_NOTIFICATION ||
               this == ORDER_NOTIFICATION ||
               this == PAYMENT_NOTIFICATION ||
               this == SHIPPING_NOTIFICATION ||
               this == WEBHOOK ||
               this == API_NOTIFICATION;
    }
    
    /**
     * Gets the default channel for this message type
     */
    public String getDefaultChannel() {
        return switch (this) {
            case EMAIL -> "email";
            case SMS -> "sms";
            case PUSH_NOTIFICATION -> "push";
            case IN_APP_NOTIFICATION -> "in-app";
            case WEBHOOK -> "webhook";
            default -> "in-app";
        };
    }
}