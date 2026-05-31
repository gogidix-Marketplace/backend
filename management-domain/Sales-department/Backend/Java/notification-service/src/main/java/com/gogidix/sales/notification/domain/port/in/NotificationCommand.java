package com.gogidix.sales.notification.domain.port.in;

import com.gogidix.sales.notification.domain.model.Notification;
import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;
import com.gogidix.sales.notification.domain.valueobject.NotificationPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Notification Commands (Input Port)
 * Defines the input commands for notification operations
 */
public interface NotificationCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateNotificationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "User ID is required")
        private String userId;

        @NotEmpty(message = "At least one recipient is required")
        private List<Notification.RecipientInfo> recipients;

        @NotNull(message = "Channel is required")
        private NotificationChannel channel;

        private String subject;

        private String content;

        private String htmlContent;

        private String templateId;

        private Map<String, Object> templateVariables;

        private NotificationPriority priority;

        private String category;

        private String actionType;

        private String actionUrl;

        private Instant scheduledAt;

        private Instant expiresAt;

        private String groupId;

        private Boolean isBatched;

        private Map<String, String> metadata;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SendNotificationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Notification ID is required")
        private String notificationId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsReadCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Notification ID is required")
        private String notificationId;

        @NotBlank(message = "User ID is required")
        private String userId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAllAsReadCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "User ID is required")
        private String userId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ScheduleNotificationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Notification ID is required")
        private String notificationId;

        @NotNull(message = "Scheduled time is required")
        private Instant scheduledAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CancelNotificationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Notification ID is required")
        private String notificationId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ArchiveNotificationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Notification ID is required")
        private String notificationId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteNotificationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Notification ID is required")
        private String notificationId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsDeliveredCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Notification ID is required")
        private String notificationId;

        private String externalMessageId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsFailedCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Notification ID is required")
        private String notificationId;

        @NotBlank(message = "Error message is required")
        private String errorMessage;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class BatchSendCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotEmpty(message = "At least one notification ID is required")
        private List<String> notificationIds;
    }
}
