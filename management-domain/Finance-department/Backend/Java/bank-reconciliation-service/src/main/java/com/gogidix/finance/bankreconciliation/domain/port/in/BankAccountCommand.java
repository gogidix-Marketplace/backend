package com.gogidix.finance.bankreconciliation.domain.port.in;

import com.gogidix.finance.bankreconciliation.domain.model.BankAccount;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Bank Account Commands (Input Port)
 * Defines the input commands for bank account operations
 */
public interface BankAccountCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateBankAccountCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Account number is required")
        private String accountNumber;

        @NotBlank(message = "Account name is required")
        private String accountName;

        @NotNull(message = "Account type is required")
        private BankAccount.AccountType accountType;

        @NotBlank(message = "Bank name is required")
        private String bankName;

        @NotBlank(message = "Currency is required")
        @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a valid ISO 4217 code")
        private String currency;

        private String bankCode;

        private BigDecimal openingBalance;

        private LocalDate balanceDate;

        private String iban;

        private String swiftCode;

        private String routingNumber;

        private String description;

        private List<String> tags;

        private BankAccount.StatementFrequency statementFrequency;

        private BigDecimal reconciliationTolerance;

        private Boolean autoReconcile;

        private Boolean isPrimary;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateBankAccountCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Account ID is required")
        private String accountId;

        private String accountName;

        private String description;

        private BigDecimal balance;

        private LocalDate balanceDate;

        private String iban;

        private String swiftCode;

        private String routingNumber;

        private List<String> tags;

        private BigDecimal reconciliationTolerance;

        private BankAccount.StatementFrequency statementFrequency;

        private Boolean autoReconcile;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateBalanceCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Account ID is required")
        private String accountId;

        @NotNull(message = "Balance is required")
        private BigDecimal newBalance;

        @NotNull(message = "Balance date is required")
        private LocalDate balanceDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SetAsPrimaryCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Account ID is required")
        private String accountId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ActivateAccountCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Account ID is required")
        private String accountId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeactivateAccountCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Account ID is required")
        private String accountId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CloseAccountCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Account ID is required")
        private String accountId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsReconciledCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Account ID is required")
        private String accountId;

        @NotNull(message = "Statement date is required")
        private LocalDate statementDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteBankAccountCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Account ID is required")
        private String accountId;
    }
}
