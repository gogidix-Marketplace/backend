package com.gogidix.digitalmarketing.emailmarketing.domain.model;

import com.gogidix.digitalmarketing.shared.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EmailSubscriber - Represents a subscriber in an email list.
 *
 * <p>Subscribers are members of email lists who have opted in to receive emails.
 * They have status, preferences, and custom fields for personalization.</p>
 *
 * <p>Tenant Isolation: All queries MUST filter by tenantId</p>
 */
@Document(collection = "email_subscribers")
@TypeAlias("email_subscriber")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "subscriber_tenant_list_idx", def = "{'tenantId': 1, 'listId': 1, 'status': 1}")
@CompoundIndex(name = "subscriber_tenant_email_idx", def = "{'tenantId': 1, 'email': 1}")
public class EmailSubscriber extends BaseEntity {

    /**
     * Subscriber email address (unique per tenant)
     */
    @Indexed
    private String email;

    /**
     * Subscriber first name
     */
    private String firstName;

    /**
     * Subscriber last name
     */
    private String lastName;

    /**
     * Full name (computed)
     */
    private String fullName;

    /**
     * Email list ID this subscriber belongs to
     */
    @Indexed
    private String listId;

    /**
     * Subscriber status (ACTIVE, UNCONFIRMED, UNSUBSCRIBED, BOUNCED, COMPLAINED)
     */
    @Indexed
    private String status;

    /**
     * Confirmation token for double opt-in
     */
    private String confirmationToken;

    /**
     * Confirmation sent date
     */
    private Instant confirmationSentAt;

    /**
     * Confirmed at date
     */
    private Instant confirmedAt;

    /**
     * Unsubscribed at date
     */
    private Instant unsubscribedAt;

    /**
     * Unsubscribe reason
     */
    private String unsubscribeReason;

    /**
     * Unsubscribe method (LINK, EMAIL, API)
     */
    private String unsubscribeMethod;

    /**
     * Bounced at date
     */
    private Instant bouncedAt;

    /**
     * Bounce type (HARD, SOFT)
     */
    private String bounceType;

    /**
     * Bounce reason
     */
    private String bounceReason;

    /**
     * Complained at date (spam report)
     */
    private Instant complainedAt;

    /**
     * IP address on signup
     */
    private String signupIp;

    /**
     * User agent on signup
     */
    private String signupUserAgent;

    /**
     * Signup source (WEB, API, IMPORT, MANUAL)
     */
    private String signupSource;

    /**
     * Signup referral URL
     */
    private String referralUrl;

    /**
     * Subscriber tags
     */
    private List<String> tags;

    /**
     * Custom field values
     */
    private Map<String, Object> customFields;

    /**
     * Email preferences
     */
    private EmailPreferences preferences;

    /**
     * Total emails sent to this subscriber
     */
    private Integer emailsSent;

    /**
     * Total emails opened
     */
    private Integer emailsOpened;

    /**
     * Total emails clicked
     */
    private Integer emailsClicked;

    /**
     * Last open date
     */
    private Instant lastOpenedAt;

    /**
     * Last click date
     */
    private Instant lastClickedAt;

    /**
     * Last email sent date
     */
    private Instant lastEmailedAt;

    /**
     * Engagement score (0-100)
     */
    private Integer engagementScore;

    /**
     * Activity level (ACTIVE, INACTIVE, DORMANT)
     */
    private String activityLevel;

    /**
     * Language preference
     */
    private String language;

    /**
     * Timezone
     */
    private String timezone;

    /**
     * Country
     */
    private String country;

    /**
     * Region/State
     */
    private String region;

    /**
     * City
     */
    private String city;

    /**
     * Additional metadata
     */
    private Map<String, Object> metadata;

    /**
     * Notes about subscriber
     */
    private List<String> notes;

    /**
     * Whether subscriber wants HTML emails
     */
    @Builder.Default
    private Boolean acceptsHtml = true;

    /**
     * Email format preference (HTML, TEXT, AUTO)
     */
    private String emailFormat;

