package com.gogidix.finance.tax.domain.model;

import com.gogidix.finance.tax.domain.event.TaxCalculationCompletedEvent;
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
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tax Calculation Domain Entity
 * Multi-tenant tax calculation records with detailed breakdown
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "tax_calculations")
@CompoundIndex(def = "{'tenantId': 1, 'transactionId': 1}", name = "tenant_transaction_idx")
public class TaxCalculation extends BaseEntity {

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
    @Field("calculation_id")
    private String calculationId;

    @Field("transaction_id")
    @Indexed
    private String transactionId;

    @Field("transaction_type")
    private TransactionType transactionType;

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
    @Builder.Default
    private List<TaxLineItem> taxBreakdown = new ArrayList<>();

    @Field("exemptions")
    @Builder.Default
    private List<Exemption> exemptions = new ArrayList<>();

    @Field("deductions")
    @Builder.Default
    private List<Deduction> deductions = new ArrayList<>();

    @Field("tax_rate_applied")
    private BigDecimal effectiveTaxRate;

    @Field("calculation_method")
    private CalculationMethod calculationMethod;

    @Field("status")
    private CalculationStatus status;

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
    @Builder.Default
    private Map<String, Object> context = new HashMap<>();

    @Builder.Default
    private List<TaxCalculationCompletedEvent> domainEvents = new ArrayList<>();

    /**
     * Transaction Type enum
     */
    public enum TransactionType {
        SALES,
        PURCHASE,
        IMPORT,
        EXPORT,
        SERVICE,
        RENTAL,
        DIGITAL_SERVICE,
        INTERCOMPANY,
        ADJUSTMENT,
        REFUND,
        OTHER
    }

    /**
     * Calculation Method enum
     */
    public enum CalculationMethod {
        FLAT_RATE,
        TIERED,
        COMPOUND,
        EXEMPTION_BASED,
        THRESHOLD_BASED,
        CUSTOM
    }

    /**
     * Calculation Status enum
     */
    public enum CalculationStatus {
        PENDING,
        CALCULATED,
        VERIFIED,
        APPLIED,
        REVERSED,
        ERROR
    }

    /**
     * Tax Line Item
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaxLineItem {
        private String taxCode;
        private TaxRate.TaxType taxType;
        private BigDecimal rate;
        private BigDecimal baseAmount;
        private BigDecimal taxAmount;
        private Boolean isRecoverable;
        private BigDecimal recoverableAmount;
        private String description;
    }

    /**
     * Exemption
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Exemption {
        private String exemptionCode;
        private String exemptionType;
        private BigDecimal amount;
        private String reason;
        private String certificateNumber;
        private LocalDate certificateExpiry;
    }

    /**
     * Deduction
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Deduction {
        private String deductionType;
        private BigDecimal amount;
        private String description;
        private String reference;
    }

    /**
     * Creates a new tax calculation
     */
    public static TaxCalculation create(String tenantId, String transactionId,
                                          TransactionType transactionType, LocalDate transactionDate,
                                          TaxRate.Jurisdiction jurisdiction, String currency,
                                          BigDecimal baseAmount, String calculatedBy) {
        TaxCalculation calculation = TaxCalculation.builder()
            .tenantId(tenantId)
            .calculationId(generateCalculationId())
            .transactionId(transactionId)
            .transactionType(transactionType)
            .transactionDate(transactionDate)
            .jurisdiction(jurisdiction)
            .currency(currency)
            .baseAmount(baseAmount)
            .taxableAmount(baseAmount)
            .totalTax(BigDecimal.ZERO)
            .netAmount(baseAmount)
            .status(CalculationStatus.PENDING)
            .calculatedAt(Instant.now())
            .calculatedBy(calculatedBy)
            .taxBreakdown(new ArrayList<>())
            .exemptions(new ArrayList<>())
            .deductions(new ArrayList<>())
            .context(new HashMap<>())
            .build();

        calculation.addDomainEvent(TaxCalculationCompletedEvent.builder()
            .calculationId(calculation.getCalculationId())
            .tenantId(tenantId)
            .transactionId(transactionId)
            .eventType("CALCULATION_INITIATED")
            .timestamp(Instant.now())
            .build());

        return calculation;
    }

