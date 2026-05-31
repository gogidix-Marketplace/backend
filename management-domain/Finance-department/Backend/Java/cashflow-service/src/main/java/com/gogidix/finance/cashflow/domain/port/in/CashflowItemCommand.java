package com.gogidix.finance.cashflow.domain.port.in;

import com.gogidix.finance.cashflow.domain.model.CashflowItem;
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
 * Cashflow Item Commands (Input Port)
 * Defines the input commands for cashflow item operations
 */
public interface CashflowItemCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateCashflowItemCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Recorded by is required")
        private String recordedBy;

        private String reference;

        @NotNull(message = "Type is required")
        private CashflowItem.CashflowType type;

        @NotNull(message = "Category is required")
        private CashflowItem.CashflowCategory category;

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal amount;

        @NotBlank(message = "Currency is required")
        private String currency;

        @NotNull(message = "Transaction date is required")
        private LocalDate transactionDate;

        private LocalDate expectedDate;

        @NotBlank(message = "Description is required")
        private String description;

        private String counterparty;

        private String account;

        private String costCenter;

        private String projectId;

        private Boolean recurring;

        private CashflowItem.RecurringFrequency recurringFrequency;

        private String parentRecurringItemId;

        private String paymentMethod;

        private BigDecimal taxAmount;

        private List<String> tags;

        private String notes;

        private String linkedExpenseId;

        private String linkedRevenueId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateCashflowItemCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Cashflow Item ID is required")
        private String cashflowItemId;

        private String description;

        private BigDecimal amount;

        private LocalDate expectedDate;

        private LocalDate transactionDate;

        private String counterparty;

        private String account;

        private String costCenter;

        private List<String> tags;

        private String notes;

        private BigDecimal taxAmount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsExpectedCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Cashflow Item ID is required")
        private String cashflowItemId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CommitCashflowItemCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Cashflow Item ID is required")
        private String cashflowItemId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SettleCashflowItemCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Cashflow Item ID is required")
        private String cashflowItemId;

        private String bankReference;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CancelCashflowItemCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Cashflow Item ID is required")
        private String cashflowItemId;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsFailedCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Cashflow Item ID is required")
        private String cashflowItemId;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SetupRecurringCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Cashflow Item ID is required")
        private String cashflowItemId;

        @NotNull(message = "Frequency is required")
        private CashflowItem.RecurringFrequency frequency;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteCashflowItemCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Cashflow Item ID is required")
        private String cashflowItemId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class BulkCreateCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Recorded by is required")
        private String recordedBy;

        @NotNull(message = "Items are required")
        private List<CreateCashflowItemCommand> items;
    }
}
