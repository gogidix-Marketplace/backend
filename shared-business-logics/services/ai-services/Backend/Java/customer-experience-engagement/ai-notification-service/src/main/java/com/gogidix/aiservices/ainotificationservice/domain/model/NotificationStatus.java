package com.gogidix.aiservices.ainotificationservice.domain.model;

import lombok.Getter;

@Getter
public enum NotificationStatus {
    PENDING("pending", "Notification queued for delivery"),
    SENT("sent", "Notification successfully sent"),
    DELIVERED("delivered", "Notification delivered to recipient"),
    FAILED("failed", "Notification delivery failed"),
    CANCELLED("cancelled", "Notification cancelled"),
    BOUNCED("bounced", "Notification bounced back");

    private final String value;
    private final String description;

    NotificationStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String toString() {
        return value;
    }

    public static NotificationStatus fromString(String value) {
        for (NotificationStatus status : NotificationStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown notification status: " + value);
    }
}
