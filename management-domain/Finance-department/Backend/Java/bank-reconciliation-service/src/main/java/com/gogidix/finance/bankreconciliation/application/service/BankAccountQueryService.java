package com.gogidix.finance.bankreconciliation.application.service;

import com.gogidix.finance.bankreconciliation.domain.model.BankAccount;
import com.gogidix.finance.bankreconciliation.domain.repository.BankAccountRepository;
import com.gogidix.finance.bankreconciliation.shared.exception.NotFoundException;
import com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Bank Account Query Service
 * Handles all read operations for bank accounts
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BankAccountQueryService {

    private final BankAccountRepository bankAccountRepository;

    public BankAccount getById(String accountId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching bank account: {} for tenant: {}", accountId, tenantId);

        return bankAccountRepository.findById(accountId)
                .filter(account -> account.getTenantId().equals(tenantId))
                .orElseThrow(() -> new NotFoundException("BankAccount", accountId));
    }

    public BankAccount getByAccountNumber(String accountNumber) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching bank account by number: {} for tenant: {}", accountNumber, tenantId);

        return bankAccountRepository.findByAccountNumberAndTenantId(accountNumber, tenantId)
                .orElseThrow(() -> new NotFoundException("BankAccount", accountNumber));
    }

    public Page<BankAccount> getAccountsByTenant(BankAccount.AccountStatus status,
                                                  BankAccount.AccountType accountType,
                                                  Boolean isPrimary,
                                                  int page, int size,
                                                  String sortBy, String sortDirection) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching bank accounts for tenant: {}", tenantId);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        List<BankAccount> accounts = bankAccountRepository.findByTenantId(tenantId);

        // Apply filters
        if (status != null) {
            accounts = accounts.stream()
                    .filter(a -> a.getStatus() == status)
                    .toList();
        }
        if (accountType != null) {
            accounts = accounts.stream()
                    .filter(a -> a.getAccountType() == accountType)
                    .toList();
        }
        if (isPrimary != null) {
            accounts = accounts.stream()
                    .filter(a -> isPrimary.equals(a.getIsPrimary()))
                    .toList();
        }

        return new PageImpl<>(accounts, pageRequest, accounts.size());
    }

    public List<BankAccount> getAllActiveAccounts() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching all active bank accounts for tenant: {}", tenantId);

        return bankAccountRepository.findByTenantIdAndStatus(
                tenantId, BankAccount.AccountStatus.ACTIVE);
    }

    public List<BankAccount> getAccountsByType(BankAccount.AccountType accountType) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching bank accounts by type: {} for tenant: {}", accountType, tenantId);

        return bankAccountRepository.findByTenantIdAndAccountType(tenantId, accountType);
    }

    public BankAccount getPrimaryAccount() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching primary bank account for tenant: {}", tenantId);

        return bankAccountRepository.findPrimaryByTenantId(tenantId)
                .orElseThrow(() -> new NotFoundException("Primary BankAccount not found"));
    }

    public List<BankAccount> getAccountsByCurrency(String currency) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching bank accounts by currency: {} for tenant: {}", currency, tenantId);

        return bankAccountRepository.findByTenantIdAndCurrency(tenantId, currency);
    }

    public List<BankAccount> getAccountsReadyForReconciliation() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching accounts ready for reconciliation for tenant: {}", tenantId);

        return bankAccountRepository.findByTenantIdAndStatus(tenantId, BankAccount.AccountStatus.ACTIVE)
                .stream()
                .filter(BankAccount::isReadyForReconciliation)
                .toList();
    }

    public List<BankAccount> getAccountsByTag(String tag) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching bank accounts by tag: {} for tenant: {}", tag, tenantId);

        return bankAccountRepository.findByTenantIdAndTagsContaining(tenantId, tag);
    }

    public long countByTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return bankAccountRepository.countByTenantId(tenantId);
    }

    public long countByStatus(BankAccount.AccountStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        return bankAccountRepository.countByTenantIdAndStatus(tenantId, status);
    }

    public AccountSummary getSummary() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching bank account summary for tenant: {}", tenantId);

        List<BankAccount> allAccounts = bankAccountRepository.findByTenantId(tenantId);

        long activeCount = allAccounts.stream()
                .filter(a -> a.getStatus() == BankAccount.AccountStatus.ACTIVE)
                .count();

        long inactiveCount = allAccounts.stream()
                .filter(a -> a.getStatus() == BankAccount.AccountStatus.INACTIVE)
                .count();

        long closedCount = allAccounts.stream()
                .filter(a -> a.getStatus() == BankAccount.AccountStatus.CLOSED)
                .count();

        java.math.BigDecimal totalBalance = allAccounts.stream()
                .filter(a -> a.getStatus() == BankAccount.AccountStatus.ACTIVE)
                .map(BankAccount::getBalance)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        return new AccountSummary(
                allAccounts.size(),
                activeCount,
                inactiveCount,
                closedCount,
                totalBalance
        );
    }

    public record AccountSummary(
            long totalCount,
            long activeCount,
            long inactiveCount,
            long closedCount,
            java.math.BigDecimal totalBalance
    ) {}
}
