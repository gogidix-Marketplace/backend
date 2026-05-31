package com.gogidix.finance.budgettracking.domain.repository;

import com.gogidix.finance.budgettracking.domain.model.BudgetTransaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Budget Transaction Repository Interface (Port)
 * Defines the contract for budget transaction persistence operations
 */
public interface BudgetTransactionRepository {

    BudgetTransaction save(BudgetTransaction transaction);

    List<BudgetTransaction> saveAll(List<BudgetTransaction> transactions);

    Optional<BudgetTransaction> findById(String id);

    Optional<BudgetTransaction> findByTransactionIdAndTenantId(String transactionId, String tenantId);

    List<BudgetTransaction> findByTenantId(String tenantId);

    List<BudgetTransaction> findByTenantIdAndBudgetId(String tenantId, String budgetId);

    List<BudgetTransaction> findByTenantIdAndBudgetCode(String tenantId, String budgetCode);

    List<BudgetTransaction> findByTenantIdAndStatus(String tenantId, BudgetTransaction.TransactionStatus status);

    List<BudgetTransaction> findByTenantIdAndTransactionType(String tenantId, BudgetTransaction.TransactionType type);

    List<BudgetTransaction> findByTenantIdAndTransactionDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<BudgetTransaction> findByTenantIdAndDepartment(String tenantId, String department);

    List<BudgetTransaction> findByTenantIdAndCategory(String tenantId, String category);

    List<BudgetTransaction> findByTenantIdAndCostCenter(String tenantId, String costCenter);

    List<BudgetTransaction> findByTenantIdAndReferenceId(String tenantId, String referenceId);

    List<BudgetTransaction> findByTenantIdAndTagsContaining(String tenantId, String tag);

    boolean existsByTransactionIdAndTenantId(String transactionId, String tenantId);

    void deleteById(String id);

    void deleteByTransactionIdAndTenantId(String transactionId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, BudgetTransaction.TransactionStatus status);

    List<BudgetTransaction> findByTenantIdAndTransactionTypeAndStatus(
        String tenantId, BudgetTransaction.TransactionType type, BudgetTransaction.TransactionStatus status);
}
