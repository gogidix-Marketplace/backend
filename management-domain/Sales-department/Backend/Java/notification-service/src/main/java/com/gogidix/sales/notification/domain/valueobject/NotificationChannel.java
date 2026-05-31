package com.gogidix.sales.notification.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

/**
 * Notification Channel Type
 * Defines the available channels for notification delivery
 */
@Getter
public enum NotificationChannel {

    EMAIL("email", "Email"),
    SMS("sms", "SMS"),
    PUSH_NOTIFICATION("push", "Push Notification"),
    IN_APP("in_app", "In-App"),
    WEBHOOK("webhook", "Webhook");

    private final String code;
    private final String displayName;

    NotificationChannel(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public static NotificationChannel fromCode(String code) {
        return Arrays.stream(values())
                .filter(c -> c.code.equalsIgnoreCase(code))
                .findFirst()
                .orElse(IN_APP);
    }

    public static NotificationChannel fromName(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return IN_APP;
        }
    }
}
