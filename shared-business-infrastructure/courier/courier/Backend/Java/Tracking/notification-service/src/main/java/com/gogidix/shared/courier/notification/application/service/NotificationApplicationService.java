package com.gogidix.shared.courier.notification.application.service;

import com.gogidix.shared.courier.notification.application.command.MarkAsReadCommand;
import com.gogidix.shared.courier.notification.application.command.SendNotificationCommand;
import com.gogidix.shared.courier.notification.application.dto.NotificationDTO;
import com.gogidix.shared.courier.notification.application.mapper.NotificationDtoMapper;
import com.gogidix.shared.courier.notification.application.query.NotificationQuery;
import com.gogidix.shared.courier.notification.domain.entity.Notification;
import com.gogidix.shared.courier.notification.domain.events.NotificationSentEvent;
import com.gogidix.shared.courier.notification.domain.repository.NotificationRepository;
import com.gogidix.shared.courier.notification.infrastructure.messaging.NotificationEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Application Service for Notification Operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationApplicationService {

    private final NotificationRepository notificationRepository;
    private final NotificationDtoMapper dtoMapper;
    private final NotificationEventPublisher eventPublisher;

    /**
     * Send a notification
     */
    @Transactional
    public NotificationDTO sendNotification(SendNotificationCommand command) {
        log.info("Sending notification to recipient: {} of type: {}",
                command.getRecipientId(), command.getNotificationType());

        Notification notification = dtoMapper.toEntity(command);
        notification = notificationRepository.save(notification);

        NotificationSentEvent event = NotificationSentEvent.builder()
                .tenantId(notification.getTenantId())
                .notificationId(notification.getNotificationId())
                .recipientId(notification.getRecipientId())
                .recipientType(notification.getRecipientType())
                .notificationType(notification.getNotificationType())
                .channel(notification.getChannel())
                .sentAt(notification.getSentAt())
                .build();
        eventPublisher.publishNotificationSent(event);

        return dtoMapper.toDTO(notification);
    }

    /**
     * Send bulk notifications
     */
    @Transactional
    public List<NotificationDTO> sendBulkNotifications(List<SendNotificationCommand> commands) {
        log.info("Sending bulk notifications: {} recipients", commands.size());

        List<Notification> notifications = commands.stream()
                .map(dtoMapper::toEntity)
                .toList();

        notifications = notificationRepository.saveAll(notifications);

        notifications.forEach(notification -> {
            NotificationSentEvent event = NotificationSentEvent.builder()
                    .tenantId(notification.getTenantId())
                    .notificationId(notification.getNotificationId())
                    .recipientId(notification.getRecipientId())
                    .recipientType(notification.getRecipientType())
                    .notificationType(notification.getNotificationType())
                    .channel(notification.getChannel())
                    .sentAt(notification.getSentAt())
                    .build();
            eventPublisher.publishNotificationSent(event);
        });

        return notifications.stream()
                .map(dtoMapper::toDTO)
                .toList();
    }

    /**
     * Mark notification as read
     */
    @Transactional
    public void markAsRead(MarkAsReadCommand command) {
        log.info("Marking notification as read: {}", command.getNotificationId());

        Notification notification = notificationRepository
                .findByTenantIdAndNotificationId(
                        command.getTenantId(),
                        command.getNotificationId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Notification not found: " + command.getNotificationId()));

        notification.setStatus(Notification.NotificationStatus.READ);
        notification.setReadAt(java.time.LocalDateTime.now());
        notificationRepository.save(notification);
    }

    /**
     * Mark all notifications as read for recipient
     */
    @Transactional
    public void markAllAsRead(String tenantId, String recipientId) {
        log.info("Marking all notifications as read for recipient: {}", recipientId);

        List<Notification> unreadNotifications = notificationRepository
                .findByTenantIdAndRecipientIdAndStatus(
                        tenantId,
                        recipientId,
                        Notification.NotificationStatus.SENT);

        unreadNotifications.forEach(notification -> {
            notification.setStatus(Notification.NotificationStatus.READ);
            notification.setReadAt(java.time.LocalDateTime.now());
        });

        notificationRepository.saveAll(unreadNotifications);
    }

    /**
     * Get notifications for recipient
     */
    public Page<NotificationDTO> getNotifications(NotificationQuery query) {
        Sort sort = Sort.by(
                "desc".equalsIgnoreCase(query.getSortDirection())
                        ? Sort.Direction.DESC : Sort.Direction.ASC,
                query.getSortBy() != null ? query.getSortBy() : "sentAt");

        Pageable pageable = PageRequest.of(
                query.getPage() != null ? query.getPage() : 0,
                query.getSize() != null ? query.getSize() : 50,
                sort);

        return notificationRepository
                .findByTenantIdAndRecipientIdAndRecipientType(
                        query.getTenantId(),
                        query.getRecipientId(),
                        query.getRecipientType(),
                        pageable)
                .map(dtoMapper::toDTO);
    }

    /**
     * Get unread count for recipient
     */
    public Long getUnreadCount(String tenantId, String recipientId) {
        return notificationRepository.countByTenantIdAndRecipientIdAndStatus(
                tenantId,
                recipientId,
                Notification.NotificationStatus.SENT);
    }

    /**
     * Get notification by ID
     */
    public java.util.Optional<NotificationDTO> getNotification(String tenantId, String notificationId) {
        return notificationRepository
                .findByTenantIdAndNotificationId(tenantId, notificationId)
                .map(dtoMapper::toDTO);
    }
}
