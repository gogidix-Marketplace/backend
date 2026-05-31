package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.persistence.mongodb;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAction;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAnalysisResult;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.RiskLevel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;

/**
 * MongoDB document entity for storing FraudAnalysisResult domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "fraud_analysis_results")
public class FraudAnalysisResultEntity {

    @Id
    private String id;

    @Indexed
    @Field("analysis_id")
    private String analysisId;

    @Indexed
    @Field("transaction_id")
    private String transactionId;

    @Indexed
    @Field("user_id")
    private String userId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("fraud_score")
    private double fraudScore;

    @Field("risk_level")
    private String riskLevel;

    @Field("recommended_action")
    private String recommendedAction;

    @Field("reasons")
    private List<String> reasons;

    @Field("timestamp")
    private Instant timestamp;

    @Field("model_version")
    private String modelVersion;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    // Default constructor for MongoDB
    public FraudAnalysisResultEntity() {
    }

    // Constructor from domain model
    public FraudAnalysisResultEntity(FraudAnalysisResult result) {
        this.analysisId = result.getAnalysisId();
        this.transactionId = result.getTransactionId();
        this.userId = result.getUserId();
        this.tenantId = result.getTenantId();
        this.fraudScore = result.getFraudScore();
        this.riskLevel = result.getRiskLevel() != null ? result.getRiskLevel().name() : null;
        this.recommendedAction = result.getRecommendedAction() != null ? result.getRecommendedAction().name() : null;
        this.reasons = result.getReasons();
        this.timestamp = result.getTimestamp();
        this.modelVersion = result.getModelVersion();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // Convert to domain model
    public FraudAnalysisResult toDomainModel() {
        return FraudAnalysisResult.builder()
                .analysisId(this.analysisId)
                .transactionId(this.transactionId)
                .userId(this.userId)
                .tenantId(this.tenantId)
                .fraudScore(this.fraudScore)
                .riskLevel(this.riskLevel != null ? RiskLevel.valueOf(this.riskLevel) : null)
                .recommendedAction(this.recommendedAction != null ? FraudAction.valueOf(this.recommendedAction) : null)
                .reasons(this.reasons)
                .timestamp(this.timestamp)
                .modelVersion(this.modelVersion)
                .build();
    }

    // Update from domain model (for partial updates)
    public void updateFrom(FraudAnalysisResult result) {
        this.fraudScore = result.getFraudScore();
        this.riskLevel = result.getRiskLevel() != null ? result.getRiskLevel().name() : null;
        this.recommendedAction = result.getRecommendedAction() != null ? result.getRecommendedAction().name() : null;
        this.reasons = result.getReasons();
        this.updatedAt = Instant.now();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public double getFraudScore() {
        return fraudScore;
    }

    public void setFraudScore(double fraudScore) {
        this.fraudScore = fraudScore;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getRecommendedAction() {
        return recommendedAction;
    }

    public void setRecommendedAction(String recommendedAction) {
        this.recommendedAction = recommendedAction;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
