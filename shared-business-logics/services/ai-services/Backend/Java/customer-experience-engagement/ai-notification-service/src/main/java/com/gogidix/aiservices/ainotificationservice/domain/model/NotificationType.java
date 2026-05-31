package com.gogidix.aiservices.ainotificationservice.domain.model;

import lombok.Getter;

@Getter
public enum NotificationType {
    EMAIL("email", "Email notification"),
    SMS("sms", "SMS message"),
    PUSH("push", "Push notification"),
    IN_APP("in_app", "In-app notification"),
    WEBHOOK("webhook", "Webhook notification");

    private final String value;
    private final String description;

    NotificationType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String toString() {
        return value;
    }

    public static NotificationType fromString(String value) {
        for (NotificationType type : NotificationType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown notification type: " + value);
    }
}
