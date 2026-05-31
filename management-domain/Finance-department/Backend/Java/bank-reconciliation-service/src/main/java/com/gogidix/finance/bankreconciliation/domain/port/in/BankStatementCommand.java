package com.gogidix.finance.bankreconciliation.domain.port.in;

import com.gogidix.finance.bankreconciliation.domain.model.BankStatement;
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
 * Bank Statement Commands (Input Port)
 * Defines the input commands for bank statement operations
 */
public interface BankStatementCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ImportBankStatementCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Account ID is required")
        private String accountId;

        @NotBlank(message = "Account number is required")
        private String accountNumber;

        @NotNull(message = "Statement date is required")
        private LocalDate statementDate;

        @NotNull(message = "Period start date is required")
        private LocalDate startDate;

        @NotNull(message = "Period end date is required")
        private LocalDate endDate;

        @NotNull(message = "Opening balance is required")
        private BigDecimal openingBalance;

        @NotNull(message = "Closing balance is required")
        private BigDecimal closingBalance;

        @NotBlank(message = "Currency is required")
        private String currency;

        @NotNull(message = "Import source is required")
        private BankStatement.ImportSource importSource;

        private String fileReference;

        private String bankReference;

        private BankStatement.StatementType statementType;

        private List<BankStatement.StatementTransaction> transactions;

        private String importedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ProcessBankStatementCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Statement ID is required")
        private String statementId;

        private String processedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ValidateBankStatementCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Statement ID is required")
        private String statementId;

        private BigDecimal tolerance;

        private Boolean validateBalances;

        private Boolean validateTransactions;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddTransactionCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Statement ID is required")
        private String statementId;

        @NotNull(message = "Transaction is required")
        private BankStatement.StatementTransaction transaction;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class LinkToReconciliationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Statement ID is required")
        private String statementId;

        @NotBlank(message = "Reconciliation ID is required")
        private String reconciliationId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteBankStatementCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Statement ID is required")
        private String statementId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RetryImportCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Statement ID is required")
        private String statementId;

        private String retriedBy;
    }
}
