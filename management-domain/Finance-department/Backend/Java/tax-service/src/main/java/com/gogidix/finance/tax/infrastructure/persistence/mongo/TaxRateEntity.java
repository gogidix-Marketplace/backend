package com.gogidix.finance.tax.infrastructure.persistence.mongo;

import com.gogidix.finance.tax.domain.model.TaxRate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * MongoDB Entity for TaxRate
 * Infrastructure layer - separate from domain model
 */
@Document(collection = "tax_rates")
@CompoundIndex(def = "{'tenantId': 1, 'jurisdiction': 1, 'taxType': 1, 'effectiveDate': -1}", name = "tenant_jurisdiction_type_date_idx")
public class TaxRateEntity {

    @Id
    private String id;

    @Field("tenant_id")
    @Indexed
    private String tenantId;

    @Indexed(unique = true)
    @Field("tax_rate_id")
    private String taxRateId;

    @Field("jurisdiction")
    @Indexed
    private TaxRate.Jurisdiction jurisdiction;

    @Field("tax_type")
    @Indexed
    private TaxRate.TaxType taxType;

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
    private TaxRate.TaxRateStatus status;

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

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    // Default constructor for MongoDB
    public TaxRateEntity() {}

    // Constructor from domain model
    public TaxRateEntity(TaxRate domain) {
        this.id = domain.getId();
        this.tenantId = domain.getTenantId();
        this.taxRateId = domain.getTaxRateId();
        this.jurisdiction = domain.getJurisdiction();
        this.taxType = domain.getTaxType();
        this.taxCode = domain.getTaxCode();
        this.ratePercentage = domain.getRatePercentage();
        this.effectiveDate = domain.getEffectiveDate();
        this.expiryDate = domain.getExpiryDate();
        this.description = domain.getDescription();
        this.isCompound = domain.getIsCompound();
        this.isRecoverable = domain.getIsRecoverable();
        this.recoveryRate = domain.getRecoveryRate();
        this.minThreshold = domain.getMinThreshold();
        this.maxThreshold = domain.getMaxThreshold();
        this.status = domain.getStatus();
        this.createdBy = domain.getCreatedBy();
        this.approvedBy = domain.getApprovedBy();
        this.approvedAt = domain.getApprovedAt();
        this.version = domain.getVersion();
        this.notes = domain.getNotes();
        this.createdAt = domain.getCreatedAt();
        this.updatedAt = domain.getUpdatedAt();
    }

    // Convert to domain model
    public TaxRate toDomain() {
        return TaxRate.builder()
            .id(this.id)
            .tenantId(this.tenantId)
            .taxRateId(this.taxRateId)
            .jurisdiction(this.jurisdiction)
            .taxType(this.taxType)
            .taxCode(this.taxCode)
            .ratePercentage(this.ratePercentage)
            .effectiveDate(this.effectiveDate)
            .expiryDate(this.expiryDate)
            .description(this.description)
            .isCompound(this.isCompound)
            .isRecoverable(this.isRecoverable)
            .recoveryRate(this.recoveryRate)
            .minThreshold(this.minThreshold)
            .maxThreshold(this.maxThreshold)
            .status(this.status)
            .createdBy(this.createdBy)
            .approvedBy(this.approvedBy)
            .approvedAt(this.approvedAt)
            .version(this.version)
            .notes(this.notes)
            .createdAt(this.createdAt)
            .updatedAt(this.updatedAt)
            .build();
    }

    // Static factory method
    public static TaxRateEntity fromDomain(TaxRate domain) {
        return new TaxRateEntity(domain);
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getTaxRateId() { return taxRateId; }
    public void setTaxRateId(String taxRateId) { this.taxRateId = taxRateId; }

    public TaxRate.Jurisdiction getJurisdiction() { return jurisdiction; }
    public void setJurisdiction(TaxRate.Jurisdiction jurisdiction) { this.jurisdiction = jurisdiction; }

    public TaxRate.TaxType getTaxType() { return taxType; }
    public void setTaxType(TaxRate.TaxType taxType) { this.taxType = taxType; }

    public String getTaxCode() { return taxCode; }
    public void setTaxCode(String taxCode) { this.taxCode = taxCode; }

    public BigDecimal getRatePercentage() { return ratePercentage; }
    public void setRatePercentage(BigDecimal ratePercentage) { this.ratePercentage = ratePercentage; }

    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getIsCompound() { return isCompound; }
    public void setIsCompound(Boolean isCompound) { this.isCompound = isCompound; }

    public Boolean getIsRecoverable() { return isRecoverable; }
    public void setIsRecoverable(Boolean isRecoverable) { this.isRecoverable = isRecoverable; }

    public BigDecimal getRecoveryRate() { return recoveryRate; }
    public void setRecoveryRate(BigDecimal recoveryRate) { this.recoveryRate = recoveryRate; }

    public BigDecimal getMinThreshold() { return minThreshold; }
    public void setMinThreshold(BigDecimal minThreshold) { this.minThreshold = minThreshold; }

    public BigDecimal getMaxThreshold() { return maxThreshold; }
    public void setMaxThreshold(BigDecimal maxThreshold) { this.maxThreshold = maxThreshold; }

    public TaxRate.TaxRateStatus getStatus() { return status; }
    public void setStatus(TaxRate.TaxRateStatus status) { this.status = status; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }

    public Instant getApprovedAt() { return approvedAt; }
    public void setApprovedAt(Instant approvedAt) { this.approvedAt = approvedAt; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
