package com.gogidix.aiservices.supplychainoptimizationservice.domain.model;

import java.util.Map;
import java.util.UUID;

public class OptimizationRecommendation {
    private final String recommendationId;
    private final String title;
    private final String description;
    private final RecommendationPriority priority;
    private final double estimatedSavings;
    private final String currency;
    private final int estimatedTimeToImplement;
    private final Map<String, Object> details;

    private OptimizationRecommendation(Builder builder) {
        this.recommendationId = builder.recommendationId != null ?
                builder.recommendationId : UUID.randomUUID().toString();
        this.title = builder.title;
        this.description = builder.description;
        this.priority = builder.priority != null ? builder.priority : RecommendationPriority.MEDIUM;
        this.estimatedSavings = builder.estimatedSavings;
        this.currency = builder.currency != null ? builder.currency : "USD";
        this.estimatedTimeToImplement = builder.estimatedTimeToImplement;
        this.details = builder.details != null ? builder.details : Map.of();
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getRecommendationId() {
        return recommendationId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public RecommendationPriority getPriority() {
        return priority;
    }

    public double getEstimatedSavings() {
        return estimatedSavings;
    }

    public String getCurrency() {
        return currency;
    }

    public int getEstimatedTimeToImplement() {
        return estimatedTimeToImplement;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public enum RecommendationPriority {
        CRITICAL, HIGH, MEDIUM, LOW
    }

    public static class Builder {
        private String recommendationId;
        private String title;
        private String description;
        private RecommendationPriority priority;
        private double estimatedSavings;
        private String currency;
        private int estimatedTimeToImplement;
        private Map<String, Object> details;

        public Builder recommendationId(String recommendationId) {
            this.recommendationId = recommendationId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder priority(RecommendationPriority priority) {
            this.priority = priority;
            return this;
        }

        public Builder estimatedSavings(double estimatedSavings) {
            this.estimatedSavings = estimatedSavings;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder estimatedTimeToImplement(int estimatedTimeToImplement) {
            this.estimatedTimeToImplement = estimatedTimeToImplement;
            return this;
        }

        public Builder details(Map<String, Object> details) {
            this.details = details;
            return this;
        }

        public OptimizationRecommendation build() {
            return new OptimizationRecommendation(this);
        }
    }
}
