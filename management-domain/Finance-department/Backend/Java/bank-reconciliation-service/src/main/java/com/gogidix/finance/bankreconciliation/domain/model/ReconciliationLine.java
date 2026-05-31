package com.gogidix.finance.bankreconciliation.domain.model;

import com.gogidix.finance.bankreconciliation.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Reconciliation Line Domain Entity
 * Individual reconciliation line items for matching transactions
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "reconciliation_lines")
public class ReconciliationLine extends BaseEntity {

    @Indexed
    @Field("line_id")
    private String lineId;

    @Indexed
    @Field("reconciliation_id")
    private String reconciliationId;

    @Indexed
    @Field("account_id")
    private String accountId;

    @Field("line_number")
    private Integer lineNumber;

    @Field("line_type")
    private LineType lineType;

    @Field("bank_transaction_id")
    private String bankTransactionId;

    @Field("bank_transaction_date")
    private LocalDate bankTransactionDate;

    @Field("bank_description")
    private String bankDescription;

    @Field("bank_reference")
    private String bankReference;

    @Field("bank_amount")
    private BigDecimal bankAmount;

    @Field("book_transaction_id")
    private String bookTransactionId;

    @Field("book_transaction_date")
    private LocalDate bookTransactionDate;

    @Field("book_description")
    private String bookDescription;

    @Field("book_reference")
    private String bookReference;

    @Field("book_amount")
    private BigDecimal bookAmount;

    @Field("amount_difference")
    private BigDecimal amountDifference;

    @Field("match_status")
    private MatchStatus matchStatus;

    @Field("match_confidence")
    private Double matchConfidence;

    @Field("matched_by")
    private MatchedBy matchedBy;

    @Field("matched_at")
    private java.time.Instant matchedAt;

    @Field("verified_by")
    private String verifiedBy;

    @Field("verified_at")
    private java.time.Instant verifiedAt;

    @Field("discrepancy_reason")
    private String discrepancyReason;

    @Field("discrepancy_category")
    private DiscrepancyCategory discrepancyCategory;

    @Field("action_required")
    private ActionRequired actionRequired;

    @Field("action_taken")
    private String actionTaken;

    @Field("notes")
    private String notes;

    @Field("currency")
    private String currency;

    @Field("auto_matched")
    private Boolean autoMatched;

    @Field("requires_manual_review")
    private Boolean requiresManualReview;

    public enum LineType {
        BANK_ONLY,
        BOOK_ONLY,
        MATCHED,
        DISCREPANCY,
        ADJUSTMENT
    }

    public enum MatchStatus {
        UNMATCHED,
        AUTO_MATCHED,
        MANUALLY_MATCHED,
        PENDING_REVIEW,
        DISCREPANCY,
        VERIFIED,
        REJECTED
    }

    public enum MatchedBy {
        SYSTEM,
        USER,
        RULE,
        AI
    }

    public enum DiscrepancyCategory {
        AMOUNT_MISMATCH,
        DATE_MISMATCH,
        MISSING_BANK_RECORD,
        MISSING_BOOK_RECORD,
        DUPLICATE_RECORD,
        CURRENCY_DIFFERENCE,
        TIMING_DIFFERENCE,
        OTHER
    }

    public enum ActionRequired {
        NONE,
        INVESTIGATE,
        ADJUST_BOOK,
        ADJUST_BANK,
        CONTACT_BANK,
        DOCUMENT_EXCEPTION,
        IGNORE
    }

    /**
     * Creates a new reconciliation line
     */
    public static ReconciliationLine create(String tenantId, String reconciliationId,
                                             String accountId, Integer lineNumber,
                                             LineType lineType) {
        ReconciliationLine line = new ReconciliationLine();
        line.setTenantId(tenantId);
        line.setReconciliationId(reconciliationId);
        line.setAccountId(accountId);
        line.setLineNumber(lineNumber);
        line.setLineType(lineType);
        line.setMatchStatus(MatchStatus.UNMATCHED);
        line.setRequiresManualReview(false);
        line.setAutoMatched(false);
        line.setAmountDifference(BigDecimal.ZERO);

        return line;
    }

    /**
     * Creates a matched line (both bank and book records)
     */
    public static ReconciliationLine createMatched(String tenantId, String reconciliationId,
                                                    String accountId, Integer lineNumber,
                                                    String bankTransactionId, LocalDate bankTransactionDate,
                                                    String bankDescription, BigDecimal bankAmount,
                                                    String bookTransactionId, LocalDate bookTransactionDate,
                                                    String bookDescription, BigDecimal bookAmount,
                                                    String currency) {
        ReconciliationLine line = create(tenantId, reconciliationId, accountId, lineNumber, LineType.MATCHED);

        line.setBankTransactionId(bankTransactionId);
        line.setBankTransactionDate(bankTransactionDate);
        line.setBankDescription(bankDescription);
        line.setBankAmount(bankAmount);

        line.setBookTransactionId(bookTransactionId);
        line.setBookTransactionDate(bookTransactionDate);
        line.setBookDescription(bookDescription);
        line.setBookAmount(bookAmount);

        line.setCurrency(currency);
        line.calculateDifference();
        line.determineMatchStatus();

        return line;
    }

