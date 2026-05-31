package com.gogidix.finance.bankreconciliation.domain.model;

import com.gogidix.finance.bankreconciliation.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Reconciliation Domain Entity
 * Multi-tenant reconciliation session with status tracking
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "reconciliations")
public class Reconciliation extends BaseEntity {

    @Indexed
    @Field("reconciliation_id")
    private String reconciliationId;

    @Indexed
    @Field("account_id")
    private String accountId;

    @Field("account_number")
    private String accountNumber;

    @Indexed
    @Field("statement_id")
    private String statementId;

    @Field("reconciliation_date")
    private LocalDate reconciliationDate;

    @Field("period_start")
    private LocalDate periodStart;

    @Field("period_end")
    private LocalDate periodEnd;

    @Field("status")
    private ReconciliationStatus status;

    @Field("starting_balance")
    private java.math.BigDecimal startingBalance;

    @Field("ending_balance")
    private java.math.BigDecimal endingBalance;

    @Field("book_balance")
    private java.math.BigDecimal bookBalance;

    @Field("bank_balance")
    private java.math.BigDecimal bankBalance;

    @Field("difference")
    private java.math.BigDecimal difference;

    @Field("tolerance")
    private java.math.BigDecimal tolerance;

    @Field("is_balanced")
    private Boolean isBalanced;

    @Field("reconciled_by")
    private String reconciledBy;

    @Field("reconciled_at")
    private Instant reconciledAt;

    @Field("approved_by")
    private String approvedBy;

    @Field("approved_at")
    private Instant approvedAt;

    @Field("line_count")
    private Integer lineCount;

    @Field("matched_count")
    private Integer matchedCount;

    @Field("unmatched_count")
    private Integer unmatchedCount;

    @Field("discrepancy_count")
    private Integer discrepancyCount;

    @Field("notes")
    private String notes;

    @Field("auto_reconciled")
    private Boolean autoReconciled;

    @Field("reconciliation_method")
    private ReconciliationMethod reconciliationMethod;

    @Field("completion_percentage")
    private Integer completionPercentage;

    @Field("error_message")
    private String errorMessage;

