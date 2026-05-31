package com.gogidix.sales.notification.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

/**
 * Notification Status
 * Defines the delivery status of notifications
 */
@Getter
public enum NotificationStatus {

    DRAFT("draft", "Draft"),
    PENDING("pending", "Pending"),
    SENDING("sending", "Sending"),
    SENT("sent", "Sent"),
    DELIVERED("delivered", "Delivered"),
    FAILED("failed", "Failed"),
    BOUNCED("bounced", "Bounced"),
    READ("read", "Read"),
    ARCHIVED("archived", "Archived"),
    SCHEDULED("scheduled", "Scheduled"),
    CANCELLED("cancelled", "Cancelled");

    private final String code;
    private final String displayName;

    NotificationStatus(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public static NotificationStatus fromCode(String code) {
        if (code == null) {
            return DRAFT;
        }
        return Arrays.stream(values())
                .filter(s -> s.code.equalsIgnoreCase(code))
                .findFirst()
                .orElse(DRAFT);
    }

    public static NotificationStatus fromName(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return DRAFT;
        }
    }

    public boolean isTerminal() {
        return this == DELIVERED || this == FAILED || this == BOUNCED || this == READ || this == ARCHIVED || this == CANCELLED;
    }

    public boolean isPending() {
        return this == DRAFT || this == PENDING || this == SENDING || this == SCHEDULED;
    }
}
