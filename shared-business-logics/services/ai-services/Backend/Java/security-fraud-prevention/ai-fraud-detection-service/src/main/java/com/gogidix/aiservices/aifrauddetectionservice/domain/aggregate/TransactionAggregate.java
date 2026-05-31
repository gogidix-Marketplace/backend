package com.gogidix.aiservices.aifrauddetectionservice.domain.aggregate;

import com.gogidix.aiservices.aifrauddetectionservice.domain.event.TransactionFlaggedEvent;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.Transaction;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Domain Aggregate for Transaction monitoring and fraud flagging.
 * This is the aggregate root for transaction operations.
 *
 * Enforces invariants:
 * - Transaction amounts must be positive
 * - Flagged transactions cannot be modified
 * - All state changes emit domain events
 */
@Aggregate(
    value = "Transaction Aggregate Root",
    type = "Transaction"
)
@Getter
@Builder
public class TransactionAggregate {

    private final String transactionId;
    private final String userId;
    private final String tenantId;
    private final BigDecimal amount;
    private final String merchant;
    private final Instant timestamp;
    private final String currency;
    private final java.util.Map<String, Object> metadata;
    private TransactionStatus status;
    private final List<AnalysisNote> analysisNotes;
    private final List<DomainEventWrapper> domainEvents;
    private String flaggedReason;
    private String flaggedBy;
    private Instant flaggedAt;

    /**
     * Transaction status tracking.
     */
    public enum TransactionStatus {
        PENDING,
        PROCESSING,
        APPROVED,
        FLAGGED_FOR_REVIEW,
        CONFIRMED_FRAUD,
        CONFIRMED_CLEAN,
        REJECTED,
        CANCELLED
    }

    /**
     * Analysis notes attached to the transaction.
     */
    @Getter
    @lombok.AllArgsConstructor
    public static class AnalysisNote {
        private final String noteId;
        private final String note;
        private final String addedBy;
        private final Instant addedAt;

        public static AnalysisNote create(String note, String addedBy) {
            return new AnalysisNote(
                    UUID.randomUUID().toString(),
                    note,
                    addedBy,
                    Instant.now()
            );
        }
    }

    /**
     * Wrapper for domain events until they are dispatched.
     */
    @Getter
    @lombok.AllArgsConstructor
    public static class DomainEventWrapper {
        private final String eventId;
        private final Object event;
        private final Instant occurredAt;
        private final String aggregateId;

        public static DomainEventWrapper create(Object event, String aggregateId) {
            return new DomainEventWrapper(
                    UUID.randomUUID().toString(),
                    event,
                    Instant.now(),
                    aggregateId
            );
        }
    }

    /**
     * Creates a new transaction aggregate from a Transaction value object.
     */
    public static TransactionAggregate create(Transaction transaction) {
        return TransactionAggregate.builder()
                .transactionId(transaction.getTransactionId())
                .userId(transaction.getUserId())
                .tenantId(transaction.getTenantId())
                .amount(transaction.getAmount())
                .merchant(transaction.getMerchant())
                .timestamp(transaction.getTimestamp())
                .currency(transaction.getCurrency())
                .metadata(transaction.getMetadata() != null ? transaction.getMetadata() : new java.util.HashMap<>())
                .status(TransactionStatus.PENDING)
                .analysisNotes(new ArrayList<>())
                .domainEvents(new ArrayList<>())
                .build();
    }

