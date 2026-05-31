package com.gogidix.sales.notification.domain.port.in;

import com.gogidix.sales.notification.domain.model.NotificationPreference;
import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;
import com.gogidix.sales.notification.domain.valueobject.NotificationPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Notification Preference Commands (Input Port)
 * Defines the input commands for preference operations
 */
public interface NotificationPreferenceCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreatePreferenceCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "User ID is required")
        private String userId;

        private Map<NotificationChannel, NotificationPreference.ChannelPreference> channelPreferences;

        private Boolean enableNotifications;

        private Boolean enableDigest;

        private String digestFrequency;

        private Instant digestTime;

        private String timeZone;

        private NotificationPriority minPriority;

        private Boolean quietHoursEnabled;

        private Instant quietHoursStart;

        private Instant quietHoursEnd;

        private Map<String, NotificationPreference.CategoryPreference> categoryPreferences;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdatePreferenceCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "User ID is required")
        private String userId;

        private Map<NotificationChannel, NotificationPreference.ChannelPreference> channelPreferences;

        private Boolean enableNotifications;

        private Boolean enableDigest;

        private String digestFrequency;

        private Instant digestTime;

        private String timeZone;

        private NotificationPriority minPriority;

        private Boolean quietHoursEnabled;

        private Instant quietHoursStart;

        private Instant quietHoursEnd;

        private Map<String, NotificationPreference.CategoryPreference> categoryPreferences;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class EnableChannelCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "User ID is required")
        private String userId;

        @NotNull(message = "Channel is required")
        private NotificationChannel channel;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DisableChannelCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "User ID is required")
        private String userId;

        @NotNull(message = "Channel is required")
        private NotificationChannel channel;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SetQuietHoursCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "User ID is required")
        private String userId;

        @NotNull(message = "Start time is required")
        private Instant quietHoursStart;

        @NotNull(message = "End time is required")
        private Instant quietHoursEnd;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeletePreferenceCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "User ID is required")
        private String userId;
    }
}
