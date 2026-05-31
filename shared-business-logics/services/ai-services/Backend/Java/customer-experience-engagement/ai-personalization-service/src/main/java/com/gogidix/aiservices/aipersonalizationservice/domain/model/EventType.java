package com.gogidix.aiservices.aipersonalizationservice.domain.model;

import lombok.Getter;

@Getter
public enum EventType {
    VIEW("view"),
    CLICK("click"),
    LIKE("like"),
    SHARE("share"),
    PURCHASE("purchase"),
    SEARCH("search");

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static EventType fromString(String value) {
        for (EventType type : EventType.values()) {
            if (type.value.equalsIgnoreCase(value) || type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + value);
    }
}
