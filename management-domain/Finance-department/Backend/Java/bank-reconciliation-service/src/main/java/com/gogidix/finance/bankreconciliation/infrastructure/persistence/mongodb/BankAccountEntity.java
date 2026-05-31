package com.gogidix.finance.bankreconciliation.infrastructure.persistence.mongodb;

import com.gogidix.finance.bankreconciliation.domain.model.BankAccount;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "bank_accounts")
public class BankAccountEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field("account_number")
    private String accountNumber;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("account_name")
    private String accountName;

    @Field("account_type")
    private String accountType;

    @Field("bank_name")
    private String bankName;

    @Field("bank_code")
    private String bankCode;

    @Field("currency")
    private String currency;

    @Field("balance")
    private BigDecimal balance;

    @Field("balance_date")
    private LocalDate balanceDate;

    @Field("status")
    private String status;

    @Field("is_primary")
    private Boolean isPrimary;

    @Field("last_reconciled_at")
    private Instant lastReconciledAt;

    @Field("last_statement_date")
    private LocalDate lastStatementDate;

    @Field("opening_balance")
    private BigDecimal openingBalance;

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
    private String statementFrequency;

    @Field("reconciliation_tolerance")
    private BigDecimal reconciliationTolerance;

    @Field("auto_reconcile")
    private Boolean autoReconcile;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    public BankAccountEntity() {
    }

    public BankAccountEntity(BankAccount account) {
        this.id = account.getId();
        this.tenantId = account.getTenantId();
        this.accountNumber = account.getAccountNumber();
        this.accountName = account.getAccountName();
        this.accountType = account.getAccountType() != null ? account.getAccountType().name() : null;
        this.bankName = account.getBankName();
        this.bankCode = account.getBankCode();
        this.currency = account.getCurrency();
        this.balance = account.getBalance();
        this.balanceDate = account.getBalanceDate();
        this.status = account.getStatus() != null ? account.getStatus().name() : null;
        this.isPrimary = account.getIsPrimary();
        this.lastReconciledAt = account.getLastReconciledAt();
        this.lastStatementDate = account.getLastStatementDate();
        this.openingBalance = account.getOpeningBalance();
        this.iban = account.getIban();
        this.swiftCode = account.getSwiftCode();
        this.routingNumber = account.getRoutingNumber();
        this.description = account.getDescription();
        this.tags = account.getTags() != null ? new ArrayList<>(account.getTags()) : new ArrayList<>();
        this.statementFrequency = account.getStatementFrequency() != null ? account.getStatementFrequency().name() : null;
        this.reconciliationTolerance = account.getReconciliationTolerance();
        this.autoReconcile = account.getAutoReconcile();
        this.createdAt = account.getCreatedAt();
        this.updatedAt = account.getUpdatedAt();
    }

    public BankAccount toDomainModel() {
        BankAccount account = new BankAccount();
        account.setId(this.id);
        account.setTenantId(this.tenantId);
        account.setAccountNumber(this.accountNumber);
        account.setAccountName(this.accountName);
        account.setAccountType(this.accountType != null ? BankAccount.AccountType.valueOf(this.accountType) : null);
        account.setBankName(this.bankName);
        account.setBankCode(this.bankCode);
        account.setCurrency(this.currency);
        account.setBalance(this.balance);
        account.setBalanceDate(this.balanceDate);
        account.setStatus(this.status != null ? BankAccount.AccountStatus.valueOf(this.status) : null);
        account.setIsPrimary(this.isPrimary);
        account.setLastReconciledAt(this.lastReconciledAt);
        account.setLastStatementDate(this.lastStatementDate);
        account.setOpeningBalance(this.openingBalance);
        account.setIban(this.iban);
        account.setSwiftCode(this.swiftCode);
        account.setRoutingNumber(this.routingNumber);
        account.setDescription(this.description);
        account.setTags(this.tags != null ? new ArrayList<>(this.tags) : new ArrayList<>());
        account.setStatementFrequency(this.statementFrequency != null ? BankAccount.StatementFrequency.valueOf(this.statementFrequency) : null);
        account.setReconciliationTolerance(this.reconciliationTolerance);
        account.setAutoReconcile(this.autoReconcile);
        account.setCreatedAt(this.createdAt);
        account.setUpdatedAt(this.updatedAt);
        return account;
    }

    public void updateFrom(BankAccount account) {
        this.accountNumber = account.getAccountNumber();
        this.accountName = account.getAccountName();
        this.accountType = account.getAccountType() != null ? account.getAccountType().name() : null;
        this.bankName = account.getBankName();
        this.bankCode = account.getBankCode();
        this.currency = account.getCurrency();
        this.balance = account.getBalance();
        this.balanceDate = account.getBalanceDate();
        this.status = account.getStatus() != null ? account.getStatus().name() : null;
        this.isPrimary = account.getIsPrimary();
        this.lastReconciledAt = account.getLastReconciledAt();
        this.lastStatementDate = account.getLastStatementDate();
        this.openingBalance = account.getOpeningBalance();
        this.iban = account.getIban();
        this.swiftCode = account.getSwiftCode();
        this.routingNumber = account.getRoutingNumber();
        this.description = account.getDescription();
        this.tags = account.getTags() != null ? new ArrayList<>(account.getTags()) : new ArrayList<>();
        this.statementFrequency = account.getStatementFrequency() != null ? account.getStatementFrequency().name() : null;
        this.reconciliationTolerance = account.getReconciliationTolerance();
        this.autoReconcile = account.getAutoReconcile();
        this.updatedAt = account.getUpdatedAt();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDate getBalanceDate() {
        return balanceDate;
    }

    public void setBalanceDate(LocalDate balanceDate) {
        this.balanceDate = balanceDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public Instant getLastReconciledAt() {
        return lastReconciledAt;
    }

    public void setLastReconciledAt(Instant lastReconciledAt) {
        this.lastReconciledAt = lastReconciledAt;
    }

    public LocalDate getLastStatementDate() {
        return lastStatementDate;
    }

    public void setLastStatementDate(LocalDate lastStatementDate) {
        this.lastStatementDate = lastStatementDate;
    }

    public BigDecimal getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getStatementFrequency() {
        return statementFrequency;
    }

    public void setStatementFrequency(String statementFrequency) {
        this.statementFrequency = statementFrequency;
    }

    public BigDecimal getReconciliationTolerance() {
        return reconciliationTolerance;
    }

    public void setReconciliationTolerance(BigDecimal reconciliationTolerance) {
        this.reconciliationTolerance = reconciliationTolerance;
    }

    public Boolean getAutoReconcile() {
        return autoReconcile;
    }

    public void setAutoReconcile(Boolean autoReconcile) {
        this.autoReconcile = autoReconcile;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
