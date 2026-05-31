package com.gogidix.aiservices.aipersonalizationservice.domain.model;

import lombok.Getter;

@Getter
public enum RecommendationReason {
    BEHAVIORAL("Based on your browsing behavior"),
    COLLABORATIVE("Users like you also liked this"),
    CONTENT_BASED("Similar to items you've viewed"),
    TRENDING("Currently popular"),
    CONTEXTUAL("Based on current context");

    private final String description;

    RecommendationReason(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name();
    }

    public static RecommendationReason fromString(String value) {
        for (RecommendationReason reason : RecommendationReason.values()) {
            if (reason.name().equalsIgnoreCase(value)) {
                return reason;
            }
        }
        throw new IllegalArgumentException("Unknown recommendation reason: " + value);
    }
}
