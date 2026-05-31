package com.gogidix.sales.revenue.domain.port.in;

import com.gogidix.sales.revenue.domain.model.Revenue;
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

        @NotBlank(message = "Contract ID is required")
        private String contractId;

        @NotBlank(message = "Customer ID is required")
        private String customerId;

        private String customerName;

        @NotBlank(message = "Product ID is required")
        private String productId;

        private String productName;

        @NotNull(message = "Revenue type is required")
        private Revenue.RevenueType revenueType;

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal totalAmount;

        @NotBlank(message = "Currency is required")
        private String currency;

        @NotNull(message = "Recognition type is required")
        private Revenue.RevenueRecognitionType recognitionType;

        @NotNull(message = "Start date is required")
        private LocalDate startDate;

        private LocalDate endDate;

        private Integer recognitionPeriodMonths;

        private String territory;

        private String region;

        private String salespersonId;

        private String salespersonName;

        private String department;

        private String costCenter;

        private String projectId;

        private String salesOrderId;

        private String opportunityId;

        private List<String> tags;

        private String description;

        private String notes;

        private String invoiceId;

        private String invoiceNumber;

        private LocalDate invoiceDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateRevenueCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Revenue ID is required")
        private String revenueId;

        private String description;

        private String notes;

        private List<String> tags;

        private String salespersonId;

        private String salespersonName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class BookRevenueCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Revenue ID is required")
        private String revenueId;

        @NotBlank(message = "User ID is required")
        private String userId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RecognizeRevenueCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Revenue ID is required")
        private String revenueId;

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal amount;

        @NotNull(message = "Recognition date is required")
        private LocalDate recognitionDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeferRevenueCommand {
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
    class ReverseRevenueCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Revenue ID is required")
        private String revenueId;

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal amount;

        @NotBlank(message = "Reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ReconcileRevenueCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Revenue ID is required")
        private String revenueId;

        @NotBlank(message = "Reconciliation ID is required")
        private String reconciliationId;
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
