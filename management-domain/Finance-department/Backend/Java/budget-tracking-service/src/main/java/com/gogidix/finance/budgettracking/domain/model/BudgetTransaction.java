package com.gogidix.finance.budgettracking.domain.model;

import com.gogidix.finance.budgettracking.domain.event.BudgetTransactionRecordedEvent;
import com.gogidix.finance.budgettracking.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Budget Transaction Domain Entity
 * Tracks individual transactions against budgets
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "budget_transactions")
public class BudgetTransaction extends BaseEntity {

    @Indexed(unique = true)
    private String transactionId;

    @Indexed
    private String tenantId;

    private String budgetId;

    private String budgetCode;

    private String referenceType;

    private String referenceId;

    private TransactionType transactionType;

    private BigDecimal amount;

    private String currency;

    private String description;

    private TransactionStatus status;

    private LocalDate transactionDate;

    private String category;

    private String department;

    private String costCenter;

    private String projectId;

    private String recordedBy;

    private String approvedBy;

    private Instant approvedAt;

    private String rejectionReason;

    private String relatedBudgetPeriod;

    private BigDecimal balanceBefore;

    private BigDecimal balanceAfter;

    private List<String> tags;

    private String notes;

    private String correlationId;

    @Builder.Default
    private List<BudgetTransactionRecordedEvent> domainEvents = new ArrayList<>();

    public enum TransactionType {
        ALLOCATION,
        COMMITMENT,
        EXPENDITURE,
        ADJUSTMENT,
        REVERSAL,
        TRANSFER_IN,
        TRANSFER_OUT,
        ENCUMBRANCE
    }

    public enum TransactionStatus {
        PENDING,
        RECORDED,
        APPROVED,
        REJECTED,
        REVERSED
    }

    /**
     * Creates a new budget transaction
     */
    public static BudgetTransaction create(String tenantId, String budgetId, String budgetCode,
                                           TransactionType type, BigDecimal amount, String currency,
                                           String description, String recordedBy) {
        BudgetTransaction transaction = BudgetTransaction.builder()
            .tenantId(tenantId)
            .budgetId(budgetId)
            .budgetCode(budgetCode)
            .transactionType(type)
            .amount(amount)
            .currency(currency)
            .description(description)
            .recordedBy(recordedBy)
            .status(TransactionStatus.PENDING)
            .transactionDate(LocalDate.now())
            .tags(new ArrayList<>())
            .build();

        transaction.addDomainEvent(BudgetTransactionRecordedEvent.builder()
            .transactionId(transaction.getTransactionId())
            .budgetId(budgetId)
            .budgetCode(budgetCode)
            .tenantId(tenantId)
            .transactionType(type.name())
            .amount(amount)
            .currency(currency)
            .timestamp(Instant.now())
            .eventType("BUDGET_TRANSACTION_CREATED")
            .build());

        return transaction;
    }

    /**
     * Records the transaction
     */
    public void record(BigDecimal balanceBefore, BigDecimal balanceAfter) {
        if (this.status != TransactionStatus.PENDING) {
            throw new IllegalStateException("Can only record pending transactions");
        }

        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.status = TransactionStatus.RECORDED;

        addDomainEvent(BudgetTransactionRecordedEvent.builder()
            .transactionId(this.transactionId)
            .budgetId(this.budgetId)
            .budgetCode(this.budgetCode)
            .tenantId(this.tenantId)
            .transactionType(this.transactionType.name())
            .amount(this.amount)
            .currency(this.currency)
            .balanceBefore(balanceBefore)
            .balanceAfter(balanceAfter)
            .timestamp(Instant.now())
            .eventType("BUDGET_TRANSACTION_RECORDED")
            .build());
    }

    /**
     * Approves the transaction
     */
    public void approve(String approver) {
        if (this.status != TransactionStatus.RECORDED) {
            throw new IllegalStateException("Can only approve recorded transactions");
        }

        this.status = TransactionStatus.APPROVED;
        this.approvedBy = approver;
        this.approvedAt = Instant.now();

        addDomainEvent(BudgetTransactionRecordedEvent.builder()
            .transactionId(this.transactionId)
            .budgetId(this.budgetId)
            .budgetCode(this.budgetCode)
            .tenantId(this.tenantId)
            .transactionType(this.transactionType.name())
            .amount(this.amount)
            .currency(this.currency)
            .timestamp(Instant.now())
            .eventType("BUDGET_TRANSACTION_APPROVED")
            .build());
    }

    /**
     * Rejects the transaction
     */
    public void reject(String rejecter, String reason) {
        if (this.status == TransactionStatus.APPROVED || this.status == TransactionStatus.REVERSED) {
            throw new IllegalStateException("Cannot reject approved or reversed transactions");
        }

        this.status = TransactionStatus.REJECTED;
        this.approvedBy = rejecter;
        this.rejectionReason = reason;

        addDomainEvent(BudgetTransactionRecordedEvent.builder()
            .transactionId(this.transactionId)
            .budgetId(this.budgetId)
            .budgetCode(this.budgetCode)
            .tenantId(this.tenantId)
            .transactionType(this.transactionType.name())
            .amount(this.amount)
            .currency(this.currency)
            .timestamp(Instant.now())
            .eventType("BUDGET_TRANSACTION_REJECTED")
            .build());
    }

    /**
     * Reverses the transaction
     */
    public void reverse(String reason) {
        if (this.status != TransactionStatus.APPROVED) {
            throw new IllegalStateException("Can only reverse approved transactions");
        }

        this.status = TransactionStatus.REVERSED;
        this.notes = (this.notes != null ? this.notes + "\n" : "") + "Reversed: " + reason;

        addDomainEvent(BudgetTransactionRecordedEvent.builder()
            .transactionId(this.transactionId)
            .budgetId(this.budgetId)
            .budgetCode(this.budgetCode)
            .tenantId(this.tenantId)
            .transactionType(this.transactionType.name())
            .amount(this.amount.negate())
            .currency(this.currency)
            .timestamp(Instant.now())
            .eventType("BUDGET_TRANSACTION_REVERSED")
            .build());
    }

    /**
     * Adds a tag to the transaction
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Removes a tag from the transaction
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }

    /**
     * Checks if the transaction affects budget balance
     */
    public boolean affectsBudgetBalance() {
        return this.transactionType == TransactionType.EXPENDITURE
            || this.transactionType == TransactionType.ALLOCATION
            || this.transactionType == TransactionType.ADJUSTMENT
            || this.transactionType == TransactionType.TRANSFER_IN
            || this.transactionType == TransactionType.TRANSFER_OUT;
    }

    public void addDomainEvent(BudgetTransactionRecordedEvent event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }
}
