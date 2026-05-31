package com.gogidix.sales.notification.domain.repository;

import com.gogidix.sales.notification.domain.model.Notification;
import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;
import com.gogidix.sales.notification.domain.valueobject.NotificationStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Notification Repository Interface (Port)
 * Defines the contract for notification persistence operations
 */
public interface NotificationRepository {

    Notification save(Notification notification);

    List<Notification> saveAll(List<Notification> notifications);

    Optional<Notification> findById(String id);

    Optional<Notification> findByNotificationIdAndTenantId(String notificationId, String tenantId);

    List<Notification> findByTenantId(String tenantId);

    List<Notification> findByUserIdAndTenantId(String userId, String tenantId);

    List<Notification> findByUserIdAndTenantIdOrderByCreatedAtDesc(String userId, String tenantId);

    List<Notification> findByRecipientIdsContainingAndTenantId(String recipientId, String tenantId);

    List<Notification> findByTenantIdAndStatus(String tenantId, NotificationStatus status);

    List<Notification> findByTenantIdAndChannel(String tenantId, NotificationChannel channel);

    List<Notification> findByScheduledAtBeforeAndStatus(Instant scheduledAt, NotificationStatus status);

    List<Notification> findByGroupIdAndTenantId(String groupId, String tenantId);

    boolean existsByNotificationIdAndTenantId(String notificationId, String tenantId);

    void deleteById(String id);

    void deleteByNotificationIdAndTenantId(String notificationId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, NotificationStatus status);

    long countUnreadByUserId(String userId);

    long countUnreadByRecipientId(String recipientId);

    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(String userId);

    List<Notification> findByUserIdAndTenantIdAndIsReadFalseOrderByCreatedAtDesc(String userId, String tenantId);

    List<Notification> findByExpiresAtBeforeAndStatusNot(Instant expiresAt, NotificationStatus status);

    List<Notification> findByTenantIdAndCreatedAtBetween(String tenantId, Instant startDate, Instant endDate);

    List<Notification> searchByContent(String tenantId, String searchTerm);
}
