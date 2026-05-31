package com.gogidix.aiservices.aifrauddetectionservice.domain.repository;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Transaction aggregate persistence.
 * Extends Spring Data MongoDB repository for standard CRUD operations.
 * Uses hexagonal architecture - this is the domain repository interface.
 */
@Repository
public interface ITransactionRepository extends MongoRepository<Transaction, String> {

    /**
     * Find transaction by transaction ID.
     */
    Optional<Transaction> findByTransactionId(String transactionId);

    /**
     * Find all transactions for a specific tenant.
     */
    List<Transaction> findAllByTenantId(String tenantId);

    /**
     * Find all transactions for a tenant with pagination.
     */
    List<Transaction> findAllByTenantId(String tenantId, org.springframework.data.domain.Pageable pageable);

    /**
     * Find transactions for a specific user.
     */
    List<Transaction> findByUserIdOrderByTimestampDesc(String userId);

    /**
     * Find transactions for a user with pagination.
     */
    List<Transaction> findByUserIdOrderByTimestampDesc(String userId, org.springframework.data.domain.Pageable pageable);

    /**
     * Find transactions by merchant.
     */
    List<Transaction> findByMerchantAndTenantIdOrderByTimestampDesc(String merchant, String tenantId);

    /**
     * Find transactions with amount above threshold.
     */
    List<Transaction> findByAmountGreaterThanEqualAndTenantId(BigDecimal amount, String tenantId);

    /**
     * Find transactions within a date range for a tenant.
     */
    List<Transaction> findByTenantIdAndTimestampBetweenOrderByTimestampDesc(
            String tenantId, Instant start, Instant end);

    /**
     * Count transactions for a tenant.
     */
    long countByTenantId(String tenantId);

    /**
     * Count transactions for a user.
     */
    long countByUserId(String userId);

    /**
     * Find transactions by currency for a tenant.
     */
    List<Transaction> findByCurrencyAndTenantId(String currency, String tenantId);

    /**
     * Custom query to find high-value transactions for a tenant.
     */
    @Query("{ 'tenantId': ?0, 'amount': { $gte: ?1 } }")
    List<Transaction> findHighValueTransactions(String tenantId, BigDecimal threshold);

    /**
     * Find transactions for multiple users (batch query).
     */
    @Query("{ 'userId': { $in: ?0 } }")
    List<Transaction> findByUserIdIn(List<String> userIds);

    /**
     * Find recent transactions for a tenant since a given time.
     */
    @Query("{ 'tenantId': ?0, 'timestamp': { $gte: ?1 } }")
    List<Transaction> findRecentTransactionsForTenant(String tenantId, Instant since);

    /**
     * Delete transactions older than specified date (for archival).
     */
    void deleteByTimestampBefore(Instant cutoff);

    /**
     * Save transaction and return the saved instance.
     */
    <S extends Transaction> S save(S transaction);

    /**
     * Find transactions by status if supported.
     */
    @Query("{ 'metadata.status': ?0, 'tenantId': ?1 }")
    List<Transaction> findByMetadataStatusAndTenantId(String status, String tenantId);
}
