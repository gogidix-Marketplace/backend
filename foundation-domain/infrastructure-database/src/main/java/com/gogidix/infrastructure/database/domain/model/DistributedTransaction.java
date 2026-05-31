package com.gogidix.infrastructure.database.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * Domain model representing distributed transaction coordination.
 *
 * <p>Manages distributed transactions using:</p>
 * <ul>
 *   <li>Two-Phase Commit (2PC) protocol</li>
 *   <li>Saga pattern for long-running transactions</li>
 *   <li>Compensating transactions</li>
 *   <li>Transaction logging and recovery</li>
 * </ul>
 *
 * <p>Provides ACID guarantees across multiple databases.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "distributed_transactions")
@CompoundIndex(def = "{'tenantId': 1, 'status': 1, 'createdAt': -1}")
@CompoundIndex(def = "{'status': 1, 'expiresAt': 1}")
public class DistributedTransaction {

    /**
     * Unique identifier for the transaction.
     */
    @Id
    private String id;

    /**
     * Tenant identifier for multi-tenancy support.
     */
    @Indexed
    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    /**
     * Environment for this transaction (dev, staging, prod).
     */
    @NotNull(message = "Environment is required")
    private Environment environment;

    /**
     * Transaction type (2PC, SAGA).
     */
    @NotNull(message = "Transaction type is required")
    private TransactionType transactionType;

    /**
     * Transaction name/description.
     */
    @NotBlank(message = "Transaction name is required")
    private String transactionName;

    /**
     * Transaction description.
     */
    private String description;

    /**
     * Current transaction status.
     */
    @Indexed
    @NotNull(message = "Status is required")
    @Builder.Default
    private TransactionStatus status = TransactionStatus.ACTIVE;

    /**
     * Previous status for rollback tracking.
     */
    private TransactionStatus previousStatus;

    /**
     * Transaction timeout in milliseconds.
     */
    @Builder.Default
    private Long timeoutMs = 60000L;

    /**
     * Transaction expiration time.
     */
    @Indexed
    private LocalDateTime expiresAt;

    /**
     * Transaction start time.
     */
    @Indexed
    private LocalDateTime startTime;

    /**
     * Transaction end time.
     */
    private LocalDateTime endTime;

    /**
     * Transaction duration in milliseconds.
     */
    private Long durationMs;

    /**
     * Initiator of the transaction.
     */
    @NotBlank(message = "Initiator is required")
    private String initiator;

    /**
     * Application/service name.
     */
    private String applicationName;

    /**
     * Correlation ID for tracing.
     */
    private String correlationId;

    /**
     * Parent transaction ID if nested.
     */
    private String parentTransactionId;

    /**
     * Child transaction IDs.
     */
    private Set<String> childTransactionIds;

    /**
     * Participants in the transaction.
     */
    @NotNull(message = "Participants are required")
    private Set<TransactionParticipant> participants;

    /**
     * Transaction context/data.
     */
    private Map<String, Object> context;

    /**
     * Saga steps for saga transactions.
     */
    private Set<SagaStep> sagaSteps;

    /**
     * Current saga step index.
     */
    private Integer currentSagaStep;

    /**
     * Two-phase commit phase.
     */
    private TwoPhaseCommitPhase twoPhaseCommitPhase;

    /**
     * Prepare phase results.
     */
    private Map<String, Boolean> prepareResults;

    /**
     * Commit phase results.
     */
    private Map<String, Boolean> commitResults;

    /**
     * Rollback phase results.
     */
    private Map<String, Boolean> rollbackResults;

    /**
     * Total number of retry attempts.
     */
    @Builder.Default
    private Integer retryCount = 0;

    /**
     * Maximum retry attempts allowed.
     */
    @Builder.Default
    private Integer maxRetries = 3;

    /**
     * Current retry state.
     */
    private RetryState retryState;

    /**
     * Next retry time.
     */
    private LocalDateTime nextRetryAt;

    /**
     * Error message if transaction failed.
     */
    private String errorMessage;

    /**
     * Error code.
     */
    private String errorCode;

    /**
     * Error stack trace.
     */
    private String errorStackTrace;

    /**
     * Failed participant IDs.
     */
    private Set<String> failedParticipantIds;

    /**
     * Compensation log for saga transactions.
     */
    private String compensationLog;

    /**
     * Transaction priority.
     */
    @Builder.Default
    private Integer priority = 0;

    /**
     * Transaction tags.
     */
    private Set<String> tags;

    /**
     * Metadata associated with this transaction.
     */
    private Map<String, Object> metadata;

    /**
     * User who created this transaction.
     */
    private String createdBy;

    /**
     * User who last updated this transaction.
     */
    private String lastUpdatedBy;

    /**
     * Current version number.
     */
    @Builder.Default
    private Integer version = 1;

