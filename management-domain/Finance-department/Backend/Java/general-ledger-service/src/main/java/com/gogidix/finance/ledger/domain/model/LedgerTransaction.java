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

/**
 * Ledger Transaction Domain Entity
 * Represents an individual transaction within a journal entry line
 * Tracks the actual debit/credit to each account
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "ledger_transactions")
public class LedgerTransaction extends BaseEntity {

    @Indexed(unique = true)
    private String transactionId;

    @Indexed
    private String tenantId;

    @Indexed
    private String journalEntryId;

    private String journalEntryNumber;

    @Indexed
    private String accountId;

    private String accountNumber;

    private String accountName;

    private LedgerAccount.AccountType accountType;

    @Indexed
    private LocalDate transactionDate;

    private LocalDateTime postingDate;

    private String periodId;

    private Integer fiscalYear;

    private Integer fiscalPeriod;

    private BigDecimal debitAmount;

    private BigDecimal creditAmount;

    private BigDecimal balance;

    private BigDecimal runningBalance;

    private String currency;

    private BigDecimal exchangeRate;

    private String baseCurrency;

    private BigDecimal baseCurrencyAmount;

    private String description;

    private String reference;

    private String sourceDocumentType;

    private String sourceDocumentId;

    private String costCenter;

    private String department;

    private String projectId;

    private String taskId;

    private String createdByUserId;

    private String postedByUserId;

    private Boolean isReversed;

    private String reversedByTransactionId;

    private String batchId;

    private Integer sequenceNumber;

    private String reconciliationStatus;

    private LocalDateTime reconciledAt;

    private String reconciledBy;

    /**
     * Creates a new ledger transaction
     */
    public static LedgerTransaction create(String tenantId, String journalEntryId,
                                          String journalEntryNumber, LocalDate transactionDate,
                                          String accountId, String accountNumber,
                                          String accountName, LedgerAccount.AccountType accountType,
                                          BigDecimal debitAmount, BigDecimal creditAmount,
                                          String currency, String description) {
        return LedgerTransaction.builder()
                .tenantId(tenantId)
                .journalEntryId(journalEntryId)
                .journalEntryNumber(journalEntryNumber)
                .transactionDate(transactionDate)
                .accountId(accountId)
                .accountNumber(accountNumber)
                .accountName(accountName)
                .accountType(accountType)
                .debitAmount(debitAmount != null ? debitAmount : BigDecimal.ZERO)
                .creditAmount(creditAmount != null ? creditAmount : BigDecimal.ZERO)
                .currency(currency)
                .description(description)
                .isReversed(false)
                .build();
    }

    /**
     * Posts the transaction
     */
    public void post(String postedBy, BigDecimal runningBalance) {
        this.postingDate = LocalDateTime.now();
        this.postedByUserId = postedBy;
        this.runningBalance = runningBalance;
        this.balance = debitAmount.subtract(creditAmount);
    }

    /**
     * Reverses the transaction
     */
    public void reverse(String reversedByTransactionId) {
        this.isReversed = true;
        this.reversedByTransactionId = reversedByTransactionId;
    }

    /**
     * Marks transaction as reconciled
     */
    public void markAsReconciled(String reconciledBy) {
        this.reconciliationStatus = "RECONCILED";
        this.reconciledAt = LocalDateTime.now();
        this.reconciledBy = reconciledBy;
    }

    /**
     * Clears reconciliation status
     */
    public void clearReconciliation() {
        this.reconciliationStatus = null;
        this.reconciledAt = null;
        this.reconciledBy = null;
    }

    /**
     * Checks if transaction is reconciled
     */
    public boolean isReconciled() {
        return "RECONCILED".equals(this.reconciliationStatus);
    }

    /**
     * Gets the net amount of the transaction
     */
    public BigDecimal getNetAmount() {
        BigDecimal debit = debitAmount != null ? debitAmount : BigDecimal.ZERO;
        BigDecimal credit = creditAmount != null ? creditAmount : BigDecimal.ZERO;
        return debit.subtract(credit);
    }

    /**
     * Checks if this is a debit transaction
     */
    public boolean isDebit() {
        BigDecimal debit = debitAmount != null ? debitAmount : BigDecimal.ZERO;
        return debit.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Checks if this is a credit transaction
     */
    public boolean isCredit() {
        BigDecimal credit = creditAmount != null ? creditAmount : BigDecimal.ZERO;
        return credit.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Converts amount to base currency
     */
    public void convertToBaseCurrency(BigDecimal exchangeRate, String baseCurrency) {
        if (exchangeRate != null && exchangeRate.compareTo(BigDecimal.ZERO) > 0) {
            this.exchangeRate = exchangeRate;
            this.baseCurrency = baseCurrency;
            this.baseCurrencyAmount = getNetAmount().multiply(exchangeRate);
        }
    }
}
