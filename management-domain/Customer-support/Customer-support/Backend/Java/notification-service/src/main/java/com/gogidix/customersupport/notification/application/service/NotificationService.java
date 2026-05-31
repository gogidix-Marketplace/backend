package com.gogidix.customersupport.notification.application.service;

import com.gogidix.customersupport.notification.application.dto.NotificationDto;
import com.gogidix.customersupport.notification.application.mapper.NotificationMapper;
import com.gogidix.customersupport.notification.domain.model.Notification;
import com.gogidix.customersupport.notification.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for Notification operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper mapper;

    private static final String DEFAULT_TENANT_ID = "default";

    /**
     * Send notification
     */
    @Transactional
    @CacheEvict(value = "notifications", allEntries = true)
    public NotificationDto sendNotification(NotificationDto.SendNotificationRequest request) {
        log.info("Sending notification for tenant: {}, type: {}, recipient: {}",
                request.getTenantId(), request.getType(), request.getRecipientId());

        String tenantId = request.getTenantId() != null ? request.getTenantId() : DEFAULT_TENANT_ID;

        Notification notification = Notification.create(
                tenantId,
                Notification.NotificationType.valueOf(request.getType()),
                request.getRecipientId(),
                Notification.NotificationChannel.valueOf(request.getChannel()),
                request.getSubject(),
                request.getContent()
        );

        notification.setRecipientEmail(request.getRecipientEmail());
        notification.setRecipientPhone(request.getRecipientPhone());
        notification.setTemplateId(request.getTemplateId());
        notification.setTemplateData(request.getTemplateData());

        if (request.getPriority() != null) {
            notification.setPriority(Notification.NotificationPriority.valueOf(request.getPriority()));
        }

        if (request.getScheduledAt() != null) {
            notification.setScheduledAt(request.getScheduledAt());
        }

        if (request.getRelatedEntityType() != null) {
            notification.setRelatedEntityType(request.getRelatedEntityType());
            notification.setRelatedEntityId(request.getRelatedEntityId());
        }

        notification.setMetadata(request.getMetadata());

        Notification saved = notificationRepository.save(notification);

        // In production, this would trigger the actual sending mechanism
        // For now, we'll mark it as sent
        if (notification.getScheduledAt() == null || notification.getScheduledAt().isBefore(Instant.now())) {
            processNotification(saved);
        }

        log.info("Notification sent with ID: {}", saved.getNotificationId());

        return mapper.toDto(saved);
    }

    /**
     * Send bulk notifications
     */
    @Transactional
    @CacheEvict(value = "notifications", allEntries = true)
    public List<NotificationDto> sendBulkNotifications(NotificationDto.BulkNotificationRequest request) {
        log.info("Sending bulk notifications for {} recipients", request.getRecipientIds().size());

        String tenantId = request.getTenantId() != null ? request.getTenantId() : DEFAULT_TENANT_ID;

        return request.getRecipientIds().stream()
                .map(recipientId -> {
                    NotificationDto.SendNotificationRequest singleRequest = new NotificationDto.SendNotificationRequest();
                    singleRequest.setTenantId(tenantId);
                    singleRequest.setType(request.getType());
                    singleRequest.setRecipientId(recipientId);
                    singleRequest.setChannel(request.getChannel());
                    singleRequest.setSubject(request.getSubject());
                    singleRequest.setContent(request.getContent());
                    singleRequest.setPriority(request.getPriority());
                    singleRequest.setTemplateId(request.getTemplateId());
                    singleRequest.setTemplateData(request.getTemplateData());
                    return sendNotification(singleRequest);
                })
                .collect(Collectors.toList());
    }

    /**
     * Get notification by ID
     */
    @Cacheable(value = "notifications", key = "#notificationId")
    public NotificationDto getNotificationById(String notificationId) {
        log.debug("Fetching notification: {}", notificationId);

        return notificationRepository.findByNotificationId(notificationId)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found: " + notificationId));
    }

    /**
     * Get notifications by recipient
     */
    public List<NotificationDto> getNotificationsByRecipient(String tenantId, String recipientId, String status, String channel) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        List<Notification> notifications;

        if (status != null && channel != null) {
            notifications = notificationRepository.findByTenantIdAndRecipientIdAndStatusAndChannel(
                    tenantId, recipientId, Notification.NotificationStatus.valueOf(status),
                    Notification.NotificationChannel.valueOf(channel));
        } else if (status != null) {
            notifications = notificationRepository.findByTenantIdAndRecipientIdAndStatusOrderByCreatedAtDesc(
                    tenantId, recipientId, Notification.NotificationStatus.valueOf(status));
        } else if (channel != null) {
            notifications = notificationRepository.findByTenantIdAndRecipientIdAndChannelOrderByCreatedAtDesc(
                    tenantId, recipientId, Notification.NotificationChannel.valueOf(channel));
        } else {
            notifications = notificationRepository.findByTenantIdAndRecipientIdOrderByCreatedAtDesc(
                    tenantId, recipientId);
        }

        return notifications.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get notifications by status
     */
    public List<NotificationDto> getNotificationsByStatus(String tenantId, String status) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;
        Notification.NotificationStatus notificationStatus = Notification.NotificationStatus.valueOf(status);

        return notificationRepository.findByTenantIdAndStatusOrderByCreatedAtDesc(tenantId, notificationStatus)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get pending notifications
     */
    public List<NotificationDto> getPendingNotifications(String tenantId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        return notificationRepository.findByTenantIdAndStatusOrderByCreatedAtDesc(
                        tenantId, Notification.NotificationStatus.PENDING)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get failed notifications
     */
    public List<NotificationDto> getFailedNotifications(String tenantId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        return notificationRepository.findByTenantIdAndStatusOrderByCreatedAtDesc(
                        tenantId, Notification.NotificationStatus.FAILED)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get notifications by date range
     */
    public List<NotificationDto> getNotificationsByDateRange(String tenantId, Instant startDate, Instant endDate) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        return notificationRepository.findByTenantIdAndCreatedAtBetweenOrderByCreatedAtDesc(
                        tenantId, startDate, endDate)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Mark notification as read
     */
    @Transactional
    @CacheEvict(value = "notifications", allEntries = true)
    public NotificationDto markAsRead(String notificationId) {
        log.info("Marking notification as read: {}", notificationId);

        Notification notification = notificationRepository.findByNotificationId(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found: " + notificationId));

        notification.markAsRead();
        Notification saved = notificationRepository.save(notification);

        return mapper.toDto(saved);
    }

    /**
     * Mark notification as delivered
     */
    @Transactional
    @CacheEvict(value = "notifications", allEntries = true)
    public NotificationDto markAsDelivered(String notificationId) {
        log.info("Marking notification as delivered: {}", notificationId);

        Notification notification = notificationRepository.findByNotificationId(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found: " + notificationId));

        notification.markAsDelivered();
        Notification saved = notificationRepository.save(notification);

        return mapper.toDto(saved);
    }

    /**
     * Retry failed notification
     */
    @Transactional
    @CacheEvict(value = "notifications", allEntries = true)
    public NotificationDto retryNotification(String notificationId) {
        log.info("Retrying notification: {}", notificationId);

        Notification notification = notificationRepository.findByNotificationId(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found: " + notificationId));

        if (!notification.canRetry()) {
            throw new IllegalArgumentException("Notification cannot be retried. Max retries exceeded.");
        }

        notification.incrementRetry();
        notification.setStatus(Notification.NotificationStatus.PENDING);
        notification.setFailureReason(null);

        Notification saved = notificationRepository.save(notification);

        // Process the notification
        processNotification(saved);

        return mapper.toDto(saved);
    }

    /**
     * Cancel notification
     */
    @Transactional
    @CacheEvict(value = "notifications", allEntries = true)
    public NotificationDto cancelNotification(String notificationId) {
        log.info("Cancelling notification: {}", notificationId);

        Notification notification = notificationRepository.findByNotificationId(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found: " + notificationId));

        notification.setStatus(Notification.NotificationStatus.CANCELLED);
        notification.updateTimestamp();

        Notification saved = notificationRepository.save(notification);
        return mapper.toDto(saved);
    }

    /**
     * Delete notification
     */
    @Transactional
    @CacheEvict(value = "notifications", allEntries = true)
    public void deleteNotification(String notificationId) {
        log.info("Deleting notification: {}", notificationId);

        Notification notification = notificationRepository.findByNotificationId(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found: " + notificationId));

        notificationRepository.delete(notification);
        log.info("Deleted notification: {}", notificationId);
    }

    /**
     * Get notification statistics
     */
    public NotificationStatisticsDto getNotificationStatistics(String tenantId, Instant startDate, Instant endDate) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        List<Notification> notifications = notificationRepository.findByTenantIdAndCreatedAtBetweenOrderByCreatedAtDesc(
                tenantId, startDate, endDate);

        NotificationStatisticsDto stats = new NotificationStatisticsDto();
        stats.setTotalNotifications((long) notifications.size());
        stats.setSentNotifications(notifications.stream().filter(n -> Notification.NotificationStatus.SENT.equals(n.getStatus())).count());
        stats.setDeliveredNotifications(notifications.stream().filter(n -> Notification.NotificationStatus.DELIVERED.equals(n.getStatus())).count());
        stats.setReadNotifications(notifications.stream().filter(n -> Notification.NotificationStatus.READ.equals(n.getStatus())).count());
        stats.setFailedNotifications(notifications.stream().filter(n -> Notification.NotificationStatus.FAILED.equals(n.getStatus())).count());
        stats.setPendingNotifications(notifications.stream().filter(n -> Notification.NotificationStatus.PENDING.equals(n.getStatus())).count());

        // Calculate success rate
        long totalProcessed = stats.getSentNotifications() + stats.getDeliveredNotifications() + stats.getReadNotifications();
        if (totalProcessed > 0) {
            stats.setSuccessRate((double) (stats.getDeliveredNotifications() + stats.getReadNotifications()) / totalProcessed * 100.0);
        }

        return stats;
    }

    /**
     * Process notification (simulate sending)
     */
    private void processNotification(Notification notification) {
        // In production, this would integrate with actual email/SMS/push providers
        notification.markAsSent();

        // Simulate delivery
        try {
            Thread.sleep(100);
            notification.markAsDelivered();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        notificationRepository.save(notification);
    }

    /**
     * Statistics DTO
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class NotificationStatisticsDto {
        private Long totalNotifications;
        private Long sentNotifications;
        private Long deliveredNotifications;
        private Long readNotifications;
        private Long failedNotifications;
        private Long pendingNotifications;
        private Double successRate;
    }
}
