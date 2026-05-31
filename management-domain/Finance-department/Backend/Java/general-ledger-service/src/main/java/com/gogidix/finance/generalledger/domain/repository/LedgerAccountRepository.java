package com.gogidix.finance.generalledger.domain.repository;

import com.gogidix.finance.ledger.domain.model.LedgerAccount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Ledger Account Repository Interface
 * Defines the contract for ledger account persistence operations
 */
public interface LedgerAccountRepository {

    LedgerAccount save(LedgerAccount ledgerAccount);

    List<LedgerAccount> saveAll(List<LedgerAccount> ledgerAccounts);

    Optional<LedgerAccount> findById(String id);

    Optional<LedgerAccount> findByAccountIdAndTenantId(String accountId, String tenantId);

    Optional<LedgerAccount> findByAccountNumberAndTenantId(String accountNumber, String tenantId);

    List<LedgerAccount> findByTenantId(String tenantId);

    List<LedgerAccount> findByTenantIdAndAccountType(String tenantId, LedgerAccount.AccountType accountType);

    List<LedgerAccount> findByTenantIdAndAccountSubType(String tenantId, LedgerAccount.AccountSubType accountSubType);

    List<LedgerAccount> findByTenantIdAndStatus(String tenantId, LedgerAccount.AccountStatus status);

    List<LedgerAccount> findByTenantIdAndParentAccountId(String tenantId, String parentAccountId);

    List<LedgerAccount> findByTenantIdAndStatusIs(String tenantId, LedgerAccount.AccountStatus status);

    List<LedgerAccount> findBalanceSheetAccountsByTenantId(String tenantId);

    List<LedgerAccount> findIncomeStatementAccountsByTenantId(String tenantId);

    List<LedgerAccount> findCashAccountsByTenantId(String tenantId);

    List<LedgerAccount> findReconcilableAccountsByTenantId(String tenantId);

    List<LedgerAccount> searchByTenantIdAndAccountNameContaining(String tenantId, String searchTerm);

    List<LedgerAccount> findByTenantIdAndCostCenter(String tenantId, String costCenter);

    List<LedgerAccount> findByTenantIdAndDepartment(String tenantId, String department);

    boolean existsByAccountIdAndTenantId(String accountId, String tenantId);

    boolean existsByAccountNumberAndTenantId(String accountNumber, String tenantId);

    void deleteById(String id);

    void deleteByAccountIdAndTenantId(String accountId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, LedgerAccount.AccountStatus status);

    BigDecimal sumCurrentBalanceByTenantIdAndAccountType(String tenantId, LedgerAccount.AccountType accountType);
}
