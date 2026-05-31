package com.gogidix.finance.budgettracking.application.service;

import com.gogidix.finance.budgettracking.domain.model.BudgetTransaction;
import com.gogidix.finance.budgettracking.domain.port.in.BudgetTransactionCommand;
import com.gogidix.finance.budgettracking.domain.port.out.EventPublisher;
import com.gogidix.finance.budgettracking.domain.repository.BudgetTransactionRepository;
import com.gogidix.finance.budgettracking.shared.exception.ConflictException;
import com.gogidix.finance.budgettracking.shared.exception.NotFoundException;
import com.gogidix.finance.budgettracking.shared.exception.ValidationException;
import com.gogidix.finance.budgettracking.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Budget Transaction Service
 * Handles all command operations for budget transactions
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetTransactionService {

    private final BudgetTransactionRepository transactionRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public BudgetTransaction create(BudgetTransactionCommand.CreateTransactionCommand command) {
        log.info("Creating budget transaction for tenant: {}, budget: {}",
            command.getTenantId(), command.getBudgetCode());

        validateTransactionAmount(command.getAmount());

        BudgetTransaction transaction = BudgetTransaction.create(
            command.getTenantId(),
            command.getBudgetId(),
            command.getBudgetCode(),
            command.getTransactionType(),
            command.getAmount(),
            command.getCurrency(),
            command.getDescription(),
            RequestContextHolder.getUserId().orElse("system")
        );

        transaction.setTransactionId(generateTransactionId());
        transaction.setReferenceType(command.getReferenceType());
        transaction.setReferenceId(command.getReferenceId());
        transaction.setCategory(command.getCategory());
        transaction.setDepartment(command.getDepartment());
        transaction.setCostCenter(command.getCostCenter());
        transaction.setProjectId(command.getProjectId());
        transaction.setTags(command.getTags());
        transaction.setNotes(command.getNotes());
        transaction.setCorrelationId(command.getCorrelationId());

        if (command.getTransactionDate() != null) {
            transaction.setTransactionDate(command.getTransactionDate());
        }

        BudgetTransaction savedTransaction = transactionRepository.save(transaction);
        publishEvents(savedTransaction);

        log.info("Created budget transaction: {} for tenant: {}",
            savedTransaction.getTransactionId(), command.getTenantId());
        return savedTransaction;
    }

    @Transactional
    public BudgetTransaction record(BudgetTransactionCommand.RecordTransactionCommand command) {
        log.info("Recording budget transaction: {} for tenant: {}",
            command.getTransactionId(), command.getTenantId());

        BudgetTransaction transaction = transactionRepository.findByTransactionIdAndTenantId(
            command.getTransactionId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetTransaction", command.getTransactionId()));

        transaction.record(command.getBalanceBefore(), command.getBalanceAfter());
        BudgetTransaction savedTransaction = transactionRepository.save(transaction);
        publishEvents(savedTransaction);

        log.info("Recorded budget transaction: {}", command.getTransactionId());
        return savedTransaction;
    }

    @Transactional
    public void approve(BudgetTransactionCommand.ApproveTransactionCommand command) {
        log.info("Approving budget transaction: {} by: {} for tenant: {}",
            command.getTransactionId(), command.getApprover(), command.getTenantId());

        BudgetTransaction transaction = transactionRepository.findByTransactionIdAndTenantId(
            command.getTransactionId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetTransaction", command.getTransactionId()));

        transaction.approve(command.getApprover());
        transactionRepository.save(transaction);
        publishEvents(transaction);

        log.info("Approved budget transaction: {}", command.getTransactionId());
    }

    @Transactional
    public void reject(BudgetTransactionCommand.RejectTransactionCommand command) {
        log.info("Rejecting budget transaction: {} by: {} for tenant: {}",
            command.getTransactionId(), command.getRejecter(), command.getTenantId());

        BudgetTransaction transaction = transactionRepository.findByTransactionIdAndTenantId(
            command.getTransactionId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetTransaction", command.getTransactionId()));

        transaction.reject(command.getRejecter(), command.getReason());
        transactionRepository.save(transaction);
        publishEvents(transaction);

        log.info("Rejected budget transaction: {}", command.getTransactionId());
    }

    @Transactional
    public void reverse(BudgetTransactionCommand.ReverseTransactionCommand command) {
        log.info("Reversing budget transaction: {} for tenant: {}",
            command.getTransactionId(), command.getTenantId());

        BudgetTransaction transaction = transactionRepository.findByTransactionIdAndTenantId(
            command.getTransactionId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetTransaction", command.getTransactionId()));

        transaction.reverse(command.getReason());
        transactionRepository.save(transaction);
        publishEvents(transaction);

        log.info("Reversed budget transaction: {}", command.getTransactionId());
    }

    @Transactional
    public BudgetTransaction update(BudgetTransactionCommand.UpdateTransactionCommand command) {
        log.info("Updating budget transaction: {} for tenant: {}",
            command.getTransactionId(), command.getTenantId());

        BudgetTransaction transaction = transactionRepository.findByTransactionIdAndTenantId(
            command.getTransactionId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetTransaction", command.getTransactionId()));

        if (transaction.getStatus() != BudgetTransaction.TransactionStatus.PENDING) {
            throw new ValidationException("Can only update pending transactions");
        }

        if (command.getDescription() != null) {
            transaction.setDescription(command.getDescription());
        }
        if (command.getAmount() != null) {
            validateTransactionAmount(command.getAmount());
            transaction.setAmount(command.getAmount());
        }
        if (command.getTags() != null) {
            transaction.setTags(command.getTags());
        }
        if (command.getNotes() != null) {
            transaction.setNotes(command.getNotes());
        }

        BudgetTransaction savedTransaction = transactionRepository.save(transaction);
        publishEvents(savedTransaction);

        return savedTransaction;
    }

    @Transactional
    public void delete(BudgetTransactionCommand.DeleteTransactionCommand command) {
        log.info("Deleting budget transaction: {} for tenant: {}",
            command.getTransactionId(), command.getTenantId());

        BudgetTransaction transaction = transactionRepository.findByTransactionIdAndTenantId(
            command.getTransactionId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetTransaction", command.getTransactionId()));

        if (transaction.getStatus() != BudgetTransaction.TransactionStatus.PENDING) {
            throw new ValidationException("Can only delete pending transactions");
        }

        transactionRepository.deleteByTransactionIdAndTenantId(
            command.getTransactionId(), command.getTenantId());

        log.info("Deleted budget transaction: {}", command.getTransactionId());
    }

    @Transactional
    public void addTag(BudgetTransactionCommand.AddTagCommand command) {
        log.info("Adding tag to budget transaction: {} for tenant: {}",
            command.getTransactionId(), command.getTenantId());

        BudgetTransaction transaction = transactionRepository.findByTransactionIdAndTenantId(
            command.getTransactionId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetTransaction", command.getTransactionId()));

        transaction.addTag(command.getTag());
        transactionRepository.save(transaction);

        log.info("Added tag {} to budget transaction: {}", command.getTag(), command.getTransactionId());
    }

    @Transactional
    public void removeTag(BudgetTransactionCommand.RemoveTagCommand command) {
        log.info("Removing tag from budget transaction: {} for tenant: {}",
            command.getTransactionId(), command.getTenantId());

        BudgetTransaction transaction = transactionRepository.findByTransactionIdAndTenantId(
            command.getTransactionId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetTransaction", command.getTransactionId()));

        transaction.removeTag(command.getTag());
        transactionRepository.save(transaction);

        log.info("Removed tag {} from budget transaction: {}", command.getTag(), command.getTransactionId());
    }

    private void validateTransactionAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("amount", "Amount must be positive");
        }
        if (amount.compareTo(new BigDecimal("1000000000")) > 0) {
            throw new ValidationException("amount", "Amount exceeds maximum limit");
        }
    }

    private void publishEvents(BudgetTransaction transaction) {
        if (!transaction.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll(new java.util.ArrayList<>(transaction.getDomainEvents()));
            transaction.clearDomainEvents();
        }
    }

    private String generateTransactionId() {
        return "BTX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public BudgetTransaction createTransaction(BudgetTransactionCommand.CreateTransactionCommand command) {
        return create(command);
    }

    public BudgetTransaction recordTransaction(String transactionId) {
        String tenantId = RequestContextHolder.getTenantId();
        BudgetTransaction tx = transactionRepository.findByTransactionIdAndTenantId(transactionId, tenantId)
            .orElseThrow(() -> new NotFoundException("BudgetTransaction", transactionId));
        tx.record(tx.getAmount(), tx.getAmount());
        return transactionRepository.save(tx);
    }

    public void approveTransaction(String transactionId, java.util.Optional<String> userId) {
        String tenantId = RequestContextHolder.getTenantId();
        BudgetTransaction tx = transactionRepository.findByTransactionIdAndTenantId(transactionId, tenantId)
            .orElseThrow(() -> new NotFoundException("BudgetTransaction", transactionId));
        tx.approve(userId.orElse("system"));
        transactionRepository.save(tx);
    }

    public void rejectTransaction(String transactionId, java.util.Optional<String> userId, String reason) {
        String tenantId = RequestContextHolder.getTenantId();
        BudgetTransaction tx = transactionRepository.findByTransactionIdAndTenantId(transactionId, tenantId)
            .orElseThrow(() -> new NotFoundException("BudgetTransaction", transactionId));
        tx.reject(userId.orElse("system"), reason);
        transactionRepository.save(tx);
    }

    public void reverseTransaction(String transactionId, String reason) {
        String tenantId = RequestContextHolder.getTenantId();
        BudgetTransaction tx = transactionRepository.findByTransactionIdAndTenantId(transactionId, tenantId)
            .orElseThrow(() -> new NotFoundException("BudgetTransaction", transactionId));
        tx.reverse(reason);
        transactionRepository.save(tx);
    }

    public BudgetTransaction getTransactionById(String transactionId) {
        String tenantId = RequestContextHolder.getTenantId();
        return transactionRepository.findByTransactionIdAndTenantId(transactionId, tenantId)
            .orElseThrow(() -> new NotFoundException("BudgetTransaction", transactionId));
    }

    public List<BudgetTransaction> getTransactionsByBudget(String budgetId) {
        return transactionRepository.findByTenantIdAndBudgetId(RequestContextHolder.getTenantId(), budgetId);
    }

    public List<BudgetTransaction> getAllTransactions() {
        return transactionRepository.findByTenantId(RequestContextHolder.getTenantId());
    }

    public List<BudgetTransaction> searchTransactions(com.gogidix.finance.budgettracking.domain.port.in.BudgetTrackingQuery.SearchTransactionsQuery query) {
        return transactionRepository.findByTenantId(RequestContextHolder.getTenantId());
    }

    public List<BudgetTransaction> getTransactionsByReference(String referenceType, String referenceId) {
        return transactionRepository.findByTenantIdAndReferenceId(RequestContextHolder.getTenantId(), referenceId);
    }
}
