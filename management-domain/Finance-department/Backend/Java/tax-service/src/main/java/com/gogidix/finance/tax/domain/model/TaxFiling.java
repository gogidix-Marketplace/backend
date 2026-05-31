package com.gogidix.finance.tax.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tax Filing Domain Entity
 * Multi-tenant tax filing records with status tracking
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "tax_filings")
@CompoundIndex(def = "{'tenantId': 1, 'filingPeriod': 1, 'jurisdiction': 1}", name = "tenant_period_jurisdiction_idx")
public class TaxFiling extends BaseEntity {

    @Id
    private String id;

    @Field("tenant_id")
    @Indexed
    private String tenantId;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    @Indexed(unique = true)
    @Field("filing_id")
    private String filingId;

    @Field("filing_period")
    @Indexed
    private YearMonth filingPeriod;

    @Field("filing_type")
    @Indexed
    private FilingType filingType;

    @Field("jurisdiction")
    @Indexed
    private TaxRate.Jurisdiction jurisdiction;

    @Field("tax_type")
    @Indexed
    private TaxRate.TaxType taxType;

    @Field("currency")
    private String currency;

    @Field("gross_sales")
    private BigDecimal grossSales;

    @Field("taxable_sales")
    private BigDecimal taxableSales;

    @Field("exempt_sales")
    private BigDecimal exemptSales;

    @Field("total_tax_collected")
    private BigDecimal totalTaxCollected;

    @Field("total_tax_paid")
    private BigDecimal totalTaxPaid;

    @Field("tax_due")
    private BigDecimal taxDue;

    @Field("tax_refund")
    private BigDecimal taxRefund;

    @Field("penalty")
    private BigDecimal penalty;

    @Field("interest")
    private BigDecimal interest;

    @Field("net_amount")
    private BigDecimal netAmount;

    @Field("status")
    private FilingStatus status;

    @Field("submission_date")
    private LocalDate submissionDate;

    @Field("due_date")
    private LocalDate dueDate;

    @Field("filing_date")
    private Instant filingDate;

    @Field("acknowledgement_date")
    private Instant acknowledgementDate;

    @Field("acknowledgement_number")
    private String acknowledgementNumber;

    @Field("submitted_by")
    private String submittedBy;

    @Field("approved_by")
    private String approvedBy;

    @Field("approved_at")
    private Instant approvedAt;

    @Field("calculation_ids")
    @Builder.Default
    private List<String> calculationIds = new ArrayList<>();

    @Field("adjustments")
    @Builder.Default
    private List<Adjustment> adjustments = new ArrayList<>();

    @Field("attachments")
    @Builder.Default
    private List<Attachment> attachments = new ArrayList<>();

    @Field("notes")
    private String notes;

    @Field("internal_notes")
    private String internalNotes;

    @Field("payment_reference")
    private String paymentReference;

    @Field("payment_date")
    private Instant paymentDate;

    @Field("metadata")
    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    /**
     * Filing Type enum
     */
    public enum FilingType {
        MONTHLY_RETURN,
        QUARTERLY_RETURN,
        ANNUAL_RETURN,
        ADVANCE_TAX,
        WITHHOLDING_RETURN,
        ADJUSTMENT_RETURN,
        FINAL_RETURN,
        AMENDED_RETURN
    }

    /**
     * Filing Status enum
     */
    public enum FilingStatus {
        DRAFT,
        PENDING_REVIEW,
        PENDING_SUBMISSION,
        SUBMITTED,
        PROCESSING,
        ACCEPTED,
        REJECTED,
        ASSESSED,
        PAID,
        OVERDUE,
        CANCELLED,
        ARCHIVED
    }

    /**
     * Adjustment record
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Adjustment {
        private String adjustmentId;
        private String adjustmentType;
        private BigDecimal amount;
        private String reason;
        private String reference;
        private LocalDate adjustmentDate;
    }

    /**
     * Attachment record
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Attachment {
        private String attachmentId;
        private String fileName;
        private String fileType;
        private Long fileSize;
        private String storageLocation;
        private String url;
        private Instant uploadedAt;
        private String uploadedBy;
    }

    /**
     * Creates a new tax filing
     */
    public static TaxFiling create(String tenantId, YearMonth filingPeriod, FilingType filingType,
                                     TaxRate.Jurisdiction jurisdiction, TaxRate.TaxType taxType,
                                     String currency, LocalDate dueDate, String submittedBy) {
        TaxFiling filing = TaxFiling.builder()
            .tenantId(tenantId)
            .filingId(generateFilingId())
            .filingPeriod(filingPeriod)
            .filingType(filingType)
            .jurisdiction(jurisdiction)
            .taxType(taxType)
            .currency(currency)
            .grossSales(BigDecimal.ZERO)
            .taxableSales(BigDecimal.ZERO)
            .exemptSales(BigDecimal.ZERO)
            .totalTaxCollected(BigDecimal.ZERO)
            .totalTaxPaid(BigDecimal.ZERO)
            .taxDue(BigDecimal.ZERO)
            .taxRefund(BigDecimal.ZERO)
            .penalty(BigDecimal.ZERO)
            .interest(BigDecimal.ZERO)
            .netAmount(BigDecimal.ZERO)
            .status(FilingStatus.DRAFT)
            .dueDate(dueDate)
            .submittedBy(submittedBy)
            .calculationIds(new ArrayList<>())
            .adjustments(new ArrayList<>())
            .attachments(new ArrayList<>())
            .metadata(new HashMap<>())
            .build();

        filing.validate();
        return filing;
    }