    /**
     * Creates a bank-only line (no matching book record)
     */
    public static ReconciliationLine createBankOnly(String tenantId, String reconciliationId,
                                                     String accountId, Integer lineNumber,
                                                     String bankTransactionId, LocalDate bankTransactionDate,
                                                     String bankDescription, BigDecimal bankAmount,
                                                     String currency) {
        ReconciliationLine line = create(tenantId, reconciliationId, accountId, lineNumber, LineType.BANK_ONLY);

        line.setBankTransactionId(bankTransactionId);
        line.setBankTransactionDate(bankTransactionDate);
        line.setBankDescription(bankDescription);
        line.setBankAmount(bankAmount);
        line.setCurrency(currency);
        line.setMatchStatus(MatchStatus.PENDING_REVIEW);
        line.setRequiresManualReview(true);

        return line;
    }

    /**
     * Creates a book-only line (no matching bank record)
     */
    public static ReconciliationLine createBookOnly(String tenantId, String reconciliationId,
                                                     String accountId, Integer lineNumber,
                                                     String bookTransactionId, LocalDate bookTransactionDate,
                                                     String bookDescription, BigDecimal bookAmount,
                                                     String currency) {
        ReconciliationLine line = create(tenantId, reconciliationId, accountId, lineNumber, LineType.BOOK_ONLY);

        line.setBookTransactionId(bookTransactionId);
        line.setBookTransactionDate(bookTransactionDate);
        line.setBookDescription(bookDescription);
        line.setBookAmount(bookAmount);
        line.setCurrency(currency);
        line.setMatchStatus(MatchStatus.PENDING_REVIEW);
        line.setRequiresManualReview(true);

        return line;
    }

    /**
     * Calculates the amount difference
     */
    public void calculateDifference() {
        if (this.bankAmount != null && this.bookAmount != null) {
            this.amountDifference = this.bankAmount.subtract(this.bookAmount);
        } else if (this.bankAmount != null) {
            this.amountDifference = this.bankAmount;
        } else if (this.bookAmount != null) {
            this.amountDifference = this.bookAmount.negate();
        } else {
            this.amountDifference = BigDecimal.ZERO;
        }
        updateTimestamp();
    }

    /**
     * Determines match status based on amounts
     */
    public void determineMatchStatus() {
        if (this.bankAmount != null && this.bookAmount != null) {
            if (this.bankAmount.compareTo(this.bookAmount) == 0) {
                this.matchStatus = MatchStatus.AUTO_MATCHED;
                this.lineType = LineType.MATCHED;
            } else {
                this.matchStatus = MatchStatus.DISCREPANCY;
                this.lineType = LineType.DISCREPANCY;
                this.discrepancyCategory = DiscrepancyCategory.AMOUNT_MISMATCH;
            }
        }
        updateTimestamp();
    }

    /**
     * Marks line as auto-matched
     */
    public void markAsAutoMatched(Double confidence) {
        this.matchStatus = MatchStatus.AUTO_MATCHED;
        this.matchedBy = MatchedBy.SYSTEM;
        this.autoMatched = true;
        this.matchConfidence = confidence;
        this.matchedAt = java.time.Instant.now();
        updateTimestamp();
    }

    /**
     * Marks line as manually matched
     */
    public void markAsManuallyMatched(String userId) {
        this.matchStatus = MatchStatus.MANUALLY_MATCHED;
        this.matchedBy = MatchedBy.USER;
        this.autoMatched = false;
        this.matchedAt = java.time.Instant.now();
        this.requiresManualReview = false;
        updateTimestamp();
    }

    /**
     * Verifies the match
     */
    public void verify(String verifiedBy) {
        if (this.matchStatus != MatchStatus.AUTO_MATCHED && this.matchStatus != MatchStatus.MANUALLY_MATCHED) {
            throw new IllegalStateException("Can only verify matched lines");
        }
        this.matchStatus = MatchStatus.VERIFIED;
        this.verifiedBy = verifiedBy;
        this.verifiedAt = java.time.Instant.now();
        updateTimestamp();
    }

    /**
     * Marks line as discrepancy
     */
    public void markAsDiscrepancy(DiscrepancyCategory category, String reason) {
        this.matchStatus = MatchStatus.DISCREPANCY;
        this.lineType = LineType.DISCREPANCY;
        this.discrepancyCategory = category;
        this.discrepancyReason = reason;
        this.requiresManualReview = true;
        updateTimestamp();
    }

    /**
     * Rejects the match
     */
    public void reject(String reason) {
        this.matchStatus = MatchStatus.REJECTED;
        this.discrepancyReason = reason;
        this.requiresManualReview = true;
        updateTimestamp();
    }

    /**
     * Sets action required
     */
    public void setActionRequired(ActionRequired action, String description) {
        this.actionRequired = action;
        this.actionTaken = description;
        updateTimestamp();
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
     * Checks if line is matched
     */
    public boolean isMatched() {
        return this.matchStatus == MatchStatus.AUTO_MATCHED ||
               this.matchStatus == MatchStatus.MANUALLY_MATCHED ||
               this.matchStatus == MatchStatus.VERIFIED;
    }

    /**
     * Checks if line has discrepancy
     */
    public boolean hasDiscrepancy() {
        return this.matchStatus == MatchStatus.DISCREPANCY ||
               this.discrepancyCategory != null;
    }

    /**
     * Checks if line needs review
     */
    public boolean needsReview() {
        return this.requiresManualReview != null && this.requiresManualReview ||
               this.matchStatus == MatchStatus.PENDING_REVIEW;
    }
}
