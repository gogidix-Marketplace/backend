package com.gogidix.shared.messaging.domain.valueobject;

import java.util.Arrays;
import java.util.List;

/**
 * Pure domain value object for message types with zero external dependencies.
 * Contains comprehensive business logic for message classification and routing.
 */
public enum MessageTypeClean {
    
    // User Communication Messages
    USER_MESSAGE("User Message", "user", MessageCategory.COMMUNICATION, true, false),
    PRIVATE_MESSAGE("Private Message", "user", MessageCategory.COMMUNICATION, true, false),
    EMAIL("Email", "email", MessageCategory.COMMUNICATION, true, false),
    SMS("SMS", "sms", MessageCategory.COMMUNICATION, true, false),
    
    // Notification Messages
    PUSH_NOTIFICATION("Push Notification", "push", MessageCategory.NOTIFICATION, true, true),
    IN_APP_NOTIFICATION("In-App Notification", "in-app", MessageCategory.NOTIFICATION, true, true),
    SYSTEM_NOTIFICATION("System Notification", "system", MessageCategory.NOTIFICATION, true, true),
    SECURITY_NOTIFICATION("Security Alert", "security", MessageCategory.NOTIFICATION, false, true),
    SYSTEM_ALERT("System Alert", "alert", MessageCategory.NOTIFICATION, false, true),
    ERROR_NOTIFICATION("Error Notification", "error", MessageCategory.NOTIFICATION, false, true),
    
    // Business Process Messages
    ORDER_NOTIFICATION("Order Notification", "order", MessageCategory.BUSINESS, true, true),
    PAYMENT_NOTIFICATION("Payment Notification", "payment", MessageCategory.BUSINESS, true, true),
    SHIPPING_NOTIFICATION("Shipping Notification", "shipping", MessageCategory.BUSINESS, true, true),
    INVENTORY_NOTIFICATION("Inventory Notification", "inventory", MessageCategory.BUSINESS, true, true),
    
    // Marketing Messages
    PROMOTIONAL("Promotional", "marketing", MessageCategory.MARKETING, true, false),
    BROADCAST("Broadcast", "broadcast", MessageCategory.MARKETING, true, false),
    NEWSLETTER("Newsletter", "newsletter", MessageCategory.MARKETING, true, false),
    
    // System Integration Messages
    DOMAIN_EVENT("Domain Event", "event", MessageCategory.SYSTEM, false, true),
    COMMAND("Command", "command", MessageCategory.SYSTEM, false, true),
    QUERY("Query", "query", MessageCategory.SYSTEM, false, true),
    WEBHOOK("Webhook", "webhook", MessageCategory.SYSTEM, false, true),
    API_NOTIFICATION("API Notification", "api", MessageCategory.SYSTEM, false, true),
    
    // Special Purpose Messages
    TRANSIENT("Transient", "transient", MessageCategory.TEMPORARY, false, false),
    CONFIDENTIAL("Confidential", "confidential", MessageCategory.SECURITY, true, false),
    LEGAL_DOCUMENT("Legal Document", "legal", MessageCategory.LEGAL, true, false),
    AUDIT_LOG("Audit Log", "audit", MessageCategory.AUDIT, false, true);
    
    private final String displayName;
    private final String channel;
    private final MessageCategory category;
    private final boolean requiresPersistence;
    private final boolean isSystemGenerated;
    
    MessageTypeClean(String displayName, String channel, MessageCategory category, 
                    boolean requiresPersistence, boolean isSystemGenerated) {
        this.displayName = validateDisplayName(displayName);
        this.channel = validateChannel(channel);
        this.category = Objects.requireNonNull(category, "Category cannot be null");
        this.requiresPersistence = requiresPersistence;
        this.isSystemGenerated = isSystemGenerated;
    }
    