    public enum ReconciliationStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        CANCELLED,
        AWAITING_APPROVAL,
        APPROVED
    }

    public enum ReconciliationMethod {
        AUTOMATIC,
        MANUAL,
        HYBRID,
        RULE_BASED,
        AI_ASSISTED
    }

    /**
     * Creates a new reconciliation session
     */
    public static Reconciliation create(String tenantId, String accountId, String accountNumber,
                                        String statementId, LocalDate reconciliationDate,
                                        LocalDate periodStart, LocalDate periodEnd,
                                        java.math.BigDecimal startingBalance, java.math.BigDecimal endingBalance,
                                        ReconciliationMethod method) {
        Reconciliation reconciliation = new Reconciliation();
        reconciliation.setTenantId(tenantId);
        reconciliation.setAccountId(accountId);
        reconciliation.setAccountNumber(accountNumber);
        reconciliation.setStatementId(statementId);
        reconciliation.setReconciliationDate(reconciliationDate);
        reconciliation.setPeriodStart(periodStart);
        reconciliation.setPeriodEnd(periodEnd);
        reconciliation.setStatus(ReconciliationStatus.PENDING);
        reconciliation.setStartingBalance(startingBalance);
        reconciliation.setEndingBalance(endingBalance);
        reconciliation.setBookBalance(startingBalance);
        reconciliation.setBankBalance(startingBalance);
        reconciliation.setDifference(java.math.BigDecimal.ZERO);
        reconciliation.setTolerance(new java.math.BigDecimal("0.01"));
        reconciliation.setIsBalanced(false);
        reconciliation.setLineCount(0);
        reconciliation.setMatchedCount(0);
        reconciliation.setUnmatchedCount(0);
        reconciliation.setDiscrepancyCount(0);
        reconciliation.setAutoReconciled(false);
        reconciliation.setReconciliationMethod(method);
        reconciliation.setCompletionPercentage(0);

        return reconciliation;
    }

    /**
     * Starts the reconciliation process
     */
    public void start() {
        if (this.status != ReconciliationStatus.PENDING) {
            throw new IllegalStateException("Can only start pending reconciliations");
        }
        this.status = ReconciliationStatus.IN_PROGRESS;
        this.completionPercentage = 0;
        updateTimestamp();
    }

    /**
     * Completes the reconciliation
     */
    public void complete(String reconciledBy) {
        if (this.status != ReconciliationStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only complete in-progress reconciliations");
        }

        calculateDifference();
        this.reconciledBy = reconciledBy;
        this.reconciledAt = Instant.now();
        this.status = this.isBalanced ? ReconciliationStatus.COMPLETED : ReconciliationStatus.AWAITING_APPROVAL;
        this.completionPercentage = 100;
        updateTimestamp();
    }

    /**
     * Marks reconciliation as failed
     */
    public void fail(String errorMessage) {
        this.status = ReconciliationStatus.FAILED;
        this.errorMessage = errorMessage;
        updateTimestamp();
    }

    /**
     * Cancels the reconciliation
     */
    public void cancel() {
        if (this.status == ReconciliationStatus.COMPLETED || this.status == ReconciliationStatus.APPROVED) {
            throw new IllegalStateException("Cannot cancel completed or approved reconciliations");
        }
        this.status = ReconciliationStatus.CANCELLED;
        updateTimestamp();
    }

    /**
     * Approves the reconciliation
     */
    public void approve(String approvedBy) {
        if (this.status != ReconciliationStatus.AWAITING_APPROVAL && this.status != ReconciliationStatus.COMPLETED) {
            throw new IllegalStateException("Can only approve completed or awaiting approval reconciliations");
        }
        this.status = ReconciliationStatus.APPROVED;
        this.approvedBy = approvedBy;
        this.approvedAt = Instant.now();
        updateTimestamp();
    }

    /**
     * Updates balances
     */
    public void updateBalances(java.math.BigDecimal bookBalance, java.math.BigDecimal bankBalance) {
        this.bookBalance = bookBalance;
        this.bankBalance = bankBalance;
        calculateDifference();
        updateTimestamp();
    }

    /**
     * Calculates the difference between book and bank balance
     */
    public void calculateDifference() {
        if (this.bookBalance != null && this.bankBalance != null) {
            this.difference = this.bookBalance.subtract(this.bankBalance);

            if (this.tolerance != null) {
                this.isBalanced = this.difference.abs().compareTo(this.tolerance) <= 0;
            } else {
                this.isBalanced = this.difference.compareTo(java.math.BigDecimal.ZERO) == 0;
            }
        }
        updateTimestamp();
    }

    /**
     * Updates matching statistics
     */
    public void updateStatistics(int totalLines, int matchedLines, int unmatchedLines, int discrepancyLines) {
        this.lineCount = totalLines;
        this.matchedCount = matchedLines;
        this.unmatchedCount = unmatchedLines;
        this.discrepancyCount = discrepancyLines;

        if (totalLines > 0) {
            this.completionPercentage = (int) ((matchedLines * 100.0) / totalLines);
        }
        updateTimestamp();
    }

    /**
     * Increments matched count
     */
    public void incrementMatched() {
        this.matchedCount = (this.matchedCount != null ? this.matchedCount : 0) + 1;
        this.unmatchedCount = (this.unmatchedCount != null ? this.unmatchedCount : 1) - 1;
        if (this.unmatchedCount < 0) this.unmatchedCount = 0;
        updateCompletionPercentage();
        updateTimestamp();
    }

    /**
     * Increments discrepancy count
     */
    public void incrementDiscrepancy() {
        this.discrepancyCount = (this.discrepancyCount != null ? this.discrepancyCount : 0) + 1;
        updateTimestamp();
    }

    /**
     * Updates completion percentage
     */
    private void updateCompletionPercentage() {
        if (this.lineCount != null && this.lineCount > 0) {
            this.completionPercentage = (int) ((this.matchedCount * 100.0) / this.lineCount);
        }
    }

    /**
     * Sets tolerance for balancing
     */
    public void setTolerance(java.math.BigDecimal tolerance) {
        if (tolerance.compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Tolerance cannot be negative");
        }
        this.tolerance = tolerance;
        calculateDifference();
        updateTimestamp();
    }

    /**
     * Checks if reconciliation is balanced
     */
    public boolean isWithinTolerance() {
        calculateDifference();
        return this.isBalanced != null && this.isBalanced;
    }

    /**
     * Checks if reconciliation can be submitted for approval
     */
    public boolean canSubmitForApproval() {
        return this.status == ReconciliationStatus.IN_PROGRESS &&
               this.lineCount != null &&
               this.lineCount > 0 &&
               (this.matchedCount != null && this.matchedCount > 0);
    }

    /**
     * Adds notes
     */
    public void addNotes(String notes) {
        if (this.notes == null || this.notes.isBlank()) {
            this.notes = notes;
        } else {
            this.notes = this.notes + "\n" + notes;
        }
        updateTimestamp();
    }

    /**
     * Marks as auto-reconciled
     */
    public void markAsAutoReconciled() {
        this.autoReconciled = true;
        updateTimestamp();
    }
}
