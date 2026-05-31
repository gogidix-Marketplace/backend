package com.gogidix.finance.bankreconciliation.application.service;

import com.gogidix.finance.bankreconciliation.domain.event.ReconciliationCompletedEvent;
import com.gogidix.finance.bankreconciliation.domain.event.ReconciliationDiscrepancyEvent;
import com.gogidix.finance.bankreconciliation.domain.model.BankStatement;
import com.gogidix.finance.bankreconciliation.domain.model.BankTransaction;
import com.gogidix.finance.bankreconciliation.domain.model.Reconciliation;
import com.gogidix.finance.bankreconciliation.domain.model.ReconciliationLine;
import com.gogidix.finance.bankreconciliation.domain.port.in.ReconciliationCommand;
import com.gogidix.finance.bankreconciliation.domain.port.out.EventPublisher;
import com.gogidix.finance.bankreconciliation.domain.repository.*;
import com.gogidix.finance.bankreconciliation.shared.exception.ConflictException;
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
 * Reconciliation Command Service
 * Handles all write operations for reconciliations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReconciliationCommandService {

    private final ReconciliationRepository reconciliationRepository;
    private final ReconciliationLineRepository reconciliationLineRepository;
    private final BankStatementRepository bankStatementRepository;
    private final BankAccountRepository bankAccountRepository;
    private final BankTransactionRepository bankTransactionRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public Reconciliation create(ReconciliationCommand.CreateReconciliationCommand command) {
        log.info("Creating reconciliation for account: {} for tenant: {}",
                command.getAccountId(), command.getTenantId());

        // Validate account exists
        bankAccountRepository.findById(command.getAccountId())
                .orElseThrow(() -> new NotFoundException("BankAccount", command.getAccountId()));

        // Validate statement exists
        BankStatement statement = bankStatementRepository.findByStatementIdAndTenantId(
                        command.getStatementId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("BankStatement", command.getStatementId()));

        if (!statement.isReadyForReconciliation()) {
            throw new ValidationException("Statement is not ready for reconciliation");
        }

        // Check for existing reconciliation
        List<Reconciliation> existingReconciliations = reconciliationRepository.findByTenantIdAndStatementId(
                        command.getTenantId(), command.getStatementId());
        if (!existingReconciliations.isEmpty()) {
            throw new ConflictException("Reconciliation already exists for this statement");
        }

        String reconciliationId = UUID.randomUUID().toString();

        Reconciliation reconciliation = Reconciliation.create(
                command.getTenantId(),
                command.getAccountId(),
                command.getAccountNumber(),
                command.getStatementId(),
                command.getReconciliationDate(),
                command.getPeriodStart(),
                command.getPeriodEnd(),
                command.getStartingBalance(),
                command.getEndingBalance(),
                command.getReconciliationMethod()
        );

        reconciliation.setReconciliationId(reconciliationId);

        if (command.getTolerance() != null) {
            reconciliation.setTolerance(command.getTolerance());
        }

        if (command.getNotes() != null) {
            reconciliation.setNotes(command.getNotes());
        }

        if (Boolean.TRUE.equals(command.getAutoReconcile())) {
            reconciliation.setAutoReconciled(true);
        }

        Reconciliation savedReconciliation = reconciliationRepository.save(reconciliation);

        // Link statement to reconciliation
        statement.linkToReconciliation(reconciliationId);
        bankStatementRepository.save(statement);

        log.info("Created reconciliation: {} for tenant: {}", reconciliationId, command.getTenantId());
        return savedReconciliation;
    }

    @Transactional
    public void start(ReconciliationCommand.StartReconciliationCommand command) {
        log.info("Starting reconciliation: {} for tenant: {}", command.getReconciliationId(),
                command.getTenantId());

        Reconciliation reconciliation = reconciliationRepository.findByReconciliationIdAndTenantId(
                command.getReconciliationId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Reconciliation", command.getReconciliationId()));

        reconciliation.start();
        reconciliationRepository.save(reconciliation);

        log.info("Started reconciliation: {}", command.getReconciliationId());
    }

    @Transactional
    public void complete(ReconciliationCommand.CompleteReconciliationCommand command) {
        log.info("Completing reconciliation: {} for tenant: {}", command.getReconciliationId(),
                command.getTenantId());

        Reconciliation reconciliation = reconciliationRepository.findByReconciliationIdAndTenantId(
                command.getReconciliationId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Reconciliation", command.getReconciliationId()));

        if (command.getBookBalance() != null && command.getBankBalance() != null) {
            reconciliation.updateBalances(command.getBookBalance(), command.getBankBalance());
        }

        if (command.getNotes() != null) {
            reconciliation.addNotes(command.getNotes());
        }

        reconciliation.complete(command.getReconciledBy());
        reconciliationRepository.save(reconciliation);

        // Update account
        bankAccountRepository.findById(reconciliation.getAccountId())
                .ifPresent(account -> {
                    account.markAsReconciled(reconciliation.getPeriodEnd());
                    bankAccountRepository.save(account);
                });

        // Publish event
        if (eventPublisher.isReady()) {
            ReconciliationCompletedEvent event = ReconciliationCompletedEvent.create(
                    reconciliation.getReconciliationId(),
                    reconciliation.getTenantId(),
                    reconciliation.getAccountId(),
                    reconciliation.getAccountNumber(),
                    reconciliation.getStatementId(),
                    reconciliation.getReconciliationDate(),
                    reconciliation.getPeriodStart(),
                    reconciliation.getPeriodEnd(),
                    reconciliation.getStartingBalance(),
                    reconciliation.getEndingBalance(),
                    reconciliation.getBookBalance(),
                    reconciliation.getBankBalance(),
                    reconciliation.getDifference(),
                    reconciliation.getIsBalanced(),
                    reconciliation.getLineCount(),
                    reconciliation.getMatchedCount(),
                    reconciliation.getUnmatchedCount(),
                    reconciliation.getDiscrepancyCount(),
                    reconciliation.getReconciledBy(),
                    reconciliation.getReconciliationMethod().name(),
                    reconciliation.getAutoReconciled(),
                    reconciliation.getCompletionPercentage()
            );
            eventPublisher.publish(event);
        }

        log.info("Completed reconciliation: {}", command.getReconciliationId());
    }

    @Transactional
    public void approve(ReconciliationCommand.ApproveReconciliationCommand command) {
        log.info("Approving reconciliation: {} for tenant: {}", command.getReconciliationId(),
                command.getTenantId());

        Reconciliation reconciliation = reconciliationRepository.findByReconciliationIdAndTenantId(
                command.getReconciliationId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Reconciliation", command.getReconciliationId()));

        reconciliation.approve(command.getApprovedBy());

        if (command.getNotes() != null) {
            reconciliation.addNotes(command.getNotes());
        }

        reconciliationRepository.save(reconciliation);

        log.info("Approved reconciliation: {}", command.getReconciliationId());
    }

    @Transactional
    public void cancel(ReconciliationCommand.CancelReconciliationCommand command) {
        log.info("Cancelling reconciliation: {} for tenant: {}", command.getReconciliationId(),
                command.getTenantId());

        Reconciliation reconciliation = reconciliationRepository.findByReconciliationIdAndTenantId(
                command.getReconciliationId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Reconciliation", command.getReconciliationId()));

        reconciliation.cancel();

        if (command.getReason() != null) {
            reconciliation.setErrorMessage(command.getReason());
        }

        reconciliationRepository.save(reconciliation);

        // Unlink statement
        bankStatementRepository.findByStatementIdAndTenantId(
                        reconciliation.getStatementId(), reconciliation.getTenantId())
                .ifPresent(statement -> {
                    statement.setReconciled(false);
                    statement.setReconciliationId(null);
                    bankStatementRepository.save(statement);
                });

        log.info("Cancelled reconciliation: {}", command.getReconciliationId());
    }

    @Transactional
    public void updateBalances(ReconciliationCommand.UpdateBalancesCommand command) {
        log.info("Updating balances for reconciliation: {} for tenant: {}",
                command.getReconciliationId(), command.getTenantId());

        Reconciliation reconciliation = reconciliationRepository.findByReconciliationIdAndTenantId(
                command.getReconciliationId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Reconciliation", command.getReconciliationId()));

        reconciliation.updateBalances(command.getBookBalance(), command.getBankBalance());
        reconciliationRepository.save(reconciliation);

        log.info("Updated balances for reconciliation: {}", command.getReconciliationId());
    }

    @Transactional
    public void setTolerance(ReconciliationCommand.SetToleranceCommand command) {
        log.info("Setting tolerance for reconciliation: {} for tenant: {}",
                command.getReconciliationId(), command.getTenantId());

        Reconciliation reconciliation = reconciliationRepository.findByReconciliationIdAndTenantId(
                command.getReconciliationId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Reconciliation", command.getReconciliationId()));

        reconciliation.setTolerance(command.getTolerance());
        reconciliationRepository.save(reconciliation);

        log.info("Set tolerance for reconciliation: {}", command.getReconciliationId());
    }

    @Transactional
    public void matchTransactions(ReconciliationCommand.MatchTransactionsCommand command) {
        log.info("Matching transactions for reconciliation: {} for tenant: {}",
                command.getReconciliationId(), command.getTenantId());

        Reconciliation reconciliation = reconciliationRepository.findByReconciliationIdAndTenantId(
                command.getReconciliationId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Reconciliation", command.getReconciliationId()));

        BankTransaction bankTx = bankTransactionRepository.findByTransactionIdAndTenantId(
                        command.getBankTransactionId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("BankTransaction", command.getBankTransactionId()));

        // Create reconciliation line
        int lineNumber = reconciliationLineRepository
                .findByReconciliationIdAndTenantId(command.getReconciliationId(), command.getTenantId())
                .size() + 1;

        ReconciliationLine line = ReconciliationLine.createMatched(
                reconciliation.getTenantId(),
                command.getReconciliationId(),
                reconciliation.getAccountId(),
                lineNumber,
                command.getBankTransactionId(),
                bankTx.getTransactionDate(),
                bankTx.getDescription(),
                bankTx.getAmount(),
                command.getBookTransactionId(),
                null, // Book transaction date would be fetched if we had book transactions
                null, // Book description
                bankTx.getAmount(), // Assuming book amount matches
                bankTx.getCurrency()
        );

        if (command.getMatchedBy() != null) {
            line.markAsManuallyMatched(command.getMatchedBy());
        } else {
            line.markAsAutoMatched(command.getMatchConfidence() != null ? command.getMatchConfidence() : 1.0);
        }

        reconciliationLineRepository.save(line);

        // Update transaction
        bankTx.markAsReconciled(command.getReconciliationId(), line.getLineId());
        bankTransactionRepository.save(bankTx);

        // Update reconciliation
        reconciliation.incrementMatched();
        reconciliationRepository.save(reconciliation);

        // Publish discrepancy event if needed
        if (line.hasDiscrepancy() && eventPublisher.isReady()) {
            ReconciliationDiscrepancyEvent event = ReconciliationDiscrepancyEvent.create(
                    reconciliation.getReconciliationId(),
                    reconciliation.getTenantId(),
                    reconciliation.getAccountId(),
                    reconciliation.getAccountNumber(),
                    line.getLineId(),
                    line.getBankTransactionDate(),
                    line.getBankTransactionId(),
                    line.getBankDescription(),
                    line.getBankAmount(),
                    line.getBookTransactionId(),
                    line.getBookDescription(),
                    line.getBookAmount(),
                    line.getAmountDifference(),
                    line.getDiscrepancyCategory(),
                    line.getDiscrepancyReason(),
                    line.getActionRequired(),
                    line.getRequiresManualReview(),
                    line.getCurrency()
            );
            eventPublisher.publish(event);
        }

        log.info("Matched transactions for reconciliation: {}", command.getReconciliationId());
    }

    @Transactional
    public void unmatchTransactions(ReconciliationCommand.UnmatchTransactionsCommand command) {
        log.info("Unmatching transaction line: {} for tenant: {}", command.getReconciliationLineId(),
                command.getTenantId());

        ReconciliationLine line = reconciliationLineRepository.findByLineIdAndTenantId(
                command.getReconciliationLineId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ReconciliationLine", command.getReconciliationLineId()));

        line.reject(command.getReason() != null ? command.getReason() : "Unmatched by user");
        reconciliationLineRepository.save(line);

        // Unreconcile bank transaction
        if (line.getBankTransactionId() != null) {
            bankTransactionRepository.findByTransactionIdAndTenantId(
                            line.getBankTransactionId(), command.getTenantId())
                    .ifPresent(bankTx -> {
                        bankTx.unreconcile();
                        bankTransactionRepository.save(bankTx);
                    });
        }

        log.info("Unmatched transaction line: {}", command.getReconciliationLineId());
    }

    @Transactional
    public void verifyMatch(ReconciliationCommand.VerifyMatchCommand command) {
        log.info("Verifying match: {} for tenant: {}", command.getReconciliationLineId(),
                command.getTenantId());

        ReconciliationLine line = reconciliationLineRepository.findByLineIdAndTenantId(
                command.getReconciliationLineId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ReconciliationLine", command.getReconciliationLineId()));

        line.verify(command.getVerifiedBy());

        if (command.getNotes() != null) {
            line.addNotes(command.getNotes());
        }

        reconciliationLineRepository.save(line);

        log.info("Verified match: {}", command.getReconciliationLineId());
    }

    @Transactional
    public void markDiscrepancy(ReconciliationCommand.MarkDiscrepancyCommand command) {
        log.info("Marking discrepancy: {} for tenant: {}", command.getReconciliationLineId(),
                command.getTenantId());

        ReconciliationLine line = reconciliationLineRepository.findByLineIdAndTenantId(
                command.getReconciliationLineId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ReconciliationLine", command.getReconciliationLineId()));

        line.markAsDiscrepancy(command.getDiscrepancyCategory(), command.getReason());

        if (command.getActionRequired() != null) {
            line.setActionRequired(command.getActionRequired(),
                    "Action required: " + command.getActionRequired().name());
        }

        reconciliationLineRepository.save(line);

        // Update reconciliation
        reconciliationRepository.findByReconciliationIdAndTenantId(
                        line.getReconciliationId(), command.getTenantId())
                .ifPresent(reconciliation -> {
                    reconciliation.incrementDiscrepancy();
                    reconciliationRepository.save(reconciliation);

                    // Publish discrepancy event
                    if (eventPublisher.isReady()) {
                        ReconciliationDiscrepancyEvent event = ReconciliationDiscrepancyEvent.create(
                                reconciliation.getReconciliationId(),
                                reconciliation.getTenantId(),
                                reconciliation.getAccountId(),
                                reconciliation.getAccountNumber(),
                                line.getLineId(),
                                line.getBankTransactionDate(),
                                line.getBankTransactionId(),
                                line.getBankDescription(),
                                line.getBankAmount(),
                                line.getBookTransactionId(),
                                line.getBookDescription(),
                                line.getBookAmount(),
                                line.getAmountDifference(),
                                line.getDiscrepancyCategory(),
                                line.getDiscrepancyReason(),
                                line.getActionRequired(),
                                line.getRequiresManualReview(),
                                line.getCurrency()
                        );
                        eventPublisher.publish(event);
                    }
                });

        log.info("Marked discrepancy: {}", command.getReconciliationLineId());
    }

    @Transactional
    public void autoReconcile(ReconciliationCommand.AutoReconcileCommand command) {
        log.info("Auto-reconciling: {} for tenant: {}", command.getReconciliationId(),
                command.getTenantId());

        Reconciliation reconciliation = reconciliationRepository.findByReconciliationIdAndTenantId(
                command.getReconciliationId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Reconciliation", command.getReconciliationId()));

        // Get bank transactions from statement
        List<BankTransaction> bankTransactions = bankTransactionRepository
                .findByTenantIdAndStatementId(command.getTenantId(), reconciliation.getStatementId());

        BigDecimal tolerance = command.getTolerance() != null
                ? command.getTolerance()
                : reconciliation.getTolerance();

        boolean requireExactMatch = command.getRequireExactAmountMatch() != null
                ? command.getRequireExactAmountMatch()
                : true;

        List<ReconciliationLine> createdLines = new ArrayList<>();
        int lineNumber = 1;

        // For each bank transaction, try to match (simplified logic)
        for (BankTransaction bankTx : bankTransactions) {
            if (Boolean.TRUE.equals(bankTx.getIsReconciled())) {
                continue; // Skip already reconciled
            }

            // Create bank-only line for now (in real scenario, would match against book transactions)
            ReconciliationLine line = ReconciliationLine.createBankOnly(
                    reconciliation.getTenantId(),
                    reconciliation.getReconciliationId(),
                    reconciliation.getAccountId(),
                    lineNumber++,
                    bankTx.getTransactionId(),
                    bankTx.getTransactionDate(),
                    bankTx.getDescription(),
                    bankTx.getAmount(),
                    bankTx.getCurrency()
            );

            line = reconciliationLineRepository.save(line);
            createdLines.add(line);

            // Mark transaction as reconciled
            bankTx.markAsReconciled(reconciliation.getReconciliationId(), line.getLineId());
            bankTransactionRepository.save(bankTx);
        }

        // Update reconciliation statistics
        int matchedCount = (int) createdLines.stream()
                .filter(ReconciliationLine::isMatched)
                .count();

        int unmatchedCount = createdLines.size() - matchedCount;
        int discrepancyCount = (int) createdLines.stream()
                .filter(ReconciliationLine::hasDiscrepancy)
                .count();

        reconciliation.updateStatistics(createdLines.size(), matchedCount, unmatchedCount, discrepancyCount);
        reconciliation.markAsAutoReconciled();
        reconciliationRepository.save(reconciliation);

        log.info("Auto-reconciled: {} with {} lines", command.getReconciliationId(), createdLines.size());
    }

    @Transactional
    public void delete(ReconciliationCommand.DeleteReconciliationCommand command) {
        log.info("Deleting reconciliation: {} for tenant: {}", command.getReconciliationId(),
                command.getTenantId());

        Reconciliation reconciliation = reconciliationRepository.findByReconciliationIdAndTenantId(
                command.getReconciliationId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Reconciliation", command.getReconciliationId()));

        if (reconciliation.getStatus() == Reconciliation.ReconciliationStatus.APPROVED) {
            throw new ValidationException("Cannot delete approved reconciliations");
        }

        // Delete reconciliation lines
        reconciliationLineRepository.deleteByReconciliationId(command.getReconciliationId());

        // Delete reconciliation
        reconciliationRepository.deleteByReconciliationIdAndTenantId(
                command.getReconciliationId(), command.getTenantId());

        log.info("Deleted reconciliation: {}", command.getReconciliationId());
    }

    @Transactional
    public void addNotes(ReconciliationCommand.AddNotesCommand command) {
        log.info("Adding notes to reconciliation: {} for tenant: {}", command.getReconciliationId(),
                command.getTenantId());

        Reconciliation reconciliation = reconciliationRepository.findByReconciliationIdAndTenantId(
                command.getReconciliationId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Reconciliation", command.getReconciliationId()));

        reconciliation.addNotes(command.getNotes());
        reconciliationRepository.save(reconciliation);

        log.info("Added notes to reconciliation: {}", command.getReconciliationId());
    }
}
