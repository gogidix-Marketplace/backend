package com.gogidix.shared.courier.notification.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Domain Service for Notification Operations
 * Contains notification template and preference logic
 */
@Slf4j
@Service
public class NotificationDomainService {

    /**
     * Build notification message from template
     */
    public String buildMessageFromTemplate(String template, Map<String, Object> params) {
        if (template == null) {
            return "";
        }

        String message = template;
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String placeholder = "${" + entry.getKey() + "}";
                String value = entry.getValue() != null ? entry.getValue().toString() : "";
                message = message.replace(placeholder, value);
            }
        }

        return message;
    }

    /**
     * Determine notification channel based on recipient type and notification type
     */
    public String determineChannel(String recipientType, String notificationType) {
        if (recipientType == null) {
            return "push";
        }

        return switch (recipientType.toLowerCase()) {
            case "driver" -> "push";
            case "customer" -> "push";
            case "admin" -> "email";
            default -> "push";
        };
    }

    /**
     * Validate notification content
     */
    public boolean validateNotification(String title, String message) {
        if (title == null || title.isBlank()) {
            return false;
        }

        if (message == null || message.isBlank()) {
            return false;
        }

        if (title.length() > 100) {
            log.warn("Notification title too long: {}", title.length());
            return false;
        }

        if (message.length() > 500) {
            log.warn("Notification message too long: {}", message.length());
            return false;
        }

        return true;
    }

    /**
     * Get notification priority
     */
    public String determinePriority(String notificationType) {
        if (notificationType == null) {
            return "normal";
        }

        return switch (notificationType.toLowerCase()) {
            case "urgent", "emergency", "alert" -> "high";
            case "promotional", "marketing" -> "low";
            default -> "normal";
        };
    }
}
