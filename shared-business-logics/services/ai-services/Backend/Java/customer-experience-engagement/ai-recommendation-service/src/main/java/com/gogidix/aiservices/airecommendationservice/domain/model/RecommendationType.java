package com.gogidix.aiservices.airecommendationservice.domain.model;

import lombok.Getter;

@Getter
public enum RecommendationType {
    COLLABORATIVE("collaborative", "Based on user behavior patterns"),
    CONTENT_BASED("content_based", "Based on item similarity"),
    HYBRID("hybrid", "Combination of multiple algorithms"),
    POPULAR("popular", "Trending and popular items"),
    PERSONALIZED("personalized", "Tailored to individual preferences");

    private final String value;
    private final String description;

    RecommendationType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String toString() {
        return value;
    }

    public static RecommendationType fromString(String value) {
        for (RecommendationType type : RecommendationType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown recommendation type: " + value);
    }
}
