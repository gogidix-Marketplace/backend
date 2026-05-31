package com.gogidix.customersupport.notification.domain.repository;

import com.gogidix.customersupport.notification.domain.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    List<Notification> findByTenantId(String tenantId);

    Optional<Notification> findByTenantIdAndId(String tenantId, String id);

    Optional<Notification> findByNotificationId(String notificationId);

    List<Notification> findByTenantIdAndRecipientId(String tenantId, String recipientId);

    List<Notification> findByTenantIdAndRecipientIdOrderBySentAtDesc(String tenantId, String recipientId);

    List<Notification> findByTenantIdAndRecipientIdOrderByCreatedAtDesc(String tenantId, String recipientId);

    List<Notification> findByTenantIdAndStatus(String tenantId, Notification.NotificationStatus status);

    List<Notification> findByTenantIdAndStatusOrderByCreatedAtDesc(String tenantId, Notification.NotificationStatus status);

    List<Notification> findByTenantIdAndType(String tenantId, Notification.NotificationType type);

    List<Notification> findByTenantIdAndChannel(String tenantId, Notification.NotificationChannel channel);

    List<Notification> findByTenantIdAndScheduledAtBefore(String tenantId, Instant scheduledAt);

    List<Notification> findByTenantIdAndStatusAndScheduledAtBefore(String tenantId, Notification.NotificationStatus status, Instant scheduledAt);

    List<Notification> findByTenantIdAndRelatedEntityId(String tenantId, String relatedEntityId);

    List<Notification> findByTenantIdAndRecipientIdAndStatusOrderByCreatedAtDesc(String tenantId, String recipientId, Notification.NotificationStatus status);

    List<Notification> findByTenantIdAndRecipientIdAndChannelOrderByCreatedAtDesc(String tenantId, String recipientId, Notification.NotificationChannel channel);

    List<Notification> findByTenantIdAndRecipientIdAndStatusAndChannel(String tenantId, String recipientId, Notification.NotificationStatus status, Notification.NotificationChannel channel);

    List<Notification> findByTenantIdAndCreatedAtBetweenOrderByCreatedAtDesc(String tenantId, Instant startDate, Instant endDate);

    void deleteByTenantIdAndId(String tenantId, String id);

    long countByTenantIdAndRecipientIdAndStatus(String tenantId, String recipientId, Notification.NotificationStatus status);

    long countByTenantIdAndStatus(String tenantId, Notification.NotificationStatus status);
}
