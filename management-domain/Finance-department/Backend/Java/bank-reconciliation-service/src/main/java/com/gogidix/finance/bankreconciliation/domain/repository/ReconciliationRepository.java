package com.gogidix.finance.bankreconciliation.domain.repository;

import com.gogidix.finance.bankreconciliation.domain.model.Reconciliation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Reconciliation Repository Interface (Port)
 * Defines the contract for reconciliation persistence operations
 */
public interface ReconciliationRepository {

    Reconciliation save(Reconciliation reconciliation);

    List<Reconciliation> saveAll(List<Reconciliation> reconciliations);

    Optional<Reconciliation> findById(String id);

    Optional<Reconciliation> findByReconciliationIdAndTenantId(String reconciliationId, String tenantId);

    List<Reconciliation> findByTenantId(String tenantId);

    List<Reconciliation> findByTenantIdAndAccountId(String tenantId, String accountId);

    List<Reconciliation> findByTenantIdAndStatementId(String tenantId, String statementId);

    List<Reconciliation> findByTenantIdAndStatus(String tenantId, Reconciliation.ReconciliationStatus status);

    List<Reconciliation> findByTenantIdAndReconciliationDateBetween(
            String tenantId, LocalDate startDate, LocalDate endDate);

    List<Reconciliation> findByTenantIdAndPeriodStartBetween(
            String tenantId, LocalDate startDate, LocalDate endDate);

    List<Reconciliation> findByTenantIdAndAccountIdAndStatus(
            String tenantId, String accountId, Reconciliation.ReconciliationStatus status);

    List<Reconciliation> findPendingByTenantId(String tenantId);

    List<Reconciliation> findInProgressByTenantId(String tenantId);

    List<Reconciliation> findAwaitingApprovalByTenantId(String tenantId);

    List<Reconciliation> findCompletedByTenantId(String tenantId);

    Optional<Reconciliation> findLatestByTenantIdAndAccountId(String tenantId, String accountId);

    boolean existsByReconciliationIdAndTenantId(String reconciliationId, String tenantId);

    void deleteById(String id);

    void deleteByReconciliationIdAndTenantId(String reconciliationId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, Reconciliation.ReconciliationStatus status);

    long countByTenantIdAndAccountId(String tenantId, String accountId);

    List<Reconciliation> findByTenantIdAndReconciliationMethod(
            String tenantId, Reconciliation.ReconciliationMethod method);

    List<Reconciliation> findByTenantIdAndIsBalanced(String tenantId, Boolean isBalanced);
}
