package com.gogidix.shared.courier.notification.application.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command to mark notification as read
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkAsReadCommand {

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    @NotBlank(message = "Notification ID is required")
    private String notificationId;
}
