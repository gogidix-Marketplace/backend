package com.gogidix.finance.ledger.infrastructure.persistence.mongodb;

import com.gogidix.finance.ledger.domain.model.LedgerAccount;
import com.gogidix.finance.ledger.domain.model.LedgerTransaction;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "ledger_transactions")
public class LedgerTransactionEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field("transaction_id")
    private String transactionId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("journal_entry_id")
    private String journalEntryId;

    @Field("journal_entry_number")
    private String journalEntryNumber;

    @Indexed
    @Field("account_id")
    private String accountId;

    @Field("account_number")
    private String accountNumber;

    @Field("account_name")
    private String accountName;

    @Field("account_type")
    private String accountType;

    @Indexed
    @Field("transaction_date")
    private LocalDate transactionDate;

    @Field("posting_date")
    private LocalDateTime postingDate;

    @Field("period_id")
    private String periodId;

    @Field("fiscal_year")
    private Integer fiscalYear;

    @Field("fiscal_period")
    private Integer fiscalPeriod;

    @Field("debit_amount")
    private BigDecimal debitAmount;

    @Field("credit_amount")
    private BigDecimal creditAmount;

    @Field("balance")
    private BigDecimal balance;

    @Field("running_balance")
    private BigDecimal runningBalance;

    @Field("currency")
    private String currency;

    @Field("exchange_rate")
    private BigDecimal exchangeRate;

    @Field("base_currency")
    private String baseCurrency;

    @Field("base_currency_amount")
    private BigDecimal baseCurrencyAmount;

    @Field("description")
    private String description;

    @Field("reference")
    private String reference;

    @Field("source_document_type")
    private String sourceDocumentType;

    @Field("source_document_id")
    private String sourceDocumentId;

    @Field("cost_center")
    private String costCenter;

    @Field("department")
    private String department;

    @Field("project_id")
    private String projectId;

    @Field("task_id")
    private String taskId;

    @Field("created_by_user_id")
    private String createdByUserId;

    @Field("posted_by_user_id")
    private String postedByUserId;

    @Field("is_reversed")
    private Boolean isReversed;

    @Field("reversed_by_transaction_id")
    private String reversedByTransactionId;

    @Field("batch_id")
    private String batchId;

    @Field("sequence_number")
    private Integer sequenceNumber;

    @Field("reconciliation_status")
    private String reconciliationStatus;

    @Field("reconciled_at")
    private LocalDateTime reconciledAt;

    @Field("reconciled_by")
    private String reconciledBy;

    public LedgerTransactionEntity() {
    }

    public LedgerTransactionEntity(LedgerTransaction transaction) {
        this.transactionId = transaction.getTransactionId();
        this.tenantId = transaction.getTenantId();
        this.journalEntryId = transaction.getJournalEntryId();
        this.journalEntryNumber = transaction.getJournalEntryNumber();
        this.accountId = transaction.getAccountId();
        this.accountNumber = transaction.getAccountNumber();
        this.accountName = transaction.getAccountName();
        this.accountType = transaction.getAccountType() != null ? transaction.getAccountType().name() : null;
        this.transactionDate = transaction.getTransactionDate();
        this.postingDate = transaction.getPostingDate();
        this.periodId = transaction.getPeriodId();
        this.fiscalYear = transaction.getFiscalYear();
        this.fiscalPeriod = transaction.getFiscalPeriod();
        this.debitAmount = transaction.getDebitAmount();
        this.creditAmount = transaction.getCreditAmount();
        this.balance = transaction.getBalance();
        this.runningBalance = transaction.getRunningBalance();
        this.currency = transaction.getCurrency();
        this.exchangeRate = transaction.getExchangeRate();
        this.baseCurrency = transaction.getBaseCurrency();
        this.baseCurrencyAmount = transaction.getBaseCurrencyAmount();
        this.description = transaction.getDescription();
        this.reference = transaction.getReference();
        this.sourceDocumentType = transaction.getSourceDocumentType();
        this.sourceDocumentId = transaction.getSourceDocumentId();
        this.costCenter = transaction.getCostCenter();
        this.department = transaction.getDepartment();
        this.projectId = transaction.getProjectId();
        this.taskId = transaction.getTaskId();
        this.createdByUserId = transaction.getCreatedByUserId();
        this.postedByUserId = transaction.getPostedByUserId();
        this.isReversed = transaction.getIsReversed();
        this.reversedByTransactionId = transaction.getReversedByTransactionId();
        this.batchId = transaction.getBatchId();
        this.sequenceNumber = transaction.getSequenceNumber();
        this.reconciliationStatus = transaction.getReconciliationStatus();
        this.reconciledAt = transaction.getReconciledAt();
        this.reconciledBy = transaction.getReconciledBy();
    }

    public LedgerTransaction toDomainModel() {
        return LedgerTransaction.builder()
                .transactionId(this.transactionId)
                .tenantId(this.tenantId)
                .journalEntryId(this.journalEntryId)
                .journalEntryNumber(this.journalEntryNumber)
                .accountId(this.accountId)
                .accountNumber(this.accountNumber)
                .accountName(this.accountName)
                .accountType(this.accountType != null ? LedgerAccount.AccountType.valueOf(this.accountType) : null)
                .transactionDate(this.transactionDate)
                .postingDate(this.postingDate)
                .periodId(this.periodId)
                .fiscalYear(this.fiscalYear)
                .fiscalPeriod(this.fiscalPeriod)
                .debitAmount(this.debitAmount)
                .creditAmount(this.creditAmount)
                .balance(this.balance)
                .runningBalance(this.runningBalance)
                .currency(this.currency)
                .exchangeRate(this.exchangeRate)
                .baseCurrency(this.baseCurrency)
                .baseCurrencyAmount(this.baseCurrencyAmount)
                .description(this.description)
                .reference(this.reference)
                .sourceDocumentType(this.sourceDocumentType)
                .sourceDocumentId(this.sourceDocumentId)
                .costCenter(this.costCenter)
                .department(this.department)
                .projectId(this.projectId)
                .taskId(this.taskId)
                .createdByUserId(this.createdByUserId)
                .postedByUserId(this.postedByUserId)
                .isReversed(this.isReversed)
                .reversedByTransactionId(this.reversedByTransactionId)
                .batchId(this.batchId)
                .sequenceNumber(this.sequenceNumber)
                .reconciliationStatus(this.reconciliationStatus)
                .reconciledAt(this.reconciledAt)
                .reconciledBy(this.reconciledBy)
                .build();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getJournalEntryId() { return journalEntryId; }
    public void setJournalEntryId(String journalEntryId) { this.journalEntryId = journalEntryId; }
    public String getJournalEntryNumber() { return journalEntryNumber; }
    public void setJournalEntryNumber(String journalEntryNumber) { this.journalEntryNumber = journalEntryNumber; }
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }
    public LocalDateTime getPostingDate() { return postingDate; }
    public void setPostingDate(LocalDateTime postingDate) { this.postingDate = postingDate; }
    public String getPeriodId() { return periodId; }
    public void setPeriodId(String periodId) { this.periodId = periodId; }
    public Integer getFiscalYear() { return fiscalYear; }
    public void setFiscalYear(Integer fiscalYear) { this.fiscalYear = fiscalYear; }
    public Integer getFiscalPeriod() { return fiscalPeriod; }
    public void setFiscalPeriod(Integer fiscalPeriod) { this.fiscalPeriod = fiscalPeriod; }
    public BigDecimal getDebitAmount() { return debitAmount; }
    public void setDebitAmount(BigDecimal debitAmount) { this.debitAmount = debitAmount; }
    public BigDecimal getCreditAmount() { return creditAmount; }
    public void setCreditAmount(BigDecimal creditAmount) { this.creditAmount = creditAmount; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public BigDecimal getRunningBalance() { return runningBalance; }
    public void setRunningBalance(BigDecimal runningBalance) { this.runningBalance = runningBalance; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public BigDecimal getExchangeRate() { return exchangeRate; }
    public void setExchangeRate(BigDecimal exchangeRate) { this.exchangeRate = exchangeRate; }
    public String getBaseCurrency() { return baseCurrency; }
    public void setBaseCurrency(String baseCurrency) { this.baseCurrency = baseCurrency; }
    public BigDecimal getBaseCurrencyAmount() { return baseCurrencyAmount; }
    public void setBaseCurrencyAmount(BigDecimal baseCurrencyAmount) { this.baseCurrencyAmount = baseCurrencyAmount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public String getSourceDocumentType() { return sourceDocumentType; }
    public void setSourceDocumentType(String sourceDocumentType) { this.sourceDocumentType = sourceDocumentType; }
    public String getSourceDocumentId() { return sourceDocumentId; }
    public void setSourceDocumentId(String sourceDocumentId) { this.sourceDocumentId = sourceDocumentId; }
    public String getCostCenter() { return costCenter; }
    public void setCostCenter(String costCenter) { this.costCenter = costCenter; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }
    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }
    public String getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(String createdByUserId) { this.createdByUserId = createdByUserId; }
    public String getPostedByUserId() { return postedByUserId; }
    public void setPostedByUserId(String postedByUserId) { this.postedByUserId = postedByUserId; }
    public Boolean getIsReversed() { return isReversed; }
    public void setIsReversed(Boolean isReversed) { this.isReversed = isReversed; }
    public String getReversedByTransactionId() { return reversedByTransactionId; }
    public void setReversedByTransactionId(String reversedByTransactionId) { this.reversedByTransactionId = reversedByTransactionId; }
    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }
    public Integer getSequenceNumber() { return sequenceNumber; }
    public void setSequenceNumber(Integer sequenceNumber) { this.sequenceNumber = sequenceNumber; }
    public String getReconciliationStatus() { return reconciliationStatus; }
    public void setReconciliationStatus(String reconciliationStatus) { this.reconciliationStatus = reconciliationStatus; }
    public LocalDateTime getReconciledAt() { return reconciledAt; }
    public void setReconciledAt(LocalDateTime reconciledAt) { this.reconciledAt = reconciledAt; }
    public String getReconciledBy() { return reconciledBy; }
    public void setReconciledBy(String reconciledBy) { this.reconciledBy = reconciledBy; }
}
