package com.gogidix.sales.crm.domain.repository;

import com.gogidix.sales.crm.domain.model.Account;

import java.util.List;
import java.util.Optional;

/**
 * Account Repository Interface (Port)
 * Defines the contract for account persistence operations
 */
public interface AccountRepository {

    Account save(Account account);

    List<Account> saveAll(List<Account> accounts);

    Optional<Account> findById(String id);

    Optional<Account> findByAccountIdAndTenantId(String accountId, String tenantId);

    Optional<Account> findByAccountNumberAndTenantId(String accountNumber, String tenantId);

    List<Account> findByTenantId(String tenantId);

    List<Account> findByTenantIdAndAccountType(String tenantId, Account.AccountType accountType);

    List<Account> findByTenantIdAndHierarchyLevel(String tenantId, Account.AccountHierarchyLevel hierarchyLevel);

    List<Account> findByTenantIdAndOwnerId(String tenantId, String ownerId);

    List<Account> findByTenantIdAndTerritory(String tenantId, String territory);

    List<Account> findByTenantIdAndParentAccountId(String tenantId, String parentAccountId);

    List<Account> findByTenantIdAndIndustry(String tenantId, String industry);

    List<Account> findByTenantIdAndIsActive(String tenantId, boolean isActive);

    List<Account> findByTenantIdAndAccountNameContainingIgnoreCase(String tenantId, String searchTerm);

    boolean existsByAccountNumberAndTenantId(String accountNumber, String tenantId);

    boolean existsByAccountIdAndTenantId(String accountId, String tenantId);

    void deleteById(String id);

    void deleteByAccountIdAndTenantId(String accountId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndAccountType(String tenantId, Account.AccountType accountType);

    long countByTenantIdAndIsActive(String tenantId, boolean isActive);

    long countByParentAccountIdAndTenantId(String parentAccountId, String tenantId);
}
