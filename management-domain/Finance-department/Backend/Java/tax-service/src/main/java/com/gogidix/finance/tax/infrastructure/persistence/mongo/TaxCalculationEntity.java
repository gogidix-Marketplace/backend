package com.gogidix.finance.tax.infrastructure.persistence.mongo;

import com.gogidix.finance.tax.domain.model.TaxCalculation;
import com.gogidix.finance.tax.domain.model.TaxRate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * MongoDB Entity for TaxCalculation
 * Infrastructure layer - separate from domain model
 */
@Document(collection = "tax_calculations")
@CompoundIndex(def = "{'tenantId': 1, 'transactionId': 1}", name = "tenant_transaction_idx")
public class TaxCalculationEntity {

    @Id
    private String id;

    @Field("tenant_id")
    @Indexed
    private String tenantId;

    @Indexed(unique = true)
    @Field("calculation_id")
    private String calculationId;

    @Field("transaction_id")
    @Indexed
    private String transactionId;

    @Field("transaction_type")
    private TaxCalculation.TransactionType transactionType;

    @Field("transaction_date")
    private LocalDate transactionDate;

    @Field("jurisdiction")
    @Indexed
    private TaxRate.Jurisdiction jurisdiction;

    @Field("currency")
    private String currency;

    @Field("base_amount")
    private BigDecimal baseAmount;

    @Field("taxable_amount")
    private BigDecimal taxableAmount;

    @Field("total_tax")
    private BigDecimal totalTax;

    @Field("net_amount")
    private BigDecimal netAmount;

    @Field("tax_breakdown")
    private List<TaxCalculation.TaxLineItem> taxBreakdown;

    @Field("exemptions")
    private List<TaxCalculation.Exemption> exemptions;

    @Field("deductions")
    private List<TaxCalculation.Deduction> deductions;

    @Field("tax_rate_applied")
    private BigDecimal effectiveTaxRate;

    @Field("calculation_method")
    private TaxCalculation.CalculationMethod calculationMethod;

    @Field("status")
    private TaxCalculation.CalculationStatus status;

    @Field("calculated_at")
    private Instant calculatedAt;

    @Field("calculated_by")
    private String calculatedBy;

    @Field("verified_at")
    private Instant verifiedAt;

    @Field("verified_by")
    private String verifiedBy;

    @Field("reference_number")
    private String referenceNumber;

    @Field("notes")
    private String notes;

    @Field("context")
    private Map<String, Object> context;

    @Field("domain_events")
    private List<?> domainEvents;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    // Default constructor for MongoDB
    public TaxCalculationEntity() {}

    // Constructor from domain model
    public TaxCalculationEntity(TaxCalculation domain) {
        this.id = domain.getId();
        this.tenantId = domain.getTenantId();
        this.calculationId = domain.getCalculationId();
        this.transactionId = domain.getTransactionId();
        this.transactionType = domain.getTransactionType();
        this.transactionDate = domain.getTransactionDate();
        this.jurisdiction = domain.getJurisdiction();
        this.currency = domain.getCurrency();
        this.baseAmount = domain.getBaseAmount();
        this.taxableAmount = domain.getTaxableAmount();
        this.totalTax = domain.getTotalTax();
        this.netAmount = domain.getNetAmount();
        this.taxBreakdown = domain.getTaxBreakdown();
        this.exemptions = domain.getExemptions();
        this.deductions = domain.getDeductions();
        this.effectiveTaxRate = domain.getEffectiveTaxRate();
        this.calculationMethod = domain.getCalculationMethod();
        this.status = domain.getStatus();
        this.calculatedAt = domain.getCalculatedAt();
        this.calculatedBy = domain.getCalculatedBy();
        this.verifiedAt = domain.getVerifiedAt();
        this.verifiedBy = domain.getVerifiedBy();
        this.referenceNumber = domain.getReferenceNumber();
        this.notes = domain.getNotes();
        this.context = domain.getContext();
        this.domainEvents = domain.getDomainEvents();
        this.createdAt = domain.getCreatedAt();
        this.updatedAt = domain.getUpdatedAt();
    }

