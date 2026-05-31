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

/**
 * Bank Transaction Domain Entity
 * Represents individual transactions from bank statements
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "bank_transactions")
public class BankTransaction extends BaseEntity {

    @Indexed
    @Field("transaction_id")
    private String transactionId;

    @Indexed
    @Field("account_id")
    private String accountId;

    @Field("account_number")
    private String accountNumber;

    @Indexed
    @Field("statement_id")
    private String statementId;

    @Field("transaction_date")
    private LocalDate transactionDate;

    @Field("value_date")
    private LocalDate valueDate;

    @Field("description")
    private String description;

    @Field("reference")
    private String reference;

    @Field("bank_reference")
    private String bankReference;

    @Field("amount")
    private java.math.BigDecimal amount;

    @Field("currency")
    private String currency;

    @Field("transaction_type")
    private TransactionType transactionType;

    @Field("category")
    private String category;

    @Field("sub_category")
    private String subCategory;

    @Field("counterparty_name")
    private String counterpartyName;

    @Field("counterparty_account")
    private String counterpartyAccount;

    @Field("counterparty_bank")
    private String counterpartyBank;

    @Field("is_reconciled")
    private Boolean isReconciled;

    @Field("reconciliation_line_id")
    private String reconciliationLineId;

    @Field("reconciliation_id")
    private String reconciliationId;

    @Field("reconciled_at")
    private Instant reconciledAt;

    @Field("balance_after")
    private java.math.BigDecimal balanceAfter;

    @Field("running_balance")
    private java.math.BigDecimal runningBalance;

    @Field("is_reversal")
    private Boolean isReversal;

    @Field("original_transaction_id")
    private String originalTransactionId;

    @Field("check_number")
    private String checkNumber;

    @Field("payment_method")
    private PaymentMethod paymentMethod;

    @Field("status")
    private TransactionStatus status;

    @Field("notes")
    private String notes;

    @Field("tags")
    private java.util.List<String> tags;

    @Field("metadata")
    private java.util.Map<String, Object> metadata;

    public enum TransactionType {
        DEBIT,
        CREDIT,
        TRANSFER_IN,
        TRANSFER_OUT,
        DIRECT_DEBIT,
        DIRECT_CREDIT,
        STANDING_ORDER,
        WIRE_TRANSFER,
        CHECK,
        INTEREST,
        FEE,
        TAX,
        REFUND,
        CHARGEBACK,
        ADJUSTMENT,
        OTHER
    }

    public enum PaymentMethod {
        CASH,
        CHECK,
        WIRE,
        ACH,
        CARD,
        ELECTRONIC,
        OTHER
    }

    public enum TransactionStatus {
        PENDING,
        POSTED,
        CLEARED,
        REVERSED,
        DISPUTED,
        HOLD
    }

    /**
     * Creates a new bank transaction
     */
    public static BankTransaction create(String tenantId, String accountId, String accountNumber,
                                         String statementId, LocalDate transactionDate,
                                         String description, java.math.BigDecimal amount,
                                         TransactionType transactionType, String currency) {
        BankTransaction transaction = new BankTransaction();
        transaction.setTenantId(tenantId);
        transaction.setAccountId(accountId);
        transaction.setAccountNumber(accountNumber);
        transaction.setStatementId(statementId);
        transaction.setTransactionDate(transactionDate);
        transaction.setDescription(description);
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);
        transaction.setCurrency(currency);
        transaction.setIsReconciled(false);
        transaction.setIsReversal(false);
        transaction.setStatus(TransactionStatus.POSTED);
        transaction.setTags(new java.util.ArrayList<>());
        transaction.setMetadata(new java.util.HashMap<>());

        return transaction;
    }

    /**
     * Marks transaction as reconciled
     */
    public void markAsReconciled(String reconciliationId, String reconciliationLineId) {
        this.isReconciled = true;
        this.reconciliationId = reconciliationId;
        this.reconciliationLineId = reconciliationLineId;
        this.reconciledAt = Instant.now();
        updateTimestamp();
    }

    /**
     * Unmarks transaction as reconciled
     */
    public void unreconcile() {
        this.isReconciled = false;
        this.reconciliationId = null;
        this.reconciliationLineId = null;
        this.reconciledAt = null;
        updateTimestamp();
    }

    /**
     * Sets the balance after transaction
     */
    public void setBalanceAfter(java.math.BigDecimal balance) {
        this.balanceAfter = balance;
        updateTimestamp();
    }

    /**
     * Sets the running balance
     */
    public void setRunningBalance(java.math.BigDecimal balance) {
        this.runningBalance = balance;
        updateTimestamp();
    }

    /**
     * Marks as a reversal transaction
     */
    public void markAsReversal(String originalTransactionId) {
        this.isReversal = true;
        this.originalTransactionId = originalTransactionId;
        updateTimestamp();
    }

    /**
     * Adds a tag
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new java.util.ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
        updateTimestamp();
    }

    /**
     * Removes a tag
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
        updateTimestamp();
    }

    /**
     * Adds metadata
     */
    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new java.util.HashMap<>();
        }
        this.metadata.put(key, value);
        updateTimestamp();
    }

    /**
     * Checks if transaction is a credit
     */
    public boolean isCredit() {
        return this.transactionType == TransactionType.CREDIT ||
               this.transactionType == TransactionType.TRANSFER_IN ||
               this.transactionType == TransactionType.DIRECT_CREDIT ||
               this.transactionType == TransactionType.REFUND;
    }

    /**
     * Checks if transaction is a debit
     */
    public boolean isDebit() {
        return this.transactionType == TransactionType.DEBIT ||
               this.transactionType == TransactionType.TRANSFER_OUT ||
               this.transactionType == TransactionType.DIRECT_DEBIT;
    }

    /**
     * Gets the absolute amount
     */
    public java.math.BigDecimal getAbsoluteAmount() {
        return this.amount != null ? this.amount.abs() : java.math.BigDecimal.ZERO;
    }

    /**
     * Gets the signed amount (negative for debits)
     */
    public java.math.BigDecimal getSignedAmount() {
        if (this.amount == null) {
            return java.math.BigDecimal.ZERO;
        }
        return isDebit() ? this.amount.negate() : this.amount;
    }
}
