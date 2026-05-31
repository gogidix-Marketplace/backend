package com.gogidix.customersupport.notification.domain.repository;

import com.gogidix.customersupport.notification.domain.model.NotificationQueue;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationQueueRepository extends MongoRepository<NotificationQueue, String> {

    List<NotificationQueue> findByTenantId(String tenantId);

    Optional<NotificationQueue> findByNotificationId(String notificationId);

    List<NotificationQueue> findByQueueName(String queueName);

    @Query("{ 'tenantId': ?0, 'processingStatus': 'PENDING', 'scheduledAt': { $lte: ?1 } }")
    List<NotificationQueue> findPendingScheduled(String tenantId, Instant now);

    @Query("{ 'tenantId': ?0, 'processingStatus': { $in: ['PENDING', 'PROCESSING'] }, 'scheduledAt': { $lte: ?1 }, '$or': [ {'lockedUntil': null}, {'lockedUntil': { $lte: ?1 } } ] }")
    List<NotificationQueue> findAvailableForProcessing(String tenantId, Instant now);

    @Query("{ 'tenantId': ?0, 'processingStatus': 'FAILED', 'attemptCount': { $lt: '$maxAttempts' } }")
    List<NotificationQueue> findRetryable(String tenantId);

    void deleteByNotificationId(String notificationId);

    void deleteByTenantIdAndId(String tenantId, String id);
}