    /**
     * Calculates tax for a single tax rate
     */
    public void calculateWithRate(String taxCode, TaxRate.TaxType taxType, BigDecimal rate,
                                   Boolean isRecoverable, String description) {
        if (this.status != CalculationStatus.PENDING) {
            throw new IllegalStateException("Cannot modify non-pending calculations");
        }

        BigDecimal taxAmount = this.taxableAmount.multiply(rate)
            .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

        TaxLineItem lineItem = TaxLineItem.builder()
            .taxCode(taxCode)
            .taxType(taxType)
            .rate(rate)
            .baseAmount(this.taxableAmount)
            .taxAmount(taxAmount)
            .isRecoverable(isRecoverable)
            .recoverableAmount(isRecoverable ? taxAmount : BigDecimal.ZERO)
            .description(description)
            .build();

        this.taxBreakdown.add(lineItem);
        this.updateTimestamp();
    }

    /**
     * Calculates compound tax (tax on tax)
     */
    public void calculateCompoundTax(String taxCode, TaxRate.TaxType taxType, BigDecimal rate,
                                      Boolean isRecoverable, String description) {
        if (this.status != CalculationStatus.PENDING) {
            throw new IllegalStateException("Cannot modify non-pending calculations");
        }

        BigDecimal baseForCompound = this.taxableAmount.add(this.totalTax);
        BigDecimal taxAmount = baseForCompound.multiply(rate)
            .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

        TaxLineItem lineItem = TaxLineItem.builder()
            .taxCode(taxCode)
            .taxType(taxType)
            .rate(rate)
            .baseAmount(baseForCompound)
            .taxAmount(taxAmount)
            .isRecoverable(isRecoverable)
            .recoverableAmount(isRecoverable ? taxAmount : BigDecimal.ZERO)
            .description(description)
            .build();

        this.taxBreakdown.add(lineItem);
        this.updateTimestamp();
    }

