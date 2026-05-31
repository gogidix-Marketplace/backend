package com.gogidix.finance.tax.infrastructure.persistence.mongo;

import com.gogidix.finance.tax.domain.model.TaxFiling;
import com.gogidix.finance.tax.domain.model.TaxRate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

/**
 * MongoDB Entity for TaxFiling
 * Infrastructure layer - separate from domain model
 */
@Document(collection = "tax_filings")
@CompoundIndex(def = "{'tenantId': 1, 'filingPeriod': 1, 'jurisdiction': 1}", name = "tenant_period_jurisdiction_idx")
public class TaxFilingEntity {

    @Id
    private String id;

    @Field("tenant_id")
    @Indexed
    private String tenantId;

    @Indexed(unique = true)
    @Field("filing_id")
    private String filingId;

    @Field("filing_period")
    @Indexed
    private YearMonth filingPeriod;

    @Field("filing_type")
    @Indexed
    private TaxFiling.FilingType filingType;

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
    private TaxFiling.FilingStatus status;

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
    private List<String> calculationIds;

    @Field("adjustments")
    private List<TaxFiling.Adjustment> adjustments;

    @Field("attachments")
    private List<TaxFiling.Attachment> attachments;

    @Field("notes")
    private String notes;

    @Field("internal_notes")
    private String internalNotes;

    @Field("payment_reference")
    private String paymentReference;

    @Field("payment_date")
    private Instant paymentDate;

    @Field("metadata")
    private Map<String, Object> metadata;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    // Default constructor for MongoDB
    public TaxFilingEntity() {}

    // Constructor from domain model
    public TaxFilingEntity(TaxFiling domain) {
        this.id = domain.getId();
        this.tenantId = domain.getTenantId();
        this.filingId = domain.getFilingId();
        this.filingPeriod = domain.getFilingPeriod();
        this.filingType = domain.getFilingType();
        this.jurisdiction = domain.getJurisdiction();
        this.taxType = domain.getTaxType();
        this.currency = domain.getCurrency();
        this.grossSales = domain.getGrossSales();
        this.taxableSales = domain.getTaxableSales();
        this.exemptSales = domain.getExemptSales();
        this.totalTaxCollected = domain.getTotalTaxCollected();
        this.totalTaxPaid = domain.getTotalTaxPaid();
        this.taxDue = domain.getTaxDue();
        this.taxRefund = domain.getTaxRefund();
        this.penalty = domain.getPenalty();
        this.interest = domain.getInterest();
        this.netAmount = domain.getNetAmount();
        this.status = domain.getStatus();
        this.submissionDate = domain.getSubmissionDate();
        this.dueDate = domain.getDueDate();
        this.filingDate = domain.getFilingDate();
        this.acknowledgementDate = domain.getAcknowledgementDate();
        this.acknowledgementNumber = domain.getAcknowledgementNumber();
        this.submittedBy = domain.getSubmittedBy();
        this.approvedBy = domain.getApprovedBy();
        this.approvedAt = domain.getApprovedAt();
        this.calculationIds = domain.getCalculationIds();
        this.adjustments = domain.getAdjustments();
        this.attachments = domain.getAttachments();
        this.notes = domain.getNotes();
        this.internalNotes = domain.getInternalNotes();
        this.paymentReference = domain.getPaymentReference();
        this.paymentDate = domain.getPaymentDate();
        this.metadata = domain.getMetadata();
        this.createdAt = domain.getCreatedAt();
        this.updatedAt = domain.getUpdatedAt();
    }

    // Convert to domain model
    public TaxFiling toDomain() {
        return TaxFiling.builder()
            .id(this.id)
            .tenantId(this.tenantId)
            .filingId(this.filingId)
            .filingPeriod(this.filingPeriod)
            .filingType(this.filingType)
            .jurisdiction(this.jurisdiction)
            .taxType(this.taxType)
            .currency(this.currency)
            .grossSales(this.grossSales)
            .taxableSales(this.taxableSales)
            .exemptSales(this.exemptSales)
            .totalTaxCollected(this.totalTaxCollected)
            .totalTaxPaid(this.totalTaxPaid)
            .taxDue(this.taxDue)
            .taxRefund(this.taxRefund)
            .penalty(this.penalty)
            .interest(this.interest)
            .netAmount(this.netAmount)
            .status(this.status)
            .submissionDate(this.submissionDate)
            .dueDate(this.dueDate)
            .filingDate(this.filingDate)
            .acknowledgementDate(this.acknowledgementDate)
            .acknowledgementNumber(this.acknowledgementNumber)
            .submittedBy(this.submittedBy)
            .approvedBy(this.approvedBy)
            .approvedAt(this.approvedAt)
            .calculationIds(this.calculationIds)
            .adjustments(this.adjustments)
            .attachments(this.attachments)
            .notes(this.notes)
            .internalNotes(this.internalNotes)
            .paymentReference(this.paymentReference)
            .paymentDate(this.paymentDate)
            .metadata(this.metadata)
            .createdAt(this.createdAt)
            .updatedAt(this.updatedAt)
            .build();
    }

