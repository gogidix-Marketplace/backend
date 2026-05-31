package com.gogidix.centralconfiguration.notificationservice.domain.model;

/**
 * Notification Type enumeration.
 */
public enum NotificationType {
    CONFIG_CREATED("CONFIG_CREATED", "Configuration created"),
    CONFIG_UPDATED("CONFIG_UPDATED", "Configuration updated"),
    CONFIG_DELETED("CONFIG_DELETED", "Configuration deleted"),
    FEATURE_FLAG_TOGGLED("FEATURE_FLAG_TOGGLED", "Feature flag toggled"),
    ENVIRONMENT_CHANGED("ENVIRONMENT_CHANGED", "Environment changed"),
    SECURITY_ALERT("SECURITY_ALERT", "Security related alert"),
    SYSTEM_ALERT("SYSTEM_ALERT", "System alert");

    private final String code;
    private final String description;

    NotificationType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
