package com.gogidix.aiservices.aifrauddetectionservice.domain.model;

import java.time.Instant;

public class FraudPattern {
    private final String patternId;
    private final String patternName;
    private final String description;
    private final String tenantId;
    private final double confidenceScore;
    private final Instant lastSeen;
    private final int occurrenceCount;

    private FraudPattern(Builder builder) {
        this.patternId = builder.patternId;
        this.patternName = builder.patternName;
        this.description = builder.description;
        this.tenantId = builder.tenantId;
        this.confidenceScore = builder.confidenceScore;
        this.lastSeen = builder.lastSeen;
        this.occurrenceCount = builder.occurrenceCount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getPatternId() {
        return patternId;
    }

    public String getPatternName() {
        return patternName;
    }

    public String getDescription() {
        return description;
    }

    public String getTenantId() {
        return tenantId;
    }

    public double getConfidenceScore() {
        return confidenceScore;
    }

    public Instant getLastSeen() {
        return lastSeen;
    }

    public int getOccurrenceCount() {
        return occurrenceCount;
    }

    public static class Builder {
        private String patternId;
        private String patternName;
        private String description;
        private String tenantId;
        private double confidenceScore;
        private Instant lastSeen;
        private int occurrenceCount;

        public Builder patternId(String patternId) {
            this.patternId = patternId;
            return this;
        }

        public Builder patternName(String patternName) {
            this.patternName = patternName;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder confidenceScore(double confidenceScore) {
            this.confidenceScore = confidenceScore;
            return this;
        }

        public Builder lastSeen(Instant lastSeen) {
            this.lastSeen = lastSeen;
            return this;
        }

        public Builder occurrenceCount(int occurrenceCount) {
            this.occurrenceCount = occurrenceCount;
            return this;
        }

        public FraudPattern build() {
            return new FraudPattern(this);
        }
    }
}
