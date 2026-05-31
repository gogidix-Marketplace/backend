package com.gogidix.aiservices.aifrauddetectionservice.domain.port.in;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Input port for transaction monitoring operations.
 * This use case provides real-time transaction monitoring, flagging suspicious
 * transactions, and retrieving transaction history.
 *
 * Supports both real-time monitoring and historical analysis of transactions.
 */
@UseCase(
    value = "Monitors transactions for fraudulent activity in real-time",
    category = "monitoring"
)
public interface TransactionMonitoringUseCase {

    /**
     * Monitors a transaction in real-time for potential fraud.
     * This is the primary entry point for transaction monitoring.
     *
     * @param request Transaction monitoring request with transaction details
     * @return Monitoring result with any detected fraud indicators
     */
    MonitoringResult monitorTransaction(MonitorTransactionRequest request);

    /**
     * Flags a transaction as suspected fraud for manual review or automated action.
     *
     * @param transactionId Unique identifier of the transaction to flag
     * @param reason Reason for flagging
     * @param flaggedBy User or system that flagged the transaction
     * @param severity Severity level of the fraud suspicion
     * @return UUID of the created flag record
     */
    UUID flagSuspectedTransaction(UUID transactionId, String reason, String flaggedBy, String severity);

    /**
     * Retrieves the monitoring history for a specific transaction.
     *
     * @param transactionId Unique identifier of the transaction
     * @param includeAnalysisNotes Include analysis notes in the result
     * @return Transaction history if found
     */
    Optional<TransactionHistory> getTransactionHistory(UUID transactionId, boolean includeAnalysisNotes);

    /**
     * Request to monitor a transaction.
     */
    record MonitorTransactionRequest(
        UUID transactionId,
        String tenantId,
        Double amount,
        String currency,
        String sourceAccount,
        String destinationAccount,
        Instant transactionTime,
        String location,
        String deviceFingerprint,
        Object additionalMetadata
    ) {}

    /**
     * Result of transaction monitoring.
     */
    record MonitoringResult(
        UUID transactionId,
        UUID monitoringId,
        boolean flagged,
        Double riskScore,
        String riskLevel, // "LOW", "MEDIUM", "HIGH", "CRITICAL"
        List<String> detectedPatterns,
        String recommendedAction, // "ALLOW", "BLOCK", "REVIEW"
        Instant monitoredAt
    ) {}

    /**
     * Transaction history containing monitoring and analysis data.
     */
    record TransactionHistory(
        UUID transactionId,
        String tenantId,
        Double amount,
        String currency,
        Instant transactionTime,
        MonitoringStatus status,
        List<MonitoringEvent> events,
        List<AnalysisNote> analysisNotes,
        Instant createdAt,
        Instant updatedAt
    ) {}

    /**
     * Current monitoring status of a transaction.
     */
    enum MonitoringStatus {
        PENDING_MONITORING,
        MONITORING,
        FLAGGED_FOR_REVIEW,
        CONFIRMED_FRAUD,
        CONFIRMED_CLEAN,
        AUTO_APPROVED,
        AUTO_REJECTED
    }

    /**
     * Individual monitoring event in the transaction history.
     */
    record MonitoringEvent(
        UUID eventId,
        String eventType,
        String description,
        Instant occurredAt,
        String triggeredBy
    ) {}

    /**
     * Analysis note added to a transaction.
     */
    record AnalysisNote(
        UUID noteId,
        String note,
        String addedBy,
        Instant addedAt
    ) {}
}
