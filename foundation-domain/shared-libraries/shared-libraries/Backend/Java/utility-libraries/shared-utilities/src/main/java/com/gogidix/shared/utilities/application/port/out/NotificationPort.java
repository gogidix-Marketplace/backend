package com.gogidix.shared.utilities.application.port.out;

/**
 * Port for sending notifications
 * Defines the contract for notifying about utility operation results
 */
public interface NotificationPort {

    /**
     * Send success notification
     * 
     * @param userId User ID to notify
     * @param message Success message
     */
    void sendSuccessNotification(String userId, String message);

    /**
     * Send error notification
     * 
     * @param userId User ID to notify
     * @param message Error message
     * @param errorDetails Error details
     */
    void sendErrorNotification(String userId, String message, String errorDetails);

    /**
     * Send warning notification
     * 
     * @param userId User ID to notify
     * @param message Warning message
     * @param warnings List of warnings
     */
    void sendWarningNotification(String userId, String message, java.util.List<String> warnings);

    /**
     * Send system notification to administrators
     * 
     * @param level Notification level (INFO, WARN, ERROR)
     * @param message System message
     * @param metadata Additional metadata
     */
    void sendSystemNotification(String level, String message, java.util.Map<String, Object> metadata);
}