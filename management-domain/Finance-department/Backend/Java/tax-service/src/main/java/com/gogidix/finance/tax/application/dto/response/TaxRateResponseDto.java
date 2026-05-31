package com.gogidix.finance.tax.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Tax Rate Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxRateResponseDto {

    private String id;

    private String taxRateId;

    private String tenantId;

    private JurisdictionDto jurisdiction;

    private TaxTypeDto taxType;

    private String taxCode;

    private BigDecimal ratePercentage;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    private String description;

    private Boolean isCompound;

    private Boolean isRecoverable;

    private BigDecimal recoveryRate;

    private BigDecimal minThreshold;

    private BigDecimal maxThreshold;

    private TaxRateStatusDto status;

    private String createdBy;

    private String approvedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant approvedAt;

    private Integer version;

    private String notes;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum JurisdictionDto {
        US_FEDERAL, US_STATE, US_LOCAL, UK, EU, CANADA_FEDERAL, CANADA_PROVINCIAL,
        AUSTRALIA, INDIA, SINGAPORE, JAPAN, INTERNATIONAL
    }

    public enum TaxTypeDto {
        SALES_TAX, VAT, GST, INCOME_TAX, CORPORATE_TAX, PAYROLL_TAX, PROPERTY_TAX,
        EXCISE_TAX, IMPORT_DUTY, EXPORT_TAX, WITHHOLDING_TAX, CAPITAL_GAINS_TAX,
        SERVICE_TAX, OTHER
    }

    public enum TaxRateStatusDto {
        DRAFT, ACTIVE, EXPIRED, PENDING_APPROVAL, ARCHIVED
    }
}
