package com.gogidix.sales.notification.domain.port.out;

import com.gogidix.sales.notification.domain.model.Notification;

/**
 * Notification Sender (Output Port)
 * Defines the contract for sending notifications through various channels
 */
public interface NotificationSender {

    /**
     * Sends a notification through the appropriate channel
     * @param notification The notification to send
     * @return The external message ID if available
     */
    String send(Notification notification);

    /**
     * Checks if the sender is ready to send notifications
     * @return true if ready, false otherwise
     */
    boolean isReady();

    /**
     * Checks if a specific channel is enabled
     * @param channel The channel to check
     * @return true if enabled, false otherwise
     */
    boolean isChannelEnabled(String channel);
}
