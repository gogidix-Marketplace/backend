package com.gogidix.finance.bankreconciliation.application.service;

import com.gogidix.finance.bankreconciliation.domain.model.BankStatement;
import com.gogidix.finance.bankreconciliation.domain.repository.BankStatementRepository;
import com.gogidix.finance.bankreconciliation.shared.exception.NotFoundException;
import com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Bank Statement Query Service
 * Handles all read operations for bank statements
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BankStatementQueryService {

    private final BankStatementRepository bankStatementRepository;

    public BankStatement getById(String statementId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching bank statement: {} for tenant: {}", statementId, tenantId);

        return bankStatementRepository.findByStatementIdAndTenantId(statementId, tenantId)
                .orElseThrow(() -> new NotFoundException("BankStatement", statementId));
    }

    public Page<BankStatement> getStatementsByAccount(String accountId,
                                                       LocalDate startDate,
                                                       LocalDate endDate,
                                                       BankStatement.ImportStatus importStatus,
                                                       int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching bank statements for account: {} in tenant: {}", accountId, tenantId);

        List<BankStatement> statements;

        if (startDate != null && endDate != null) {
            statements = bankStatementRepository.findByTenantIdAndStatementDateBetween(
                    tenantId, startDate, endDate);
        } else {
            statements = bankStatementRepository.findByTenantIdAndAccountId(tenantId, accountId);
        }

        // Filter by status if provided
        if (importStatus != null) {
            statements = statements.stream()
                    .filter(s -> s.getImportStatus() == importStatus)
                    .toList();
        }

        // Filter by account
        statements = statements.stream()
                .filter(s -> s.getAccountId().equals(accountId))
                .toList();

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "statementDate"));
        return new PageImpl<>(statements, pageRequest, statements.size());
    }

    public List<BankStatement> getStatementsByStatus(BankStatement.ImportStatus importStatus) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching bank statements by status: {} for tenant: {}", importStatus, tenantId);

        return bankStatementRepository.findByTenantIdAndImportStatus(tenantId, importStatus);
    }

    public List<BankStatement> getUnreconciledStatements() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching unreconciled bank statements for tenant: {}", tenantId);

        return bankStatementRepository.findUnreconciledByTenantId(tenantId);
    }

    public List<BankStatement> getStatementsReadyForReconciliation(String accountId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching statements ready for reconciliation for account: {} in tenant: {}",
                accountId, tenantId);

        List<BankStatement> statements = accountId != null
                ? bankStatementRepository.findByTenantIdAndAccountId(tenantId, accountId)
                : bankStatementRepository.findUnreconciledByTenantId(tenantId);

        return statements.stream()
                .filter(BankStatement::isReadyForReconciliation)
                .toList();
    }

    public List<BankStatement> getStatementsBySource(BankStatement.ImportSource importSource) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching bank statements by source: {} for tenant: {}", importSource, tenantId);

        return bankStatementRepository.findByTenantIdAndImportSource(tenantId, importSource);
    }

    public BankStatement getStatementByAccountAndDate(String accountId, LocalDate statementDate) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching bank statement for account: {} on date: {} in tenant: {}",
                accountId, statementDate, tenantId);

        return bankStatementRepository.findByTenantIdAndAccountIdAndStatementDate(
                        tenantId, accountId, statementDate)
                .orElseThrow(() -> new NotFoundException("BankStatement not found for account " +
                        accountId + " on " + statementDate));
    }

    public List<BankStatement> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching all bank statements for tenant: {}", tenantId);

        return bankStatementRepository.findByTenantId(tenantId);
    }

    public long countByTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return bankStatementRepository.countByTenantId(tenantId);
    }

    public long countByStatus(BankStatement.ImportStatus importStatus) {
        String tenantId = RequestContextHolder.getTenantId();
        return bankStatementRepository.countByTenantIdAndImportStatus(tenantId, importStatus);
    }

    public long countByAccount(String accountId) {
        String tenantId = RequestContextHolder.getTenantId();
        return bankStatementRepository.countByTenantIdAndAccountId(tenantId, accountId);
    }

    public StatementSummary getSummary(String accountId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching bank statement summary for tenant: {}", tenantId);

        List<BankStatement> statements = accountId != null
                ? bankStatementRepository.findByTenantIdAndAccountId(tenantId, accountId)
                : bankStatementRepository.findByTenantId(tenantId);

        long completedCount = statements.stream()
                .filter(s -> s.getImportStatus() == BankStatement.ImportStatus.COMPLETED)
                .count();

        long failedCount = statements.stream()
                .filter(s -> s.getImportStatus() == BankStatement.ImportStatus.FAILED)
                .count();

        long pendingCount = statements.stream()
                .filter(s -> s.getImportStatus() == BankStatement.ImportStatus.PENDING ||
                           s.getImportStatus() == BankStatement.ImportStatus.PROCESSING)
                .count();

        long reconciledCount = statements.stream()
                .filter(s -> Boolean.TRUE.equals(s.getReconciled()))
                .count();

        return new StatementSummary(
                statements.size(),
                completedCount,
                failedCount,
                pendingCount,
                reconciledCount
        );
    }

    public record StatementSummary(
            long totalCount,
            long completedCount,
            long failedCount,
            long pendingCount,
            long reconciledCount
    ) {}
}
