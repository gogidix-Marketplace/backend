package com.gogidix.finance.bankreconciliation.domain.repository;

import com.gogidix.finance.bankreconciliation.domain.model.BankAccount;

import java.util.List;
import java.util.Optional;

/**
 * Bank Account Repository Interface (Port)
 * Defines the contract for bank account persistence operations
 */
public interface BankAccountRepository {

    BankAccount save(BankAccount account);

    List<BankAccount> saveAll(List<BankAccount> accounts);

    Optional<BankAccount> findById(String id);

    Optional<BankAccount> findByAccountNumberAndTenantId(String accountNumber, String tenantId);

    List<BankAccount> findByTenantId(String tenantId);

    List<BankAccount> findByTenantIdAndStatus(String tenantId, BankAccount.AccountStatus status);

    List<BankAccount> findByTenantIdAndAccountType(String tenantId, BankAccount.AccountType accountType);

    List<BankAccount> findByTenantIdAndIsPrimary(String tenantId, Boolean isPrimary);

    List<BankAccount> findByTenantIdAndCurrency(String tenantId, String currency);

    List<BankAccount> findByTenantIdAndBankName(String tenantId, String bankName);

    List<BankAccount> findByTenantIdAndStatusNot(String tenantId, BankAccount.AccountStatus status);

    boolean existsByAccountNumberAndTenantId(String accountNumber, String tenantId);

    void deleteById(String id);

    void deleteByAccountNumberAndTenantId(String accountNumber, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, BankAccount.AccountStatus status);

    Optional<BankAccount> findPrimaryByTenantId(String tenantId);

    List<BankAccount> findByTenantIdAndTagsContaining(String tenantId, String tag);

    List<BankAccount> findByTenantIdAndLastReconciledAtBefore(String tenantId, java.time.Instant date);
}
