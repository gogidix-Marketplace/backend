package com.gogidix.finance.ledger.domain.port.in;

import com.gogidix.finance.ledger.domain.model.LedgerAccount;
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
 * Ledger Account Commands (Input Port)
 * Defines the input commands for ledger account operations
 */
public interface LedgerAccountCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateAccountCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Account number is required")
        @Pattern(regexp = "^\\d{4,8}$", message = "Account number must be 4-8 digits")
        private String accountNumber;

        @NotBlank(message = "Account name is required")
        private String accountName;

        @NotNull(message = "Account type is required")
        private LedgerAccount.AccountType accountType;

        @NotNull(message = "Account subtype is required")
        private LedgerAccount.AccountSubType accountSubType;

        @NotBlank(message = "Currency is required")
        private String currency;

        @NotBlank(message = "Created by is required")
        private String createdBy;

        private String parentAccountId;

        private Integer accountLevel;

        private String description;

        private String costCenter;

        private String department;

        private String location;

        private Boolean isCashAccount;

        private Boolean isReconcilable;

        private Boolean isTaxAccount;

        private String taxCode;

        private Boolean allowsManualEntry;

        private BigDecimal creditLimit;

        private String notes;

        private List<LedgerAccount.AccountTag> tags;

        private BigDecimal openingBalance;

        private LocalDate openingBalanceDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateAccountCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Account ID is required")
        private String accountId;

        private String accountName;

        private String description;

        private String costCenter;

        private String department;

        private String location;

        private Boolean isCashAccount;

        private Boolean isReconcilable;

        private String taxCode;

        private Boolean allowsManualEntry;

        private BigDecimal creditLimit;

        private String notes;

        private List<LedgerAccount.AccountTag> tags;
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
    class FreezeAccountCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Account ID is required")
        private String accountId;

        @NotBlank(message = "Reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UnfreezeAccountCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Account ID is required")
        private String accountId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ArchiveAccountCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Account ID is required")
        private String accountId;

        @NotBlank(message = "Archived by is required")
        private String archivedBy;

        @NotBlank(message = "Reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SetOpeningBalanceCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Account ID is required")
        private String accountId;

        @NotNull(message = "Opening balance is required")
        private BigDecimal openingBalance;

        @NotNull(message = "As of date is required")
        private LocalDate asOfDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddTagCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Account ID is required")
        private String accountId;

        @NotBlank(message = "Tag key is required")
        private String key;

        private String value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RemoveTagCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Account ID is required")
        private String accountId;

        @NotBlank(message = "Tag key is required")
        private String key;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteAccountCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Account ID is required")
        private String accountId;
    }
}
