package com.gogidix.finance.ledger.domain.model;

import com.gogidix.finance.ledger.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Ledger Account Domain Entity
 * Represents an account in the chart of accounts
 * Multi-tenant with account hierarchy support
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "ledger_accounts")
public class LedgerAccount extends BaseEntity {

    @Indexed(unique = true)
    private String accountId;

    @Indexed
    private String tenantId;

    @Indexed
    private String accountNumber;

    private String accountName;

    private AccountType accountType;

    private AccountSubType accountSubType;

    private String parentAccountId;

    private Integer accountLevel;

    @Indexed
    private AccountStatus status;

    private String currency;

    private BigDecimal currentBalance;

    private BigDecimal debitBalance;

    private BigDecimal creditBalance;

    private BigDecimal openingBalance;

    private LocalDate openingBalanceDate;

    private String description;

    private String costCenter;

    private String department;

    private String location;

    private Boolean isCashAccount;

    private Boolean isReconcilable;

    private Boolean isTaxAccount;

    private String taxCode;

    private Boolean allowsManualEntry;

    private Integer normalBalanceSide; // 1 = Debit, -1 = Credit

    private String createdByUserId;

    private LocalDateTime lastReconciledAt;

    private String lastReconciledBy;

    @Builder.Default
    private List<AccountTag> tags = new ArrayList<>();

    private BigDecimal creditLimit;

    private String notes;

    private LocalDateTime archivedAt;

    private String archivedBy;

    private String archivedReason;

    @Builder.Default
    private List<AccountBalance> balances = new ArrayList<>();

    public enum AccountType {
        ASSET,
        LIABILITY,
        EQUITY,
        REVENUE,
        EXPENSE
    }

    public enum AccountSubType {
        // Asset subtypes
        CURRENT_ASSET,
        FIXED_ASSET,
        INTANGIBLE_ASSET,
        NON_CURRENT_ASSET,

        // Liability subtypes
        CURRENT_LIABILITY,
        LONG_TERM_LIABILITY,
        PROVISION,

        // Equity subtypes
        SHARE_CAPITAL,
        RETAINED_EARNINGS,
        RESERVES,
        OTHER_EQUITY,

        // Revenue subtypes
        OPERATING_REVENUE,
        NON_OPERATING_REVENUE,
        OTHER_INCOME,

        // Expense subtypes
        COST_OF_SALES,
        OPERATING_EXPENSE,
        NON_OPERATING_EXPENSE,
        DEPRECIATION,
        AMORTIZATION
    }

