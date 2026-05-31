package com.gogidix.ecommerce.pushnotification.application.command;

public record UpdatePushNotificationCommand(String tenantId, String id, String name) {
}