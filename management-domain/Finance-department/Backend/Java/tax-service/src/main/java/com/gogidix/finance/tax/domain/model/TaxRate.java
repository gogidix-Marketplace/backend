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

/**
 * Tax Rate Domain Entity
 * Multi-tenant tax rate configuration for different jurisdictions
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "tax_rates")
@CompoundIndex(def = "{'tenantId': 1, 'jurisdiction': 1, 'taxType': 1, 'effectiveDate': -1}", name = "tenant_jurisdiction_type_date_idx")
public class TaxRate extends BaseEntity {

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
    @Field("tax_rate_id")
    private String taxRateId;

    @Field("jurisdiction")
    @Indexed
    private Jurisdiction jurisdiction;

    @Field("tax_type")
    @Indexed
    private TaxType taxType;

    @Field("tax_code")
    @Indexed
    private String taxCode;

    @Field("rate_percentage")
    private BigDecimal ratePercentage;

    @Field("effective_date")
    @Indexed
    private LocalDate effectiveDate;

    @Field("expiry_date")
    private LocalDate expiryDate;

    @Field("description")
    private String description;

    @Field("is_compound")
    private Boolean isCompound;

    @Field("is_recoverable")
    private Boolean isRecoverable;

    @Field("recovery_rate")
    private BigDecimal recoveryRate;

    @Field("min_threshold")
    private BigDecimal minThreshold;

    @Field("max_threshold")
    private BigDecimal maxThreshold;

    @Field("status")
    private TaxRateStatus status;

    @Field("created_by")
    private String createdBy;

    @Field("approved_by")
    private String approvedBy;

    @Field("approved_at")
    private Instant approvedAt;

    @Field("version")
    private Integer version;

    @Field("notes")
    private String notes;

    /**
     * Jurisdiction enum for tax regions
     */
    public enum Jurisdiction {
        US_FEDERAL,
        US_STATE,
        US_LOCAL,
        UK,
        EU,
        CANADA_FEDERAL,
        CANADA_PROVINCIAL,
        AUSTRALIA,
        INDIA,
        SINGAPORE,
        JAPAN,
        INTERNATIONAL
    }

    /**
     * Tax Type enum
     */
    public enum TaxType {
        SALES_TAX,
        VAT,
        GST,
        INCOME_TAX,
        CORPORATE_TAX,
        PAYROLL_TAX,
        PROPERTY_TAX,
        EXCISE_TAX,
        IMPORT_DUTY,
        EXPORT_TAX,
        WITHHOLDING_TAX,
        CAPITAL_GAINS_TAX,
        SERVICE_TAX,
        OTHER
    }

    /**
     * Tax Rate Status enum
     */
    public enum TaxRateStatus {
        DRAFT,
        ACTIVE,
        EXPIRED,
        PENDING_APPROVAL,
        ARCHIVED
    }

    /**
     * Creates a new tax rate
     */
    public static TaxRate create(String tenantId, Jurisdiction jurisdiction, TaxType taxType,
                                  String taxCode, BigDecimal ratePercentage, LocalDate effectiveDate,
                                  String createdBy) {
        TaxRate taxRate = TaxRate.builder()
            .tenantId(tenantId)
            .taxRateId(generateTaxRateId())
            .jurisdiction(jurisdiction)
            .taxType(taxType)
            .taxCode(taxCode)
            .ratePercentage(ratePercentage)
            .effectiveDate(effectiveDate)
            .status(TaxRateStatus.DRAFT)
            .isCompound(false)
            .isRecoverable(false)
            .createdBy(createdBy)
            .version(1)
            .build();

        taxRate.validate();
        return taxRate;
    }

    /**
     * Activates the tax rate
     */
    public void activate(String approvedBy) {
        if (this.status != TaxRateStatus.DRAFT && this.status != TaxRateStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Can only activate draft or pending tax rates");
        }

        validate();

        this.status = TaxRateStatus.ACTIVE;
        this.approvedBy = approvedBy;
        this.approvedAt = Instant.now();
        this.updateTimestamp();
    }

    /**
     * Expires the tax rate
     */
    public void expire() {
        if (this.status != TaxRateStatus.ACTIVE) {
            throw new IllegalStateException("Can only expire active tax rates");
        }

        this.status = TaxRateStatus.EXPIRED;
        this.updateTimestamp();
    }

    /**
     * Archives the tax rate
     */
    public void archive() {
        if (this.status == TaxRateStatus.ARCHIVED) {
            throw new IllegalStateException("Tax rate is already archived");
        }

        this.status = TaxRateStatus.ARCHIVED;
        this.updateTimestamp();
    }

    /**
     * Updates the rate percentage
     */
    public void updateRate(BigDecimal newRate, String updatedBy) {
        if (this.status == TaxRateStatus.ACTIVE) {
            throw new IllegalStateException("Cannot update active tax rates. Create a new version instead.");
        }

        if (newRate.compareTo(BigDecimal.ZERO) < 0 || newRate.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Tax rate must be between 0 and 100");
        }

        this.ratePercentage = newRate;
        this.updateTimestamp();
    }

    /**
     * Checks if the tax rate is effective on a given date
     */
    public boolean isEffectiveOn(LocalDate date) {
        if (this.status != TaxRateStatus.ACTIVE) {
            return false;
        }

        boolean isAfterEffective = !date.isBefore(this.effectiveDate);
        boolean isBeforeExpiry = this.expiryDate == null || !date.isAfter(this.expiryDate);

        return isAfterEffective && isBeforeExpiry;
    }

    /**
     * Calculates tax amount for a given base amount
     */
    public BigDecimal calculateTax(BigDecimal baseAmount) {
        if (!this.isEffectiveOn(LocalDate.now())) {
            throw new IllegalStateException("Tax rate is not currently effective");
        }

        return baseAmount.multiply(this.ratePercentage)
            .divide(new BigDecimal("100"), 4, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Validates the tax rate
     */
    public void validate() {
        if (this.ratePercentage == null) {
            throw new IllegalArgumentException("Rate percentage is required");
        }

        if (this.ratePercentage.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Rate percentage cannot be negative");
        }

        if (this.ratePercentage.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Rate percentage cannot exceed 100");
        }

        if (this.effectiveDate == null) {
            throw new IllegalArgumentException("Effective date is required");
        }

        if (this.expiryDate != null && this.expiryDate.isBefore(this.effectiveDate)) {
            throw new IllegalArgumentException("Expiry date cannot be before effective date");
        }

        if (this.jurisdiction == null) {
            throw new IllegalArgumentException("Jurisdiction is required");
        }

        if (this.taxType == null) {
            throw new IllegalArgumentException("Tax type is required");
        }

        if (this.taxCode == null || this.taxCode.isBlank()) {
            throw new IllegalArgumentException("Tax code is required");
        }
    }

    /**
     * Creates a new version of this tax rate
     */
    public TaxRate createNewVersion(BigDecimal newRate, LocalDate newEffectiveDate, String createdBy) {
        TaxRate newVersion = TaxRate.builder()
            .tenantId(this.tenantId)
            .taxRateId(generateTaxRateId())
            .jurisdiction(this.jurisdiction)
            .taxType(this.taxType)
            .taxCode(this.taxCode)
            .ratePercentage(newRate)
            .effectiveDate(newEffectiveDate)
            .description(this.description)
            .isCompound(this.isCompound)
            .isRecoverable(this.isRecoverable)
            .recoveryRate(this.recoveryRate)
            .minThreshold(this.minThreshold)
            .maxThreshold(this.maxThreshold)
            .status(TaxRateStatus.DRAFT)
            .createdBy(createdBy)
            .version(this.version + 1)
            .notes("Version " + (this.version + 1) + " of tax rate " + this.taxRateId)
            .build();

        newVersion.validate();
        return newVersion;
    }

    private static String generateTaxRateId() {
        return "TR-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
