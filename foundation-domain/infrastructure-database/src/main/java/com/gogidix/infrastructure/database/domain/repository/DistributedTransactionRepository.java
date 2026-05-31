package com.gogidix.infrastructure.database.domain.repository;

import com.gogidix.infrastructure.database.domain.model.DistributedTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for managing distributed transactions.
 */
@Repository
public interface DistributedTransactionRepository extends MongoRepository<DistributedTransaction, String> {

    /**
     * Find by tenant ID.
     */
    List<DistributedTransaction> findAllByTenantId(String tenantId);

    /**
     * Find by tenant ID paginated.
     */
    Page<DistributedTransaction> findAllByTenantId(String tenantId, Pageable pageable);

    /**
     * Find by transaction ID.
     */
    Optional<DistributedTransaction> findByTransactionId(String transactionId);

    /**
     * Find by tenant ID and status.
     */
    List<DistributedTransaction> findAllByTenantIdAndStatus(
            String tenantId,
            DistributedTransaction.TransactionStatus status);

    /**
     * Find by tenant ID and transaction type.
     */
    List<DistributedTransaction> findAllByTenantIdAndTransactionType(
            String tenantId,
            DistributedTransaction.TransactionType transactionType);

    /**
     * Find active transactions.
     */
    @Query("{'status': {$in: ['ACTIVE', 'PREPARING', 'PREPARED', 'COMMITTING']}}")
    List<DistributedTransaction> findActiveTransactions();

    /**
     * Find active transactions by tenant.
     */
    @Query("{'tenantId': ?0, 'status': {$in: ['ACTIVE', 'PREPARING', 'PREPARED', 'COMMITTING']}}")
    List<DistributedTransaction> findActiveTransactionsByTenant(String tenantId);

    /**
     * Find timed out transactions.
     */
    @Query("{'status': {$in: ['ACTIVE', 'PREPARING', 'PREPARED', 'COMMITTING']}, 'expiresAt': {$lt: ?0}}")
    List<DistributedTransaction> findTimedOutTransactions(LocalDateTime now);

    /**
     * Find transactions to retry.
     */
    @Query("{'status': 'FAILED', 'transactionType': 'SAGA', 'retryCount': {$lt: '$maxRetries'}}")
    List<DistributedTransaction> findTransactionsToRetry();

    /**
     * Find by correlation ID.
     */
    List<DistributedTransaction> findAllByCorrelationId(String correlationId);

    /**
     * Find by parent transaction ID.
     */
    List<DistributedTransaction> findAllByParentTransactionId(String parentTransactionId);

    /**
     * Find by initiator.
     */
    List<DistributedTransaction> findAllByInitiator(String initiator);

    /**
     * Find by application name.
     */
    List<DistributedTransaction> findAllByApplicationName(String applicationName);

    /**
     * Find transactions within time range.
     */
    List<DistributedTransaction> findAllByCreatedAtBetween(
            LocalDateTime startTime,
            LocalDateTime endTime);

    /**
     * Find transactions within time range for tenant.
     */
    List<DistributedTransaction> findAllByTenantIdAndCreatedAtBetween(
            String tenantId,
            LocalDateTime startTime,
            LocalDateTime endTime);

    /**
     * Count by tenant ID and status.
     */
    Long countByTenantIdAndStatus(String tenantId, DistributedTransaction.TransactionStatus status);

    /**
     * Count active transactions by tenant.
     */
    Long countByTenantIdAndStatusIn(
            String tenantId,
            List<DistributedTransaction.TransactionStatus> statuses);

    /**
     * Count by transaction type.
     */
    Long countByTransactionType(DistributedTransaction.TransactionType transactionType);

    /**
     * Find transactions with specific participant.
     */
    @Query("{'participants.participantId': ?0}")
    List<DistributedTransaction> findByParticipantId(String participantId);

    /**
     * Find by tags containing.
     */
    @Query("{'tags': {$in: ?0}}")
    List<DistributedTransaction> findByTagsContaining(List<String> tags);

    /**
     * Find by priority.
     */
    List<DistributedTransaction> findAllByTenantIdAndPriorityOrderByCreatedAtDesc(
            String tenantId,
            Integer priority);

    /**
     * Find pending transactions.
     */
    @Query("{'status': 'PENDING', 'tenantId': ?0}")
    List<DistributedTransaction> findPendingTransactions(String tenantId);

    /**
     * Find committed transactions for reporting.
     */
    @Query("{'status': 'COMMITTED', 'createdAt': {$gte: ?0, $lte: ?1}}")
    List<DistributedTransaction> findCommittedTransactionsForReport(LocalDateTime start, LocalDateTime end);

    /**
     * Find failed transactions for analysis.
     */
    List<DistributedTransaction> findAllByStatusOrderByCreatedAtDesc(
            DistributedTransaction.TransactionStatus status);

    /**
     * Find transactions pending compensation.
     */
    @Query("{'status': 'FAILED', 'transactionType': 'SAGA', 'sagaSteps.status': 'COMPLETED'}")
    List<DistributedTransaction> findTransactionsPendingCompensation();

    /**
     * Count total transactions by tenant.
     */
    Long countByTenantId(String tenantId);

    /**
     * Find by status paginated.
     */
    Page<DistributedTransaction> findAllByStatus(
            DistributedTransaction.TransactionStatus status,
            Pageable pageable);
}
