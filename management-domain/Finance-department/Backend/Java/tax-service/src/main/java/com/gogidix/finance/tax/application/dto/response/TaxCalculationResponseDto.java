package com.gogidix.finance.tax.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Tax Calculation Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxCalculationResponseDto {

    private String id;

    private String calculationId;

    private String tenantId;

    private String transactionId;

    private TransactionTypeDto transactionType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;

    private JurisdictionDto jurisdiction;

    private String currency;

    private BigDecimal baseAmount;

    private BigDecimal taxableAmount;

    private BigDecimal totalTax;

    private BigDecimal netAmount;

    private List<TaxLineItemDto> taxBreakdown;

    private List<ExemptionDto> exemptions;

    private List<DeductionDto> deductions;

    private BigDecimal effectiveTaxRate;

    private CalculationMethodDto calculationMethod;

    private CalculationStatusDto status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant calculatedAt;

    private String calculatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant verifiedAt;

    private String verifiedBy;

    private String referenceNumber;

    private String notes;

    private Map<String, Object> context;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaxLineItemDto {
        private String taxCode;
        private TaxTypeDto taxType;
        private BigDecimal rate;
        private BigDecimal baseAmount;
        private BigDecimal taxAmount;
        private Boolean isRecoverable;
        private BigDecimal recoverableAmount;
        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExemptionDto {
        private String exemptionCode;
        private String exemptionType;
        private BigDecimal amount;
        private String reason;
        private String certificateNumber;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate certificateExpiry;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeductionDto {
        private String deductionType;
        private BigDecimal amount;
        private String description;
        private String reference;
    }

    public enum TransactionTypeDto {
        SALES, PURCHASE, IMPORT, EXPORT, SERVICE, RENTAL, DIGITAL_SERVICE,
        INTERCOMPANY, ADJUSTMENT, REFUND, OTHER
    }

    public enum CalculationMethodDto {
        FLAT_RATE, TIERED, COMPOUND, EXEMPTION_BASED, THRESHOLD_BASED, CUSTOM
    }

    public enum CalculationStatusDto {
        PENDING, CALCULATED, VERIFIED, APPLIED, REVERSED, ERROR
    }

    public enum JurisdictionDto {
        US_FEDERAL, US_STATE, US_LOCAL, UK, EU, CANADA_FEDERAL, CANADA_PROVINCIAL,
        AUSTRALIA, INDIA, SINGAPORE, JAPAN, INTERNATIONAL
    }

    public enum TaxTypeDto {
        SALES_TAX, VAT, GST, INCOME_TAX, CORPORATE_TAX, PAYROLL_TAX, PROPERTY_TAX,
        EXCISE_TAX, IMPORT_DUTY, EXPORT_TAX, WITHHOLDING_TAX, CAPITAL_GAINS_TAX,
        SERVICE_TAX, OTHER
    }
}
