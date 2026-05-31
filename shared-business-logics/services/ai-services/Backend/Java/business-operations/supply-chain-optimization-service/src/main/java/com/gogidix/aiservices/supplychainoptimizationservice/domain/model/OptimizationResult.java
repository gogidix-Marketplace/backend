package com.gogidix.aiservices.supplychainoptimizationservice.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OptimizationResult {
    private final UUID resultId;
    private final UUID requestId;
    private final OptimizationType type;
    private final List<OptimizationMetric> metrics;
    private final List<OptimizationRecommendation> recommendations;
    private final Map<String, Object> data;
    private final double confidenceScore;
    private final Instant generatedAt;
    private final String summary;

    private OptimizationResult(Builder builder) {
        this.resultId = builder.resultId != null ? builder.resultId : UUID.randomUUID();
        this.requestId = builder.requestId;
        this.type = builder.type;
        this.metrics = builder.metrics != null ? builder.metrics : List.of();
        this.recommendations = builder.recommendations != null ? builder.recommendations : List.of();
        this.data = builder.data != null ? builder.data : Map.of();
        this.confidenceScore = builder.confidenceScore;
        this.generatedAt = builder.generatedAt != null ? builder.generatedAt : Instant.now();
        this.summary = builder.summary;
    }

    public static Builder builder() {
        return new Builder();
    }

    public UUID getResultId() {
        return resultId;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public OptimizationType getType() {
        return type;
    }

    public List<OptimizationMetric> getMetrics() {
        return metrics;
    }

    public List<OptimizationRecommendation> getRecommendations() {
        return recommendations;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public double getConfidenceScore() {
        return confidenceScore;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public String getSummary() {
        return summary;
    }

    public boolean hasHighConfidence() {
        return confidenceScore >= 0.7;
    }

    public double getTotalSavings() {
        return metrics.stream()
                .filter(m -> m.getMetricName().toLowerCase().contains("cost") ||
                        m.getMetricName().toLowerCase().contains("savings"))
                .mapToDouble(OptimizationMetric::getImprovement)
                .sum();
    }

    public static class Builder {
        private UUID resultId;
        private UUID requestId;
        private OptimizationType type;
        private List<OptimizationMetric> metrics;
        private List<OptimizationRecommendation> recommendations;
        private Map<String, Object> data;
        private double confidenceScore = 0.5;
        private Instant generatedAt;
        private String summary;

        public Builder resultId(UUID resultId) {
            this.resultId = resultId;
            return this;
        }

        public Builder requestId(UUID requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder type(OptimizationType type) {
            this.type = type;
            return this;
        }

        public Builder metrics(List<OptimizationMetric> metrics) {
            this.metrics = metrics;
            return this;
        }

        public Builder recommendations(List<OptimizationRecommendation> recommendations) {
            this.recommendations = recommendations;
            return this;
        }

        public Builder data(Map<String, Object> data) {
            this.data = data;
            return this;
        }

        public Builder confidenceScore(double confidenceScore) {
            this.confidenceScore = confidenceScore;
            return this;
        }

        public Builder generatedAt(Instant generatedAt) {
            this.generatedAt = generatedAt;
            return this;
        }

        public Builder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public OptimizationResult build() {
            return new OptimizationResult(this);
        }
    }
}
