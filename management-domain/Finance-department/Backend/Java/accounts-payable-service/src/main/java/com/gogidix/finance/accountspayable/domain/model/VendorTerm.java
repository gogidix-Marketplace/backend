package com.gogidix.finance.accountspayable.domain.model;

import com.gogidix.finance.accountspayable.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Vendor Term Domain Entity
 * Manages payment terms and conditions for vendors
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "vendor_terms")
public class VendorTerm extends BaseEntity {

    private String termId;

    private String tenantId;

    private String vendorId;

    private String termCode;

    private String termName;

    private String description;

    private TermType termType;

    private Integer netDays;

    private Integer discountDays;

    private BigDecimal discountPercentage;

    private String currency;

    private BigDecimal creditLimit;

    private LocalDate validFrom;

    private LocalDate validUntil;

    private Boolean isActive;

    private Boolean isDefault;

    private String paymentMethod;

    private String bankAccountId;

    private String createdBy;

    private String approvedBy;

    private LocalDate effectiveDate;

    private String notes;

    private String penaltyClause;

    private BigDecimal lateFeePercentage;

    private Integer gracePeriodDays;

    private Boolean partialPaymentAllowed;

    private BigDecimal minimumPaymentAmount;

    private String billingCycle;

    private Integer billingDayOfMonth;

    public enum TermType {
        NET_DAYS,
        DISCOUNT_TERMS,
        INSTALLMENT,
        MILESTONE_BASED,
        ON_RECEIPT,
        END_OF_MONTH,
        CUSTOM
    }

    /**
     * Creates new vendor terms
     */
    public static VendorTerm create(String tenantId, String vendorId, String termCode,
                                      String termName, TermType termType, Integer netDays,
                                      String currency, String createdBy) {
        return VendorTerm.builder()
            .tenantId(tenantId)
            .vendorId(vendorId)
            .termCode(termCode)
            .termName(termName)
            .termType(termType)
            .netDays(netDays)
            .currency(currency)
            .isActive(true)
            .isDefault(false)
            .partialPaymentAllowed(true)
            .createdBy(createdBy)
            .build();
    }

    /**
     * Creates discount terms (e.g., 2/10 Net 30)
     */
    public static VendorTerm createDiscountTerms(String tenantId, String vendorId, String termCode,
                                                   String termName, Integer discountDays,
                                                   BigDecimal discountPercentage, Integer netDays,
                                                   String currency, String createdBy) {
        return VendorTerm.builder()
            .tenantId(tenantId)
            .vendorId(vendorId)
            .termCode(termCode)
            .termName(termName)
            .termType(TermType.DISCOUNT_TERMS)
            .discountDays(discountDays)
            .discountPercentage(discountPercentage)
            .netDays(netDays)
            .currency(currency)
            .isActive(true)
            .isDefault(false)
            .partialPaymentAllowed(true)
            .createdBy(createdBy)
            .build();
    }

    /**
     * Activates the terms
     */
    public void activate(String approvedBy) {
        this.isActive = true;
        this.approvedBy = approvedBy;
        this.effectiveDate = LocalDate.now();
    }

    /**
     * Deactivates the terms
     */
    public void deactivate() {
        this.isActive = false;
        this.validUntil = LocalDate.now();
    }

    /**
     * Sets as default terms
     */
    public void setAsDefault() {
        this.isDefault = true;
    }

    /**
     * Updates discount terms
     */
    public void updateDiscountTerms(Integer discountDays, BigDecimal discountPercentage) {
        this.discountDays = discountDays;
        this.discountPercentage = discountPercentage;
        this.termType = TermType.DISCOUNT_TERMS;
    }

    /**
     * Sets credit limit
     */
    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    /**
     * Sets late fee configuration
     */
    public void setLateFeeConfig(BigDecimal lateFeePercentage, Integer gracePeriodDays) {
        this.lateFeePercentage = lateFeePercentage;
        this.gracePeriodDays = gracePeriodDays;
    }

    /**
     * Sets validity period
     */
    public void setValidityPeriod(LocalDate validFrom, LocalDate validUntil) {
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    /**
     * Sets payment method
     */
    public void setPaymentMethod(String paymentMethod, String bankAccountId) {
        this.paymentMethod = paymentMethod;
        this.bankAccountId = bankAccountId;
    }

    /**
     * Checks if terms are currently valid
     */
    public boolean isValid() {
        if (!this.isActive) {
            return false;
        }

        LocalDate today = LocalDate.now();

        if (this.validFrom != null && today.isBefore(this.validFrom)) {
            return false;
        }

        if (this.validUntil != null && today.isAfter(this.validUntil)) {
            return false;
        }

        return true;
    }

    /**
     * Calculates due date from invoice date
     */
    public LocalDate calculateDueDate(LocalDate invoiceDate) {
        if (this.netDays == null) {
            return invoiceDate.plusDays(30); // Default
        }
        return invoiceDate.plusDays(this.netDays);
    }

    /**
     * Calculates discount date from invoice date
     */
    public LocalDate calculateDiscountDate(LocalDate invoiceDate) {
        if (this.discountDays == null) {
            return null;
        }
        return invoiceDate.plusDays(this.discountDays);
    }

    /**
     * Calculates discount amount
     */
    public BigDecimal calculateDiscountAmount(BigDecimal invoiceAmount) {
        if (this.discountPercentage == null || this.discountPercentage.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return invoiceAmount.multiply(this.discountPercentage)
            .divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Checks if discount is available for date
     */
    public boolean isDiscountAvailable(LocalDate invoiceDate) {
        LocalDate discountDate = calculateDiscountDate(invoiceDate);
        return discountDate != null && !LocalDate.now().isAfter(discountDate);
    }

    /**
     * Calculates late fee
     */
    public BigDecimal calculateLateFee(BigDecimal invoiceAmount, int daysOverdue) {
        if (this.lateFeePercentage == null ||
            (this.gracePeriodDays != null && daysOverdue <= this.gracePeriodDays)) {
            return BigDecimal.ZERO;
        }

        int chargeableDays = this.gracePeriodDays != null ?
            daysOverdue - this.gracePeriodDays : daysOverdue;

        return invoiceAmount.multiply(this.lateFeePercentage)
            .multiply(new BigDecimal(chargeableDays))
            .divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Gets term summary description
     */
    public String getTermSummary() {
        StringBuilder summary = new StringBuilder();

        if (this.termType == TermType.DISCOUNT_TERMS && this.discountPercentage != null) {
            summary.append(this.discountPercentage.intValue())
                .append("%/")
                .append(this.discountDays)
                .append(" ");
        }

        if (this.netDays != null) {
            summary.append("Net ").append(this.netDays);
        }

        if (this.creditLimit != null) {
            summary.append(" | Credit: ").append(this.currency).append(" ").append(this.creditLimit);
        }

        return summary.toString();
    }
}