    // Static factory method
    public static TaxFilingEntity fromDomain(TaxFiling domain) {
        return new TaxFilingEntity(domain);
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getFilingId() { return filingId; }
    public void setFilingId(String filingId) { this.filingId = filingId; }

    public YearMonth getFilingPeriod() { return filingPeriod; }
    public void setFilingPeriod(YearMonth filingPeriod) { this.filingPeriod = filingPeriod; }

    public TaxFiling.FilingType getFilingType() { return filingType; }
    public void setFilingType(TaxFiling.FilingType filingType) { this.filingType = filingType; }

    public TaxRate.Jurisdiction getJurisdiction() { return jurisdiction; }
    public void setJurisdiction(TaxRate.Jurisdiction jurisdiction) { this.jurisdiction = jurisdiction; }

    public TaxRate.TaxType getTaxType() { return taxType; }
    public void setTaxType(TaxRate.TaxType taxType) { this.taxType = taxType; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public BigDecimal getGrossSales() { return grossSales; }
    public void setGrossSales(BigDecimal grossSales) { this.grossSales = grossSales; }

    public BigDecimal getTaxableSales() { return taxableSales; }
    public void setTaxableSales(BigDecimal taxableSales) { this.taxableSales = taxableSales; }

    public BigDecimal getExemptSales() { return exemptSales; }
    public void setExemptSales(BigDecimal exemptSales) { this.exemptSales = exemptSales; }

    public BigDecimal getTotalTaxCollected() { return totalTaxCollected; }
    public void setTotalTaxCollected(BigDecimal totalTaxCollected) { this.totalTaxCollected = totalTaxCollected; }

    public BigDecimal getTotalTaxPaid() { return totalTaxPaid; }
    public void setTotalTaxPaid(BigDecimal totalTaxPaid) { this.totalTaxPaid = totalTaxPaid; }

    public BigDecimal getTaxDue() { return taxDue; }
    public void setTaxDue(BigDecimal taxDue) { this.taxDue = taxDue; }

    public BigDecimal getTaxRefund() { return taxRefund; }
    public void setTaxRefund(BigDecimal taxRefund) { this.taxRefund = taxRefund; }

    public BigDecimal getPenalty() { return penalty; }
    public void setPenalty(BigDecimal penalty) { this.penalty = penalty; }

    public BigDecimal getInterest() { return interest; }
    public void setInterest(BigDecimal interest) { this.interest = interest; }

    public BigDecimal getNetAmount() { return netAmount; }
    public void setNetAmount(BigDecimal netAmount) { this.netAmount = netAmount; }

    public TaxFiling.FilingStatus getStatus() { return status; }
    public void setStatus(TaxFiling.FilingStatus status) { this.status = status; }

    public LocalDate getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(LocalDate submissionDate) { this.submissionDate = submissionDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Instant getFilingDate() { return filingDate; }
    public void setFilingDate(Instant filingDate) { this.filingDate = filingDate; }

    public Instant getAcknowledgementDate() { return acknowledgementDate; }
    public void setAcknowledgementDate(Instant acknowledgementDate) { this.acknowledgementDate = acknowledgementDate; }

    public String getAcknowledgementNumber() { return acknowledgementNumber; }
    public void setAcknowledgementNumber(String acknowledgementNumber) { this.acknowledgementNumber = acknowledgementNumber; }

    public String getSubmittedBy() { return submittedBy; }
    public void setSubmittedBy(String submittedBy) { this.submittedBy = submittedBy; }

    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }

    public Instant getApprovedAt() { return approvedAt; }
    public void setApprovedAt(Instant approvedAt) { this.approvedAt = approvedAt; }

    public List<String> getCalculationIds() { return calculationIds; }
    public void setCalculationIds(List<String> calculationIds) { this.calculationIds = calculationIds; }

    public List<TaxFiling.Adjustment> getAdjustments() { return adjustments; }
    public void setAdjustments(List<TaxFiling.Adjustment> adjustments) { this.adjustments = adjustments; }

    public List<TaxFiling.Attachment> getAttachments() { return attachments; }
    public void setAttachments(List<TaxFiling.Attachment> attachments) { this.attachments = attachments; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getInternalNotes() { return internalNotes; }
    public void setInternalNotes(String internalNotes) { this.internalNotes = internalNotes; }

    public String getPaymentReference() { return paymentReference; }
    public void setPaymentReference(String paymentReference) { this.paymentReference = paymentReference; }

    public Instant getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Instant paymentDate) { this.paymentDate = paymentDate; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