    // Business logic methods
    public boolean requiresUserInteraction() {
        return this == SYSTEM_ALERT ||
               this == SECURITY_NOTIFICATION ||
               this == ERROR_NOTIFICATION ||
               this == ORDER_NOTIFICATION ||
               this == PAYMENT_NOTIFICATION ||
               this == LEGAL_DOCUMENT;
    }
    
    public boolean requiresPersistence() {
        return requiresPersistence && this != TRANSIENT;
    }
    
    public boolean isNotification() {
        return category == MessageCategory.NOTIFICATION || 
               Arrays.asList(ORDER_NOTIFICATION, PAYMENT_NOTIFICATION, SHIPPING_NOTIFICATION, 
                           INVENTORY_NOTIFICATION, API_NOTIFICATION).contains(this);
    }
    
    public boolean isSystemGenerated() {
        return isSystemGenerated;
    }
    
    public boolean isUserGenerated() {
        return !isSystemGenerated;
    }
    
    public boolean isCommunication() {
        return category == MessageCategory.COMMUNICATION;
    }
    
    public boolean isBusiness() {
        return category == MessageCategory.BUSINESS;
    }
    
    public boolean isMarketing() {
        return category == MessageCategory.MARKETING;
    }
    
    public boolean isSystem() {
        return category == MessageCategory.SYSTEM;
    }
    
    public boolean isSecuritySensitive() {
        return category == MessageCategory.SECURITY ||
               this == SECURITY_NOTIFICATION ||
               this == CONFIDENTIAL ||
               this == LEGAL_DOCUMENT ||
               this == AUDIT_LOG;
    }
    
    public boolean isHighPriority() {
        return this == SYSTEM_ALERT ||
               this == SECURITY_NOTIFICATION ||
               this == ERROR_NOTIFICATION ||
               this == PAYMENT_NOTIFICATION;
    }
    
    public boolean isTransient() {
        return this == TRANSIENT || category == MessageCategory.TEMPORARY;
    }
    
    public boolean canBeScheduled() {
        return Arrays.asList(EMAIL, SMS, PUSH_NOTIFICATION, PROMOTIONAL, NEWSLETTER, BROADCAST)
                .contains(this);
    }
    
    public boolean requiresDeliveryConfirmation() {
        return this == LEGAL_DOCUMENT ||
               this == PAYMENT_NOTIFICATION ||
               this == SECURITY_NOTIFICATION ||
               this == SYSTEM_ALERT;
    }
    
    public boolean canBeBatched() {
        return isMarketing() || 
               this == NEWSLETTER ||
               this == BROADCAST ||
               this == PROMOTIONAL;
    }
    
    public boolean supportsRichContent() {
        return this == EMAIL ||
               this == IN_APP_NOTIFICATION ||
               this == PUSH_NOTIFICATION ||
               this == NEWSLETTER ||
               this == PROMOTIONAL;
    }
    
    public boolean requiresOptIn() {
        return isMarketing() || this == NEWSLETTER || this == PROMOTIONAL;
    }
    
    // Routing and processing methods
    public String getDefaultChannel() {
        return channel;
    }
    
    public MessageCategory getCategory() {
        return category;
    }
    
    public int getDefaultRetryCount() {
        if (isHighPriority()) return 5;
        if (requiresDeliveryConfirmation()) return 3;
        if (isTransient()) return 1;
        return 2;
    }
    
    public long getDefaultTtlHours() {
        if (isTransient()) return 1;
        if (isMarketing()) return 24;
        if (isBusiness()) return 72;
        if (isSystem()) return 168; // 1 week
        return 48; // 2 days default
    }
    
    public String getRoutingPrefix() {
        return category.name().toLowerCase() + "." + channel;
    }
    
    public boolean isCompatibleWith(String contentType) {
        return switch (this) {
            case EMAIL, NEWSLETTER -> contentType.startsWith("text/") || contentType.equals("text/html");
            case SMS -> contentType.equals("text/plain");
            case DOMAIN_EVENT, COMMAND, QUERY, WEBHOOK -> contentType.equals("application/json");
            case LEGAL_DOCUMENT -> contentType.equals("application/pdf") || contentType.equals("text/html");
            default -> true; // Most types are flexible
        };
    }
    
