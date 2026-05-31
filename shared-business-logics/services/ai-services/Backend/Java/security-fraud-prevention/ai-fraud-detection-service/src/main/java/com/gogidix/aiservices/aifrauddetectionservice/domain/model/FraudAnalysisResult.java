package com.gogidix.aiservices.aifrauddetectionservice.domain.model;

import java.time.Instant;
import java.util.List;

public class FraudAnalysisResult {
    private final String analysisId;
    private final String transactionId;
    private final String userId;
    private final String tenantId;
    private final double fraudScore;
    private final RiskLevel riskLevel;
    private final FraudAction recommendedAction;
    private final List<String> reasons;
    private final Instant timestamp;
    private final String modelVersion;

    private FraudAnalysisResult(Builder builder) {
        this.analysisId = builder.analysisId;
        this.transactionId = builder.transactionId;
        this.userId = builder.userId;
        this.tenantId = builder.tenantId;
        this.fraudScore = builder.fraudScore;
        this.riskLevel = builder.riskLevel;
        this.recommendedAction = builder.recommendedAction;
        this.reasons = builder.reasons;
        this.timestamp = builder.timestamp;
        this.modelVersion = builder.modelVersion;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getAnalysisId() {
        return analysisId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public double getFraudScore() {
        return fraudScore;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public FraudAction getRecommendedAction() {
        return recommendedAction;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public boolean isFraudulent() {
        return fraudScore > 0.8;
    }

    public boolean requiresReview() {
        return fraudScore > 0.5 && fraudScore <= 0.8;
    }

    public boolean shouldAllow() {
        return fraudScore <= 0.5;
    }

    public static class Builder {
        private String analysisId;
        private String transactionId;
        private String userId;
        private String tenantId;
        private double fraudScore;
        private RiskLevel riskLevel;
        private FraudAction recommendedAction;
        private List<String> reasons;
        private Instant timestamp;
        private String modelVersion;

        public Builder analysisId(String analysisId) {
            this.analysisId = analysisId;
            return this;
        }

        public Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder fraudScore(double fraudScore) {
            this.fraudScore = fraudScore;
            return this;
        }

        public Builder riskLevel(RiskLevel riskLevel) {
            this.riskLevel = riskLevel;
            return this;
        }

        public Builder recommendedAction(FraudAction recommendedAction) {
            this.recommendedAction = recommendedAction;
            return this;
        }

        public Builder reasons(List<String> reasons) {
            this.reasons = reasons;
            return this;
        }

        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder modelVersion(String modelVersion) {
            this.modelVersion = modelVersion;
            return this;
        }

        public FraudAnalysisResult build() {
            return new FraudAnalysisResult(this);
        }
    }
}