    public enum AccountStatus {
        ACTIVE,
        INACTIVE,
        PENDING_APPROVAL,
        ARCHIVED,
        FROZEN
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountTag {
        private String key;
        private String value;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountBalance {
        private String currency;
        private BigDecimal balance;
        private LocalDateTime asOfDate;
    }

    /**
     * Creates a new ledger account
     */
    public static LedgerAccount create(String tenantId, String accountNumber, String accountName,
                                       AccountType accountType, AccountSubType accountSubType,
                                       String currency, String createdBy) {
        LedgerAccount account = LedgerAccount.builder()
                .tenantId(tenantId)
                .accountNumber(accountNumber)
                .accountName(accountName)
                .accountType(accountType)
                .accountSubType(accountSubType)
                .currency(currency)
                .status(AccountStatus.ACTIVE)
                .currentBalance(BigDecimal.ZERO)
                .debitBalance(BigDecimal.ZERO)
                .creditBalance(BigDecimal.ZERO)
                .openingBalance(BigDecimal.ZERO)
                .isCashAccount(false)
                .isReconcilable(false)
                .isTaxAccount(false)
                .allowsManualEntry(true)
                .createdByUserId(createdBy)
                .accountLevel(0)
                .build();

        // Set normal balance side based on account type
        account.setNormalBalanceSide(account.getNormalBalanceSideForType(accountType));

        return account;
    }

    /**
     * Activates the account
     */
    public void activate() {
        if (this.status == AccountStatus.ARCHIVED) {
            throw new IllegalStateException("Cannot activate an archived account");
        }
        this.status = AccountStatus.ACTIVE;
    }

    /**
     * Freezes the account - prevents new entries
     */
    public void freeze(String reason) {
        if (this.status != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Can only freeze active accounts");
        }
        this.status = AccountStatus.FROZEN;
        this.notes = reason;
    }

    /**
     * Unfreezes the account
     */
    public void unfreeze() {
        if (this.status != AccountStatus.FROZEN) {
            throw new IllegalStateException("Can only unfrozen frozen accounts");
        }
        this.status = AccountStatus.ACTIVE;
    }

    /**
     * Archives the account
     */
    public void archive(String archivedBy, String reason) {
        if (this.status == AccountStatus.FROZEN) {
            throw new IllegalStateException("Cannot archive a frozen account");
        }
        if (hasNonZeroBalance()) {
            throw new IllegalStateException("Cannot archive account with non-zero balance");
        }
        this.status = AccountStatus.ARCHIVED;
        this.archivedAt = LocalDateTime.now();
        this.archivedBy = archivedBy;
        this.archivedReason = reason;
    }

    /**
     * Updates the balance
     */
    public void updateBalance(BigDecimal debitAmount, BigDecimal creditAmount) {
        if (debitAmount.compareTo(BigDecimal.ZERO) > 0) {
            this.debitBalance = this.debitBalance.add(debitAmount);
        }
        if (creditAmount.compareTo(BigDecimal.ZERO) > 0) {
            this.creditBalance = this.creditBalance.add(creditAmount);
        }

        // Calculate current balance based on normal balance side
        if (normalBalanceSide != null && normalBalanceSide == 1) {
            // Debit normal balance (Assets, Expenses)
            this.currentBalance = this.debitBalance.subtract(this.creditBalance);
        } else {
            // Credit normal balance (Liabilities, Equity, Revenue)
            this.currentBalance = this.creditBalance.subtract(this.debitBalance);
        }
    }

    /**
     * Sets opening balance
     */
    public void setOpeningBalance(BigDecimal amount, LocalDate asOfDate) {
        if (this.status != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Can only set opening balance for active accounts");
        }
        this.openingBalance = amount;
        this.openingBalanceDate = asOfDate;

        // Initialize balances based on opening balance
        if (this.normalBalanceSide != null && this.normalBalanceSide == 1) {
            if (amount.compareTo(BigDecimal.ZERO) >= 0) {
                this.debitBalance = amount;
            } else {
                this.creditBalance = amount.abs();
            }
        } else {
            if (amount.compareTo(BigDecimal.ZERO) >= 0) {
                this.creditBalance = amount;
            } else {
                this.debitBalance = amount.abs();
            }
        }
        this.currentBalance = amount;
    }

    /**
     * Checks if account has non-zero balance
     */
    public boolean hasNonZeroBalance() {
        return currentBalance != null && currentBalance.compareTo(BigDecimal.ZERO) != 0;
    }

    /**
     * Checks if account allows manual entry
     */
    public boolean allowsEntry() {
        return status == AccountStatus.ACTIVE &&
               (allowsManualEntry == null || allowsManualEntry);
    }

    /**
     * Adds a tag to the account
     */
    public void addTag(String key, String value) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.removeIf(tag -> tag.getKey().equals(key));
        this.tags.add(AccountTag.builder()
                .key(key)
                .value(value)
                .build());
    }

    /**
     * Removes a tag from the account
     */
    public void removeTag(String key) {
        if (this.tags != null) {
            this.tags.removeIf(tag -> tag.getKey().equals(key));
        }
    }

    /**
     * Updates reconciliation info
     */
    public void markAsReconciled(String reconciledBy) {
        this.lastReconciledAt = LocalDateTime.now();
        this.lastReconciledBy = reconciledBy;
    }

    /**
     * Gets the normal balance side for account type
     */
    private Integer getNormalBalanceSideForType(AccountType type) {
        return switch (type) {
            case ASSET, EXPENSE -> 1; // Debit normal
            case LIABILITY, EQUITY, REVENUE -> -1; // Credit normal
        };
    }

    /**
     * Validates account number format
     */
    public boolean isValidAccountNumber() {
        return accountNumber != null && accountNumber.matches("^\\d{4,8}$");
    }

    /**
     * Checks if this is a balance sheet account
     */
    public boolean isBalanceSheetAccount() {
        return accountType == AccountType.ASSET ||
               accountType == AccountType.LIABILITY ||
               accountType == AccountType.EQUITY;
    }

    /**
     * Checks if this is an income statement account
     */
    public boolean isIncomeStatementAccount() {
        return accountType == AccountType.REVENUE ||
               accountType == AccountType.EXPENSE;
    }
}
