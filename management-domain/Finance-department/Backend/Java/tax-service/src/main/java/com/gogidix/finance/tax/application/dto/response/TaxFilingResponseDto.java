package com.gogidix.finance.tax.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

/**
 * Tax Filing Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxFilingResponseDto {

    private String id;

    private String filingId;

    private String tenantId;

    @JsonFormat(pattern = "yyyy-MM")
    private YearMonth filingPeriod;

    private FilingTypeDto filingType;

    private JurisdictionDto jurisdiction;

    private TaxTypeDto taxType;

    private String currency;

    private BigDecimal grossSales;

    private BigDecimal taxableSales;

    private BigDecimal exemptSales;

    private BigDecimal totalTaxCollected;

    private BigDecimal totalTaxPaid;

    private BigDecimal taxDue;

    private BigDecimal taxRefund;

    private BigDecimal penalty;

    private BigDecimal interest;

    private BigDecimal netAmount;

    private FilingStatusDto status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate submissionDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant filingDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant acknowledgementDate;

    private String acknowledgementNumber;

    private String submittedBy;

    private String approvedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant approvedAt;

    private List<String> calculationIds;

    private List<AdjustmentDto> adjustments;

    private List<AttachmentDto> attachments;

    private String notes;

    private String internalNotes;

    private String paymentReference;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant paymentDate;

    private Map<String, Object> metadata;

    private Boolean isOverdue;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdjustmentDto {
        private String adjustmentId;
        private String adjustmentType;
        private BigDecimal amount;
        private String reason;
        private String reference;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate adjustmentDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttachmentDto {
        private String attachmentId;
        private String fileName;
        private String fileType;
        private Long fileSize;
        private String storageLocation;
        private String url;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
        private Instant uploadedAt;
        private String uploadedBy;
    }

    public enum FilingTypeDto {
        MONTHLY_RETURN, QUARTERLY_RETURN, ANNUAL_RETURN, ADVANCE_TAX,
        WITHHOLDING_RETURN, ADJUSTMENT_RETURN, FINAL_RETURN, AMENDED_RETURN
    }

    public enum FilingStatusDto {
        DRAFT, PENDING_REVIEW, PENDING_SUBMISSION, SUBMITTED, PROCESSING,
        ACCEPTED, REJECTED, ASSESSED, PAID, OVERDUE, CANCELLED, ARCHIVED
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
