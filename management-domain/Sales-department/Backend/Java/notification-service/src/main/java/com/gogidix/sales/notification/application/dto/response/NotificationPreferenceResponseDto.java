package com.gogidix.sales.notification.application.dto.response;

import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;
import com.gogidix.sales.notification.domain.valueobject.NotificationPriority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Notification Preference Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreferenceResponseDto {

    private String id;
    private String preferenceId;
    private String tenantId;
    private String userId;
    private Map<NotificationChannel, ChannelPreferenceDto> channelPreferences;
    private Boolean enableNotifications;
    private Boolean enableDigest;
    private String digestFrequency;
    private Instant digestTime;
    private String timeZone;
    private NotificationPriority minPriority;
    private Boolean quietHoursEnabled;
    private Instant quietHoursStart;
    private Instant quietHoursEnd;
    private Map<String, CategoryPreferenceDto> categoryPreferences;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChannelPreferenceDto {
        private Boolean enabled;
        private Boolean allowBatching;
        private Integer maxPerHour;
        private Integer maxPerDay;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryPreferenceDto {
        private Boolean enabled;
        private NotificationChannel[] channels;
        private Boolean immediate;
    }
}
