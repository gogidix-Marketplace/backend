package com.gogidix.finance.bankreconciliation.application.service;

import com.gogidix.finance.bankreconciliation.domain.event.BankStatementImportedEvent;
import com.gogidix.finance.bankreconciliation.domain.model.BankStatement;
import com.gogidix.finance.bankreconciliation.domain.port.in.BankStatementCommand;
import com.gogidix.finance.bankreconciliation.domain.port.out.EventPublisher;
import com.gogidix.finance.bankreconciliation.domain.repository.BankAccountRepository;
import com.gogidix.finance.bankreconciliation.domain.repository.BankStatementRepository;
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
import java.util.List;
import java.util.UUID;

/**
 * Bank Statement Command Service
 * Handles all write operations for bank statements
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BankStatementCommandService {

    private final BankStatementRepository bankStatementRepository;
    private final BankAccountRepository bankAccountRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public BankStatement importStatement(BankStatementCommand.ImportBankStatementCommand command) {
        log.info("Importing bank statement for account: {} for tenant: {}",
                command.getAccountId(), command.getTenantId());

        // Validate account exists
        bankAccountRepository.findById(command.getAccountId())
                .orElseThrow(() -> new NotFoundException("BankAccount", command.getAccountId()));

        // Check for duplicate statement
        bankStatementRepository.findByTenantIdAndAccountIdAndStatementDate(
                        command.getTenantId(), command.getAccountId(), command.getStatementDate())
                .ifPresent(existing -> {
                    throw new ConflictException("BankStatement already exists for account " +
                            command.getAccountId() + " on " + command.getStatementDate());
                });

        BankStatement statement = BankStatement.create(
                command.getTenantId(),
                command.getAccountId(),
                command.getAccountNumber(),
                command.getStatementDate(),
                command.getStartDate(),
                command.getEndDate(),
                command.getOpeningBalance(),
                command.getClosingBalance(),
                command.getCurrency(),
                command.getImportSource()
        );

        statement.setStatementId(UUID.randomUUID().toString());
        statement.setBankReference(command.getBankReference());
        statement.setStatementType(command.getStatementType());
        statement.setFileReference(command.getFileReference());

        // Add transactions if provided
        if (command.getTransactions() != null && !command.getTransactions().isEmpty()) {
            command.getTransactions().forEach(statement::addTransaction);
            statement.setTransactionCount(command.getTransactions().size());
            statement.calculateTotals();
        }

        BankStatement savedStatement = bankStatementRepository.save(statement);

        // Publish event
        if (eventPublisher.isReady()) {
            BankStatementImportedEvent event = BankStatementImportedEvent.create(
                    savedStatement.getStatementId(),
                    savedStatement.getTenantId(),
                    savedStatement.getAccountId(),
                    savedStatement.getAccountNumber(),
                    savedStatement.getStatementDate(),
                    savedStatement.getStartDate(),
                    savedStatement.getEndDate(),
                    savedStatement.getOpeningBalance(),
                    savedStatement.getClosingBalance(),
                    savedStatement.getCurrency(),
                    savedStatement.getImportSource(),
                    savedStatement.getFileReference(),
                    savedStatement.getTransactionCount(),
                    savedStatement.getTotalDebits(),
                    savedStatement.getTotalCredits(),
                    statement.validateBalances(),
                    savedStatement.getImportWarnings() != null && !savedStatement.getImportWarnings().isEmpty(),
                    savedStatement.getImportWarnings() != null ? savedStatement.getImportWarnings().size() : 0,
                    command.getImportedBy(),
                    savedStatement.getStatementType(),
                    savedStatement.getBankReference()
            );
            eventPublisher.publish(event);
        }

        log.info("Imported bank statement: {} for tenant: {}", savedStatement.getStatementId(),
                command.getTenantId());
        return savedStatement;
    }

    @Transactional
    public void process(BankStatementCommand.ProcessBankStatementCommand command) {
        log.info("Processing bank statement: {} for tenant: {}", command.getStatementId(),
                command.getTenantId());

        BankStatement statement = bankStatementRepository.findByStatementIdAndTenantId(
                command.getStatementId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("BankStatement", command.getStatementId()));

        statement.markAsProcessing();
        bankStatementRepository.save(statement);

        log.info("Processing bank statement: {}", command.getStatementId());
    }

    @Transactional
    public boolean validate(BankStatementCommand.ValidateBankStatementCommand command) {
        log.info("Validating bank statement: {} for tenant: {}", command.getStatementId(),
                command.getTenantId());

        BankStatement statement = bankStatementRepository.findByStatementIdAndTenantId(
                command.getStatementId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("BankStatement", command.getStatementId()));

        boolean isValid = true;

        if (Boolean.TRUE.equals(command.getValidateBalances())) {
            BigDecimal tolerance = command.getTolerance() != null ? command.getTolerance() : BigDecimal.ZERO;
            isValid &= statement.validateBalances();
        }

        if (Boolean.TRUE.equals(command.getValidateTransactions())) {
            // Validate transactions
            if (statement.getTransactions() == null || statement.getTransactions().isEmpty()) {
                statement.addImportWarning("No transactions found in statement");
                isValid = false;
            }
        }

        bankStatementRepository.save(statement);
        log.info("Validated bank statement: {}, valid: {}", command.getStatementId(), isValid);

        return isValid;
    }

    @Transactional
    public void addTransaction(BankStatementCommand.AddTransactionCommand command) {
        log.info("Adding transaction to statement: {} for tenant: {}", command.getStatementId(),
                command.getTenantId());

        BankStatement statement = bankStatementRepository.findByStatementIdAndTenantId(
                command.getStatementId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("BankStatement", command.getStatementId()));

        statement.addTransaction(command.getTransaction());
        statement.calculateTotals();
        bankStatementRepository.save(statement);

        log.info("Added transaction to statement: {}", command.getStatementId());
    }

    @Transactional
    public void linkToReconciliation(BankStatementCommand.LinkToReconciliationCommand command) {
        log.info("Linking statement: {} to reconciliation: {} for tenant: {}",
                command.getStatementId(), command.getReconciliationId(), command.getTenantId());

        BankStatement statement = bankStatementRepository.findByStatementIdAndTenantId(
                command.getStatementId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("BankStatement", command.getStatementId()));

        statement.linkToReconciliation(command.getReconciliationId());
        bankStatementRepository.save(statement);

        log.info("Linked statement: {} to reconciliation: {}", command.getStatementId(),
                command.getReconciliationId());
    }

    @Transactional
    public void delete(BankStatementCommand.DeleteBankStatementCommand command) {
        log.info("Deleting bank statement: {} for tenant: {}", command.getStatementId(),
                command.getTenantId());

        BankStatement statement = bankStatementRepository.findByStatementIdAndTenantId(
                command.getStatementId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("BankStatement", command.getStatementId()));

        if (Boolean.TRUE.equals(statement.getReconciled())) {
            throw new ValidationException("Cannot delete reconciled statements");
        }

        bankStatementRepository.deleteByStatementIdAndTenantId(
                command.getStatementId(), command.getTenantId());

        log.info("Deleted bank statement: {}", command.getStatementId());
    }

    @Transactional
    public void retryImport(BankStatementCommand.RetryImportCommand command) {
        log.info("Retrying import for statement: {} for tenant: {}", command.getStatementId(),
                command.getTenantId());

        BankStatement statement = bankStatementRepository.findByStatementIdAndTenantId(
                command.getStatementId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("BankStatement", command.getStatementId()));

        if (statement.getImportStatus() != BankStatement.ImportStatus.FAILED) {
            throw new ValidationException("Can only retry failed imports");
        }

        statement.setImportStatus(BankStatement.ImportStatus.PENDING);
        statement.setImportErrors(new ArrayList<>());
        bankStatementRepository.save(statement);

        log.info("Retrying import for statement: {}", command.getStatementId());
    }
}
