package com.gogidix.shared.courier.notification.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Command to send a notification
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationCommand {

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    @NotBlank(message = "Recipient ID is required")
    private String recipientId;

    @NotBlank(message = "Recipient type is required")
    private String recipientType;

    @NotBlank(message = "Notification type is required")
    private String notificationType;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Message is required")
    private String message;

    private String actionUrl;
    private String actionLabel;
    private Map<String, Object> metadata;
    private String priority;
    private String channel;
}
