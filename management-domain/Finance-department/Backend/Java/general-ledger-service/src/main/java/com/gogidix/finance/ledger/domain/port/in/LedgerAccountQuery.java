package com.gogidix.finance.ledger.domain.port.in;

import com.gogidix.finance.ledger.domain.model.LedgerAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Ledger Account Query (Input Port)
 * Defines query operations for ledger accounts
 */
public interface LedgerAccountQuery {

    LedgerAccount getById(String accountId);

    LedgerAccount getByAccountNumber(String tenantId, String accountNumber);

    List<LedgerAccount> getAllForTenant(String tenantId);

    List<LedgerAccount> getByType(String tenantId, LedgerAccount.AccountType accountType);

    List<LedgerAccount> getBySubType(String tenantId, LedgerAccount.AccountSubType accountSubType);

    List<LedgerAccount> getByStatus(String tenantId, LedgerAccount.AccountStatus status);

    List<LedgerAccount> getByParent(String tenantId, String parentAccountId);

    List<LedgerAccount> getActiveAccounts(String tenantId);

    List<LedgerAccount> getBalanceSheetAccounts(String tenantId);

    List<LedgerAccount> getIncomeStatementAccounts(String tenantId);

    List<LedgerAccount> getCashAccounts(String tenantId);

    List<LedgerAccount> getReconcilableAccounts(String tenantId);

    List<LedgerAccount> searchByName(String tenantId, String searchTerm);

    List<LedgerAccount> getByCostCenter(String tenantId, String costCenter);

    List<LedgerAccount> getByDepartment(String tenantId, String department);

    AccountBalanceSummary getBalanceSummary(String tenantId, String accountId, LocalDate asOfDate);

    TrialBalance getTrialBalance(String tenantId, LocalDate asOfDate);

    ChartOfAccounts getChartOfAccounts(String tenantId);

    List<LedgerAccount> getChildAccounts(String tenantId, String parentAccountId);

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class AccountBalanceSummary {
        private String accountId;
        private String accountNumber;
        private String accountName;
        private LedgerAccount.AccountType accountType;
        private BigDecimal currentBalance;
        private BigDecimal debitBalance;
        private BigDecimal creditBalance;
        private BigDecimal openingBalance;
        private String currency;
        private LocalDate asOfDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class TrialBalance {
        private LocalDate asOfDate;
        private String currency;
        private BigDecimal totalDebits;
        private BigDecimal totalCredits;
        private Boolean isBalanced;
        private BigDecimal difference;
        private List<AccountBalanceDetail> accountDetails;
        private Integer fiscalYear;
        private Integer fiscalPeriod;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class AccountBalanceDetail {
        private String accountId;
        private String accountNumber;
        private String accountName;
        private LedgerAccount.AccountType accountType;
        private LedgerAccount.AccountSubType accountSubType;
        private BigDecimal debitBalance;
        private BigDecimal creditBalance;
        private String currency;
        private LedgerAccount.AccountStatus status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class ChartOfAccounts {
        private String tenantId;
        private LocalDate generatedAt;
        private List<AccountHierarchy> accounts;
        private Integer totalAccounts;
        private Integer activeAccounts;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class AccountHierarchy {
        private String accountId;
        private String accountNumber;
        private String accountName;
        private LedgerAccount.AccountType accountType;
        private LedgerAccount.AccountSubType accountSubType;
        private LedgerAccount.AccountStatus status;
        private BigDecimal currentBalance;
        private String currency;
        private Integer level;
        private List<AccountHierarchy> children;
    }
}
