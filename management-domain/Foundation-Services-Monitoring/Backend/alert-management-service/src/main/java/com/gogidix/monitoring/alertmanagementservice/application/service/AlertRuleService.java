package com.gogidix.monitoring.alertmanagementservice.application.service;

import com.gogidix.monitoring.alertmanagementservice.domain.model.AlertRule;

/**
 * Service for alert rule operations (helper class for static methods).
 */
public class AlertRuleService {

    public static AlertRule.NotificationChannel parseNotificationChannel(String value) {
        if (value == null) return null;
        try {
            return AlertRule.NotificationChannel.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
