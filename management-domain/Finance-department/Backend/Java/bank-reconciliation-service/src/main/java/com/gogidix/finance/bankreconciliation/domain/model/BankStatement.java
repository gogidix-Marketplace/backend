package com.gogidix.finance.bankreconciliation.domain.model;

import com.gogidix.finance.bankreconciliation.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Bank Statement Domain Entity
 * Multi-tenant bank statement with import status tracking
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "bank_statements")
public class BankStatement extends BaseEntity {

    @Indexed
    @Field("statement_id")
    private String statementId;

    @Indexed
    @Field("account_id")
    private String accountId;

    @Field("account_number")
    private String accountNumber;

    @Field("statement_date")
    private LocalDate statementDate;

    @Field("start_date")
    private LocalDate startDate;

    @Field("end_date")
    private LocalDate endDate;

    @Field("opening_balance")
    private java.math.BigDecimal openingBalance;

    @Field("closing_balance")
    private java.math.BigDecimal closingBalance;

    @Field("currency")
    private String currency;

    @Field("import_status")
    private ImportStatus importStatus;

    @Field("import_source")
    private ImportSource importSource;

    @Field("file_reference")
    private String fileReference;

    @Field("transaction_count")
    private Integer transactionCount;

    @Field("total_debits")
    private java.math.BigDecimal totalDebits;

    @Field("total_credits")
    private java.math.BigDecimal totalCredits;

    @Field("import_errors")
    private List<String> importErrors;

    @Field("import_warnings")
    private List<String> importWarnings;

    @Field("processed_at")
    private java.time.Instant processedAt;

    @Field("validated_at")
    private java.time.Instant validatedAt;

    @Field("reconciled")
    private Boolean reconciled;

    @Field("reconciliation_id")
    private String reconciliationId;

    @Field("statement_type")
    private StatementType statementType;

    @Field("bank_reference")
    private String bankReference;

    @Field("transactions")
    private List<StatementTransaction> transactions;

    public enum ImportStatus {
        PENDING,
        PROCESSING,
        COMPLETED,
        FAILED,
        PARTIAL_SUCCESS,
        VALIDATING
    }

    public enum ImportSource {
        MANUAL_UPLOAD,
        BANK_API,
        SFTP,
        EMAIL,
        BATCH_IMPORT,
        AUTOMATIC_FEED
    }

    public enum StatementType {
        STATEMENT,
        INTERIM,
        FINAL,
        CORRECTION
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatementTransaction {
        private String transactionId;
        private LocalDate transactionDate;
        private String description;
        private String reference;
        private java.math.BigDecimal amount;
        private TransactionType transactionType;
        private String category;
        private Boolean isReconciled;
        private String reconciliationLineId;
    }

    public enum TransactionType {
        DEBIT,
        CREDIT,
        TRANSFER_IN,
        TRANSFER_OUT,
        INTEREST,
        FEE,
        TAX
    }

    /**
     * Creates a new bank statement
     */
    public static BankStatement create(String tenantId, String accountId, String accountNumber,
                                       LocalDate statementDate, LocalDate startDate, LocalDate endDate,
                                       java.math.BigDecimal openingBalance, java.math.BigDecimal closingBalance,
                                       String currency, ImportSource source) {
        BankStatement statement = new BankStatement();
        statement.setTenantId(tenantId);
        statement.setAccountId(accountId);
        statement.setAccountNumber(accountNumber);
        statement.setStatementDate(statementDate);
        statement.setStartDate(startDate);
        statement.setEndDate(endDate);
        statement.setOpeningBalance(openingBalance);
        statement.setClosingBalance(closingBalance);
        statement.setCurrency(currency);
        statement.setImportStatus(ImportStatus.PENDING);
        statement.setImportSource(source);
        statement.setReconciled(false);
        statement.setStatementType(StatementType.STATEMENT);
        statement.setTransactions(new ArrayList<>());
        statement.setImportErrors(new ArrayList<>());
        statement.setImportWarnings(new ArrayList<>());

        return statement;
    }

