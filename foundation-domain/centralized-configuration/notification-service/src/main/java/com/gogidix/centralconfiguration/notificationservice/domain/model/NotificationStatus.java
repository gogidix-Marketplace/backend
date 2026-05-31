package com.gogidix.centralconfiguration.notificationservice.domain.model;

/**
 * Notification Status enumeration.
 */
public enum NotificationStatus {
    PENDING("pending", "Notification is pending delivery"),
    SENT("sent", "Notification sent successfully"),
    FAILED("failed", "Notification delivery failed"),
    RETRYING("retrying", "Notification is being retried");

    private final String code;
    private final String description;

    NotificationStatus(String code, String description) {
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
