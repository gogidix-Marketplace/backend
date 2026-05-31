package com.gogidix.transaction.status.domain.repository;

import com.gogidix.transaction.status.domain.entity.Subscriber;
import com.gogidix.transaction.status.domain.entity.Subscriber.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA Repository for Subscriber entities.
 * Converted from MongoDB to PostgreSQL.
 */
@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, UUID> {

    Optional<Subscriber> findByConnectionId(String connectionId);

    List<Subscriber> findBySessionId(String sessionId);

    List<Subscriber> findByUserIdAndStatus(String userId, SubscriptionStatus status);

    List<Subscriber> findByStatus(SubscriptionStatus status);

    List<Subscriber> findByLastActivityBefore(LocalDateTime threshold);

    List<Subscriber> findByTransactionFilter(String transactionFilter);

    void deleteByConnectionId(String connectionId);

    void deleteBySessionId(String sessionId);

    long countByStatus(SubscriptionStatus status);

    long countByUserId(String userId);

    @Query("SELECT s FROM Subscriber s WHERE s.status = :status AND s.lastActivity < :threshold")
    List<Subscriber> findInactiveSubscribers(@Param("status") SubscriptionStatus status,
                                              @Param("threshold") LocalDateTime threshold);

    @Query("SELECT s FROM Subscriber s WHERE s.transactionFilter = :transactionFilter AND s.status = :status")
    List<Subscriber> findActiveSubscribersByTransactionFilter(@Param("transactionFilter") String transactionFilter,
                                                               @Param("status") SubscriptionStatus status);

    @Query("SELECT s FROM Subscriber s WHERE s.userId = :userId ORDER BY s.connectedAt DESC")
    List<Subscriber> findByUserIdOrderByConnectedAtDesc(@Param("userId") String userId);
}
