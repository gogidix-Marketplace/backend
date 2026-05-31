package com.gogidix.aiservices.aifrauddetectionservice.domain.event;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Domain event emitted when a transaction is flagged as suspected fraud.
 * This event triggers the review workflow and alerts.
 */
@Value
@Builder
public class TransactionFlaggedEvent implements DomainEvent {

    /**
     * Unique identifier for this event instance.
     */
    String eventId;

    /**
     * ID of the transaction that was flagged.
     */
    String transactionId;

    /**
     * User or system that flagged the transaction.
     */
    String flaggedBy;

    /**
     * Reason for flagging the transaction.
     */
    String reason;

    /**
     * Severity level of the flag.
     */
    String severity;

    /**
     * Tenant ID for multi-tenancy support.
     */
    String tenantId;

    /**
     * User ID associated with the transaction.
     */
    String userId;

    /**
     * When the transaction was flagged.
     */
    Instant occurredAt;

    /**
     * ID of the aggregate that generated this event.
     */
    String aggregateId;

    /**
     * Transaction amount.
     */
    BigDecimal amount;

    /**
     * Transaction currency.
     */
    String currency;

    /**
     * Associated analysis ID if available.
     */
    String analysisId;

    /**
     * List of pattern IDs that triggered the flag.
     */
    String matchedPatterns;

    /**
     * Fraud score associated with this flag.
     */
    Double fraudScore;

    /**
     * Recommended action.
     */
    String recommendedAction;

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
        return "TransactionFlagged";
    }

    @Override
    public String getAggregateType() {
        return "Transaction";
    }

    /**
     * Creates a new TransactionFlaggedEvent with generated ID and timestamp.
     */
    public static TransactionFlaggedEvent create(String transactionId, String flaggedBy,
                                                   String reason, String severity) {
        return TransactionFlaggedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .transactionId(transactionId)
                .flaggedBy(flaggedBy)
                .reason(reason)
                .severity(severity)
                .occurredAt(Instant.now())
                .aggregateId(transactionId)
                .build();
    }

    /**
     * Checks if this is a critical severity flag.
     */
    public boolean isCritical() {
        return "CRITICAL".equalsIgnoreCase(severity);
    }

    /**
     * Checks if this is a high severity flag.
     */
    public boolean isHighSeverity() {
        return "HIGH".equalsIgnoreCase(severity) || "CRITICAL".equalsIgnoreCase(severity);
    }

    /**
     * Checks if this is a low severity flag.
     */
    public boolean isLowSeverity() {
        return "LOW".equalsIgnoreCase(severity);
    }

    /**
     * Checks if this flag requires immediate blocking of the transaction.
     */
    public boolean requiresImmediateBlock() {
        return isCritical() || "BLOCK".equalsIgnoreCase(recommendedAction);
    }

    /**
     * Checks if this flag requires manual review.
     */
    public boolean requiresReview() {
        return "MEDIUM".equalsIgnoreCase(severity) ||
               "REVIEW".equalsIgnoreCase(recommendedAction);
    }
}
