package com.gogidix.sales.notification.domain.repository;

import com.gogidix.sales.notification.domain.model.NotificationDelivery;
import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;
import com.gogidix.sales.notification.domain.valueobject.NotificationStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Notification Delivery Repository Interface (Port)
 * Defines the contract for delivery tracking persistence operations
 */
public interface NotificationDeliveryRepository {

    NotificationDelivery save(NotificationDelivery delivery);

    List<NotificationDelivery> saveAll(List<NotificationDelivery> deliveries);

    Optional<NotificationDelivery> findById(String id);

    Optional<NotificationDelivery> findByDeliveryIdAndTenantId(String deliveryId, String tenantId);

    List<NotificationDelivery> findByNotificationIdAndTenantId(String notificationId, String tenantId);

    List<NotificationDelivery> findByRecipientIdAndTenantId(String recipientId, String tenantId);

    List<NotificationDelivery> findByRecipientIdAndTenantIdOrderByCreatedAtDesc(String recipientId, String tenantId);

    List<NotificationDelivery> findByNotificationIdAndRecipientId(String notificationId, String recipientId);

    List<NotificationDelivery> findByTenantIdAndStatus(String tenantId, NotificationStatus status);

    List<NotificationDelivery> findByTenantIdAndChannel(String tenantId, NotificationChannel channel);

    List<NotificationDelivery> findByStatusAndNextRetryAtBefore(NotificationStatus status, Instant retryAt);

    void deleteById(String id);

    void deleteByNotificationIdAndTenantId(String notificationId, String tenantId);

    void deleteByDeliveryIdAndTenantId(String deliveryId, String tenantId);

    long countByTenantId(String tenantId);

    long countByNotificationId(String notificationId);

    long countByRecipientIdAndStatus(String recipientId, NotificationStatus status);

    long countFailedDeliveriesByTenantId(String tenantId);
}
