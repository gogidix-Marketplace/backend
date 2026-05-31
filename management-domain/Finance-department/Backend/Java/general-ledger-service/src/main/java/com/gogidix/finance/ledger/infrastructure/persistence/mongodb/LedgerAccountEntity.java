package com.gogidix.finance.ledger.infrastructure.persistence.mongodb;

import com.gogidix.finance.ledger.domain.model.LedgerAccount;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "ledger_accounts")
public class LedgerAccountEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field("account_id")
    private String accountId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("account_number")
    private String accountNumber;

    @Field("account_name")
    private String accountName;

    @Field("account_type")
    private String accountType;

    @Field("account_sub_type")
    private String accountSubType;

    @Field("parent_account_id")
    private String parentAccountId;

    @Field("account_level")
    private Integer accountLevel;

    @Indexed
    @Field("status")
    private String status;

    @Field("currency")
    private String currency;

    @Field("current_balance")
    private BigDecimal currentBalance;

    @Field("debit_balance")
    private BigDecimal debitBalance;

    @Field("credit_balance")
    private BigDecimal creditBalance;

    @Field("opening_balance")
    private BigDecimal openingBalance;

    @Field("opening_balance_date")
    private LocalDate openingBalanceDate;

    @Field("description")
    private String description;

    @Field("cost_center")
    private String costCenter;

    @Field("department")
    private String department;

    @Field("location")
    private String location;

    @Field("is_cash_account")
    private Boolean isCashAccount;

    @Field("is_reconcilable")
    private Boolean isReconcilable;

    @Field("is_tax_account")
    private Boolean isTaxAccount;

    @Field("tax_code")
    private String taxCode;

    @Field("allows_manual_entry")
    private Boolean allowsManualEntry;

    @Field("normal_balance_side")
    private Integer normalBalanceSide;

    @Field("created_by_user_id")
    private String createdByUserId;

    @Field("last_reconciled_at")
    private LocalDateTime lastReconciledAt;

    @Field("last_reconciled_by")
    private String lastReconciledBy;

    @Field("tags")
    private List<AccountTagEmbed> tags;

    @Field("credit_limit")
    private BigDecimal creditLimit;

    @Field("notes")
    private String notes;

    @Field("archived_at")
    private LocalDateTime archivedAt;

    @Field("archived_by")
    private String archivedBy;

    @Field("archived_reason")
    private String archivedReason;

    @Field("balances")
    private List<AccountBalanceEmbed> balances;

    public LedgerAccountEntity() {
    }

    public LedgerAccountEntity(LedgerAccount account) {
        this.accountId = account.getAccountId();
        this.tenantId = account.getTenantId();
        this.accountNumber = account.getAccountNumber();
        this.accountName = account.getAccountName();
        this.accountType = account.getAccountType() != null ? account.getAccountType().name() : null;
        this.accountSubType = account.getAccountSubType() != null ? account.getAccountSubType().name() : null;
        this.parentAccountId = account.getParentAccountId();
        this.accountLevel = account.getAccountLevel();
        this.status = account.getStatus() != null ? account.getStatus().name() : null;
        this.currency = account.getCurrency();
        this.currentBalance = account.getCurrentBalance();
        this.debitBalance = account.getDebitBalance();
        this.creditBalance = account.getCreditBalance();
        this.openingBalance = account.getOpeningBalance();
        this.openingBalanceDate = account.getOpeningBalanceDate();
        this.description = account.getDescription();
        this.costCenter = account.getCostCenter();
        this.department = account.getDepartment();
        this.location = account.getLocation();
        this.isCashAccount = account.getIsCashAccount();
        this.isReconcilable = account.getIsReconcilable();
        this.isTaxAccount = account.getIsTaxAccount();
        this.taxCode = account.getTaxCode();
        this.allowsManualEntry = account.getAllowsManualEntry();
        this.normalBalanceSide = account.getNormalBalanceSide();
        this.createdByUserId = account.getCreatedByUserId();
        this.lastReconciledAt = account.getLastReconciledAt();
        this.lastReconciledBy = account.getLastReconciledBy();
        this.tags = account.getTags() != null ? account.getTags().stream().map(AccountTagEmbed::new).toList() : new ArrayList<>();
        this.creditLimit = account.getCreditLimit();
        this.notes = account.getNotes();
        this.archivedAt = account.getArchivedAt();
        this.archivedBy = account.getArchivedBy();
        this.archivedReason = account.getArchivedReason();
        this.balances = account.getBalances() != null ? account.getBalances().stream().map(AccountBalanceEmbed::new).toList() : new ArrayList<>();
    }

    public LedgerAccount toDomainModel() {
        return LedgerAccount.builder()
                .accountId(this.accountId)
                .tenantId(this.tenantId)
                .accountNumber(this.accountNumber)
                .accountName(this.accountName)
                .accountType(this.accountType != null ? LedgerAccount.AccountType.valueOf(this.accountType) : null)
                .accountSubType(this.accountSubType != null ? LedgerAccount.AccountSubType.valueOf(this.accountSubType) : null)
                .parentAccountId(this.parentAccountId)
                .accountLevel(this.accountLevel)
                .status(this.status != null ? LedgerAccount.AccountStatus.valueOf(this.status) : null)
                .currency(this.currency)
                .currentBalance(this.currentBalance)
                .debitBalance(this.debitBalance)
                .creditBalance(this.creditBalance)
                .openingBalance(this.openingBalance)
                .openingBalanceDate(this.openingBalanceDate)
                .description(this.description)
                .costCenter(this.costCenter)
                .department(this.department)
                .location(this.location)
                .isCashAccount(this.isCashAccount)
                .isReconcilable(this.isReconcilable)
                .isTaxAccount(this.isTaxAccount)
                .taxCode(this.taxCode)
                .allowsManualEntry(this.allowsManualEntry)
                .normalBalanceSide(this.normalBalanceSide)
                .createdByUserId(this.createdByUserId)
                .lastReconciledAt(this.lastReconciledAt)
                .lastReconciledBy(this.lastReconciledBy)
                .tags(this.tags != null ? this.tags.stream().map(AccountTagEmbed::toDomainModel).toList() : new ArrayList<>())
                .creditLimit(this.creditLimit)
                .notes(this.notes)
                .archivedAt(this.archivedAt)
                .archivedBy(this.archivedBy)
                .archivedReason(this.archivedReason)
                .balances(this.balances != null ? this.balances.stream().map(AccountBalanceEmbed::toDomainModel).toList() : new ArrayList<>())
                .build();
    }

    public static class AccountTagEmbed {
        private String key;
        private String value;

        public AccountTagEmbed() {
        }

        public AccountTagEmbed(LedgerAccount.AccountTag tag) {
            this.key = tag.getKey();
            this.value = tag.getValue();
        }

        public LedgerAccount.AccountTag toDomainModel() {
            return LedgerAccount.AccountTag.builder()
                    .key(this.key)
                    .value(this.value)
                    .build();
        }

        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
    }

    public static class AccountBalanceEmbed {
        private String currency;
        private BigDecimal balance;
        private LocalDateTime asOfDate;

        public AccountBalanceEmbed() {
        }

        public AccountBalanceEmbed(LedgerAccount.AccountBalance balance) {
            this.currency = balance.getCurrency();
            this.balance = balance.getBalance();
            this.asOfDate = balance.getAsOfDate();
        }

        public LedgerAccount.AccountBalance toDomainModel() {
            return LedgerAccount.AccountBalance.builder()
                    .currency(this.currency)
                    .balance(this.balance)
                    .asOfDate(this.asOfDate)
                    .build();
        }

        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        public BigDecimal getBalance() { return balance; }
        public void setBalance(BigDecimal balance) { this.balance = balance; }
        public LocalDateTime getAsOfDate() { return asOfDate; }
        public void setAsOfDate(LocalDateTime asOfDate) { this.asOfDate = asOfDate; }
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public String getAccountSubType() { return accountSubType; }
    public void setAccountSubType(String accountSubType) { this.accountSubType = accountSubType; }
    public String getParentAccountId() { return parentAccountId; }
    public void setParentAccountId(String parentAccountId) { this.parentAccountId = parentAccountId; }
    public Integer getAccountLevel() { return accountLevel; }
    public void setAccountLevel(Integer accountLevel) { this.accountLevel = accountLevel; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public BigDecimal getCurrentBalance() { return currentBalance; }
    public void setCurrentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; }
    public BigDecimal getDebitBalance() { return debitBalance; }
    public void setDebitBalance(BigDecimal debitBalance) { this.debitBalance = debitBalance; }
    public BigDecimal getCreditBalance() { return creditBalance; }
    public void setCreditBalance(BigDecimal creditBalance) { this.creditBalance = creditBalance; }
    public BigDecimal getOpeningBalance() { return openingBalance; }
    public void setOpeningBalance(BigDecimal openingBalance) { this.openingBalance = openingBalance; }
    public LocalDate getOpeningBalanceDate() { return openingBalanceDate; }
    public void setOpeningBalanceDate(LocalDate openingBalanceDate) { this.openingBalanceDate = openingBalanceDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCostCenter() { return costCenter; }
    public void setCostCenter(String costCenter) { this.costCenter = costCenter; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Boolean getIsCashAccount() { return isCashAccount; }
    public void setIsCashAccount(Boolean isCashAccount) { this.isCashAccount = isCashAccount; }
    public Boolean getIsReconcilable() { return isReconcilable; }
    public void setIsReconcilable(Boolean isReconcilable) { this.isReconcilable = isReconcilable; }
    public Boolean getIsTaxAccount() { return isTaxAccount; }
    public void setIsTaxAccount(Boolean isTaxAccount) { this.isTaxAccount = isTaxAccount; }
    public String getTaxCode() { return taxCode; }
    public void setTaxCode(String taxCode) { this.taxCode = taxCode; }
    public Boolean getAllowsManualEntry() { return allowsManualEntry; }
    public void setAllowsManualEntry(Boolean allowsManualEntry) { this.allowsManualEntry = allowsManualEntry; }
    public Integer getNormalBalanceSide() { return normalBalanceSide; }
    public void setNormalBalanceSide(Integer normalBalanceSide) { this.normalBalanceSide = normalBalanceSide; }
    public String getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(String createdByUserId) { this.createdByUserId = createdByUserId; }
    public LocalDateTime getLastReconciledAt() { return lastReconciledAt; }
    public void setLastReconciledAt(LocalDateTime lastReconciledAt) { this.lastReconciledAt = lastReconciledAt; }
    public String getLastReconciledBy() { return lastReconciledBy; }
    public void setLastReconciledBy(String lastReconciledBy) { this.lastReconciledBy = lastReconciledBy; }
    public List<AccountTagEmbed> getTags() { return tags; }
    public void setTags(List<AccountTagEmbed> tags) { this.tags = tags; }
    public BigDecimal getCreditLimit() { return creditLimit; }
    public void setCreditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getArchivedAt() { return archivedAt; }
    public void setArchivedAt(LocalDateTime archivedAt) { this.archivedAt = archivedAt; }
    public String getArchivedBy() { return archivedBy; }
    public void setArchivedBy(String archivedBy) { this.archivedBy = archivedBy; }
    public String getArchivedReason() { return archivedReason; }
    public void setArchivedReason(String archivedReason) { this.archivedReason = archivedReason; }
    public List<AccountBalanceEmbed> getBalances() { return balances; }
    public void setBalances(List<AccountBalanceEmbed> balances) { this.balances = balances; }
}
