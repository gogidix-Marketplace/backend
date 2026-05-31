package com.gogidix.centralconfiguration.notificationservice.application.service;

import com.gogidix.centralconfiguration.notificationservice.domain.model.Notification;
import com.gogidix.centralconfiguration.notificationservice.domain.model.NotificationChannel;
import com.gogidix.centralconfiguration.notificationservice.domain.model.NotificationType;
import com.gogidix.centralconfiguration.notificationservice.domain.repository.NotificationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for Notification operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    @PersistenceContext
    private EntityManager entityManager;

    private static final String DEFAULT_TENANT_ID = "default";

    /**
     * Create and send notification
     */
    @Transactional
    public Notification sendNotification(String tenantId, NotificationType type, NotificationChannel channel,
                                        String recipient, String subject, String message, String metadata) {
        log.info("Sending notification: tenantId={}, type={}, channel={}", tenantId, type, channel);

        String effectiveTenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        Notification notification = Notification.builder()
                .tenantId(effectiveTenantId)
                .notificationType(type)
                .channel(channel)
                .recipient(recipient)
                .subject(subject)
                .message(message)
                .metadata(metadata)
                .status(com.gogidix.centralconfiguration.notificationservice.domain.model.NotificationStatus.PENDING)
                .build();

        notification = notificationRepository.save(notification);

        // Send notification based on channel
        try {
            sendNotificationInternal(notification);
            notification.markAsSent();
            log.info("Notification sent successfully: id={}", notification.getId());
        } catch (Exception e) {
            notification.markAsFailed(e.getMessage());
            log.error("Failed to send notification: id={}", notification.getId(), e);
        }

        return notificationRepository.save(notification);
    }

    /**
     * Send notification based on channel type
     */
    private void sendNotificationInternal(Notification notification) {
        switch (notification.getChannel()) {
            case EMAIL:
                sendEmail(notification);
                break;
            case WEBHOOK:
                sendWebhook(notification);
                break;
            case SLACK:
                sendSlack(notification);
                break;
            default:
                throw new IllegalArgumentException("Unsupported notification channel: " + notification.getChannel());
        }
    }

    /**
     * Send email notification
     */
    private void sendEmail(Notification notification) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(notification.getRecipient());
        message.setSubject(notification.getSubject());
        message.setText(notification.getMessage());
        mailSender.send(message);
    }

    /**
     * Send webhook notification
     */
    private void sendWebhook(Notification notification) {
        // TODO: Implement webhook sending logic
        log.debug("Sending webhook to: {}", notification.getRecipient());
    }

    /**
     * Send Slack notification
     */
    private void sendSlack(Notification notification) {
        // TODO: Implement Slack notification logic
        log.debug("Sending Slack notification to: {}", notification.getRecipient());
    }

    /**
     * Get all notifications for tenant
     */
    public List<Notification> getNotifications(String tenantId) {
        String effectiveTenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;
        return notificationRepository.findByTenantId(effectiveTenantId);
    }

    /**
     * Retry failed notifications
     */
    @Transactional
    public void retryFailedNotifications(String tenantId) {
        String effectiveTenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        List<Notification> failedNotifications = notificationRepository
                .findByTenantIdAndStatus(effectiveTenantId,
                        com.gogidix.centralconfiguration.notificationservice.domain.model.NotificationStatus.FAILED);

        for (Notification notification : failedNotifications) {
            if (notification.canRetry()) {
                log.info("Retrying notification: id={}", notification.getId());
                try {
                    sendNotificationInternal(notification);
                    notification.markAsSent();
                    notificationRepository.save(notification);
                } catch (Exception e) {
                    notification.incrementRetry();
                    notification.markAsFailed(e.getMessage());
                    notificationRepository.save(notification);
                }
            }
        }
    }
}