    /**
     * Email preferences inner class
     */
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailPreferences {
        private Boolean receiveNewsletters = true;

        private Boolean receivePromotional = true;

        private Boolean receiveTransactional = true;

        private Boolean receiveWeeklyDigest = false;

        private Boolean receiveProductUpdates = false;

        private Integer frequency; // 1=daily, 2=biweekly, 3=weekly, 4=monthly

        private Boolean onlyImportantUpdates = false;

        private Map<String, Boolean> customPreferences;

        // Getters and Setters
        public Boolean getReceiveNewsletters() { return receiveNewsletters; }
        public void setReceiveNewsletters(Boolean receiveNewsletters) { this.receiveNewsletters = receiveNewsletters; }
        public Boolean getReceivePromotional() { return receivePromotional; }
        public void setReceivePromotional(Boolean receivePromotional) { this.receivePromotional = receivePromotional; }
        public Boolean getReceiveTransactional() { return receiveTransactional; }
        public void setReceiveTransactional(Boolean receiveTransactional) { this.receiveTransactional = receiveTransactional; }
        public Boolean getReceiveWeeklyDigest() { return receiveWeeklyDigest; }
        public void setReceiveWeeklyDigest(Boolean receiveWeeklyDigest) { this.receiveWeeklyDigest = receiveWeeklyDigest; }
        public Boolean getReceiveProductUpdates() { return receiveProductUpdates; }
        public void setReceiveProductUpdates(Boolean receiveProductUpdates) { this.receiveProductUpdates = receiveProductUpdates; }
        public Integer getFrequency() { return frequency; }
        public void setFrequency(Integer frequency) { this.frequency = frequency; }
        public Boolean getOnlyImportantUpdates() { return onlyImportantUpdates; }
        public void setOnlyImportantUpdates(Boolean onlyImportantUpdates) { this.onlyImportantUpdates = onlyImportantUpdates; }
        public Map<String, Boolean> getCustomPreferences() { return customPreferences; }
        public void setCustomPreferences(Map<String, Boolean> customPreferences) { this.customPreferences = customPreferences; }
    }

    /**
     * Create a new subscriber for a tenant and list.
     *
     * @param tenantId the tenant ID
     * @param email    the email address
     * @param listId   the list ID
     */
    public EmailSubscriber(String tenantId, String email, String listId) {
        super(tenantId);
        this.email = email;
        this.listId = listId;
        this.status = "UNCONFIRMED";
        this.confirmationToken = generateToken();
        this.tags = new ArrayList<>();
        this.customFields = new HashMap<>();
        this.preferences = new EmailPreferences();
        this.metadata = new HashMap<>();
        this.notes = new ArrayList<>();
        this.emailsSent = 0;
        this.emailsOpened = 0;
        this.emailsClicked = 0;
        this.acceptsHtml = true;
        this.emailFormat = "AUTO";
        this.engagementScore = 50;
        this.activityLevel = "INACTIVE";
    }

    /**
     * Create a confirmed subscriber.
     *
     * @param tenantId the tenant ID
     * @param email    the email address
     * @param listId   the list ID
     * @param confirmed true if already confirmed
     */
    public EmailSubscriber(String tenantId, String email, String listId, boolean confirmed) {
        this(tenantId, email, listId);
        if (confirmed) {
            this.status = "ACTIVE";
            this.confirmedAt = Instant.now();
        }
    }

