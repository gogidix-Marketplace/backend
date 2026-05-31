package com.gogidix.hr.notification.domain.model;

import com.gogidix.hr.notification.domain.enums.NotificationChannel;
import com.gogidix.hr.notification.domain.enums.NotificationType;
import com.gogidix.hr.notification.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NotificationPreference Domain Entity
 * Manages user notification preferences
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "notification_preferences")
public class NotificationPreference extends BaseEntity {

    @Indexed(unique = true)
    private String preferenceCode;

    @Indexed
    private String tenantId;

    @Indexed
    private String userId;

    private String userName;
    private String userEmail;

    @Indexed
    private NotificationType type;

    @Indexed
    private Boolean isEnabled;

    @Builder.Default
    private Map<NotificationChannel, ChannelPreference> channelPreferences = new HashMap<>();

    @Builder.Default
    private Map<String, Object> rules = new HashMap<>();

    private LocalTime quietHoursStart;
    private LocalTime quietHoursEnd;

    @Indexed
    private List<String> weekdaysOnly;

    private Boolean receiveDigest;
    private String digestFrequency; // DAILY, WEEKLY, MONTHLY

    @Builder.Default
    private List<String> blockedSenders = new ArrayList<>();

    @Builder.Default
    private List<String> prioritySenders = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    /**
     * Channel preference
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChannelPreference {
        private Boolean enabled;
        private Boolean isPrimary;
        private String destination; // email address, phone number, etc.
        private LocalTime quietHoursStart;
        private LocalTime quietHoursEnd;
    }

    /**
     * Creates a new notification preference
     */
    public static NotificationPreference create(String tenantId, String userId,
                                                String userName, String userEmail,
                                                NotificationType type) {
        String preferenceCode = generatePreferenceCode(userId, type);

        NotificationPreference preference = new NotificationPreference();
        preference.setTenantId(tenantId);
        preference.setUserId(userId);
        preference.setUserName(userName);
        preference.setUserEmail(userEmail);
        preference.setType(type);
        preference.setPreferenceCode(preferenceCode);
        preference.setIsEnabled(true);
        preference.setReceiveDigest(false);
        preference.setChannelPreferences(new HashMap<>());
        preference.setRules(new HashMap<>());
        preference.setWeekdaysOnly(new ArrayList<>());
        preference.setBlockedSenders(new ArrayList<>());
        preference.setPrioritySenders(new ArrayList<>());
        preference.setMetadata(new HashMap<>());

        return preference;
    }

    /**
     * Enables notification
     */
    public void enable() {
        this.isEnabled = true;
    }

    /**
     * Disables notification
     */
    public void disable() {
        this.isEnabled = false;
    }

    /**
     * Sets channel preference
     */
    public void setChannelPreference(NotificationChannel channel, Boolean enabled,
                                      String destination, Boolean isPrimary) {
        if (this.channelPreferences == null) {
            this.channelPreferences = new HashMap<>();
        }
        ChannelPreference preference = new ChannelPreference();
        preference.setEnabled(enabled);
        preference.setDestination(destination);
        preference.setIsPrimary(isPrimary);
        this.channelPreferences.put(channel, preference);
    }

    /**
     * Checks if channel is enabled
     */
    public boolean isChannelEnabled(NotificationChannel channel) {
        ChannelPreference preference = this.channelPreferences.get(channel);
        return preference != null && preference.getEnabled();
    }

    /**
     * Gets primary channel
     */
    public NotificationChannel getPrimaryChannel() {
        for (Map.Entry<NotificationChannel, ChannelPreference> entry : this.channelPreferences.entrySet()) {
            if (entry.getValue() != null && entry.getValue().getIsPrimary()) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Sets quiet hours
     */
    public void setQuietHours(LocalTime start, LocalTime end) {
        this.quietHoursStart = start;
        this.quietHoursEnd = end;
    }

    /**
     * Checks if is quiet hours
     */
    public boolean isQuietHours() {
        if (this.quietHoursStart == null || this.quietHoursEnd == null) {
            return false;
        }
        LocalTime now = LocalTime.now();
        if (this.quietHoursStart.isBefore(this.quietHoursEnd)) {
            return now.isAfter(this.quietHoursStart) && now.isBefore(this.quietHoursEnd);
        } else {
            return now.isAfter(this.quietHoursStart) || now.isBefore(this.quietHoursEnd);
        }
    }

    /**
     * Sets weekdays only
     */
    public void setWeekdaysOnly(List<String> weekdays) {
        this.weekdaysOnly = weekdays;
    }

    /**
     * Enables digest
     */
    public void enableDigest(String frequency) {
        this.receiveDigest = true;
        this.digestFrequency = frequency;
    }

    /**
     * Disables digest
     */
    public void disableDigest() {
        this.receiveDigest = false;
        this.digestFrequency = null;
    }

    /**
     * Adds blocked sender
     */
    public void addBlockedSender(String senderId) {
        if (this.blockedSenders == null) {
            this.blockedSenders = new ArrayList<>();
        }
        if (!this.blockedSenders.contains(senderId)) {
            this.blockedSenders.add(senderId);
        }
    }

    /**
     * Removes blocked sender
     */
    public void removeBlockedSender(String senderId) {
        if (this.blockedSenders != null) {
            this.blockedSenders.remove(senderId);
        }
    }

    /**
     * Adds priority sender
     */
    public void addPrioritySender(String senderId) {
        if (this.prioritySenders == null) {
            this.prioritySenders = new ArrayList<>();
        }
        if (!this.prioritySenders.contains(senderId)) {
            this.prioritySenders.add(senderId);
        }
    }

    /**
     * Checks if sender is blocked
     */
    public boolean isSenderBlocked(String senderId) {
        return this.blockedSenders != null && this.blockedSenders.contains(senderId);
    }

    /**
     * Checks if sender is priority
     */
    public boolean isSenderPriority(String senderId) {
        return this.prioritySenders != null && this.prioritySenders.contains(senderId);
    }

    /**
     * Sets rule
     */
    public void setRule(String key, Object value) {
        if (this.rules == null) {
            this.rules = new HashMap<>();
        }
        this.rules.put(key, value);
    }

    /**
     * Generates preference code
     */
    private static String generatePreferenceCode(String userId, NotificationType type) {
        return "PRF-" + userId.substring(0, 8) + "-" + type.name().substring(0, 3).toUpperCase();
    }

    @Builder.Default
    private List<Object> domainEvents = new ArrayList<>();

    public void addDomainEvent(Object event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }
}
