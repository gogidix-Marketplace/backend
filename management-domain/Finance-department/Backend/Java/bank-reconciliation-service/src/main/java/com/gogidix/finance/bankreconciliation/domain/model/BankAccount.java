package com.gogidix.finance.bankreconciliation.domain.model;

import com.gogidix.finance.bankreconciliation.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Bank Account Domain Entity
 * Multi-tenant bank account management for reconciliation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "bank_accounts")
public class BankAccount extends BaseEntity {

    @Indexed(unique = true)
    @Field("account_number")
    private String accountNumber;

    @Field("account_name")
    private String accountName;

    @Field("account_type")
    private AccountType accountType;

    @Field("bank_name")
    private String bankName;

    @Field("bank_code")
    private String bankCode;

    @Field("currency")
    private String currency;

    @Field("balance")
    private java.math.BigDecimal balance;

    @Field("balance_date")
    private LocalDate balanceDate;

    @Field("status")
    private AccountStatus status;

    @Field("is_primary")
    private Boolean isPrimary;

    @Field("last_reconciled_at")
    private Instant lastReconciledAt;

    @Field("last_statement_date")
    private LocalDate lastStatementDate;

    @Field("opening_balance")
    private java.math.BigDecimal openingBalance;

    @Field("iban")
    private String iban;

    @Field("swift_code")
    private String swiftCode;

    @Field("routing_number")
    private String routingNumber;

    @Field("description")
    private String description;

    @Field("tags")
    private List<String> tags;

    @Field("statement_frequency")
    private StatementFrequency statementFrequency;

    @Field("reconciliation_tolerance")
    private java.math.BigDecimal reconciliationTolerance;

    @Field("auto_reconcile")
    private Boolean autoReconcile;

    public enum AccountType {
        CHECKING,
        SAVINGS,
        MONEY_MARKET,
        CREDIT_CARD,
        LOAN,
        INVESTMENT,
        CASH,
        OTHER
    }

    public enum AccountStatus {
        ACTIVE,
        INACTIVE,
        FROZEN,
        CLOSED,
        PENDING_ACTIVATION
    }

    public enum StatementFrequency {
        DAILY,
        WEEKLY,
        MONTHLY,
        QUARTERLY,
        ANNUALLY,
        ON_DEMAND
    }

    /**
     * Creates a new bank account
     */
    public static BankAccount create(String tenantId, String accountNumber, String accountName,
                                      AccountType accountType, String bankName, String currency) {
        BankAccount account = new BankAccount();
        account.setTenantId(tenantId);
        account.setAccountNumber(accountNumber);
        account.setAccountName(accountName);
        account.setAccountType(accountType);
        account.setBankName(bankName);
        account.setCurrency(currency);
        account.setStatus(AccountStatus.ACTIVE);
        account.setIsPrimary(false);
        account.setAutoReconcile(false);
        account.setReconciliationTolerance(new java.math.BigDecimal("0.01"));
        account.setStatementFrequency(StatementFrequency.MONTHLY);
        account.setTags(new ArrayList<>());
        account.setBalance(java.math.BigDecimal.ZERO);
        account.setOpeningBalance(java.math.BigDecimal.ZERO);

        return account;
    }

    /**
     * Updates the current balance
     */
    public void updateBalance(java.math.BigDecimal newBalance, LocalDate balanceDate) {
        if (newBalance.compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        this.balance = newBalance;
        this.balanceDate = balanceDate;
        updateTimestamp();
    }

    /**
     * Sets as primary account
     */
    public void setAsPrimary() {
        this.isPrimary = true;
        updateTimestamp();
    }

    /**
     * Deactivates the account
     */
    public void deactivate() {
        if (this.status == AccountStatus.CLOSED) {
            throw new IllegalStateException("Cannot deactivate a closed account");
        }
        this.status = AccountStatus.INACTIVE;
        updateTimestamp();
    }

    /**
     * Activates the account
     */
    public void activate() {
        if (this.status == AccountStatus.CLOSED) {
            throw new IllegalStateException("Cannot activate a closed account");
        }
        this.status = AccountStatus.ACTIVE;
        updateTimestamp();
    }

    /**
     * Closes the account
     */
    public void close() {
        if (this.status == AccountStatus.CLOSED) {
            throw new IllegalStateException("Account is already closed");
        }
        if (this.balance.compareTo(java.math.BigDecimal.ZERO) != 0) {
            throw new IllegalStateException("Cannot close account with non-zero balance");
        }
        this.status = AccountStatus.CLOSED;
        updateTimestamp();
    }

    /**
     * Updates last reconciled timestamp
     */
    public void markAsReconciled(LocalDate statementDate) {
        this.lastReconciledAt = Instant.now();
        this.lastStatementDate = statementDate;
        updateTimestamp();
    }

    /**
     * Checks if account is ready for reconciliation
     */
    public boolean isReadyForReconciliation() {
        return this.status == AccountStatus.ACTIVE &&
               this.balance != null &&
               this.balanceDate != null;
    }

    /**
     * Adds a tag to the account
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
        updateTimestamp();
    }

    /**
     * Removes a tag from the account
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
        updateTimestamp();
    }

    /**
     * Sets reconciliation tolerance
     */
    public void setReconciliationTolerance(java.math.BigDecimal tolerance) {
        if (tolerance.compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Tolerance cannot be negative");
        }
        this.reconciliationTolerance = tolerance;
        updateTimestamp();
    }
}
