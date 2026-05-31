package com.gogidix.finance.revenue.domain.port.in;

import com.gogidix.finance.revenue.domain.model.Revenue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Revenue Commands (Input Port)
 * Defines the input commands for revenue operations
 */
public interface RevenueCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateRevenueCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Customer ID is required")
        private String customerId;

        private String customerName;

        private String contractId;

        private String projectId;

        @NotNull(message = "Revenue type is required")
        private Revenue.RevenueType type;

        @NotNull(message = "Total amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal totalAmount;

        @NotBlank(message = "Currency is required")
        private String currency;

        @NotNull(message = "Recognition method is required")
        private Revenue.RecognitionMethod recognitionMethod;

        @NotNull(message = "Transaction date is required")
        private LocalDate transactionDate;

        private String description;

        private String category;

        private String productCode;

        private String productSku;

        private String department;

        private String costCenter;

        private String salespersonId;

        private String region;

        private String territory;

        private Revenue.PaymentTerms paymentTerms;

        private LocalDate invoiceDate;

        private String invoiceNumber;

        private LocalDate dueDate;

        private Integer recognitionPeriods;

        private LocalDate recognitionStartDate;

        private List<Revenue.RevenueMilestone> milestones;

        private List<String> tags;

        private String notes;

        private Boolean isRecurring;

        private String recurringSchedule;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RecognizeRevenueCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Revenue ID is required")
        private String revenueId;

        @NotNull(message = "Recognition amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal amount;

        @NotBlank(message = "Recognized by is required")
        private String recognizedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeferRevenueCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Revenue ID is required")
        private String revenueId;

        @NotNull(message = "Periods is required")
        private Integer periods;

        @NotNull(message = "Start date is required")
        private LocalDate startDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CompleteMilestoneCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Revenue ID is required")
        private String revenueId;

        @NotBlank(message = "Milestone ID is required")
        private String milestoneId;

        @NotNull(message = "Completed date is required")
        private LocalDate completedDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CancelRevenueCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Revenue ID is required")
        private String revenueId;

        @NotBlank(message = "Reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RefundRevenueCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Revenue ID is required")
        private String revenueId;

        @NotNull(message = "Refund amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal refundAmount;

        @NotBlank(message = "Reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsPaidCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Revenue ID is required")
        private String revenueId;

        @NotNull(message = "Paid date is required")
        private LocalDate paidDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteRevenueCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Revenue ID is required")
        private String revenueId;
    }
}
