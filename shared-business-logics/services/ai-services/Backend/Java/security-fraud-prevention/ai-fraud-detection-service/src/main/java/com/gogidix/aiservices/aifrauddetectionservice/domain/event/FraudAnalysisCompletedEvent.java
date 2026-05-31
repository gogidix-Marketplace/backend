package com.gogidix.aiservices.aifrauddetectionservice.domain.event;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.RiskLevel;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event emitted when a fraud analysis is completed.
 * This event indicates that the analysis process has finished with a result.
 */
@Value
@Builder
public class FraudAnalysisCompletedEvent implements DomainEvent {

    /**
     * Unique identifier for this event instance.
     */
    String eventId;

    /**
     * ID of the completed analysis.
     */
    String analysisId;

    /**
     * The result of the analysis.
     */
    String result;

    /**
     * The fraud score calculated (0-1).
     */
    double score;

    /**
     * When the analysis was completed.
     */
    Instant timestamp;

    /**
     * ID of the aggregate that generated this event.
     */
    String aggregateId;

    /**
     * The assessed risk level.
     */
    RiskLevel riskLevel;

    /**
     * The transaction ID that was analyzed.
     */
    String transactionId;

    /**
     * Tenant ID for multi-tenancy support.
     */
    String tenantId;

    /**
     * User ID associated with the transaction.
     */
    String userId;

    /**
     * The model version used for analysis.
     */
    String modelVersion;

    /**
     * Processing time in milliseconds.
     */
    Long processingTimeMs;

    @Override
    public Instant getOccurredAt() {
        return timestamp;
    }

    @Override
    public String getAggregateId() {
        return aggregateId;
    }

    @Override
    public String getEventType() {
        return "FraudAnalysisCompleted";
    }

    @Override
    public String getAggregateType() {
        return "FraudAnalysis";
    }

    /**
     * Creates a new FraudAnalysisCompletedEvent with generated ID and timestamp.
     */
    public static FraudAnalysisCompletedEvent create(String analysisId, String result,
                                                     double score, RiskLevel riskLevel) {
        return FraudAnalysisCompletedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .analysisId(analysisId)
                .result(result)
                .score(score)
                .riskLevel(riskLevel)
                .timestamp(Instant.now())
                .aggregateId(analysisId)
                .build();
    }

    /**
     * Checks if the analysis resulted in fraud detection.
     */
    public boolean isFraudDetected() {
        return "FRAUD_DETECTED".equalsIgnoreCase(result) || score > 0.5;
    }

    /**
     * Checks if the analysis resulted in a clean transaction.
     */
    public boolean isClean() {
        return "CLEAN".equalsIgnoreCase(result) || score <= 0.3;
    }

    /**
     * Checks if the transaction requires manual review.
     */
    public boolean requiresReview() {
        return "REQUIRES_REVIEW".equalsIgnoreCase(result) ||
               (score > 0.3 && score <= 0.5);
    }
}
