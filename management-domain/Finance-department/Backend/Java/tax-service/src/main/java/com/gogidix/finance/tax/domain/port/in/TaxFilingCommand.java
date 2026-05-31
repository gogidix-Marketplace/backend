package com.gogidix.finance.tax.domain.port.in;

import com.gogidix.finance.tax.domain.model.TaxFiling;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Tax Filing Commands (Input Port)
 * Defines the input commands for tax filing operations
 */
public interface TaxFilingCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateFilingCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotNull(message = "Filing period is required")
        private YearMonth filingPeriod;

        @NotNull(message = "Filing type is required")
        private TaxFiling.FilingType filingType;

        @NotNull(message = "Jurisdiction is required")
        private com.gogidix.finance.tax.domain.model.TaxRate.Jurisdiction jurisdiction;

        @NotNull(message = "Tax type is required")
        private com.gogidix.finance.tax.domain.model.TaxRate.TaxType taxType;

        @NotBlank(message = "Currency is required")
        private String currency;

        @NotNull(message = "Due date is required")
        private LocalDate dueDate;

        @NotBlank(message = "Submitted by is required")
        private String submittedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateFilingFiguresCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Filing ID is required")
        private String filingId;

        private BigDecimal grossSales;

        private BigDecimal taxableSales;

        private BigDecimal exemptSales;

        private BigDecimal totalTaxCollected;

        private BigDecimal totalTaxPaid;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SubmitForReviewCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Filing ID is required")
        private String filingId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SubmitFilingCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Filing ID is required")
        private String filingId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AcknowledgeFilingCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Filing ID is required")
        private String filingId;

        @NotBlank(message = "Acknowledgement number is required")
        private String acknowledgementNumber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AcceptFilingCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Filing ID is required")
        private String filingId;

        @NotBlank(message = "Approved by is required")
        private String approvedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RejectFilingCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Filing ID is required")
        private String filingId;

        @NotBlank(message = "Reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddAdjustmentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Filing ID is required")
        private String filingId;

        @NotBlank(message = "Adjustment type is required")
        private String adjustmentType;

        @NotNull(message = "Amount is required")
        private BigDecimal amount;

        private String reason;

        private String reference;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class LinkCalculationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Filing ID is required")
        private String filingId;

        @NotBlank(message = "Calculation ID is required")
        private String calculationId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsPaidCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Filing ID is required")
        private String filingId;

        private String paymentReference;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ArchiveFilingCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Filing ID is required")
        private String filingId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CancelFilingCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Filing ID is required")
        private String filingId;

        @NotBlank(message = "Reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddAttachmentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Filing ID is required")
        private String filingId;

        @NotBlank(message = "File name is required")
        private String fileName;

        @NotBlank(message = "File type is required")
        private String fileType;

        private Long fileSize;

        private String storageLocation;

        private String url;

        @NotBlank(message = "Uploaded by is required")
        private String uploadedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AutoGenerateFilingCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotNull(message = "Filing period is required")
        private YearMonth filingPeriod;

        @NotNull(message = "Jurisdiction is required")
        private com.gogidix.finance.tax.domain.model.TaxRate.Jurisdiction jurisdiction;

        @NotNull(message = "Tax type is required")
        private com.gogidix.finance.tax.domain.model.TaxRate.TaxType taxType;

        @NotNull(message = "Filing type is required")
        private TaxFiling.FilingType filingType;

        @NotBlank(message = "Generated by is required")
        private String generatedBy;
    }
}