    /**
     * Timestamp when this transaction was created.
     */
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * Timestamp when this transaction was last modified.
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * Environment enumeration.
     */
    public enum Environment {
        DEV,
        STAGING,
        PROD,
        TEST
    }

    /**
     * Transaction type enumeration.
     */
    public enum TransactionType {
        TWO_PHASE_COMMIT,
        SAGA,
        COMPENSATING
    }

    /**
     * Transaction status enumeration.
     */
    public enum TransactionStatus {
        ACTIVE,
        PREPARING,
        PREPARED,
        COMMITTING,
        COMMITTED,
        ROLLING_BACK,
        ROLLED_BACK,
        TIMED_OUT,
        FAILED,
        CANCELLED
    }

    /**
     * Two-phase commit phase enumeration.
     */
    public enum TwoPhaseCommitPhase {
        NOT_STARTED,
        PREPARE,
        COMMIT,
        ROLLBACK,
        COMPLETED
    }

    /**
     * Retry state enumeration.
     */
    public enum RetryState {
        NOT_RETRYING,
        WAITING_TO_RETRY,
        RETRYING,
        MAX_RETRIES_EXCEEDED
    }

    /**
     * Transaction participant.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionParticipant {
        @NotBlank
        private String participantId;

        @NotBlank
        private String databaseName;

        private String connectionPoolName;

        @NotBlank
        private String endpoint;

        @Builder.Default
        private ParticipantStatus status = ParticipantStatus.PENDING;

        private Integer order;

        private String prepareQuery;

        private String commitQuery;

        private String rollbackQuery;

        private LocalDateTime preparedAt;

        private LocalDateTime committedAt;

        private LocalDateTime rolledBackAt;

        private String errorMessage;

        private Map<String, Object> context;
    }

    /**
     * Saga step.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SagaStep {
        @NotBlank
        private String stepId;

        @NotBlank
        private String stepName;

        @Builder.Default
        private SagaStepStatus status = SagaStepStatus.PENDING;

        private Integer order;

        @NotBlank
        private String action;

        private String compensatingAction;

        private LocalDateTime executedAt;

        private LocalDateTime compensatedAt;

        private String errorMessage;

        private Integer retryCount;

        private Map<String, Object> context;
    }

    /**
     * Participant status enumeration.
     */
    public enum ParticipantStatus {
        PENDING,
        PREPARING,
        PREPARED,
        COMMITTING,
        COMMITTED,
        ROLLING_BACK,
        ROLLED_BACK,
        FAILED
    }

    /**
     * Saga step status enumeration.
     */
    public enum SagaStepStatus {
        PENDING,
        RUNNING,
        COMPLETED,
        COMPENSATING,
        COMPENSATED,
        FAILED,
        SKIPPED
    }

    /**
     * Checks if transaction is in a terminal state.
     */
    public boolean isTerminalState() {
        return status == TransactionStatus.COMMITTED
                || status == TransactionStatus.ROLLED_BACK
                || status == TransactionStatus.TIMED_OUT
                || status == TransactionStatus.FAILED
                || status == TransactionStatus.CANCELLED;
    }

    /**
     * Checks if transaction has timed out.
     */
    public boolean isTimedOut() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    /**
     * Checks if transaction can be retried.
     */
    public boolean canRetry() {
        return status == TransactionStatus.FAILED
                && retryCount < maxRetries
                && transactionType == TransactionType.SAGA;
    }

    /**
     * Checks if all participants have prepared successfully.
     */
    public boolean allParticipantsPrepared() {
        if (participants == null) {
            return false;
        }
        return participants.stream()
                .allMatch(p -> p.getStatus() == ParticipantStatus.PREPARED);
    }

    /**
     * Checks if all participants have committed.
     */
    public boolean allParticipantsCommitted() {
        if (participants == null) {
            return false;
        }
        return participants.stream()
                .allMatch(p -> p.getStatus() == ParticipantStatus.COMMITTED);
    }

    /**
     * Gets the number of completed participants.
     */
    public long getCompletedParticipantCount() {
        if (participants == null) {
            return 0;
        }
        return participants.stream()
                .filter(p -> p.getStatus() == ParticipantStatus.COMMITTED)
                .count();
    }

    /**
     * Calculates transaction duration.
     */
    public void calculateDuration() {
        if (startTime != null) {
            LocalDateTime endTime = this.endTime != null ? this.endTime : LocalDateTime.now();
            durationMs = java.time.Duration.between(startTime, endTime).toMillis();
        }
    }

    /**
     * Sets expiration time based on timeout.
     */
    public void setExpirationTime() {
        if (timeoutMs != null && startTime != null) {
            expiresAt = startTime.plusNanos(timeoutMs * 1_000_000);
        }
    }
}
