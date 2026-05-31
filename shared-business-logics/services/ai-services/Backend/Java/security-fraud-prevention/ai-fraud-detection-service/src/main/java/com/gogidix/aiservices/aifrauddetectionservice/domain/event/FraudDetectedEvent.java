package com.gogidix.aiservices.aifrauddetectionservice.domain.event;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.RiskLevel;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event emitted when fraud is detected during transaction analysis.
 * This event contains the analysis results and risk assessment.
 */
@Value
@Builder
public class FraudDetectedEvent implements DomainEvent {

    /**
     * Unique identifier for this event instance.
     */
    String eventId;

    /**
     * ID of the analysis that detected the fraud.
     */
    String analysisId;

    /**
     * ID of the transaction analyzed.
     */
    String transactionId;

    /**
     * The risk level assessed for this transaction.
     */
    RiskLevel riskLevel;

    /**
     * The fraud score calculated (0-1).
     */
    double fraudScore;

    /**
     * Optional tenant ID for multi-tenancy.
     */
    String tenantId;

    /**
     * Optional user ID associated with the transaction.
     */
    String userId;

    /**
     * List of pattern IDs that matched.
     */
    String matchedPatterns;

    /**
     * Reason for the fraud detection.
     */
    String reason;

    /**
     * When the fraud was detected.
     */
    Instant occurredAt;

    /**
     * ID of the aggregate that generated this event.
     */
    String aggregateId;

    /**
     * Additional metadata about the detection.
     */
    String metadata;

    @Override
    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public String getAggregateId() {
        return aggregateId;
    }

    @Override
    public String getEventType() {
        return "FraudDetected";
    }

    @Override
    public String getAggregateType() {
        return "FraudAnalysis";
    }

    /**
     * Creates a new FraudDetectedEvent with generated ID and timestamp.
     */
    public static FraudDetectedEvent create(String analysisId, String transactionId,
                                           RiskLevel riskLevel, double fraudScore) {
        return FraudDetectedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .analysisId(analysisId)
                .transactionId(transactionId)
                .riskLevel(riskLevel)
                .fraudScore(fraudScore)
                .occurredAt(Instant.now())
                .aggregateId(analysisId)
                .build();
    }

    /**
     * Checks if this is a high-severity fraud detection.
     */
    public boolean isHighSeverity() {
        return riskLevel == RiskLevel.HIGH || fraudScore >= 0.8;
    }

    /**
     * Checks if this fraud detection requires immediate action.
     */
    public boolean requiresImmediateAction() {
        return fraudScore >= 0.9;
    }
}
