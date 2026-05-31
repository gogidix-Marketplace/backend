package com.gogidix.sales.notification.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

/**
 * Notification Priority
 * Defines the urgency level of notifications
 */
@Getter
public enum NotificationPriority {

    LOW(1, "low", "Low"),
    NORMAL(2, "normal", "Normal"),
    HIGH(3, "high", "High"),
    URGENT(4, "urgent", "Urgent"),
    CRITICAL(5, "critical", "Critical");

    private final Integer level;
    private final String code;
    private final String displayName;

    NotificationPriority(Integer level, String code, String displayName) {
        this.level = level;
        this.code = code;
        this.displayName = displayName;
    }

    @JsonValue
    public Integer getLevel() {
        return level;
    }

    public String getCode() {
        return code;
    }

    public static NotificationPriority fromLevel(Integer level) {
        if (level == null) {
            return NORMAL;
        }
        return Arrays.stream(values())
                .filter(p -> p.level.equals(level))
                .findFirst()
                .orElse(NORMAL);
    }

    public static NotificationPriority fromCode(String code) {
        if (code == null) {
            return NORMAL;
        }
        return Arrays.stream(values())
                .filter(p -> p.code.equalsIgnoreCase(code))
                .findFirst()
                .orElse(NORMAL);
    }
}