    /**
     * Check if subscriber is active.
     *
     * @return true if active
     */
    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }

    /**
     * Check if subscriber is confirmed.
     *
     * @return true if confirmed
     */
    public boolean isConfirmed() {
        return "ACTIVE".equals(this.status) || "UNSUBSCRIBED".equals(this.status) ||
            "BOUNCED".equals(this.status) || "COMPLAINED".equals(this.status);
    }

    /**
     * Check if subscriber is unsubscribed.
     *
     * @return true if unsubscribed
     */
    public boolean isUnsubscribed() {
        return "UNSUBSCRIBED".equals(this.status);
    }

    /**
     * Check if subscriber is bounced.
     *
     * @return true if bounced
     */
    public boolean isBounced() {
        return "BOUNCED".equals(this.status);
    }

    /**
     * Check if is hard bounce.
     *
     * @return true if hard bounce
     */
    public boolean isHardBounce() {
        return "BOUNCED".equals(this.status) && "HARD".equals(this.bounceType);
    }

    /**
     * Check if subscriber can receive emails.
     *
     * @return true if can receive
     */
    public boolean canReceiveEmails() {
        return "ACTIVE".equals(this.status);
    }

    /**
     * Confirm the subscriber.
     */
    public void confirm() {
        this.status = "ACTIVE";
        this.confirmedAt = Instant.now();
        this.touch();
    }

    /**
     * Unsubscribe the subscriber.
     *
     * @param reason the unsubscribe reason
     * @param method the unsubscribe method
     */
    public void unsubscribe(String reason, String method) {
        this.status = "UNSUBSCRIBED";
        this.unsubscribedAt = Instant.now();
        this.unsubscribeReason = reason;
        this.unsubscribeMethod = method;
        this.touch();
    }

    /**
     * Mark as bounced.
     *
     * @param bounceType the bounce type
     * @param reason     the bounce reason
     */
    public void markAsBounced(String bounceType, String reason) {
        this.status = "BOUNCED";
        this.bouncedAt = Instant.now();
        this.bounceType = bounceType;
        this.bounceReason = reason;
        this.touch();
    }

    /**
     * Mark as complained.
     */
    public void markAsComplained() {
        this.status = "COMPLAINED";
        this.complainedAt = Instant.now();
        this.touch();
    }

    /**
     * Record email sent.
     */
    public void recordEmailSent() {
        this.emailsSent = (this.emailsSent != null ? this.emailsSent : 0) + 1;
        this.lastEmailedAt = Instant.now();
        this.updateActivityLevel();
        this.touch();
    }

    /**
     * Record email opened.
     */
    public void recordEmailOpened() {
        this.emailsOpened = (this.emailsOpened != null ? this.emailsOpened : 0) + 1;
        this.lastOpenedAt = Instant.now();
        this.updateActivityLevel();
        this.touch();
    }

    /**
     * Record email clicked.
     */
    public void recordEmailClicked() {
        this.emailsClicked = (this.emailsClicked != null ? this.emailsClicked : 0) + 1;
        this.lastClickedAt = Instant.now();
        this.updateActivityLevel();
        this.touch();
    }

    /**
     * Update activity level based on recent engagement.
     */
    private void updateActivityLevel() {
        Instant thirtyDaysAgo = Instant.now().minusSeconds(30 * 86400L);
        Instant ninetyDaysAgo = Instant.now().minusSeconds(90 * 86400L);

        if (lastOpenedAt != null && lastOpenedAt.isAfter(thirtyDaysAgo)) {
            this.activityLevel = "ACTIVE";
        } else if (lastOpenedAt != null && lastOpenedAt.isAfter(ninetyDaysAgo)) {
            this.activityLevel = "INACTIVE";
        } else {
            this.activityLevel = "DORMANT";
        }
    }

    /**
     * Calculate engagement score.
     *
     * @return score 0-100
     */
    public int calculateEngagementScore() {
        int sent = emailsSent != null ? emailsSent : 0;
        int opened = emailsOpened != null ? emailsOpened : 0;
        int clicked = emailsClicked != null ? emailsClicked : 0;

        if (sent == 0) {
            return 50;
        }

        int score = 0;

        // Open rate contribution (0-40 points)
        double openRate = ((double) opened / sent) * 100;
        score += Math.min(40, (int) (openRate * 0.4));

        // Click rate contribution (0-40 points)
        double clickRate = ((double) clicked / sent) * 100;
        score += Math.min(40, (int) (clickRate * 0.4));

        // Recent activity bonus (0-20 points)
        Instant thirtyDaysAgo = Instant.now().minusSeconds(30 * 86400L);
        if (lastOpenedAt != null && lastOpenedAt.isAfter(thirtyDaysAgo)) {
            score += 20;
        }

        this.engagementScore = Math.max(0, Math.min(100, score));
        return this.engagementScore;
    }

    /**
     * Get display name.
     *
     * @return full name or first name or email
     */
    public String getDisplayName() {
        if (fullName != null && !fullName.isBlank()) {
            return fullName;
        }
        if (firstName != null && !firstName.isBlank()) {
            StringBuilder name = new StringBuilder(firstName);
            if (lastName != null && !lastName.isBlank()) {
                name.append(" ").append(lastName);
            }
            return name.toString();
        }
        return email;
    }

    /**
     * Get initials.
     *
     * @return initials (up to 2 characters)
     */
    public String getInitials() {
        StringBuilder initials = new StringBuilder();
        if (firstName != null && !firstName.isBlank()) {
            initials.append(firstName.charAt(0));
        }
        if (lastName != null && !lastName.isBlank()) {
            initials.append(lastName.charAt(0));
        }
        if (initials.isEmpty() && email != null && !email.isBlank()) {
            initials.append(email.charAt(0));
        }
        return initials.toString().toUpperCase();
    }

    /**
     * Add a tag.
     *
     * @param tag the tag to add
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Remove a tag.
     *
     * @param tag the tag to remove
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }

    /**
     * Set custom field value.
     *
     * @param key   the field name
     * @param value the field value
     */
    public void setCustomField(String key, Object value) {
        if (this.customFields == null) {
            this.customFields = new HashMap<>();
        }
        this.customFields.put(key, value);
    }

    /**
     * Get custom field value.
     *
     * @param key the field name
     * @return the field value, or null if not found
     */
    public Object getCustomField(String key) {
        if (this.customFields == null) {
            return null;
        }
        return this.customFields.get(key);
    }

    /**
     * Add a note.
     *
     * @param note the note to add
     */
    public void addNote(String note) {
        if (this.notes == null) {
            this.notes = new ArrayList<>();
        }
        this.notes.add(note);
    }

    /**
     * Add metadata.
     *
     * @param key   the metadata key
     * @param value the metadata value
     */
    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }

    /**
     * Generate a random confirmation token.
     *
     * @return random token
     */
    private String generateToken() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Check if confirmation token is valid.
     *
     * @param token the token to check
     * @return true if valid
     */
    public boolean isConfirmationTokenValid(String token) {
        return token != null && token.equals(this.confirmationToken);
    }

    /**
     * Get open rate.
     *
     * @return open rate percentage
     */
    public double getOpenRate() {
        int sent = emailsSent != null ? emailsSent : 0;
        if (sent == 0) {
            return 0.0;
        }
        int opened = emailsOpened != null ? emailsOpened : 0;
        return ((double) opened / sent) * 100.0;
    }

    /**
     * Get click rate.
     *
     * @return click rate percentage
     */
    public double getClickRate() {
        int sent = emailsSent != null ? emailsSent : 0;
        if (sent == 0) {
            return 0.0;
        }
        int clicked = emailsClicked != null ? emailsClicked : 0;
        return ((double) clicked / sent) * 100.0;
    }

    /**
     * Check if subscriber should receive newsletter.
     *
     * @return true if should receive
     */
    public boolean receivesNewsletter() {
        return preferences != null && Boolean.TRUE.equals(preferences.getReceiveNewsletters());
    }

    /**
     * Check if subscriber should receive promotional emails.
     *
     * @return true if should receive
     */
    public boolean receivesPromotional() {
        return preferences != null && Boolean.TRUE.equals(preferences.getReceivePromotional());
    }

    /**
     * Check if subscriber should receive transactional emails.
     *
     * @return true if should receive
     */
    public boolean receivesTransactional() {
        return preferences != null && Boolean.TRUE.equals(preferences.getReceiveTransactional());
    }

    /**
     * Update name parts and compute full name.
     *
     * @param firstName first name
     * @param lastName  last name
     */
    public void setName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        if (firstName != null && !firstName.isBlank()) {
            StringBuilder fullName = new StringBuilder(firstName);
            if (lastName != null && !lastName.isBlank()) {
                fullName.append(" ").append(lastName);
            }
            this.fullName = fullName.toString();
        } else if (lastName != null && !lastName.isBlank()) {
            this.fullName = lastName;
        } else {
            this.fullName = null;
        }
    }

    /**
     * Re-subscribe an unsubscribed user.
     */
    public void resubscribe() {
        if ("UNSUBSCRIBED".equals(this.status)) {
            this.status = "ACTIVE";
            this.unsubscribedAt = null;
            this.unsubscribeReason = null;
            this.unsubscribeMethod = null;
            this.touch();
        }
    }
}