package com.gogidix.aiservices.aicontentgenerationservice.domain.model;

import lombok.Getter;

@Getter
public enum GenerationStatus {
    PENDING("pending", "Generation request received"),
    PROCESSING("processing", "Content is being generated"),
    COMPLETED("completed", "Content generation successful"),
    FAILED("failed", "Content generation failed"),
    CANCELLED("cancelled", "Generation request cancelled");

    private final String value;
    private final String description;

    GenerationStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String toString() {
        return value;
    }

    public static GenerationStatus fromString(String value) {
        for (GenerationStatus status : GenerationStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown generation status: " + value);
    }
}
