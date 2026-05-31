package com.gogidix.finance.bankreconciliation.domain.repository;

import com.gogidix.finance.bankreconciliation.domain.model.BankStatement;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Bank Statement Repository Interface (Port)
 * Defines the contract for bank statement persistence operations
 */
public interface BankStatementRepository {

    BankStatement save(BankStatement statement);

    List<BankStatement> saveAll(List<BankStatement> statements);

    Optional<BankStatement> findById(String id);

    Optional<BankStatement> findByStatementIdAndTenantId(String statementId, String tenantId);

    List<BankStatement> findByTenantId(String tenantId);

    List<BankStatement> findByTenantIdAndAccountId(String tenantId, String accountId);

    List<BankStatement> findByTenantIdAndAccountNumber(String tenantId, String accountNumber);

    List<BankStatement> findByTenantIdAndStatementDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<BankStatement> findByTenantIdAndImportStatus(String tenantId, BankStatement.ImportStatus importStatus);

    List<BankStatement> findByTenantIdAndImportSource(String tenantId, BankStatement.ImportSource importSource);

    List<BankStatement> findByTenantIdAndStatementType(String tenantId, BankStatement.StatementType statementType);

    List<BankStatement> findByTenantIdAndReconciled(String tenantId, Boolean reconciled);

    Optional<BankStatement> findByTenantIdAndAccountIdAndStatementDate(
            String tenantId, String accountId, LocalDate statementDate);

    List<BankStatement> findUnreconciledByTenantId(String tenantId);

    List<BankStatement> findByTenantIdAndAccountIdAndReconciled(
            String tenantId, String accountId, Boolean reconciled);

    boolean existsByStatementIdAndTenantId(String statementId, String tenantId);

    void deleteById(String id);

    void deleteByStatementIdAndTenantId(String statementId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndImportStatus(String tenantId, BankStatement.ImportStatus importStatus);

    long countByTenantIdAndAccountId(String tenantId, String accountId);

    List<BankStatement> findPendingProcessingByTenantId(String tenantId);

    List<BankStatement> findByTenantIdAndBankReference(String tenantId, String bankReference);
}