    // Convert to domain model
    public TaxCalculation toDomain() {
        return TaxCalculation.builder()
            .id(this.id)
            .tenantId(this.tenantId)
            .calculationId(this.calculationId)
            .transactionId(this.transactionId)
            .transactionType(this.transactionType)
            .transactionDate(this.transactionDate)
            .jurisdiction(this.jurisdiction)
            .currency(this.currency)
            .baseAmount(this.baseAmount)
            .taxableAmount(this.taxableAmount)
            .totalTax(this.totalTax)
            .netAmount(this.netAmount)
            .taxBreakdown(this.taxBreakdown)
            .exemptions(this.exemptions)
            .deductions(this.deductions)
            .effectiveTaxRate(this.effectiveTaxRate)
            .calculationMethod(this.calculationMethod)
            .status(this.status)
            .calculatedAt(this.calculatedAt)
            .calculatedBy(this.calculatedBy)
            .verifiedAt(this.verifiedAt)
            .verifiedBy(this.verifiedBy)
            .referenceNumber(this.referenceNumber)
            .notes(this.notes)
            .context(this.context)
            .domainEvents(this.domainEvents != null ? (List) this.domainEvents : List.of())
            .createdAt(this.createdAt)
            .updatedAt(this.updatedAt)
            .build();
    }

    // Static factory method
    public static TaxCalculationEntity fromDomain(TaxCalculation domain) {
        return new TaxCalculationEntity(domain);
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getCalculationId() { return calculationId; }
    public void setCalculationId(String calculationId) { this.calculationId = calculationId; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public TaxCalculation.TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TaxCalculation.TransactionType transactionType) { this.transactionType = transactionType; }

    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }

    public TaxRate.Jurisdiction getJurisdiction() { return jurisdiction; }
    public void setJurisdiction(TaxRate.Jurisdiction jurisdiction) { this.jurisdiction = jurisdiction; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public BigDecimal getBaseAmount() { return baseAmount; }
    public void setBaseAmount(BigDecimal baseAmount) { this.baseAmount = baseAmount; }

    public BigDecimal getTaxableAmount() { return taxableAmount; }
    public void setTaxableAmount(BigDecimal taxableAmount) { this.taxableAmount = taxableAmount; }

    public BigDecimal getTotalTax() { return totalTax; }
    public void setTotalTax(BigDecimal totalTax) { this.totalTax = totalTax; }

    public BigDecimal getNetAmount() { return netAmount; }
    public void setNetAmount(BigDecimal netAmount) { this.netAmount = netAmount; }

    public List<TaxCalculation.TaxLineItem> getTaxBreakdown() { return taxBreakdown; }
    public void setTaxBreakdown(List<TaxCalculation.TaxLineItem> taxBreakdown) { this.taxBreakdown = taxBreakdown; }

    public List<TaxCalculation.Exemption> getExemptions() { return exemptions; }
    public void setExemptions(List<TaxCalculation.Exemption> exemptions) { this.exemptions = exemptions; }

    public List<TaxCalculation.Deduction> getDeductions() { return deductions; }
    public void setDeductions(List<TaxCalculation.Deduction> deductions) { this.deductions = deductions; }

    public BigDecimal getEffectiveTaxRate() { return effectiveTaxRate; }
    public void setEffectiveTaxRate(BigDecimal effectiveTaxRate) { this.effectiveTaxRate = effectiveTaxRate; }

    public TaxCalculation.CalculationMethod getCalculationMethod() { return calculationMethod; }
    public void setCalculationMethod(TaxCalculation.CalculationMethod calculationMethod) { this.calculationMethod = calculationMethod; }

    public TaxCalculation.CalculationStatus getStatus() { return status; }
    public void setStatus(TaxCalculation.CalculationStatus status) { this.status = status; }

    public Instant getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(Instant calculatedAt) { this.calculatedAt = calculatedAt; }

    public String getCalculatedBy() { return calculatedBy; }
    public void setCalculatedBy(String calculatedBy) { this.calculatedBy = calculatedBy; }

    public Instant getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(Instant verifiedAt) { this.verifiedAt = verifiedAt; }

    public String getVerifiedBy() { return verifiedBy; }
    public void setVerifiedBy(String verifiedBy) { this.verifiedBy = verifiedBy; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Map<String, Object> getContext() { return context; }
    public void setContext(Map<String, Object> context) { this.context = context; }

    public List<?> getDomainEvents() { return domainEvents; }
    public void setDomainEvents(List<?> domainEvents) { this.domainEvents = domainEvents; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
