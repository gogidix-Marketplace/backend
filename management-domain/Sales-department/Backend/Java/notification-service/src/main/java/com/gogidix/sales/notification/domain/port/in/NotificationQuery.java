package com.gogidix.sales.notification.domain.port.in;

/**
 * Notification Query (Input Port)
 * Defines the query operations for notifications
 */
public interface NotificationQuery {

    Object getById(String notificationId);

    Object getAllForTenant();

    Object getAllForUser(String userId);

    Object getUnreadForUser(String userId);

    Object getByStatus(String status);

    Object getByChannel(String channel);

    Object getByDateRange(String startDate, String endDate);

    Object search(String searchTerm);

    Object getDeliveries(String notificationId);

    Object getDeliveryHistory(String recipientId);
}
