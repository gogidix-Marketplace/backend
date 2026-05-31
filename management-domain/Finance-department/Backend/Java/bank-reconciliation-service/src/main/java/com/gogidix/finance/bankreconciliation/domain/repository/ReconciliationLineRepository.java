package com.gogidix.finance.bankreconciliation.domain.repository;

import com.gogidix.finance.bankreconciliation.domain.model.ReconciliationLine;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Reconciliation Line Repository Interface (Port)
 * Defines the contract for reconciliation line persistence operations
 */
public interface ReconciliationLineRepository {

    ReconciliationLine save(ReconciliationLine line);

    List<ReconciliationLine> saveAll(List<ReconciliationLine> lines);

    Optional<ReconciliationLine> findById(String id);

    Optional<ReconciliationLine> findByLineIdAndTenantId(String lineId, String tenantId);

    List<ReconciliationLine> findByTenantId(String tenantId);

    List<ReconciliationLine> findByReconciliationId(String reconciliationId);

    List<ReconciliationLine> findByReconciliationIdAndTenantId(String reconciliationId, String tenantId);

    List<ReconciliationLine> findByTenantIdAndAccountId(String tenantId, String accountId);

    List<ReconciliationLine> findByReconciliationIdAndMatchStatus(
            String reconciliationId, ReconciliationLine.MatchStatus matchStatus);

    List<ReconciliationLine> findByReconciliationIdAndRequiresManualReview(
            String reconciliationId, Boolean requiresManualReview);

    List<ReconciliationLine> findByReconciliationIdAndLineType(
            String reconciliationId, ReconciliationLine.LineType lineType);

    List<ReconciliationLine> findByBankTransactionId(String bankTransactionId);

    List<ReconciliationLine> findByBookTransactionId(String bookTransactionId);

    List<ReconciliationLine> findByReconciliationIdAndBankTransactionId(
            String reconciliationId, String bankTransactionId);

    List<ReconciliationLine> findByReconciliationIdAndBookTransactionId(
            String reconciliationId, String bookTransactionId);

    List<ReconciliationLine> findDiscrepanciesByReconciliationId(String reconciliationId);

    List<ReconciliationLine> findUnmatchedByReconciliationId(String reconciliationId);

    List<ReconciliationLine> findPendingReviewByReconciliationId(String reconciliationId);

    List<ReconciliationLine> findByTenantIdAndDiscrepancyCategory(
            String tenantId, ReconciliationLine.DiscrepancyCategory discrepancyCategory);

    boolean existsByLineIdAndTenantId(String lineId, String tenantId);

    void deleteById(String id);

    void deleteByLineIdAndTenantId(String lineId, String tenantId);

    void deleteByReconciliationId(String reconciliationId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByReconciliationIdAndTenantId(String reconciliationId, String tenantId);

    long countByReconciliationIdAndMatchStatus(String reconciliationId, ReconciliationLine.MatchStatus matchStatus);

    long countDiscrepanciesByReconciliationId(String reconciliationId);

    List<ReconciliationLine> findByBankTransactionDateBetween(
            String tenantId, String accountId, LocalDate startDate, LocalDate endDate);

    void deleteByReconciliationIdAndLineType(String reconciliationId, ReconciliationLine.LineType lineType);
}
