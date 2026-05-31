package com.gogidix.finance.bankreconciliation.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Statement Line Value Object
 * Represents individual line items within a bank statement
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatementLine {

    @Field("line_number")
    private Integer lineNumber;

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
    private BigDecimal amount;

    @Field("currency")
    private String currency;

    @Field("transaction_type")
    private TransactionType transactionType;

    @Field("category")
    private String category;

    @Field("counterparty_name")
    private String counterpartyName;

    @Field("counterparty_account")
    private String counterpartyAccount;

    @Field("check_number")
    private String checkNumber;

    @Field("is_reconciled")
    private Boolean isReconciled;

    @Field("reconciliation_line_id")
    private String reconciliationLineId;

    @Field("balance_after")
    private BigDecimal balanceAfter;

    @Field("notes")
    private String notes;

    public enum TransactionType {
        DEBIT,
        CREDIT,
        TRANSFER_IN,
        TRANSFER_OUT,
        INTEREST,
        FEE,
        TAX,
        REFUND,
        ADJUSTMENT
    }

    /**
     * Checks if this is a credit transaction
     */
    public boolean isCredit() {
        return transactionType == TransactionType.CREDIT ||
               transactionType == TransactionType.TRANSFER_IN ||
               transactionType == TransactionType.REFUND ||
               transactionType == TransactionType.INTEREST;
    }

    /**
     * Checks if this is a debit transaction
     */
    public boolean isDebit() {
        return transactionType == TransactionType.DEBIT ||
               transactionType == TransactionType.TRANSFER_OUT ||
               transactionType == TransactionType.FEE ||
               transactionType == TransactionType.TAX;
    }

    /**
     * Gets the signed amount (negative for debits)
     */
    public BigDecimal getSignedAmount() {
        if (amount == null) {
            return BigDecimal.ZERO;
        }
        return isDebit() ? amount.negate() : amount;
    }

    /**
     * Gets the absolute amount
     */
    public BigDecimal getAbsoluteAmount() {
        return amount != null ? amount.abs() : BigDecimal.ZERO;
    }

    /**
     * Marks line as reconciled
     */
    public void markAsReconciled(String reconciliationLineId) {
        this.isReconciled = true;
        this.reconciliationLineId = reconciliationLineId;
    }

    /**
     * Unmarks line as reconciled
     */
    public void unreconcile() {
        this.isReconciled = false;
        this.reconciliationLineId = null;
    }

    /**
     * Creates a StatementLine from a BankTransaction
     */
    public static StatementLine fromTransaction(BankTransaction transaction, Integer lineNumber) {
        return StatementLine.builder()
                .lineNumber(lineNumber)
                .transactionDate(transaction.getTransactionDate())
                .description(transaction.getDescription())
                .reference(transaction.getReference())
                .bankReference(transaction.getBankReference())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .transactionType(mapTransactionType(transaction.getTransactionType()))
                .category(transaction.getCategory())
                .counterpartyName(transaction.getCounterpartyName())
                .counterpartyAccount(transaction.getCounterpartyAccount())
                .checkNumber(transaction.getCheckNumber())
                .isReconciled(transaction.getIsReconciled())
                .reconciliationLineId(transaction.getReconciliationLineId())
                .balanceAfter(transaction.getBalanceAfter())
                .build();
    }

    private static TransactionType mapTransactionType(BankTransaction.TransactionType type) {
        if (type == null) {
            return TransactionType.ADJUSTMENT;
        }
        try {
            return TransactionType.valueOf(type.name());
        } catch (IllegalArgumentException e) {
            return TransactionType.ADJUSTMENT;
        }
    }
}