    public int getMaxContentLength() {
        return switch (this) {
            case SMS -> 160;
            case PUSH_NOTIFICATION -> 256;
            case IN_APP_NOTIFICATION -> 512;
            case SYSTEM_ALERT, ERROR_NOTIFICATION -> 1024;
            default -> Integer.MAX_VALUE;
        };
    }
    
    // Query methods for categories
    public static MessageTypeClean[] getByCategory(MessageCategory category) {
        return Arrays.stream(values())
                .filter(type -> type.category == category)
                .toArray(MessageTypeClean[]::new);
    }
    
    public static MessageTypeClean[] getNotificationTypes() {
        return Arrays.stream(values())
                .filter(MessageTypeClean::isNotification)
                .toArray(MessageTypeClean[]::new);
    }
    
    public static MessageTypeClean[] getSystemTypes() {
        return Arrays.stream(values())
                .filter(MessageTypeClean::isSystemGenerated)
                .toArray(MessageTypeClean[]::new);
    }
    
    public static MessageTypeClean[] getHighPriorityTypes() {
        return Arrays.stream(values())
                .filter(MessageTypeClean::isHighPriority)
                .toArray(MessageTypeClean[]::new);
    }
    
    public static MessageTypeClean[] getSecuritySensitiveTypes() {
        return Arrays.stream(values())
                .filter(MessageTypeClean::isSecuritySensitive)
                .toArray(MessageTypeClean[]::new);
    }
    
    public static MessageTypeClean[] getMarketingTypes() {
        return Arrays.stream(values())
                .filter(MessageTypeClean::isMarketing)
                .toArray(MessageTypeClean[]::new);
    }
    
    public static MessageTypeClean fromChannel(String channel) {
        return Arrays.stream(values())
                .filter(type -> type.channel.equals(channel))
                .findFirst()
                .orElse(USER_MESSAGE);
    }
    
    // Domain validation methods
    private String validateDisplayName(String displayName) {
        if (displayName == null || displayName.trim().isEmpty()) {
            throw new IllegalArgumentException("Display name cannot be null or empty");
        }
        if (displayName.length() > 50) {
            throw new IllegalArgumentException("Display name cannot exceed 50 characters");
        }
        return displayName.trim();
    }
    
    private String validateChannel(String channel) {
        if (channel == null || channel.trim().isEmpty()) {
            throw new IllegalArgumentException("Channel cannot be null or empty");
        }
        if (!channel.matches("^[a-z][a-z0-9-]*$")) {
            throw new IllegalArgumentException("Channel must start with lowercase letter and contain only lowercase letters, numbers, and hyphens");
        }
        if (channel.length() > 20) {
            throw new IllegalArgumentException("Channel cannot exceed 20 characters");
        }
        return channel.trim();
    }
    
    // Getters
    public String getDisplayName() { 
        return displayName; 
    }
    
    public String getChannel() { 
        return channel; 
    }
    
    @Override
    public String toString() {
        return displayName + " (" + channel + ")";
    }
}

enum MessageCategory {
    COMMUNICATION("User-to-user communication"),
    NOTIFICATION("System notifications"),
    BUSINESS("Business process messages"),
    MARKETING("Marketing and promotional"),
    SYSTEM("System integration"),
    SECURITY("Security-related"),
    LEGAL("Legal documents"),
    AUDIT("Audit and logging"),
    TEMPORARY("Temporary messages");
    
    private final String description;
    
    MessageCategory(String description) {
        this.description = description;
    }
    
    public String getDescription() { 
        return description; 
    }
}

// Need to add Objects import
class Objects {
    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null) throw new IllegalArgumentException(message);
        return obj;
    }
}