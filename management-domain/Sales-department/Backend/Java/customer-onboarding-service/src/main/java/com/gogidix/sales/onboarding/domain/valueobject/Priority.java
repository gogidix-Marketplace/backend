package com.gogidix.sales.onboarding.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Priority Value Object
 * Defines the priority level for onboarding
 */
public enum Priority {
    LOW("Low", 1),
    MEDIUM("Medium", 2),
    HIGH("High", 3),
    URGENT("Urgent", 4);

    private final String value;
    private final int level;

    Priority(String value, int level) {
        this.value = value;
        this.level = level;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public int getLevel() {
        return level;
    }

    @JsonCreator
    public static Priority fromValue(String value) {
        for (Priority priority : Priority.values()) {
            if (priority.value.equalsIgnoreCase(value) || priority.name().equalsIgnoreCase(value)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Unknown Priority: " + value);
    }
}
