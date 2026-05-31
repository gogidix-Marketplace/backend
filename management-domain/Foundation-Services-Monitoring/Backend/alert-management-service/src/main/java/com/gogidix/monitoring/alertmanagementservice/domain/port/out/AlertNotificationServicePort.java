package com.gogidix.monitoring.alertmanagementservice.domain.port.out;

import com.gogidix.monitoring.alertmanagementservice.domain.model.Alert;
import com.gogidix.monitoring.alertmanagementservice.domain.model.AlertRule;

import java.util.List;

/**
 * Output port for alert notification service.
 */
public interface AlertNotificationServicePort {

    /**
     * Send a notification for an alert.
     *
     * @param alert    the alert
     * @param channel  the notification channel
     * @param recipients the recipients
     */
    void sendNotification(Alert alert, AlertRule.NotificationChannel channel, List<String> recipients);

    /**
     * Send batch notifications.
     *
     * @param alerts the alerts to notify
     */
    void sendBatchNotifications(List<Alert> alerts);
}
