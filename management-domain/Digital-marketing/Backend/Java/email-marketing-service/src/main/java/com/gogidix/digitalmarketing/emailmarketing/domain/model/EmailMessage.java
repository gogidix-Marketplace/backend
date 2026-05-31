package com.gogidix.digitalmarketing.emailmarketing.domain.model;

import com.gogidix.digitalmarketing.shared.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
 * EmailMessage - Individual email message with delivery tracking.
 *
 * <p>Represents a single email sent to a recipient. Tracks delivery status,
 * opens, clicks, and bounces.</p>
 *
 * <p>Tenant Isolation: All queries MUST filter by tenantId</p>
 */
@Document(collection = "email_messages")
@TypeAlias("email_message")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "message_tenant_campaign_idx", def = "{'tenantId': 1, 'campaignId': 1, 'status': 1}")
@CompoundIndex(name = "message_tenant_recipient_idx", def = "{'tenantId': 1, 'recipientEmail': 1, 'sentAt': -1}")
@CompoundIndex(name = "message_tenant_status_idx", def = "{'tenantId': 1, 'status': 1, 'scheduledAt': 1}")
public class EmailMessage extends BaseEntity {

    /**
     * Recipient email address
     */
    @Indexed
    private String recipientEmail;

    /**
     * Recipient first name
     */
    private String recipientFirstName;

    /**
     * Recipient last name
     */
    private String recipientLastName;

    /**
     * Recipient full name (computed)
     */
    private String recipientFullName;

    /**
     * Recipient ID from the email list
     */
    @Indexed
    private String subscriberId;

    /**
     * Email list ID
     */
    @Indexed
    private String listId;

    /**
     * Campaign ID
     */
    @Indexed
    private String campaignId;

    /**
     * Template ID used
     */
    @Indexed
    private String templateId;

    /**
     * Message subject
     */
    @Indexed
    private String subject;

    /**
     * Preheader text
     */
    private String preheader;

    /**
     * HTML content
     */
    private String htmlContent;

    /**
     * Plain text content
     */
    private String textContent;

    /**
     * From name
     */
    private String fromName;

    /**
     * From email
     */
    private String fromEmail;

    /**
     * Reply-to email
     */
    private String replyToEmail;

    /**
     * Message status (QUEUED, SENDING, SENT, DELIVERED, BOUNCED, OPENED, CLICKED, FAILED, DEFERRED)
     */
    @Indexed
    private String status;

    /**
     * Delivery status details
     */
    private DeliveryStatus deliveryStatus;

    /**
     * Bounce type (HARD, SOFT, NONE)
     */
    private String bounceType;

    /**
     * Bounce reason
     */
    private String bounceReason;

    /**
     * Scheduled send time
     */
    @Indexed
    private Instant scheduledAt;

    /**
     * Actual sent time
     */
    private Instant sentAt;

    /**
     * Delivered time
     */
    private Instant deliveredAt;

    /**
     * First opened time
     */
    private Instant openedAt;

    /**
     * Open count
     */
    private Integer openCount;

    /**
     * First clicked time
     */
    private Instant clickedAt;

    /**
     * Click count
     */
    private Integer clickCount;

    /**
     * Clicked links
     */
    private List<ClickEvent> clickEvents;

    /**
     * Unsubscribed time
     */
    private Instant unsubscribedAt;

    /**
     * Unsubscribe reason
     */
    private String unsubscribeReason;

    /**
     * Complained time (spam report)
     */
    private Instant complainedAt;

    /**
     * Retry count
     */
    private Integer retryCount;

    /**
     * Max retries allowed
     */
    private Integer maxRetries;

    /**
     * Last error message
     */
    private String lastError;

    /**
     * Priority (1-10, higher = more important)
     */
    private Integer priority;

    /**
     * Message ID from email provider
     */
    @Indexed
    private String providerMessageId;

    /**
     * Tracking ID for opens/clicks
     */
    @Indexed
    private String trackingId;

    /**
     * Variables used for rendering
     */
    private Map<String, Object> variables;

    /**
     * Custom headers
     */
    private Map<String, String> customHeaders;

    /**
     * Tags
     */
    private List<String> tags;

    /**
     * Additional metadata
     */
    private Map<String, Object> metadata;

    /**
     * User agent (for opens)
     */
    private String userAgent;

    /**
     * IP address (for opens)
     */
    private String ipAddress;

    /**
     * Device type (desktop, mobile, tablet)
     */
    private String deviceType;

    /**
     * Location (country, city)
     */
    private String location;

    /**
     * ESP (Email Service Provider) used
     */
    private String esp;

    /**
     * Cost of sending this email
     */
    private Double cost;