    /**
     * Creates a new transaction with explicit parameters.
     */
    public static TransactionAggregate create(
            String transactionId,
            String userId,
            String tenantId,
            BigDecimal amount,
            String merchant,
            String currency,
            java.util.Map<String, Object> metadata) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive");
        }

        return TransactionAggregate.builder()
                .transactionId(transactionId)
                .userId(userId)
                .tenantId(tenantId)
                .amount(amount)
                .merchant(merchant)
                .timestamp(Instant.now())
                .currency(currency)
                .metadata(metadata != null ? metadata : new java.util.HashMap<>())
                .status(TransactionStatus.PENDING)
                .analysisNotes(new ArrayList<>())
                .domainEvents(new ArrayList<>())
                .build();
    }

    /**
     * Flags the transaction as suspected fraud.
     * Emits TransactionFlaggedEvent.
     *
     * @param reason Reason for flagging
     * @param flaggedBy User or system that flagged the transaction
     * @param severity Severity level of the suspicion
     */
    public void flagAsFraud(String reason, String flaggedBy, String severity) {
        if (this.status == TransactionStatus.CONFIRMED_FRAUD ||
            this.status == TransactionStatus.CONFIRMED_CLEAN) {
            throw new IllegalStateException("Cannot flag a transaction with final status: " + this.status);
        }

        this.status = TransactionStatus.FLAGGED_FOR_REVIEW;
        this.flaggedReason = reason;
        this.flaggedBy = flaggedBy;
        this.flaggedAt = Instant.now();

        TransactionFlaggedEvent event = TransactionFlaggedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .transactionId(this.transactionId)
                .flaggedBy(flaggedBy)
                .reason(reason)
                .severity(severity)
                .occurredAt(Instant.now())
                .aggregateId(this.transactionId)
                .amount(this.amount)
                .currency(this.currency)
                .userId(this.userId)
                .tenantId(this.tenantId)
                .build();

        addDomainEvent(DomainEventWrapper.create(event, this.transactionId));
    }

    /**
     * Adds an analysis note to the transaction.
     *
     * @param note The note content
     * @param addedBy Who added the note
     */
    public void addAnalysisNote(String note, String addedBy) {
        AnalysisNote analysisNote = AnalysisNote.create(note, addedBy);
        this.analysisNotes.add(analysisNote);
    }

    /**
     * Confirms the transaction as fraudulent.
     */
    public void confirmAsFraud(String confirmedBy) {
        if (this.status != TransactionStatus.FLAGGED_FOR_REVIEW &&
            this.status != TransactionStatus.PROCESSING &&
            this.status != TransactionStatus.PENDING) {
            throw new IllegalStateException("Cannot confirm as fraud from status: " + this.status);
        }

        this.status = TransactionStatus.CONFIRMED_FRAUD;
        addAnalysisNote("Confirmed as fraud by: " + confirmedBy, confirmedBy);
    }

    /**
     * Confirms the transaction as clean (not fraudulent).
     */
    public void confirmAsClean(String confirmedBy) {
        if (this.status != TransactionStatus.FLAGGED_FOR_REVIEW &&
            this.status != TransactionStatus.PROCESSING &&
            this.status != TransactionStatus.PENDING) {
            throw new IllegalStateException("Cannot confirm as clean from status: " + this.status);
        }

        this.status = TransactionStatus.CONFIRMED_CLEAN;
        addAnalysisNote("Confirmed as clean by: " + confirmedBy, confirmedBy);
    }

    /**
     * Rejects the transaction.
     */
    public void reject(String rejectedBy, String reason) {
        if (this.status == TransactionStatus.REJECTED ||
            this.status == TransactionStatus.CONFIRMED_FRAUD ||
            this.status == TransactionStatus.CONFIRMED_CLEAN) {
            throw new IllegalStateException("Cannot reject a transaction with status: " + this.status);
        }

        this.status = TransactionStatus.REJECTED;
        addAnalysisNote("Rejected by: " + rejectedBy + ". Reason: " + reason, rejectedBy);
    }

    /**
     * Approves the transaction.
     */
    public void approve(String approvedBy) {
        if (this.status == TransactionStatus.APPROVED ||
            this.status == TransactionStatus.REJECTED ||
            this.status == TransactionStatus.CONFIRMED_FRAUD ||
            this.status == TransactionStatus.CONFIRMED_CLEAN) {
            throw new IllegalStateException("Cannot approve a transaction with status: " + this.status);
        }

        this.status = TransactionStatus.APPROVED;
        addAnalysisNote("Approved by: " + approvedBy, approvedBy);
    }

    /**
     * Marks transaction as currently being processed.
     */
    public void markAsProcessing() {
        if (this.status != TransactionStatus.PENDING) {
            throw new IllegalStateException("Cannot mark as processing from status: " + this.status);
        }
        this.status = TransactionStatus.PROCESSING;
    }

    /**
     * Cancels the transaction.
     */
    public void cancel(String cancelledBy) {
        if (this.status == TransactionStatus.APPROVED ||
            this.status == TransactionStatus.REJECTED ||
            this.status == TransactionStatus.CONFIRMED_FRAUD ||
            this.status == TransactionStatus.CONFIRMED_CLEAN) {
            throw new IllegalStateException("Cannot cancel a transaction with status: " + this.status);
        }

        this.status = TransactionStatus.CANCELLED;
        addAnalysisNote("Cancelled by: " + cancelledBy, cancelledBy);
    }

    /**
     * Adds a domain event to the list of uncommitted events.
     */
    private void addDomainEvent(DomainEventWrapper event) {
        this.domainEvents.add(event);
    }

    /**
     * Returns and clears all uncommitted domain events.
     */
    public List<DomainEventWrapper> getAndClearDomainEvents() {
        List<DomainEventWrapper> events = new ArrayList<>(this.domainEvents);
        this.domainEvents.clear();
        return events;
    }

    /**
     * Returns uncommitted domain events without clearing them.
     */
    public List<DomainEventWrapper> getUncommittedDomainEvents() {
        return Collections.unmodifiableList(this.domainEvents);
    }

    /**
     * Checks if the transaction is flagged.
     */
    public boolean isFlagged() {
        return this.status == TransactionStatus.FLAGGED_FOR_REVIEW;
    }

    /**
     * Checks if the transaction has a final status.
     */
    public boolean hasFinalStatus() {
        return this.status == TransactionStatus.APPROVED ||
               this.status == TransactionStatus.REJECTED ||
               this.status == TransactionStatus.CONFIRMED_FRAUD ||
               this.status == TransactionStatus.CONFIRMED_CLEAN ||
               this.status == TransactionStatus.CANCELLED;
    }

    /**
     * Gets all analysis notes.
     */
    public List<AnalysisNote> getAnalysisNotes() {
        return Collections.unmodifiableList(this.analysisNotes);
    }

    /**
     * Converts this aggregate to a Transaction value object.
     */
    public Transaction toValueObject() {
        return Transaction.builder()
                .transactionId(this.transactionId)
                .userId(this.userId)
                .tenantId(this.tenantId)
                .amount(this.amount)
                .merchant(this.merchant)
                .timestamp(this.timestamp)
                .currency(this.currency)
                .metadata(this.metadata)
                .build();
    }

    /**
     * Checks if the amount exceeds a given threshold.
     */
    public boolean exceedsAmount(BigDecimal threshold) {
        return this.amount.compareTo(threshold) > 0;
    }

    /**
     * Gets the transaction age (time since creation).
     */
    public long getAgeInSeconds() {
        return Instant.now().getEpochSecond() - this.timestamp.getEpochSecond();
    }
}
