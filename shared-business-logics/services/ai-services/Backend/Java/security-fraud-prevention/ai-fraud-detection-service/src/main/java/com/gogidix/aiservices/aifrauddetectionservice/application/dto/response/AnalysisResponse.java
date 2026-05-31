package com.gogidix.aiservices.aifrauddetectionservice.application.dto.response;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAction;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAnalysisResult;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.RiskLevel;

import java.time.Instant;
import java.util.List;

public class AnalysisResponse {
    private String analysisId;
    private String transactionId;
    private String userId;
    private String tenantId;
    private double fraudScore;
    private RiskLevel riskLevel;
    private FraudAction action;
    private List<String> reasons;
    private Instant timestamp;
    private String modelVersion;

    public AnalysisResponse() {}

    public static Builder builder() {
        return new Builder();
    }

    public static AnalysisResponse fromDomain(FraudAnalysisResult result) {
        return AnalysisResponse.builder()
                .analysisId(result.getAnalysisId())
                .transactionId(result.getTransactionId())
                .userId(result.getUserId())
                .tenantId(result.getTenantId())
                .fraudScore(result.getFraudScore())
                .riskLevel(result.getRiskLevel())
                .action(result.getRecommendedAction())
                .reasons(result.getReasons())
                .timestamp(result.getTimestamp())
                .modelVersion(result.getModelVersion())
                .build();
    }

    // Getters and Setters
    public String getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(String analysisId) {
        this.analysisId = analysisId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getFraudScore() {
        return fraudScore;
    }

    public void setFraudScore(double fraudScore) {
        this.fraudScore = fraudScore;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public FraudAction getAction() {
        return action;
    }

    public void setAction(FraudAction action) {
        this.action = action;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public static class Builder {
        private String analysisId;
        private String transactionId;
        private String userId;
        private String tenantId;
        private double fraudScore;
        private RiskLevel riskLevel;
        private FraudAction action;
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

        public Builder action(FraudAction action) {
            this.action = action;
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

        public AnalysisResponse build() {
            AnalysisResponse response = new AnalysisResponse();
            response.analysisId = this.analysisId;
            response.transactionId = this.transactionId;
            response.userId = this.userId;
            response.tenantId = this.tenantId;
            response.fraudScore = this.fraudScore;
            response.riskLevel = this.riskLevel;
            response.action = this.action;
            response.reasons = this.reasons;
            response.timestamp = this.timestamp;
            response.modelVersion = this.modelVersion;
            return response;
        }
    }
}
