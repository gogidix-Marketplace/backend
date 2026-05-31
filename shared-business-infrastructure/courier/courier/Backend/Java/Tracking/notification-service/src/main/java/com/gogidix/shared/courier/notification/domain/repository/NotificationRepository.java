package com.gogidix.shared.courier.notification.domain.repository;

import com.gogidix.shared.courier.notification.domain.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for notification entities
 */
@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    java.util.Optional<Notification> findByTenantIdAndNotificationId(
            String tenantId, String notificationId);

    Page<Notification> findByTenantIdAndRecipientIdAndRecipientType(
            String tenantId,
            String recipientId,
            String recipientType,
            Pageable pageable);

    List<Notification> findByTenantIdAndRecipientIdAndStatus(
            String tenantId,
            String recipientId,
            Notification.NotificationStatus status);

    long countByTenantIdAndRecipientIdAndStatus(
            String tenantId,
            String recipientId,
            Notification.NotificationStatus status);

    List<Notification> findByTenantIdAndRecipientIdOrderBySentAtDesc(
            String tenantId, String recipientId);

    List<Notification> findByTenantIdAndNotificationTypeOrderBySentAtDesc(
            String tenantId, String notificationType);
}
