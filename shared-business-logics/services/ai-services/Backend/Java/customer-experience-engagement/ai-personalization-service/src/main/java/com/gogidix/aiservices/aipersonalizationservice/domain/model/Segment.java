package com.gogidix.aiservices.aipersonalizationservice.domain.model;

import lombok.Getter;

@Getter
public enum Segment {
    NEW_USER("new_user"),
    ACTIVE("active"),
    CHURNED("churned"),
    VIP("vip"),
    INACTIVE("inactive");

    private final String value;

    Segment(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Segment fromString(String value) {
        for (Segment segment : Segment.values()) {
            if (segment.value.equalsIgnoreCase(value)) {
                return segment;
            }
        }
        throw new IllegalArgumentException("Unknown segment: " + value);
    }
}