    /**
     * Delivery status inner class
     */
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeliveryStatus {
        private String state; // pending, delivered, bounced, deferred, rejected
        private String code;
        private String message;
        private Instant timestamp;
        private String smtpResponse;

        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Instant getTimestamp() { return timestamp; }
        public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
        public String getSmtpResponse() { return smtpResponse; }
        public void setSmtpResponse(String smtpResponse) { this.smtpResponse = smtpResponse; }
    }

    /**
     * Click event inner class
     */
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClickEvent {
        private String url;
        private Instant timestamp;
        private String linkId;
        private Integer count;

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public Instant getTimestamp() { return timestamp; }
        public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
        public String getLinkId() { return linkId; }
        public void setLinkId(String linkId) { this.linkId = linkId; }
        public Integer getCount() { return count; }
        public void setCount(Integer count) { this.count = count; }
    }

    /**
     * Create a new message for a recipient.
     *
     * @param tenantId       the tenant ID
     * @param recipientEmail the recipient email
     * @param subject        the subject
     * @param campaignId     the campaign ID
     */
    public EmailMessage(String tenantId, String recipientEmail, String subject, String campaignId) {
        super(tenantId);
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.campaignId = campaignId;
        this.status = "QUEUED";
        this.openCount = 0;
        this.clickCount = 0;
        this.retryCount = 0;
        this.maxRetries = 3;
        this.priority = 5;
        this.clickEvents = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.variables = new HashMap<>();
        this.customHeaders = new HashMap<>();
        this.metadata = new HashMap<>();
    }

    /**
     * Create a new message with list.
     *
     * @param tenantId       the tenant ID
     * @param recipientEmail the recipient email
     * @param subject        the subject
     * @param campaignId     the campaign ID
     * @param listId         the list ID
     */
    public EmailMessage(String tenantId, String recipientEmail, String subject, String campaignId, String listId) {
        this(tenantId, recipientEmail, subject, campaignId);
        this.listId = listId;
    }

    /**
     * Check if message was delivered.
     *
     * @return true if delivered
     */
    public boolean isDelivered() {
        return "DELIVERED".equals(this.status) || "OPENED".equals(this.status) || "CLICKED".equals(this.status);
    }

    /**
     * Check if message was opened.
     *
     * @return true if opened
     */
    public boolean isOpened() {
        return "OPENED".equals(this.status) || "CLICKED".equals(this.status);
    }

    /**
     * Check if message was clicked.
     *
     * @return true if clicked
     */
    public boolean isClicked() {
        return "CLICKED".equals(this.status);
    }

    /**
     * Check if message bounced.
     *
     * @return true if bounced
     */
    public boolean isBounced() {
        return "BOUNCED".equals(this.status);
    }

    /**
     * Check if message failed permanently.
     *
     * @return true if failed
     */
    public boolean isFailed() {
        return "FAILED".equals(this.status) || "BOUNCED".equals(this.status);
    }

