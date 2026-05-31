package com.gogidix.finance.budgettracking.domain.port.in;

import com.gogidix.finance.budgettracking.domain.model.BudgetTransaction;
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
 * Budget Transaction Commands (Input Port)
 * Defines the input commands for budget transaction operations
 */
public interface BudgetTransactionCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateTransactionCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Budget ID is required")
        private String budgetId;

        @NotBlank(message = "Budget code is required")
        private String budgetCode;

        @NotNull(message = "Transaction type is required")
        private BudgetTransaction.TransactionType transactionType;

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal amount;

        @NotBlank(message = "Currency is required")
        private String currency;

        @NotBlank(message = "Description is required")
        private String description;

        private String referenceType;

        private String referenceId;

        private String category;

        private String department;

        private String costCenter;

        private String projectId;

        private LocalDate transactionDate;

        private List<String> tags;

        private String notes;

        private String correlationId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RecordTransactionCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Transaction ID is required")
        private String transactionId;

        @NotNull(message = "Balance before is required")
        private BigDecimal balanceBefore;

        @NotNull(message = "Balance after is required")
        private BigDecimal balanceAfter;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ApproveTransactionCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Transaction ID is required")
        private String transactionId;

        @NotBlank(message = "Approver is required")
        private String approver;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RejectTransactionCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Transaction ID is required")
        private String transactionId;

        @NotBlank(message = "Rejecter is required")
        private String rejecter;

        @NotBlank(message = "Reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ReverseTransactionCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Transaction ID is required")
        private String transactionId;

        @NotBlank(message = "Reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateTransactionCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Transaction ID is required")
        private String transactionId;

        private String description;

        private BigDecimal amount;

        private List<String> tags;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteTransactionCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Transaction ID is required")
        private String transactionId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddTagCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Transaction ID is required")
        private String transactionId;

        @NotBlank(message = "Tag is required")
        private String tag;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RemoveTagCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Transaction ID is required")
        private String transactionId;

        @NotBlank(message = "Tag is required")
        private String tag;
    }
}
