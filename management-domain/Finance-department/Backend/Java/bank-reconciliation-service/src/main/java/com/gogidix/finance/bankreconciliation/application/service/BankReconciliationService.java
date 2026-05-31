package com.gogidix.finance.bankreconciliation.application.service;

import com.gogidix.finance.bankreconciliation.domain.model.*;
import com.gogidix.finance.bankreconciliation.domain.port.in.ReconciliationCommand;
import com.gogidix.finance.bankreconciliation.domain.repository.*;
import com.gogidix.finance.bankreconciliation.shared.exception.NotFoundException;
import com.gogidix.finance.bankreconciliation.shared.exception.ValidationException;
import com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Bank Reconciliation Service
 * Main orchestration service for bank reconciliation operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BankReconciliationService {

    private final ReconciliationRepository reconciliationRepository;
    private final ReconciliationLineRepository reconciliationLineRepository;
    private final BankStatementRepository bankStatementRepository;
    private final BankAccountRepository bankAccountRepository;
    private final BankTransactionRepository bankTransactionRepository;
    private final ReconciliationCommandService reconciliationCommandService;
    private final ReconciliationQueryService reconciliationQueryService;

    /**
     * Creates a new reconciliation session and processes the statement
     */
    @Transactional
    public Reconciliation initiateReconciliation(String accountId, String statementId,
                                                 LocalDate reconciliationDate,
                                                 Reconciliation.ReconciliationMethod method) {
        String tenantId = RequestContextHolder.getTenantId();

        log.info("Initiating reconciliation for account: {}, statement: {}", accountId, statementId);

        BankAccount account = bankAccountRepository.findById(accountId)
                .filter(acc -> acc.getTenantId().equals(tenantId))
                .orElseThrow(() -> new NotFoundException("BankAccount", accountId));

        BankStatement statement = bankStatementRepository.findByStatementIdAndTenantId(
                        statementId, tenantId)
                .orElseThrow(() -> new NotFoundException("BankStatement", statementId));

        if (!statement.isReadyForReconciliation()) {
            throw new ValidationException("Statement is not ready for reconciliation");
        }

        ReconciliationCommand.CreateReconciliationCommand command =
                new ReconciliationCommand.CreateReconciliationCommand(
                        tenantId,
                        accountId,
                        account.getAccountNumber(),
                        statementId,
                        reconciliationDate,
                        statement.getStartDate(),
                        statement.getEndDate(),
                        statement.getOpeningBalance(),
                        statement.getClosingBalance(),
                        account.getReconciliationTolerance() != null
                                ? account.getReconciliationTolerance()
                                : new BigDecimal("0.01"),
                        method,
                        Boolean.TRUE.equals(account.getAutoReconcile()),
                        "Auto-initiated reconciliation",
                        RequestContextHolder.getUserId()
                );

        Reconciliation reconciliation = reconciliationCommandService.create(command);

        // Start reconciliation
        reconciliationCommandService.start(new ReconciliationCommand.StartReconciliationCommand(
                tenantId, reconciliation.getReconciliationId(), RequestContextHolder.getUserId()
        ));

        // Get bank transactions
        List<BankTransaction> transactions = bankTransactionRepository
                .findByTenantIdAndStatementId(tenantId, statementId);

        // Create reconciliation lines
        createReconciliationLines(reconciliation, transactions, account.getCurrency());

        return reconciliation;
    }

    /**
     * Processes reconciliation matching using specified rules
     */
    @Transactional
    public MatchingResult processMatching(String reconciliationId, MatchingRules rules) {
        String tenantId = RequestContextHolder.getTenantId();

        log.info("Processing matching for reconciliation: {}", reconciliationId);

        Reconciliation reconciliation = reconciliationQueryService.getById(reconciliationId);

        List<BankTransaction> unreconciledTransactions = bankTransactionRepository
                .findByTenantIdAndStatementId(tenantId, reconciliation.getStatementId())
                .stream()
                .filter(tx -> !Boolean.TRUE.equals(tx.getIsReconciled()))
                .toList();

        List<ReconciliationLine> matchedLines = new ArrayList<>();
        List<ReconciliationLine> unmatchedLines = new ArrayList<>();
        List<ReconciliationLine> discrepancyLines = new ArrayList<>();

        // Simple matching logic (in production, would use more sophisticated algorithms)
        for (BankTransaction transaction : unreconciledTransactions) {
            // For now, create bank-only lines since we don't have book transactions
            int lineNumber = reconciliationLineRepository
                    .findByReconciliationIdAndTenantId(reconciliationId, tenantId)
                    .size() + 1;

            ReconciliationLine line = ReconciliationLine.createBankOnly(
                    tenantId,
                    reconciliationId,
                    reconciliation.getAccountId(),
                    lineNumber,
                    transaction.getTransactionId(),
                    transaction.getTransactionDate(),
                    transaction.getDescription(),
                    transaction.getAmount(),
                    transaction.getCurrency()
            );

            line = reconciliationLineRepository.save(line);

            // Mark transaction as reconciled
            transaction.markAsReconciled(reconciliationId, line.getLineId());
            bankTransactionRepository.save(transaction);

            unmatchedLines.add(line);
        }

        // Update reconciliation statistics
        int totalLines = matchedLines.size() + unmatchedLines.size() + discrepancyLines.size();
        reconciliation.updateStatistics(
                totalLines,
                matchedLines.size(),
                unmatchedLines.size(),
                discrepancyLines.size()
        );
        reconciliationRepository.save(reconciliation);

        return new MatchingResult(
                matchedLines.size(),
                unmatchedLines.size(),
                discrepancyLines.size(),
                unreconciledTransactions.size() - matchedLines.size()
        );
    }

    /**
     * Finalizes reconciliation and updates account balances
     */
    @Transactional
    public Reconciliation finalizeReconciliation(String reconciliationId,
                                                 BigDecimal bookBalance,
                                                 String notes) {
        String tenantId = RequestContextHolder.getTenantId();

        log.info("Finalizing reconciliation: {}", reconciliationId);

        Reconciliation reconciliation = reconciliationQueryService.getById(reconciliationId);

        // Get bank balance from statement
        BankStatement statement = bankStatementRepository.findByStatementIdAndTenantId(
                        reconciliation.getStatementId(), tenantId)
                .orElseThrow();

        BigDecimal bankBalance = statement.getClosingBalance();

        // Complete reconciliation
        reconciliationCommandService.complete(new ReconciliationCommand.CompleteReconciliationCommand(
                tenantId,
                reconciliationId,
                RequestContextHolder.getUserId(),
                bookBalance,
                bankBalance,
                notes
        ));

        // Update account
        bankAccountRepository.findById(reconciliation.getAccountId())
                .ifPresent(account -> {
                    account.markAsReconciled(reconciliation.getPeriodEnd());
                    bankAccountRepository.save(account);
                });

        return reconciliationQueryService.getById(reconciliationId);
    }

    /**
     * Gets reconciliation summary for dashboard
     */
    public ReconciliationSummary getReconciliationSummary(String accountId, LocalDate startDate,
                                                          LocalDate endDate) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching reconciliation summary for account: {}", accountId);

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
        long completedCount = reconciliations.stream()
                .filter(r -> r.getStatus() == Reconciliation.ReconciliationStatus.COMPLETED ||
                           r.getStatus() == Reconciliation.ReconciliationStatus.APPROVED)
                .count();

        long pendingCount = reconciliations.stream()
                .filter(r -> r.getStatus() == Reconciliation.ReconciliationStatus.PENDING ||
                           r.getStatus() == Reconciliation.ReconciliationStatus.IN_PROGRESS)
                .count();

        long balancedCount = reconciliations.stream()
                .filter(r -> Boolean.TRUE.equals(r.getIsBalanced()))
                .count();

        BigDecimal totalDiscrepancyAmount = reconciliations.stream()
                .filter(r -> !Boolean.TRUE.equals(r.getIsBalanced()))
                .map(Reconciliation::getDifference)
                .filter(d -> d != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalLines = reconciliations.stream()
                .mapToInt(r -> r.getLineCount() != null ? r.getLineCount() : 0)
                .sum();

        int totalMatched = reconciliations.stream()
                .mapToInt(r -> r.getMatchedCount() != null ? r.getMatchedCount() : 0)
                .sum();

        List<BankStatement> unreconciledStatements = bankStatementRepository
                .findUnreconciledByTenantId(tenantId)
                .stream()
                .filter(BankStatement::isReadyForReconciliation)
                .toList();

        return new ReconciliationSummary(
                totalReconciliations,
                completedCount,
                pendingCount,
                balancedCount,
                totalDiscrepancyAmount,
                totalLines,
                totalMatched,
                unreconciledStatements.size()
        );
    }

    private void createReconciliationLines(Reconciliation reconciliation,
                                          List<BankTransaction> transactions,
                                          String currency) {
        String tenantId = reconciliation.getTenantId();
        int lineNumber = 1;

        for (BankTransaction transaction : transactions) {
            ReconciliationLine line = ReconciliationLine.createBankOnly(
                    tenantId,
                    reconciliation.getReconciliationId(),
                    reconciliation.getAccountId(),
                    lineNumber++,
                    transaction.getTransactionId(),
                    transaction.getTransactionDate(),
                    transaction.getDescription(),
                    transaction.getAmount(),
                    currency
            );

            reconciliationLineRepository.save(line);
        }

        // Update reconciliation line count
        reconciliation.updateStatistics(lineNumber - 1, 0, lineNumber - 1, 0);
        reconciliationRepository.save(reconciliation);
    }

    public record MatchingRules(
            BigDecimal tolerance,
            boolean requireExactAmountMatch,
            boolean allowDateVariance,
            int dateVarianceDays,
            double minimumMatchConfidence
    ) {}

    public record MatchingRule(
            String ruleId,
            String field,
            String condition,
            Object value,
            int priority,
            boolean enabled
    ) {}

    public record MatchingResult(
            int matchedCount,
            int unmatchedCount,
            int discrepancyCount,
            int remainingTransactions
    ) {}

    public record ReconciliationSummary(
            long totalReconciliations,
            long completedCount,
            long pendingCount,
            long balancedCount,
            BigDecimal totalDiscrepancyAmount,
            int totalLines,
            int totalMatched,
            long pendingStatements
    ) {}
}