    /**
     * Finalizes the calculation
     */
    public void finalize() {
        if (this.status != CalculationStatus.PENDING) {
            throw new IllegalStateException("Can only finalize pending calculations");
        }

        // Calculate totals
        this.totalTax = this.taxBreakdown.stream()
            .map(TaxLineItem::getTaxAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.netAmount = this.baseAmount.add(this.totalTax);

        // Calculate effective tax rate
        if (this.baseAmount.compareTo(BigDecimal.ZERO) > 0) {
            this.effectiveTaxRate = this.totalTax.multiply(new BigDecimal("100"))
                .divide(this.baseAmount, 4, RoundingMode.HALF_UP);
        }

        // Determine calculation method
        if (this.taxBreakdown.size() > 1) {
            this.calculationMethod = CalculationMethod.COMPOUND;
        } else if (!this.exemptions.isEmpty()) {
            this.calculationMethod = CalculationMethod.EXEMPTION_BASED;
        } else {
            this.calculationMethod = CalculationMethod.FLAT_RATE;
        }

        this.status = CalculationStatus.CALCULATED;
        this.calculatedAt = Instant.now();
        this.referenceNumber = generateReferenceNumber();
        this.updateTimestamp();

        addDomainEvent(TaxCalculationCompletedEvent.builder()
            .calculationId(this.calculationId)
            .tenantId(this.tenantId)
            .transactionId(this.transactionId)
            .eventType("CALCULATION_COMPLETED")
            .totalTax(this.totalTax)
            .netAmount(this.netAmount)
            .effectiveRate(this.effectiveTaxRate)
            .timestamp(Instant.now())
            .build());
    }

    /**
     * Adds an exemption
     */
    public void addExemption(String exemptionCode, String exemptionType, BigDecimal amount,
                              String reason, String certificateNumber, LocalDate certificateExpiry) {
        if (this.status != CalculationStatus.PENDING) {
            throw new IllegalStateException("Cannot modify non-pending calculations");
        }

        Exemption exemption = Exemption.builder()
            .exemptionCode(exemptionCode)
            .exemptionType(exemptionType)
            .amount(amount)
            .reason(reason)
            .certificateNumber(certificateNumber)
            .certificateExpiry(certificateExpiry)
            .build();

        this.exemptions.add(exemption);
        this.taxableAmount = this.taxableAmount.subtract(amount);
        this.updateTimestamp();
    }

    /**
     * Adds a deduction
     */
    public void addDeduction(String deductionType, BigDecimal amount, String description, String reference) {
        if (this.status != CalculationStatus.PENDING) {
            throw new IllegalStateException("Cannot modify non-pending calculations");
        }

        Deduction deduction = Deduction.builder()
            .deductionType(deductionType)
            .amount(amount)
            .description(description)
            .reference(reference)
            .build();

        this.deductions.add(deduction);
        this.taxableAmount = this.taxableAmount.subtract(amount);
        this.updateTimestamp();
    }

    /**
     * Verifies the calculation
     */
    public void verify(String verifiedBy) {
        if (this.status != CalculationStatus.CALCULATED) {
            throw new IllegalStateException("Can only verify calculated calculations");
        }

        this.status = CalculationStatus.VERIFIED;
        this.verifiedBy = verifiedBy;
        this.verifiedAt = Instant.now();
        this.updateTimestamp();

        addDomainEvent(TaxCalculationCompletedEvent.builder()
            .calculationId(this.calculationId)
            .tenantId(this.tenantId)
            .transactionId(this.transactionId)
            .eventType("CALCULATION_VERIFIED")
            .verifiedBy(verifiedBy)
            .timestamp(Instant.now())
            .build());
    }

    /**
     * Marks the calculation as applied
     */
    public void markAsApplied() {
        if (this.status != CalculationStatus.VERIFIED && this.status != CalculationStatus.CALCULATED) {
            throw new IllegalStateException("Can only apply verified or calculated calculations");
        }

        this.status = CalculationStatus.APPLIED;
        this.updateTimestamp();

        addDomainEvent(TaxCalculationCompletedEvent.builder()
            .calculationId(this.calculationId)
            .tenantId(this.tenantId)
            .transactionId(this.transactionId)
            .eventType("CALCULATION_APPLIED")
            .timestamp(Instant.now())
            .build());
    }

    /**
     * Reverses the calculation
     */
    public void reverse(String reason) {
        if (this.status != CalculationStatus.APPLIED) {
            throw new IllegalStateException("Can only reverse applied calculations");
        }

        this.status = CalculationStatus.REVERSED;
        this.notes = (this.notes != null ? this.notes + " | " : "") + "REVERSED: " + reason;
        this.updateTimestamp();

        addDomainEvent(TaxCalculationCompletedEvent.builder()
            .calculationId(this.calculationId)
            .tenantId(this.tenantId)
            .transactionId(this.transactionId)
            .eventType("CALCULATION_REVERSED")
            .reason(reason)
            .timestamp(Instant.now())
            .build());
    }

    /**
     * Gets total recoverable tax amount
     */
    public BigDecimal getTotalRecoverableTax() {
        return this.taxBreakdown.stream()
            .filter(item -> Boolean.TRUE.equals(item.getIsRecoverable()))
            .map(TaxLineItem::getRecoverableAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Gets total non-recoverable tax amount
     */
    public BigDecimal getTotalNonRecoverableTax() {
        return this.totalTax.subtract(getTotalRecoverableTax());
    }

    /**
     * Adds context information
     */
    public void addContext(String key, Object value) {
        if (this.context == null) {
            this.context = new HashMap<>();
        }
        this.context.put(key, value);
    }

    public void addDomainEvent(TaxCalculationCompletedEvent event) {
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

    private static String generateCalculationId() {
        return "TC-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private static String generateReferenceNumber() {
        return "REF-" + java.util.UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }
}
