package com.gogidix.ecommerce.pushnotification.application.query;

public record GetPushNotificationListQuery(String tenantId, int page, int size) {
}