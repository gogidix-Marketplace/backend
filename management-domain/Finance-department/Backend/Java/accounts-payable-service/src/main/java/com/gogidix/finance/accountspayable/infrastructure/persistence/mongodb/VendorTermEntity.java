package com.gogidix.finance.accountspayable.infrastructure.persistence.mongodb;

import com.gogidix.finance.accountspayable.domain.model.VendorTerm;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDate;

@Document(collection = "vendor_terms")
public class VendorTermEntity {

    @Id
    private String id;

    @Indexed
    @Field("term_id")
    private String termId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("vendor_id")
    private String vendorId;

    @Field("term_code")
    private String termCode;

    @Field("term_name")
    private String termName;

    @Field("description")
    private String description;

    @Field("term_type")
    private String termType;

    @Field("net_days")
    private Integer netDays;

    @Field("discount_days")
    private Integer discountDays;

    @Field("discount_percentage")
    private BigDecimal discountPercentage;

    @Field("currency")
    private String currency;

    @Field("credit_limit")
    private BigDecimal creditLimit;

    @Field("valid_from")
    private LocalDate validFrom;

    @Field("valid_until")
    private LocalDate validUntil;

    @Field("is_active")
    private Boolean isActive;

    @Field("is_default")
    private Boolean isDefault;

    @Field("payment_method")
    private String paymentMethod;

    @Field("bank_account_id")
    private String bankAccountId;

    @Field("created_by")
    private String createdBy;

    @Field("approved_by")
    private String approvedBy;

    @Field("effective_date")
    private LocalDate effectiveDate;

    @Field("notes")
    private String notes;

    @Field("penalty_clause")
    private String penaltyClause;

    @Field("late_fee_percentage")
    private BigDecimal lateFeePercentage;

    @Field("grace_period_days")
    private Integer gracePeriodDays;

    @Field("partial_payment_allowed")
    private Boolean partialPaymentAllowed;

    @Field("minimum_payment_amount")
    private BigDecimal minimumPaymentAmount;

    @Field("billing_cycle")
    private String billingCycle;

    @Field("billing_day_of_month")
    private Integer billingDayOfMonth;

    public VendorTermEntity() {
    }

    public VendorTermEntity(VendorTerm term) {
        this.termId = term.getTermId();
        this.tenantId = term.getTenantId();
        this.vendorId = term.getVendorId();
        this.termCode = term.getTermCode();
        this.termName = term.getTermName();
        this.description = term.getDescription();
        this.termType = term.getTermType() != null ? term.getTermType().name() : null;
        this.netDays = term.getNetDays();
        this.discountDays = term.getDiscountDays();
        this.discountPercentage = term.getDiscountPercentage();
        this.currency = term.getCurrency();
        this.creditLimit = term.getCreditLimit();
        this.validFrom = term.getValidFrom();
        this.validUntil = term.getValidUntil();
        this.isActive = term.getIsActive();
        this.isDefault = term.getIsDefault();
        this.paymentMethod = term.getPaymentMethod();
        this.bankAccountId = term.getBankAccountId();
        this.createdBy = term.getCreatedBy();
        this.approvedBy = term.getApprovedBy();
        this.effectiveDate = term.getEffectiveDate();
        this.notes = term.getNotes();
        this.penaltyClause = term.getPenaltyClause();
        this.lateFeePercentage = term.getLateFeePercentage();
        this.gracePeriodDays = term.getGracePeriodDays();
        this.partialPaymentAllowed = term.getPartialPaymentAllowed();
        this.minimumPaymentAmount = term.getMinimumPaymentAmount();
        this.billingCycle = term.getBillingCycle();
        this.billingDayOfMonth = term.getBillingDayOfMonth();
    }