    /**
     * Check if message is pending.
     *
     * @return true if pending
     */
    public boolean isPending() {
        return "QUEUED".equals(this.status) || "SENDING".equals(this.status) || "DEFERRED".equals(this.status);
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
     * Check if is soft bounce.
     *
     * @return true if soft bounce
     */
    public boolean isSoftBounce() {
        return "BOUNCED".equals(this.status) && "SOFT".equals(this.bounceType);
    }

    /**
     * Check if can retry.
     *
     * @return true if can retry
     */
    public boolean canRetry() {
        return (this.retryCount == null || this.retryCount < (this.maxRetries != null ? this.maxRetries : 3)) &&
            ("DEFERRED".equals(this.status) || "FAILED".equals(this.status));
    }

    /**
     * Mark as sent.
     */
    public void markAsSent() {
        this.status = "SENT";
        this.sentAt = Instant.now();
        this.touch();
    }

    /**
     * Mark as delivered.
     */
    public void markAsDelivered() {
        this.status = "DELIVERED";
        this.deliveredAt = Instant.now();
        this.touch();
    }

    /**
     * Mark as opened.
     */
    public void markAsOpened() {
        if (!"CLICKED".equals(this.status)) {
            this.status = "OPENED";
        }
        this.openedAt = Instant.now();
        this.openCount = (this.openCount != null ? this.openCount : 0) + 1;
        this.touch();
    }

    /**
     * Mark as clicked.
     */
    public void markAsClicked(String url) {
        this.status = "CLICKED";
        this.clickedAt = Instant.now();
        this.clickCount = (this.clickCount != null ? this.clickCount : 0) + 1;
        if (this.clickEvents == null) {
            this.clickEvents = new ArrayList<>();
        }
        ClickEvent event = new ClickEvent();
        event.setUrl(url);
        event.setTimestamp(Instant.now());
        event.setCount(1);
        this.clickEvents.add(event);
        this.touch();
    }

    /**
     * Mark as bounced.
     *
     * @param bounceType the bounce type
     * @param reason     the reason
     */
    public void markAsBounced(String bounceType, String reason) {
        this.status = "BOUNCED";
        this.bounceType = bounceType;
        this.bounceReason = reason;
        this.touch();
    }

    /**
     * Mark as failed.
     *
     * @param error the error message
     */
    public void markAsFailed(String error) {
        this.status = "FAILED";
        this.lastError = error;
        this.touch();
    }

    /**
     * Mark as deferred.
     */
    public void markAsDeferred() {
        this.status = "DEFERRED";
        this.retryCount = (this.retryCount != null ? this.retryCount : 0) + 1;
        this.touch();
    }

    /**
     * Mark as unsubscribed.
     *
     * @param reason the reason
     */
    public void markAsUnsubscribed(String reason) {
        this.unsubscribedAt = Instant.now();
        this.unsubscribeReason = reason;
        this.touch();
    }

    /**
     * Mark as complained.
     */
    public void markAsComplained() {
        this.complainedAt = Instant.now();
        this.touch();
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
     * Add a variable.
     *
     * @param key   the variable key
     * @param value the variable value
     */
    public void addVariable(String key, Object value) {
        if (this.variables == null) {
            this.variables = new HashMap<>();
        }
        this.variables.put(key, value);
    }

    /**
     * Add a custom header.
     *
     * @param key   the header key
     * @param value the header value
     */
    public void addCustomHeader(String key, String value) {
        if (this.customHeaders == null) {
            this.customHeaders = new HashMap<>();
        }
        this.customHeaders.put(key, value);
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
     * Get recipient full name.
     *
     * @return full name or email if no name set
     */
    public String getRecipientDisplayName() {
        if (recipientFullName != null && !recipientFullName.isBlank()) {
            return recipientFullName;
        }
        if (recipientFirstName != null && !recipientFirstName.isBlank()) {
            StringBuilder name = new StringBuilder(recipientFirstName);
            if (recipientLastName != null && !recipientLastName.isBlank()) {
                name.append(" ").append(recipientLastName);
            }
            return name.toString();
        }
        return recipientEmail;
    }

    /**
     * Get unique clicked URLs.
     *
     * @return list of unique URLs clicked
     */
    public List<String> getUniqueClickedUrls() {
        if (this.clickEvents == null) {
            return new ArrayList<>();
        }
        return this.clickEvents.stream()
            .map(ClickEvent::getUrl)
            .distinct()
            .toList();
    }

    /**
     * Get all click events.
     *
     * @return list of click events
     */
    public List<ClickEvent> getAllClickEvents() {
        return this.clickEvents != null ? this.clickEvents : new ArrayList<>();
    }

    /**
     * Calculate engagement time (time from delivery to first open).
     *
     * @return time in seconds, or null if not available
     */
    public Long getEngagementTimeSeconds() {
        if (deliveredAt != null && openedAt != null) {
            return java.time.Duration.between(deliveredAt, openedAt).getSeconds();
        }
        return null;
    }

    /**
     * Generate tracking ID.
     */
    public void generateTrackingId() {
        if (this.trackingId == null) {
            this.trackingId = java.util.UUID.randomUUID().toString();
        }
    }

    /**
     * Set recipient name parts.
     *
     * @param firstName first name
     * @param lastName  last name
     */
    public void setRecipientName(String firstName, String lastName) {
        this.recipientFirstName = firstName;
        this.recipientLastName = lastName;
        if (firstName != null && !firstName.isBlank()) {
            StringBuilder fullName = new StringBuilder(firstName);
            if (lastName != null && !lastName.isBlank()) {
                fullName.append(" ").append(lastName);
            }
            this.recipientFullName = fullName.toString();
        }
    }

    /**
     * Get recipient initials.
     *
     * @return initials (up to 2 characters)
     */
    public String getRecipientInitials() {
        StringBuilder initials = new StringBuilder();
        if (recipientFirstName != null && !recipientFirstName.isBlank()) {
            initials.append(recipientFirstName.charAt(0));
        }
        if (recipientLastName != null && !recipientLastName.isBlank()) {
            initials.append(recipientLastName.charAt(0));
        }
        if (initials.isEmpty() && recipientEmail != null && !recipientEmail.isBlank()) {
            initials.append(recipientEmail.charAt(0));
        }
        return initials.toString().toUpperCase();
    }
}
