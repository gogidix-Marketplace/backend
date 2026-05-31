package com.gogidix.aiservices.nlpprocessingservice.domain.model;

public enum EntityType {
    PERSON("Person", "PER"),
    ORGANIZATION("Organization", "ORG"),
    LOCATION("Location", "LOC"),
    DATE("Date", "DATE"),
    TIME("Time", "TIME"),
    MONEY("Money", "MONEY"),
    PERCENT("Percentage", "PERCENT"),
    NUMBER("Number", "NUMBER"),
    EMAIL("Email", "EMAIL"),
    PHONE("Phone", "PHONE"),
    URL("URL", "URL"),
    UNKNOWN("Unknown", "UNKNOWN");

    private final String displayName;
    private final String shortCode;

    EntityType(String displayName, String shortCode) {
        this.displayName = displayName;
        this.shortCode = shortCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getShortCode() {
        return shortCode;
    }

    public static EntityType fromShortCode(String code) {
        for (EntityType type : values()) {
            if (type.shortCode.equals(code)) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
