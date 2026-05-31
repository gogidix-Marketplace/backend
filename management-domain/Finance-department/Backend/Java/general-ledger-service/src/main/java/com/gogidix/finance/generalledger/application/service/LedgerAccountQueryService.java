package com.gogidix.finance.generalledger.application.service;

import com.gogidix.finance.generalledger.domain.repository.LedgerAccountRepository;
import com.gogidix.finance.generalledger.shared.exception.NotFoundException;
import com.gogidix.finance.generalledger.shared.requestcontext.RequestContextHolder;
import com.gogidix.finance.ledger.domain.model.LedgerAccount;
import com.gogidix.finance.ledger.domain.port.in.LedgerAccountQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Ledger Account Query Service
 * Handles all read operations for ledger accounts
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LedgerAccountQueryService implements LedgerAccountQuery {

    private final LedgerAccountRepository ledgerAccountRepository;

    @Cacheable(value = "ledgerAccounts", key = "#accountId")
    @Override
    public LedgerAccount getById(String accountId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching ledger account: {} for tenant: {}", accountId, tenantId);

        return ledgerAccountRepository.findByAccountIdAndTenantId(accountId, tenantId)
            .orElseThrow(() -> new NotFoundException("LedgerAccount", accountId));
    }

    @Override
    public LedgerAccount getByAccountNumber(String tenantId, String accountNumber) {
        log.debug("Fetching ledger account by number: {} for tenant: {}", accountNumber, tenantId);

        return ledgerAccountRepository.findByAccountNumberAndTenantId(accountNumber, tenantId)
            .orElseThrow(() -> new NotFoundException("LedgerAccount", accountNumber));
    }

    @Cacheable(value = "ledgerAccounts", key = "'all'")
    @Override
    public List<LedgerAccount> getAllForTenant(String tenantId) {
        log.debug("Fetching all ledger accounts for tenant: {}", tenantId);
        return ledgerAccountRepository.findByTenantId(tenantId);
    }

    @Override
    public List<LedgerAccount> getByType(String tenantId, LedgerAccount.AccountType accountType) {
        log.debug("Fetching ledger accounts by type: {} for tenant: {}", accountType, tenantId);
        return ledgerAccountRepository.findByTenantIdAndAccountType(tenantId, accountType);
    }

    @Override
    public List<LedgerAccount> getBySubType(String tenantId, LedgerAccount.AccountSubType accountSubType) {
        log.debug("Fetching ledger accounts by subtype: {} for tenant: {}", accountSubType, tenantId);
        return ledgerAccountRepository.findByTenantIdAndAccountSubType(tenantId, accountSubType);
    }

    @Override
    public List<LedgerAccount> getByStatus(String tenantId, LedgerAccount.AccountStatus status) {
        log.debug("Fetching ledger accounts by status: {} for tenant: {}", status, tenantId);
        return ledgerAccountRepository.findByTenantIdAndStatus(tenantId, status);
    }

    @Override
    public List<LedgerAccount> getByParent(String tenantId, String parentAccountId) {
        log.debug("Fetching child accounts for parent: {} in tenant: {}", parentAccountId, tenantId);
        return ledgerAccountRepository.findByTenantIdAndParentAccountId(tenantId, parentAccountId);
    }

    @Cacheable(value = "activeAccounts", key = "#tenantId")
    @Override
    public List<LedgerAccount> getActiveAccounts(String tenantId) {
        log.debug("Fetching active accounts for tenant: {}", tenantId);
        return ledgerAccountRepository.findByTenantIdAndStatusIs(tenantId, LedgerAccount.AccountStatus.ACTIVE);
    }

    @Override
    public List<LedgerAccount> getBalanceSheetAccounts(String tenantId) {
        log.debug("Fetching balance sheet accounts for tenant: {}", tenantId);
        return ledgerAccountRepository.findBalanceSheetAccountsByTenantId(tenantId);
    }

    @Override
    public List<LedgerAccount> getIncomeStatementAccounts(String tenantId) {
        log.debug("Fetching income statement accounts for tenant: {}", tenantId);
        return ledgerAccountRepository.findIncomeStatementAccountsByTenantId(tenantId);
    }

    @Override
    public List<LedgerAccount> getCashAccounts(String tenantId) {
        log.debug("Fetching cash accounts for tenant: {}", tenantId);
        return ledgerAccountRepository.findCashAccountsByTenantId(tenantId);
    }

    @Override
    public List<LedgerAccount> getReconcilableAccounts(String tenantId) {
        log.debug("Fetching reconcilable accounts for tenant: {}", tenantId);
        return ledgerAccountRepository.findReconcilableAccountsByTenantId(tenantId);
    }

    @Override
    public List<LedgerAccount> searchByName(String tenantId, String searchTerm) {
        log.debug("Searching accounts by name: {} in tenant: {}", searchTerm, tenantId);
        return ledgerAccountRepository.searchByTenantIdAndAccountNameContaining(tenantId, searchTerm);
    }

    @Override
    public List<LedgerAccount> getByCostCenter(String tenantId, String costCenter) {
        log.debug("Fetching accounts by cost center: {} in tenant: {}", costCenter, tenantId);
        return ledgerAccountRepository.findByTenantIdAndCostCenter(tenantId, costCenter);
    }

    @Override
    public List<LedgerAccount> getByDepartment(String tenantId, String department) {
        log.debug("Fetching accounts by department: {} in tenant: {}", department, tenantId);
        return ledgerAccountRepository.findByTenantIdAndDepartment(tenantId, department);
    }

    @Override
    public AccountBalanceSummary getBalanceSummary(String tenantId, String accountId, LocalDate asOfDate) {
        log.debug("Fetching balance summary for account: {} in tenant: {}", accountId, tenantId);

        LedgerAccount account = ledgerAccountRepository.findByAccountIdAndTenantId(accountId, tenantId)
            .orElseThrow(() -> new NotFoundException("LedgerAccount", accountId));

        return AccountBalanceSummary.builder()
            .accountId(account.getAccountId())
            .accountNumber(account.getAccountNumber())
            .accountName(account.getAccountName())
            .accountType(account.getAccountType())
            .currentBalance(account.getCurrentBalance())
            .debitBalance(account.getDebitBalance())
            .creditBalance(account.getCreditBalance())
            .openingBalance(account.getOpeningBalance())
            .currency(account.getCurrency())
            .asOfDate(asOfDate != null ? asOfDate : LocalDate.now())
            .build();
    }

    @Override
    public TrialBalance getTrialBalance(String tenantId, LocalDate asOfDate) {
        log.debug("Generating trial balance for tenant: {} as of: {}", tenantId, asOfDate);

        List<LedgerAccount> accounts = ledgerAccountRepository.findByTenantId(tenantId);

        BigDecimal totalDebits = BigDecimal.ZERO;
        BigDecimal totalCredits = BigDecimal.ZERO;
        List<AccountBalanceDetail> accountDetails = new ArrayList<>();

        for (LedgerAccount account : accounts) {
            if (account.getStatus() != LedgerAccount.AccountStatus.ACTIVE) {
                continue;
            }

            AccountBalanceDetail detail = AccountBalanceDetail.builder()
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .accountName(account.getAccountName())
                .accountType(account.getAccountType())
                .debitBalance(account.getDebitBalance())
                .creditBalance(account.getCreditBalance())
                .currency(account.getCurrency())
                .build();

            accountDetails.add(detail);

            totalDebits = totalDebits.add(account.getDebitBalance() != null ? account.getDebitBalance() : BigDecimal.ZERO);
            totalCredits = totalCredits.add(account.getCreditBalance() != null ? account.getCreditBalance() : BigDecimal.ZERO);
        }

        BigDecimal difference = totalDebits.subtract(totalCredits);
        boolean isBalanced = difference.abs().compareTo(new BigDecimal("0.01")) <= 0;

        return TrialBalance.builder()
            .asOfDate(asOfDate != null ? asOfDate : LocalDate.now())
            .currency(accounts.isEmpty() ? "USD" : accounts.get(0).getCurrency())
            .totalDebits(totalDebits)
            .totalCredits(totalCredits)
            .isBalanced(isBalanced)
            .difference(difference)
            .accountDetails(accountDetails)
            .build();
    }

    @Override
    public ChartOfAccounts getChartOfAccounts(String tenantId) {
        log.debug("Generating chart of accounts for tenant: {}", tenantId);

        List<LedgerAccount> allAccounts = ledgerAccountRepository.findByTenantId(tenantId);

        List<AccountHierarchy> hierarchy = buildAccountHierarchy(allAccounts);

        return ChartOfAccounts.builder()
            .tenantId(tenantId)
            .generatedAt(LocalDate.now())
            .accounts(hierarchy)
            .totalAccounts(allAccounts.size())
            .activeAccounts((int) allAccounts.stream().filter(a -> a.getStatus() == LedgerAccount.AccountStatus.ACTIVE).count())
            .build();
    }

    @Override
    public List<LedgerAccount> getChildAccounts(String tenantId, String parentAccountId) {
        log.debug("Fetching child accounts for parent: {} in tenant: {}", parentAccountId, tenantId);
        return ledgerAccountRepository.findByTenantIdAndParentAccountId(tenantId, parentAccountId);
    }

    private List<AccountHierarchy> buildAccountHierarchy(List<LedgerAccount> accounts) {
        List<LedgerAccount> rootAccounts = accounts.stream()
            .filter(a -> a.getParentAccountId() == null || a.getParentAccountId().isEmpty())
            .collect(Collectors.toList());

        List<AccountHierarchy> hierarchy = new ArrayList<>();

        for (LedgerAccount account : rootAccounts) {
            hierarchy.add(toAccountHierarchy(account, accounts));
        }

        return hierarchy;
    }

    private AccountHierarchy toAccountHierarchy(LedgerAccount account, List<LedgerAccount> allAccounts) {
        List<AccountHierarchy> children = allAccounts.stream()
            .filter(a -> account.getAccountId().equals(a.getParentAccountId()))
            .map(child -> toAccountHierarchy(child, allAccounts))
            .collect(Collectors.toList());

        return AccountHierarchy.builder()
            .accountId(account.getAccountId())
            .accountNumber(account.getAccountNumber())
            .accountName(account.getAccountName())
            .accountType(account.getAccountType())
            .accountSubType(account.getAccountSubType())
            .status(account.getStatus())
            .currentBalance(account.getCurrentBalance())
            .currency(account.getCurrency())
            .level(account.getAccountLevel())
            .children(children)
            .build();
    }

    // Type conversion methods
    private LedgerAccount.AccountType convertAccountType(AccountType type) {
        return LedgerAccount.AccountType.valueOf(type.name());
    }

    private AccountType convertToQueryAccountType(LedgerAccount.AccountType type) {
        return AccountType.valueOf(type.name());
    }

    private LedgerAccount.AccountSubType convertAccountSubType(AccountSubType subType) {
        return LedgerAccount.AccountSubType.valueOf(subType.name());
    }

    private AccountSubType convertToQueryAccountSubType(LedgerAccount.AccountSubType subType) {
        return AccountSubType.valueOf(subType.name());
    }

    private LedgerAccount.AccountStatus convertAccountStatus(AccountStatus status) {
        return LedgerAccount.AccountStatus.valueOf(status.name());
    }

    private AccountStatus convertToQueryAccountStatus(LedgerAccount.AccountStatus status) {
        return AccountStatus.valueOf(status.name());
    }

    // Enum definitions matching the query interface
    public enum AccountType {
        ASSET, LIABILITY, EQUITY, REVENUE, EXPENSE
    }

    public enum AccountSubType {
        CURRENT_ASSET, FIXED_ASSET, INTANGIBLE_ASSET, NON_CURRENT_ASSET,
        CURRENT_LIABILITY, LONG_TERM_LIABILITY, PROVISION,
        SHARE_CAPITAL, RETAINED_EARNINGS, RESERVES, OTHER_EQUITY,
        OPERATING_REVENUE, NON_OPERATING_REVENUE, OTHER_INCOME,
        COST_OF_SALES, OPERATING_EXPENSE, NON_OPERATING_EXPENSE, DEPRECIATION, AMORTIZATION
    }

    public enum AccountStatus {
        ACTIVE, INACTIVE, PENDING_APPROVAL, ARCHIVED, FROZEN
    }
}
