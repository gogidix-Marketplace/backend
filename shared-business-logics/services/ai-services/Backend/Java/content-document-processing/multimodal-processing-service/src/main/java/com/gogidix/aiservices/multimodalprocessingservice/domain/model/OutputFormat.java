package com.gogidix.aiservices.multimodalprocessingservice.domain.model;

public enum OutputFormat {
    EMBEDDING,
    SUMMARY,
    TAGS,
    FULL;

    public static OutputFormat fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return EMBEDDING;
        }

        try {
            return OutputFormat.valueOf(value.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return EMBEDDING;
        }
    }
}