    /**
     * Updates the filing figures
     */
    public void updateFigures(BigDecimal grossSales, BigDecimal taxableSales, BigDecimal exemptSales,
                               BigDecimal totalTaxCollected, BigDecimal totalTaxPaid) {
        if (this.status != FilingStatus.DRAFT && this.status != FilingStatus.PENDING_REVIEW) {
            throw new IllegalStateException("Cannot update figures for submitted filings");
        }

        this.grossSales = grossSales;
        this.taxableSales = taxableSales;
        this.exemptSales = exemptSales;
        this.totalTaxCollected = totalTaxCollected;
        this.totalTaxPaid = totalTaxPaid;

        // Calculate tax due or refund
        BigDecimal netTax = totalTaxCollected.subtract(totalTaxPaid);
        if (netTax.compareTo(BigDecimal.ZERO) >= 0) {
            this.taxDue = netTax;
            this.taxRefund = BigDecimal.ZERO;
        } else {
            this.taxDue = BigDecimal.ZERO;
            this.taxRefund = netTax.abs();
        }

        this.netAmount = this.taxDue.add(this.penalty).add(this.interest).subtract(this.taxRefund);
        this.updateTimestamp();
    }

    /**
     * Submits for review
     */
    public void submitForReview() {
        if (this.status != FilingStatus.DRAFT) {
            throw new IllegalStateException("Can only submit draft filings for review");
        }

        validateFigures();

        this.status = FilingStatus.PENDING_REVIEW;
        this.updateTimestamp();
    }

    /**
     * Submits the filing
     */
    public void submit() {
        if (this.status != FilingStatus.PENDING_REVIEW && this.status != FilingStatus.PENDING_SUBMISSION) {
            throw new IllegalStateException("Can only submit filings that are pending review or submission");
        }

        validateFigures();

        this.status = FilingStatus.SUBMITTED;
        this.submissionDate = LocalDate.now();
        this.filingDate = Instant.now();
        this.updateTimestamp();
    }

    /**
     * Acknowledges the filing (received by tax authority)
     */
    public void acknowledge(String acknowledgementNumber) {
        if (this.status != FilingStatus.SUBMITTED && this.status != FilingStatus.PROCESSING) {
            throw new IllegalStateException("Can only acknowledge submitted or processing filings");
        }

        this.status = FilingStatus.PROCESSING;
        this.acknowledgementNumber = acknowledgementNumber;
        this.acknowledgementDate = Instant.now();
        this.updateTimestamp();
    }

    /**
     * Marks the filing as accepted
     */
    public void accept(String approvedBy) {
        if (this.status != FilingStatus.PROCESSING) {
            throw new IllegalStateException("Can only accept processing filings");
        }

        this.status = FilingStatus.ACCEPTED;
        this.approvedBy = approvedBy;
        this.approvedAt = Instant.now();
        this.updateTimestamp();
    }

    /**
     * Marks the filing as rejected
     */
    public void reject(String reason) {
        if (this.status != FilingStatus.SUBMITTED && this.status != FilingStatus.PROCESSING) {
            throw new IllegalStateException("Can only reject submitted or processing filings");
        }

        this.status = FilingStatus.REJECTED;
        this.notes = (this.notes != null ? this.notes + "\n" : "") + "REJECTED: " + reason;
        this.updateTimestamp();
    }

    /**
     * Adds an adjustment
     */
    public void addAdjustment(String adjustmentType, BigDecimal amount, String reason, String reference) {
        if (this.status == FilingStatus.ACCEPTED || this.status == FilingStatus.PAID) {
            throw new IllegalStateException("Cannot add adjustments to accepted or paid filings");
        }

        Adjustment adjustment = Adjustment.builder()
            .adjustmentId(generateAdjustmentId())
            .adjustmentType(adjustmentType)
            .amount(amount)
            .reason(reason)
            .reference(reference)
            .adjustmentDate(LocalDate.now())
            .build();

        this.adjustments.add(adjustment);

        // Update net amount
        if ("PENALTY".equals(adjustmentType)) {
            this.penalty = this.penalty.add(amount);
        } else if ("INTEREST".equals(adjustmentType)) {
            this.interest = this.interest.add(amount);
        }

        recalculateNetAmount();
        this.updateTimestamp();
    }