    public VendorTerm toDomainModel() {
        return VendorTerm.builder()
                .termId(this.termId)
                .tenantId(this.tenantId)
                .vendorId(this.vendorId)
                .termCode(this.termCode)
                .termName(this.termName)
                .description(this.description)
                .termType(this.termType != null ? VendorTerm.TermType.valueOf(this.termType) : null)
                .netDays(this.netDays)
                .discountDays(this.discountDays)
                .discountPercentage(this.discountPercentage)
                .currency(this.currency)
                .creditLimit(this.creditLimit)
                .validFrom(this.validFrom)
                .validUntil(this.validUntil)
                .isActive(this.isActive)
                .isDefault(this.isDefault)
                .paymentMethod(this.paymentMethod)
                .bankAccountId(this.bankAccountId)
                .createdBy(this.createdBy)
                .approvedBy(this.approvedBy)
                .effectiveDate(this.effectiveDate)
                .notes(this.notes)
                .penaltyClause(this.penaltyClause)
                .lateFeePercentage(this.lateFeePercentage)
                .gracePeriodDays(this.gracePeriodDays)
                .partialPaymentAllowed(this.partialPaymentAllowed)
                .minimumPaymentAmount(this.minimumPaymentAmount)
                .billingCycle(this.billingCycle)
                .billingDayOfMonth(this.billingDayOfMonth)
                .build();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTermId() { return termId; }
    public void setTermId(String termId) { this.termId = termId; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getVendorId() { return vendorId; }
    public void setVendorId(String vendorId) { this.vendorId = vendorId; }
    public String getTermCode() { return termCode; }
    public void setTermCode(String termCode) { this.termCode = termCode; }
    public String getTermName() { return termName; }
    public void setTermName(String termName) { this.termName = termName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTermType() { return termType; }
    public void setTermType(String termType) { this.termType = termType; }
    public Integer getNetDays() { return netDays; }
    public void setNetDays(Integer netDays) { this.netDays = netDays; }
    public Integer getDiscountDays() { return discountDays; }
    public void setDiscountDays(Integer discountDays) { this.discountDays = discountDays; }
    public BigDecimal getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public BigDecimal getCreditLimit() { return creditLimit; }
    public void setCreditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; }
    public LocalDate getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDate validFrom) { this.validFrom = validFrom; }
    public LocalDate getValidUntil() { return validUntil; }
    public void setValidUntil(LocalDate validUntil) { this.validUntil = validUntil; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Boolean getIsDefault() { return isDefault; }
    public void setIsDefault(Boolean isDefault) { this.isDefault = isDefault; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getBankAccountId() { return bankAccountId; }
    public void setBankAccountId(String bankAccountId) { this.bankAccountId = bankAccountId; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getPenaltyClause() { return penaltyClause; }
    public void setPenaltyClause(String penaltyClause) { this.penaltyClause = penaltyClause; }
    public BigDecimal getLateFeePercentage() { return lateFeePercentage; }
    public void setLateFeePercentage(BigDecimal lateFeePercentage) { this.lateFeePercentage = lateFeePercentage; }
    public Integer getGracePeriodDays() { return gracePeriodDays; }
    public void setGracePeriodDays(Integer gracePeriodDays) { this.gracePeriodDays = gracePeriodDays; }
    public Boolean getPartialPaymentAllowed() { return partialPaymentAllowed; }
    public void setPartialPaymentAllowed(Boolean partialPaymentAllowed) { this.partialPaymentAllowed = partialPaymentAllowed; }
    public BigDecimal getMinimumPaymentAmount() { return minimumPaymentAmount; }
    public void setMinimumPaymentAmount(BigDecimal minimumPaymentAmount) { this.minimumPaymentAmount = minimumPaymentAmount; }
    public String getBillingCycle() { return billingCycle; }
    public void setBillingCycle(String billingCycle) { this.billingCycle = billingCycle; }
    public Integer getBillingDayOfMonth() { return billingDayOfMonth; }
    public void setBillingDayOfMonth(Integer billingDayOfMonth) { this.billingDayOfMonth = billingDayOfMonth; }
}