    /**
     * Marks statement as processing
     */
    public void markAsProcessing() {
        if (this.importStatus != ImportStatus.PENDING) {
            throw new IllegalStateException("Can only process pending statements");
        }
        this.importStatus = ImportStatus.PROCESSING;
        updateTimestamp();
    }

    /**
     * Marks statement as completed
     */
    public void markAsCompleted() {
        this.importStatus = ImportStatus.COMPLETED;
        this.processedAt = java.time.Instant.now();
        updateTimestamp();
    }

    /**
     * Marks statement as failed
     */
    public void markAsFailed(String error) {
        this.importStatus = ImportStatus.FAILED;
        if (this.importErrors == null) {
            this.importErrors = new ArrayList<>();
        }
        this.importErrors.add(error);
        updateTimestamp();
    }

    /**
     * Adds an import error
     */
    public void addImportError(String error) {
        if (this.importErrors == null) {
            this.importErrors = new ArrayList<>();
        }
        this.importErrors.add(error);
        updateTimestamp();
    }

    /**
     * Adds an import warning
     */
    public void addImportWarning(String warning) {
        if (this.importWarnings == null) {
            this.importWarnings = new ArrayList<>();
        }
        this.importWarnings.add(warning);
        updateTimestamp();
    }

    /**
     * Adds a transaction to the statement
     */
    public void addTransaction(StatementTransaction transaction) {
        if (this.transactions == null) {
            this.transactions = new ArrayList<>();
        }
        this.transactions.add(transaction);
        this.transactionCount = this.transactions.size();
        updateTimestamp();
    }

    /**
     * Calculates totals
     */
    public void calculateTotals() {
        if (this.transactions == null || this.transactions.isEmpty()) {
            this.totalDebits = java.math.BigDecimal.ZERO;
            this.totalCredits = java.math.BigDecimal.ZERO;
            return;
        }

        java.math.BigDecimal debits = java.math.BigDecimal.ZERO;
        java.math.BigDecimal credits = java.math.BigDecimal.ZERO;

        for (StatementTransaction transaction : this.transactions) {
            if (transaction.getAmount() != null) {
                if (transaction.getTransactionType() == TransactionType.DEBIT ||
                    transaction.getTransactionType() == TransactionType.TRANSFER_OUT ||
                    transaction.getTransactionType() == TransactionType.FEE) {
                    debits = debits.add(transaction.getAmount().abs());
                } else {
                    credits = credits.add(transaction.getAmount().abs());
                }
            }
        }

        this.totalDebits = debits;
        this.totalCredits = credits;
        updateTimestamp();
    }

    /**
     * Validates statement balances
     */
    public boolean validateBalances() {
        this.validatedAt = java.time.Instant.now();

        java.math.BigDecimal calculatedClosing = this.openingBalance
                .add(this.totalCredits != null ? this.totalCredits : java.math.BigDecimal.ZERO)
                .subtract(this.totalDebits != null ? this.totalDebits : java.math.BigDecimal.ZERO);

        boolean isValid = calculatedClosing.compareTo(
                this.closingBalance != null ? this.closingBalance : java.math.BigDecimal.ZERO) == 0;

        if (!isValid) {
            addImportWarning("Balance mismatch: Expected " + this.closingBalance + ", calculated " + calculatedClosing);
        }

        updateTimestamp();
        return isValid;
    }

    /**
     * Links statement to reconciliation
     */
    public void linkToReconciliation(String reconciliationId) {
        this.reconciliationId = reconciliationId;
        this.reconciled = true;
        updateTimestamp();
    }

    /**
     * Checks if statement is ready for reconciliation
     */
    public boolean isReadyForReconciliation() {
        return this.importStatus == ImportStatus.COMPLETED &&
               this.transactions != null &&
               !this.transactions.isEmpty() &&
               this.openingBalance != null &&
               this.closingBalance != null;
    }

    /**
     * Gets unreconciled transactions
     */
    public List<StatementTransaction> getUnreconciledTransactions() {
        if (this.transactions == null) {
            return new ArrayList<>();
        }
        return this.transactions.stream()
                .filter(t -> t.getIsReconciled() == null || !t.getIsReconciled())
                .toList();
    }
}