    /**
     * Adds an attachment
     */
    public void addAttachment(String fileName, String fileType, Long fileSize,
                              String storageLocation, String url, String uploadedBy) {
        Attachment attachment = Attachment.builder()
            .attachmentId(java.util.UUID.randomUUID().toString())
            .fileName(fileName)
            .fileType(fileType)
            .fileSize(fileSize)
            .storageLocation(storageLocation)
            .url(url)
            .uploadedAt(Instant.now())
            .uploadedBy(uploadedBy)
            .build();

        if (this.attachments == null) {
            this.attachments = new ArrayList<>();
        }

        this.attachments.add(attachment);
        this.updateTimestamp();
    }

    /**
     * Links a calculation to this filing
     */
    public void linkCalculation(String calculationId) {
        if (this.calculationIds == null) {
            this.calculationIds = new ArrayList<>();
        }

        if (!this.calculationIds.contains(calculationId)) {
            this.calculationIds.add(calculationId);
            this.updateTimestamp();
        }
    }

    /**
     * Marks the filing as paid
     */
    public void markAsPaid(String paymentReference) {
        if (this.status != FilingStatus.ACCEPTED && this.status != FilingStatus.ASSESSED) {
            throw new IllegalStateException("Can only mark accepted or assessed filings as paid");
        }

        if (this.netAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("No payment due for this filing");
        }

        this.status = FilingStatus.PAID;
        this.paymentReference = paymentReference;
        this.paymentDate = Instant.now();
        this.updateTimestamp();
    }

    /**
     * Checks if the filing is overdue
     */
    public boolean isOverdue() {
        if (this.dueDate == null) {
            return false;
        }

        LocalDate today = LocalDate.now();
        boolean pastDueDate = today.isAfter(this.dueDate);
        boolean notPaid = this.status != FilingStatus.PAID &&
                         this.status != FilingStatus.ARCHIVED &&
                         this.status != FilingStatus.CANCELLED;

        return pastDueDate && notPaid;
    }

    /**
     * Calculates penalty for late filing
     */
    public BigDecimal calculateLatePenalty(BigDecimal penaltyRatePerDay) {
        if (this.dueDate == null || !isOverdue()) {
            return BigDecimal.ZERO;
        }

        long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(this.dueDate, LocalDate.now());
        return this.taxDue.multiply(penaltyRatePerDay)
            .multiply(new BigDecimal(daysOverdue))
            .divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Adds metadata
     */
    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
        this.updateTimestamp();
    }

    /**
     * Archives the filing
     */
    public void archive() {
        if (this.status != FilingStatus.PAID && this.status != FilingStatus.ACCEPTED) {
            throw new IllegalStateException("Can only archive paid or accepted filings");
        }

        this.status = FilingStatus.ARCHIVED;
        this.updateTimestamp();
    }

    /**
     * Cancels the filing
     */
    public void cancel(String reason) {
        if (this.status == FilingStatus.SUBMITTED || this.status == FilingStatus.PROCESSING ||
            this.status == FilingStatus.ACCEPTED || this.status == FilingStatus.PAID) {
            throw new IllegalStateException("Cannot cancel submitted, processing, accepted, or paid filings");
        }

        this.status = FilingStatus.CANCELLED;
        this.notes = (this.notes != null ? this.notes + "\n" : "") + "CANCELLED: " + reason;
        this.updateTimestamp();
    }

    /**
     * Validates the filing
     */
    public void validate() {
        if (this.filingPeriod == null) {
            throw new IllegalArgumentException("Filing period is required");
        }

        if (this.filingType == null) {
            throw new IllegalArgumentException("Filing type is required");
        }

        if (this.jurisdiction == null) {
            throw new IllegalArgumentException("Jurisdiction is required");
        }

        if (this.taxType == null) {
            throw new IllegalArgumentException("Tax type is required");
        }

        if (this.currency == null || this.currency.isBlank()) {
            throw new IllegalArgumentException("Currency is required");
        }

        if (this.dueDate == null) {
            throw new IllegalArgumentException("Due date is required");
        }
    }

    private void validateFigures() {
        if (this.grossSales == null || this.grossSales.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Gross sales must be non-negative");
        }

        if (this.taxableSales == null || this.taxableSales.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Taxable sales must be non-negative");
        }

        if (this.totalTaxCollected == null || this.totalTaxCollected.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total tax collected must be non-negative");
        }
    }

    private void recalculateNetAmount() {
        this.netAmount = this.taxDue.add(this.penalty).add(this.interest).subtract(this.taxRefund);
    }

    private static String generateFilingId() {
        return "TF-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private static String generateAdjustmentId() {
        return "ADJ-" + java.util.UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
