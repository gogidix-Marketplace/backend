package com.gogidix.centralconfiguration.notificationservice.domain.model;

/**
 * Notification Channel enumeration.
 */
public enum NotificationChannel {
    EMAIL("email", "Email notification"),
    WEBHOOK("webhook", "Webhook notification"),
    SLACK("slack", "Slack notification"),
    SMS("sms", "SMS notification"),
    TEAMS("teams", "Microsoft Teams notification");

    private final String code;
    private final String description;

    NotificationChannel(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
