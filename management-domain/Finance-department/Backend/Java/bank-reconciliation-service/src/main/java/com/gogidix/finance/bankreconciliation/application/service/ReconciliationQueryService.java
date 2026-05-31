package com.gogidix.finance.bankreconciliation.application.service;

import com.gogidix.finance.bankreconciliation.domain.model.BankTransaction;
import com.gogidix.finance.bankreconciliation.domain.model.Reconciliation;
import com.gogidix.finance.bankreconciliation.domain.model.ReconciliationLine;
import com.gogidix.finance.bankreconciliation.domain.repository.*;
import com.gogidix.finance.bankreconciliation.shared.exception.NotFoundException;
import com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Reconciliation Query Service
 * Handles all read operations for reconciliations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReconciliationQueryService {

    private final ReconciliationRepository reconciliationRepository;
    private final ReconciliationLineRepository reconciliationLineRepository;
    private final BankTransactionRepository bankTransactionRepository;

    public Reconciliation getById(String reconciliationId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching reconciliation: {} for tenant: {}", reconciliationId, tenantId);

        return reconciliationRepository.findByReconciliationIdAndTenantId(reconciliationId, tenantId)
                .orElseThrow(() -> new NotFoundException("Reconciliation", reconciliationId));
    }

    public Page<Reconciliation> getReconciliationsByAccount(String accountId,
                                                             LocalDate startDate,
                                                             LocalDate endDate,
                                                             Reconciliation.ReconciliationStatus status,
                                                             int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching reconciliations for account: {} in tenant: {}", accountId, tenantId);

        List<Reconciliation> reconciliations;

        if (startDate != null && endDate != null) {
            reconciliations = reconciliationRepository.findByTenantIdAndReconciliationDateBetween(
                    tenantId, startDate, endDate);
        } else {
            reconciliations = reconciliationRepository.findByTenantIdAndAccountId(tenantId, accountId);
        }

        // Filter by status
        if (status != null) {
            reconciliations = reconciliations.stream()
                    .filter(r -> r.getStatus() == status)
                    .collect(Collectors.toList());
        }

        // Filter by account
        reconciliations = reconciliations.stream()
                .filter(r -> r.getAccountId().equals(accountId))
                .collect(Collectors.toList());

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "reconciliationDate"));
        return new PageImpl<>(reconciliations, pageRequest, reconciliations.size());
    }

    public Page<Reconciliation> getReconciliationsByStatus(Reconciliation.ReconciliationStatus status,
                                                            int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching reconciliations by status: {} for tenant: {}", status, tenantId);

        List<Reconciliation> reconciliations = reconciliationRepository.findByTenantIdAndStatus(
                tenantId, status);

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "reconciliationDate"));
        return new PageImpl<>(reconciliations, pageRequest, reconciliations.size());
    }

    public List<Reconciliation> getPendingReconciliations() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching pending reconciliations for tenant: {}", tenantId);

        return reconciliationRepository.findPendingByTenantId(tenantId);
    }

    public List<Reconciliation> getInProgressReconciliations() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching in-progress reconciliations for tenant: {}", tenantId);

        return reconciliationRepository.findInProgressByTenantId(tenantId);
    }

    public List<Reconciliation> getAwaitingApprovalReconciliations() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching reconciliations awaiting approval for tenant: {}", tenantId);

        return reconciliationRepository.findAwaitingApprovalByTenantId(tenantId);
    }

    public List<Reconciliation> getCompletedReconciliations() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching completed reconciliations for tenant: {}", tenantId);

        return reconciliationRepository.findCompletedByTenantId(tenantId);
    }

    public List<ReconciliationLine> getReconciliationLines(String reconciliationId,
                                                            ReconciliationLine.MatchStatus matchStatus,
                                                            Boolean requiresManualReview,
                                                            int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching reconciliation lines for: {} for tenant: {}", reconciliationId, tenantId);

        List<ReconciliationLine> lines = reconciliationLineRepository
                .findByReconciliationIdAndTenantId(reconciliationId, tenantId);

        if (matchStatus != null) {
            lines = lines.stream()
                    .filter(l -> l.getMatchStatus() == matchStatus)
                    .collect(Collectors.toList());
        }

        if (requiresManualReview != null) {
            lines = lines.stream()
                    .filter(l -> requiresManualReview.equals(l.getRequiresManualReview()))
                    .collect(Collectors.toList());
        }

        return lines;
    }

    public List<BankTransaction> getUnreconciledTransactions(String statementId, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching unreconciled transactions for statement: {} for tenant: {}",
                statementId, tenantId);

        return bankTransactionRepository.findByTenantIdAndStatementId(tenantId, statementId)
                .stream()
                .filter(tx -> !Boolean.TRUE.equals(tx.getIsReconciled()))
                .collect(Collectors.toList());
    }

    public List<ReconciliationLine> getDiscrepancies(String reconciliationId,
                                                       String accountId,
                                                       LocalDate startDate,
                                                       LocalDate endDate,
                                                       ReconciliationLine.DiscrepancyCategory discrepancyCategory) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching discrepancies for tenant: {}", tenantId);

        List<ReconciliationLine> discrepancies;

        if (reconciliationId != null) {
            discrepancies = reconciliationLineRepository.findDiscrepanciesByReconciliationId(reconciliationId);
        } else if (accountId != null) {
            discrepancies = reconciliationLineRepository.findByTenantIdAndAccountId(tenantId, accountId)
                    .stream()
                    .filter(ReconciliationLine::hasDiscrepancy)
                    .collect(Collectors.toList());

            if (startDate != null && endDate != null) {
                discrepancies = reconciliationLineRepository.findByBankTransactionDateBetween(
                        tenantId, accountId, startDate, endDate)
                        .stream()
                        .filter(ReconciliationLine::hasDiscrepancy)
                        .collect(Collectors.toList());
            }
        } else {
            discrepancies = reconciliationLineRepository.findByTenantId(tenantId)
                    .stream()
                    .filter(ReconciliationLine::hasDiscrepancy)
                    .collect(Collectors.toList());
        }

        if (discrepancyCategory != null) {
            discrepancies = discrepancies.stream()
                    .filter(l -> discrepancyCategory.equals(l.getDiscrepancyCategory()))
                    .collect(Collectors.toList());
        }

        return discrepancies;
    }

    public Reconciliation getLatestByAccount(String accountId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching latest reconciliation for account: {} in tenant: {}", accountId, tenantId);

        return reconciliationRepository.findLatestByTenantIdAndAccountId(tenantId, accountId)
                .orElseThrow(() -> new NotFoundException("No reconciliation found for account: " + accountId));
    }

    public List<BankTransaction> searchTransactions(String accountId,
                                                    LocalDate startDate,
                                                    LocalDate endDate,
                                                    String searchTerm,
                                                    BigDecimal minAmount,
                                                    BigDecimal maxAmount,
                                                    BankTransaction.TransactionType transactionType,
                                                    Boolean isReconciled,
                                                    int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Searching transactions for account: {} in tenant: {}", accountId, tenantId);

        List<BankTransaction> transactions;

        if (startDate != null && endDate != null) {
            transactions = bankTransactionRepository.findByTenantIdAndAccountIdAndTransactionDateBetween(
                    tenantId, accountId, startDate, endDate);
        } else {
            transactions = bankTransactionRepository.findByTenantIdAndAccountId(tenantId, accountId);
        }

        // Apply filters
        if (isReconciled != null) {
            transactions = transactions.stream()
                    .filter(tx -> isReconciled.equals(tx.getIsReconciled()))
                    .collect(Collectors.toList());
        }

        if (transactionType != null) {
            transactions = transactions.stream()
                    .filter(tx -> transactionType.equals(tx.getTransactionType()))
                    .collect(Collectors.toList());
        }

        if (minAmount != null || maxAmount != null) {
            final BigDecimal min = minAmount != null ? minAmount : BigDecimal.ZERO;
            final BigDecimal max = maxAmount != null ? maxAmount : new BigDecimal("999999999.99");
            transactions = transactions.stream()
                    .filter(tx -> {
                        BigDecimal amount = tx.getAmount();
                        return amount != null && amount.compareTo(min) >= 0 && amount.compareTo(max) <= 0;
                    })
                    .collect(Collectors.toList());
        }

        if (searchTerm != null && !searchTerm.isBlank()) {
            final String term = searchTerm.toLowerCase();
            transactions = transactions.stream()
                    .filter(tx -> {
                        String desc = tx.getDescription();
                        String ref = tx.getReference();
                        return (desc != null && desc.toLowerCase().contains(term)) ||
                               (ref != null && ref.toLowerCase().contains(term));
                    })
                    .collect(Collectors.toList());
        }

        return transactions;
    }

    public ReconciliationStatistics getStatistics(String accountId, LocalDate startDate, LocalDate endDate) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching reconciliation statistics for tenant: {}", tenantId);

        List<Reconciliation> reconciliations;

        if (accountId != null) {
            reconciliations = reconciliationRepository.findByTenantIdAndAccountId(tenantId, accountId);
        } else if (startDate != null && endDate != null) {
            reconciliations = reconciliationRepository.findByTenantIdAndReconciliationDateBetween(
                    tenantId, startDate, endDate);
        } else {
            reconciliations = reconciliationRepository.findByTenantId(tenantId);
        }

        long totalReconciliations = reconciliations.size();
        long completedReconciliations = reconciliations.stream()
                .filter(r -> r.getStatus() == Reconciliation.ReconciliationStatus.COMPLETED ||
                           r.getStatus() == Reconciliation.ReconciliationStatus.APPROVED)
                .count();

        long balancedReconciliations = reconciliations.stream()
                .filter(r -> Boolean.TRUE.equals(r.getIsBalanced()))
                .count();

        long totalLines = reconciliations.stream()
                .mapToLong(r -> r.getLineCount() != null ? r.getLineCount() : 0)
                .sum();

        long totalMatched = reconciliations.stream()
                .mapToLong(r -> r.getMatchedCount() != null ? r.getMatchedCount() : 0)
                .sum();

        long totalDiscrepancies = reconciliations.stream()
                .mapToLong(r -> r.getDiscrepancyCount() != null ? r.getDiscrepancyCount() : 0)
                .sum();

        BigDecimal totalDifference = reconciliations.stream()
                .map(Reconciliation::getDifference)
                .filter(d -> d != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ReconciliationStatistics(
                totalReconciliations,
                completedReconciliations,
                balancedReconciliations,
                totalLines,
                totalMatched,
                totalDiscrepancies,
                totalDifference
        );
    }

    public record ReconciliationStatistics(
            long totalReconciliations,
            long completedReconciliations,
            long balancedReconciliations,
            long totalLines,
            long totalMatched,
            long totalDiscrepancies,
            BigDecimal totalDifference
    ) {}
}
