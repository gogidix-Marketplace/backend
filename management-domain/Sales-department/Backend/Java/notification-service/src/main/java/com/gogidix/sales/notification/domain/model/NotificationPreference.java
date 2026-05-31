package com.gogidix.sales.notification.domain.model;

import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;
import com.gogidix.sales.notification.domain.valueobject.NotificationPriority;
import com.gogidix.sales.notification.shared.base.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Notification Preference Domain Entity
 * Multi-tenant user notification preferences
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "notification_preferences")
@CompoundIndex(def = "{'tenantId': 1, 'userId': 1}", unique = true)
public class NotificationPreference extends AuditableEntity {

    @Indexed
    private String preferenceId;

    @Indexed
    private String tenantId;

    @Indexed
    private String userId;

    private Map<NotificationChannel, ChannelPreference> channelPreferences;

    private Boolean enableNotifications;

    private Boolean enableDigest;

    private String digestFrequency; // HOURLY, DAILY, WEEKLY

    private Instant digestTime;

    private String timeZone;

    private NotificationPriority minPriority;

    private Boolean quietHoursEnabled;

    private Instant quietHoursStart;

    private Instant quietHoursEnd;

    private Map<String, CategoryPreference> categoryPreferences;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChannelPreference {
        private Boolean enabled;
        private Boolean allowBatching;
        private Integer maxPerHour;
        private Integer maxPerDay;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryPreference {
        private Boolean enabled;
        private NotificationChannel[] channels;
        private Boolean immediate;
    }

    /**
     * Creates a new notification preference with defaults
     */
    public static NotificationPreference createDefault(String tenantId, String userId) {
        Map<NotificationChannel, ChannelPreference> channelPreferences = new HashMap<>();
        channelPreferences.put(NotificationChannel.EMAIL, ChannelPreference.builder()
                .enabled(true)
                .allowBatching(true)
                .maxPerHour(10)
                .maxPerDay(100)
                .build());
        channelPreferences.put(NotificationChannel.SMS, ChannelPreference.builder()
                .enabled(false)
                .allowBatching(false)
                .maxPerHour(5)
                .maxPerDay(20)
                .build());
        channelPreferences.put(NotificationChannel.PUSH_NOTIFICATION, ChannelPreference.builder()
                .enabled(true)
                .allowBatching(true)
                .maxPerHour(20)
                .maxPerDay(200)
                .build());
        channelPreferences.put(NotificationChannel.IN_APP, ChannelPreference.builder()
                .enabled(true)
                .allowBatching(false)
                .maxPerHour(null)
                .maxPerDay(null)
                .build());

        return NotificationPreference.builder()
                .preferenceId(java.util.UUID.randomUUID().toString())
                .tenantId(tenantId)
                .userId(userId)
                .channelPreferences(channelPreferences)
                .enableNotifications(true)
                .enableDigest(false)
                .digestFrequency("DAILY")
                .timeZone("UTC")
                .minPriority(NotificationPriority.NORMAL)
                .quietHoursEnabled(false)
                .categoryPreferences(new HashMap<>())
                .build();
    }

    /**
     * Checks if a channel is enabled for the user
     */
    public boolean isChannelEnabled(NotificationChannel channel) {
        if (!enableNotifications || channelPreferences == null) {
            return false;
        }
        ChannelPreference preference = channelPreferences.get(channel);
        return preference != null && preference.getEnabled();
    }

    /**
     * Checks if batching is allowed for a channel
     */
    public boolean isBatchingAllowed(NotificationChannel channel) {
        if (channelPreferences == null) {
            return false;
        }
        ChannelPreference preference = channelPreferences.get(channel);
        return preference != null && preference.getAllowBatching();
    }

    /**
     * Checks if a category is enabled
     */
    public boolean isCategoryEnabled(String category) {
        if (categoryPreferences == null) {
            return true;
        }
        CategoryPreference preference = categoryPreferences.get(category);
        return preference == null || preference.getEnabled();
    }

    /**
     * Checks if notifications are allowed during quiet hours
     */
    public boolean isWithinQuietHours() {
        if (!quietHoursEnabled || quietHoursStart == null || quietHoursEnd == null) {
            return false;
        }

        Instant now = Instant.now();
        // Simple check - in production, you'd need proper time zone handling
        return now.isAfter(quietHoursStart) && now.isBefore(quietHoursEnd);
    }

    /**
     * Checks if a notification should be sent based on priority
     */
    public boolean meetsMinPriority(NotificationPriority priority) {
        return priority != null && priority.getLevel() >= minPriority.getLevel();
    }

    /**
     * Enables a channel
     */
    public void enableChannel(NotificationChannel channel) {
        ensureChannelPreference(channel).setEnabled(true);
    }

    /**
     * Disables a channel
     */
    public void disableChannel(NotificationChannel channel) {
        ensureChannelPreference(channel).setEnabled(false);
    }

    /**
     * Sets quiet hours
     */
    public void setQuietHours(Instant start, Instant end) {
        this.quietHoursEnabled = true;
        this.quietHoursStart = start;
        this.quietHoursEnd = end;
    }

    /**
     * Disables quiet hours
     */
    public void disableQuietHours() {
        this.quietHoursEnabled = false;
        this.quietHoursStart = null;
        this.quietHoursEnd = null;
    }

    private ChannelPreference ensureChannelPreference(NotificationChannel channel) {
        if (channelPreferences == null) {
            channelPreferences = new HashMap<>();
        }
        return channelPreferences.computeIfAbsent(channel,
            k -> ChannelPreference.builder().enabled(true).allowBatching(false).build());
    }
}
