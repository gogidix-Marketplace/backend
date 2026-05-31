package com.gogidix.infrastructure.database.application.service;

import com.gogidix.infrastructure.database.domain.model.DistributedTransaction;
import com.gogidix.infrastructure.database.domain.repository.DistributedTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service for managing distributed transactions.
 *
 * <p>Handles two-phase commit and saga-based distributed transactions
 * across multiple databases.</p>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DistributedTransactionService {

    private final DistributedTransactionRepository repository;
    private final ConnectionPoolService connectionPoolService;

    private final Map<String, TransactionContext> activeTransactions = new ConcurrentHashMap<>();

    /**
     * Transaction context for tracking in-memory state.
     */
    private static class TransactionContext {
        String transactionId;
        Map<String, Boolean> preparedParticipants = new HashMap<>();
        Map<String, Boolean> committedParticipants = new HashMap<>();
        Map<String, Boolean> rolledBackParticipants = new HashMap<>();
    }

    /**
     * Create a new distributed transaction.
     */
    @Transactional
    @CacheEvict(value = "transactions", allEntries = true)
    public DistributedTransaction createTransaction(@Valid DistributedTransaction transaction) {
        log.info("Creating distributed transaction: {}", transaction.getTransactionName());

        transaction.setStatus(DistributedTransaction.TransactionStatus.ACTIVE);
        transaction.setStartTime(LocalDateTime.now());
        transaction.setExpirationTime();

        return repository.save(transaction);
    }

    /**
     * Get transaction by ID.
     */
    @Cacheable(value = "transactions", key = "#id")
    public Optional<DistributedTransaction> getTransaction(String id) {
        return repository.findById(id);
    }

    /**
     * Get all transactions for a tenant.
     */
    public List<DistributedTransaction> getTransactionsByTenant(String tenantId) {
        return repository.findAllByTenantId(tenantId);
    }

    /**
     * Get transactions by tenant paginated.
     */
    public Page<DistributedTransaction> getTransactionsByTenant(String tenantId, Pageable pageable) {
        return repository.findAllByTenantId(tenantId, pageable);
    }

    /**
     * Execute a two-phase commit transaction.
     */
    @Transactional
    @CacheEvict(value = "transactions", allEntries = true)
    public DistributedTransaction executeTwoPhaseCommit(String id) {
        DistributedTransaction transaction = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + id));

        if (transaction.getTransactionType() != DistributedTransaction.TransactionType.TWO_PHASE_COMMIT) {
            throw new IllegalArgumentException("Transaction is not a two-phase commit transaction");
        }

        log.info("Executing two-phase commit for transaction: {}", id);

        TransactionContext context = new TransactionContext();
        context.transactionId = id;
        activeTransactions.put(id, context);

        try {
            // Phase 1: Prepare
            transaction.setStatus(DistributedTransaction.TransactionStatus.PREPARING);
            transaction.setTwoPhaseCommitPhase(DistributedTransaction.TwoPhaseCommitPhase.PREPARE);
            repository.save(transaction);

            boolean allPrepared = prepareParticipants(transaction);

            if (!allPrepared) {
                // Prepare failed, rollback
                log.warn("Prepare phase failed for transaction: {}, initiating rollback", id);
                rollbackTwoPhaseCommit(transaction, context);
                return transaction;
            }

            transaction.setStatus(DistributedTransaction.TransactionStatus.PREPARED);
            repository.save(transaction);

            // Phase 2: Commit
            transaction.setStatus(DistributedTransaction.TransactionStatus.COMMITTING);
            transaction.setTwoPhaseCommitPhase(DistributedTransaction.TwoPhaseCommitPhase.COMMIT);
            repository.save(transaction);

            boolean allCommitted = commitParticipants(transaction);

            if (allCommitted) {
                transaction.setStatus(DistributedTransaction.TransactionStatus.COMMITTED);
                transaction.setEndTime(LocalDateTime.now());
                transaction.calculateDuration();
                log.info("Two-phase commit completed successfully: {}", id);
            } else {
                log.error("Commit phase failed for transaction: {}", id);
                transaction.setStatus(DistributedTransaction.TransactionStatus.FAILED);
            }

            transaction.setTwoPhaseCommitPhase(DistributedTransaction.TwoPhaseCommitPhase.COMPLETED);

        } catch (Exception e) {
            log.error("Two-phase commit failed for transaction {}: {}", id, e.getMessage());
            transaction.setStatus(DistributedTransaction.TransactionStatus.FAILED);
            transaction.setErrorMessage(e.getMessage());
            rollbackTwoPhaseCommit(transaction, context);
        } finally {
            activeTransactions.remove(id);
        }

        return repository.save(transaction);
    }

    /**
     * Prepare all participants.
     */
    private boolean prepareParticipants(DistributedTransaction transaction) {
        Map<String, Boolean> prepareResults = new HashMap<>();

        for (DistributedTransaction.TransactionParticipant participant : transaction.getParticipants()) {
            try {
                log.debug("Preparing participant: {}", participant.getParticipantId());

                boolean prepared = prepareParticipant(participant);
                prepareResults.put(participant.getParticipantId(), prepared);
                participant.setStatus(prepared ?
                        DistributedTransaction.ParticipantStatus.PREPARED :
                        DistributedTransaction.ParticipantStatus.FAILED);
                participant.setPreparedAt(LocalDateTime.now());

                if (!prepared) {
                    log.warn("Participant prepare failed: {}", participant.getParticipantId());
                    break;
                }

            } catch (Exception e) {
                log.error("Error preparing participant {}: {}",
                        participant.getParticipantId(), e.getMessage());
                prepareResults.put(participant.getParticipantId(), false);
                participant.setStatus(DistributedTransaction.ParticipantStatus.FAILED);
                participant.setErrorMessage(e.getMessage());
                break;
            }
        }

        transaction.setPrepareResults(prepareResults);
        return prepareResults.values().stream().allMatch(Boolean::booleanValue);
    }

    /**
     * Prepare a single participant.
     */
    private boolean prepareParticipant(DistributedTransaction.TransactionParticipant participant) {
        // TODO: Implement actual prepare (execute prepare query on participant)
        log.info("Executing prepare on participant: {}", participant.getParticipantId());

        if (participant.getPrepareQuery() != null) {
            // Execute prepare query
        }

        return true; // Placeholder
    }

    /**
     * Commit all participants.
     */
    private boolean commitParticipants(DistributedTransaction transaction) {
        Map<String, Boolean> commitResults = new HashMap<>();

        for (DistributedTransaction.TransactionParticipant participant : transaction.getParticipants()) {
            try {
                log.debug("Committing participant: {}", participant.getParticipantId());

                boolean committed = commitParticipant(participant);
                commitResults.put(participant.getParticipantId(), committed);
                participant.setStatus(committed ?
                        DistributedTransaction.ParticipantStatus.COMMITTED :
                        DistributedTransaction.ParticipantStatus.FAILED);
                participant.setCommittedAt(LocalDateTime.now());

            } catch (Exception e) {
                log.error("Error committing participant {}: {}",
                        participant.getParticipantId(), e.getMessage());
                commitResults.put(participant.getParticipantId(), false);
            }
        }

        transaction.setCommitResults(commitResults);
        return commitResults.values().stream().allMatch(Boolean::booleanValue);
    }

    /**
     * Commit a single participant.
     */
    private boolean commitParticipant(DistributedTransaction.TransactionParticipant participant) {
        log.info("Executing commit on participant: {}", participant.getParticipantId());

        if (participant.getCommitQuery() != null) {
            // Execute commit query
        }

        return true; // Placeholder
    }

    /**
     * Rollback two-phase commit transaction.
     */
    private void rollbackTwoPhaseCommit(DistributedTransaction transaction, TransactionContext context) {
        log.info("Rolling back two-phase commit transaction: {}", transaction.getId());

        transaction.setStatus(DistributedTransaction.TransactionStatus.ROLLING_BACK);
        transaction.setTwoPhaseCommitPhase(DistributedTransaction.TwoPhaseCommitPhase.ROLLBACK);
        repository.save(transaction);

        Map<String, Boolean> rollbackResults = new HashMap<>();

        for (DistributedTransaction.TransactionParticipant participant : transaction.getParticipants()) {
            if (participant.getStatus() == DistributedTransaction.ParticipantStatus.PREPARED ||
                participant.getStatus() == DistributedTransaction.ParticipantStatus.COMMITTING) {

                try {
                    boolean rolledBack = rollbackParticipant(participant);
                    rollbackResults.put(participant.getParticipantId(), rolledBack);
                    participant.setStatus(DistributedTransaction.ParticipantStatus.ROLLED_BACK);
                    participant.setRolledBackAt(LocalDateTime.now());

                } catch (Exception e) {
                    log.error("Error rolling back participant {}: {}",
                            participant.getParticipantId(), e.getMessage());
                    rollbackResults.put(participant.getParticipantId(), false);
                }
            }
        }

        transaction.setRollbackResults(rollbackResults);
        transaction.setStatus(DistributedTransaction.TransactionStatus.ROLLED_BACK);
        transaction.setEndTime(LocalDateTime.now());
        transaction.calculateDuration();
    }

    /**
     * Rollback a single participant.
     */
    private boolean rollbackParticipant(DistributedTransaction.TransactionParticipant participant) {
        log.info("Executing rollback on participant: {}", participant.getParticipantId());

        if (participant.getRollbackQuery() != null) {
            // Execute rollback query
        }

        return true; // Placeholder
    }

    /**
     * Execute a saga transaction.
     */
    @Transactional
    @CacheEvict(value = "transactions", allEntries = true)
    public DistributedTransaction executeSaga(String id) {
        DistributedTransaction transaction = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + id));

        if (transaction.getTransactionType() != DistributedTransaction.TransactionType.SAGA) {
            throw new IllegalArgumentException("Transaction is not a saga transaction");
        }

        log.info("Executing saga transaction: {}", id);

        try {
            List<DistributedTransaction.SagaStep> steps = new ArrayList<>(transaction.getSagaSteps());
            steps.sort(Comparator.comparing(DistributedTransaction.SagaStep::getOrder));

            transaction.setCurrentSagaStep(0);
            transaction.setStatus(DistributedTransaction.TransactionStatus.ACTIVE);
            repository.save(transaction);

            // Execute forward steps
            for (int i = 0; i < steps.size(); i++) {
                DistributedTransaction.SagaStep step = steps.get(i);
                transaction.setCurrentSagaStep(i);

                try {
                    executeSagaStep(step);
                    step.setStatus(DistributedTransaction.SagaStepStatus.COMPLETED);
                    step.setExecutedAt(LocalDateTime.now());
                    repository.save(transaction);

                } catch (Exception e) {
                    log.error("Saga step failed: {}, initiating compensation", step.getStepName());
                    step.setStatus(DistributedTransaction.SagaStepStatus.FAILED);
                    step.setErrorMessage(e.getMessage());

                    // Compensate completed steps
                    compensateSaga(transaction, i);
                    break;
                }
            }

            if (transaction.getStatus() != DistributedTransaction.TransactionStatus.FAILED) {
                transaction.setStatus(DistributedTransaction.TransactionStatus.COMMITTED);
                transaction.setEndTime(LocalDateTime.now());
                transaction.calculateDuration();
                log.info("Saga completed successfully: {}", id);
            }

        } catch (Exception e) {
            log.error("Saga execution failed: {}: {}", id, e.getMessage());
            transaction.setStatus(DistributedTransaction.TransactionStatus.FAILED);
            transaction.setErrorMessage(e.getMessage());
        }

        return repository.save(transaction);
    }

    /**
     * Execute a saga step.
     */
    private void executeSagaStep(DistributedTransaction.SagaStep step) {
        log.info("Executing saga step: {}", step.getStepName());

        // TODO: Execute step action
        step.setStatus(DistributedTransaction.SagaStepStatus.RUNNING);
    }

    /**
     * Compensate saga steps.
     */
    private void compensateSaga(DistributedTransaction transaction, int failedStepIndex) {
        log.info("Compensating saga transaction: {}", transaction.getId());

        transaction.setStatus(DistributedTransaction.TransactionStatus.ROLLING_BACK);

        List<DistributedTransaction.SagaStep> steps = new ArrayList<>(transaction.getSagaSteps());
        steps.sort(Comparator.comparing(DistributedTransaction.SagaStep::getOrder).reversed());

        // Compensate completed steps in reverse order
        for (int i = 0; i <= failedStepIndex; i++) {
            DistributedTransaction.SagaStep step = steps.get(i);

            if (step.getStatus() == DistributedTransaction.SagaStepStatus.COMPLETED) {
                try {
                    compensateSagaStep(step);
                    step.setStatus(DistributedTransaction.SagaStepStatus.COMPENSATED);
                    step.setCompensatedAt(LocalDateTime.now());

                } catch (Exception e) {
                    log.error("Compensation failed for step {}: {}",
                            step.getStepName(), e.getMessage());
                    step.setStatus(DistributedTransaction.SagaStepStatus.FAILED);
                }
            }
        }

        transaction.setStatus(DistributedTransaction.TransactionStatus.ROLLED_BACK);
    }

    /**
     * Compensate a saga step.
     */
    private void compensateSagaStep(DistributedTransaction.SagaStep step) {
        log.info("Compensating saga step: {}", step.getStepName());

        if (step.getCompensatingAction() != null) {
            // TODO: Execute compensating action
        }
    }

    /**
     * Rollback a transaction.
     */
    @Transactional
    @CacheEvict(value = "transactions", allEntries = true)
    public DistributedTransaction rollbackTransaction(String id) {
        DistributedTransaction transaction = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + id));

        log.info("Rolling back transaction: {}", id);

        if (transaction.getTransactionType() == DistributedTransaction.TransactionType.SAGA) {
            List<DistributedTransaction.SagaStep> steps = new ArrayList<>(transaction.getSagaSteps());
            steps.sort(Comparator.comparing(DistributedTransaction.SagaStep::getOrder).reversed());

            int maxStep = steps.size();
            compensateSaga(transaction, maxStep);

        } else {
            TransactionContext context = activeTransactions.get(id);
            if (context == null) {
                context = new TransactionContext();
                context.transactionId = id;
            }
            rollbackTwoPhaseCommit(transaction, context);
        }

        return repository.save(transaction);
    }

    /**
     * Get transaction statistics.
     */
    public Map<String, Object> getTransactionStatistics(String tenantId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("tenantId", tenantId);

        stats.put("totalTransactions", repository.countByTenantId(tenantId));

        long activeCount = repository.countByTenantIdAndStatusIn(tenantId,
                Arrays.asList(DistributedTransaction.TransactionStatus.ACTIVE,
                        DistributedTransaction.TransactionStatus.PREPARING,
                        DistributedTransaction.TransactionStatus.COMMITTING));
        stats.put("activeTransactions", activeCount);

        long committedCount = repository.countByTenantIdAndStatus(tenantId,
                DistributedTransaction.TransactionStatus.COMMITTED);
        stats.put("committedTransactions", committedCount);

        long rolledBackCount = repository.countByTenantIdAndStatus(tenantId,
                DistributedTransaction.TransactionStatus.ROLLED_BACK);
        stats.put("rolledBackTransactions", rolledBackCount);

        long failedCount = repository.countByTenantIdAndStatus(tenantId,
                DistributedTransaction.TransactionStatus.FAILED);
        stats.put("failedTransactions", failedCount);

        return stats;
    }

    /**
     * Scheduled task to check for timed out transactions.
     */
    @Scheduled(fixedDelay = 30000)
    public void checkTimedOutTransactions() {
        List<DistributedTransaction> timedOutTransactions = repository.findTimedOutTransactions(
                LocalDateTime.now());

        log.debug("Found {} timed out transactions", timedOutTransactions.size());

        for (DistributedTransaction transaction : timedOutTransactions) {
            try {
                log.warn("Transaction timed out: {}, rolling back", transaction.getId());
                rollbackTransaction(transaction.getId());
            } catch (Exception e) {
                log.error("Failed to rollback timed out transaction {}: {}",
                        transaction.getId(), e.getMessage());
            }
        }
    }
}
