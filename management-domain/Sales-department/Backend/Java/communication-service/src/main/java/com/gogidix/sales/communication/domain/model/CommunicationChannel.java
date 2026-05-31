package com.gogidix.sales.communication.domain.model;

import com.gogidix.sales.communication.shared.base.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Communication Channel Domain Entity
 * Multi-tenant communication channel configuration
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "communication_channels")
public class CommunicationChannel extends AuditableEntity {

    @Indexed
    private String channelId;

    @Indexed
    private String tenantId;

    private String name;

    private String description;

    @Indexed
    private ChannelType type;

    private ChannelStatus status;

    private Boolean isDefault;

    private ConfigParameters config;

    private WebhookConfig webhookConfig;

    private TemplateSettings templateSettings;

    private Integer priority;

    private RateLimiting rateLimiting;

    private RetryPolicy retryPolicy;

    private List<String> allowedSenders;

    private List<String> blockedRecipients;

    private String timeZone;

    private Map<String, Object> metadata;

    private Instant lastUsedAt;

    private Long totalSent;

    private Long totalDelivered;

    private Long totalFailed;

    public enum ChannelType {
        EMAIL,
        SMS,
        IN_APP,
        WHATSAPP,
        PUSH_NOTIFICATION,
        WEBHOOK
    }

    public enum ChannelStatus {
        ACTIVE,
        INACTIVE,
        SUSPENDED,
        MAINTENANCE
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConfigParameters {
        // Email specific
        private String smtpHost;
        private Integer smtpPort;
        private String smtpUsername;
        private String smtpPassword;
        private Boolean useTls;
        private String fromEmail;
        private String fromName;
        private String replyToEmail;

        // SMS specific
        private String provider;
        private String apiKey;
        private String apiSecret;
        private String senderId;
        private String shortCode;

        // WhatsApp specific
        private String businessAccountId;
        private String phoneNumberId;
        private String accessToken;
        private String templateNamespace;

        // Push notification specific
        private String firebaseServerKey;
        private String apnsCertificate;
        private String apnsPrivateKey;

        // Webhook specific
        private String webhookUrl;
        private String authenticationMethod; // BASIC, OAUTH2, API_KEY
        private String authUsername;
        private String authPassword;
        private String webhookApiKey;
        private List<String> headers;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WebhookConfig {
        private String webhookUrl;
        private Boolean isEnabled;
        private List<String> subscribedEvents;
        private String secretKey;
        private Integer retryAttempts;
        private Integer timeoutSeconds;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplateSettings {
        private Boolean isEnabled;
        private String templateEngine; // THYMELEAF, FREEMARKER, HANDLEBARS
        private String templateLocation;
        private Map<String, String> defaultTemplates;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RateLimiting {
        private Boolean isEnabled;
        private Integer maxMessagesPerMinute;
        private Integer maxMessagesPerHour;
        private Integer maxMessagesPerDay;
        private Integer burstSize;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RetryPolicy {
        private Boolean isEnabled;
        private Integer maxRetries;
        private Integer retryIntervalSeconds;
        private Double backoffMultiplier;
        private List<String> retryableErrors;
    }

    /**
     * Creates a new communication channel
     */
    public static CommunicationChannel create(String tenantId, String name, ChannelType type,
                                               ConfigParameters config) {
        CommunicationChannel channel = CommunicationChannel.builder()
                .tenantId(tenantId)
                .name(name)
                .type(type)
                .status(ChannelStatus.ACTIVE)
                .isDefault(false)
                .config(config)
                .priority(1)
                .allowedSenders(new ArrayList<>())
                .blockedRecipients(new ArrayList<>())
                .totalSent(0L)
                .totalDelivered(0L)
                .totalFailed(0L)
                .metadata(new HashMap<>())
                .build();

        // Set default retry policy
        channel.setRetryPolicy(RetryPolicy.builder()
                .isEnabled(true)
                .maxRetries(3)
                .retryIntervalSeconds(60)
                .backoffMultiplier(2.0)
                .retryableErrors(List.of("TIMEOUT", "CONNECTION_ERROR", "SERVICE_UNAVAILABLE"))
                .build());

        // Set default rate limiting
        channel.setRateLimiting(RateLimiting.builder()
                .isEnabled(false)
                .maxMessagesPerMinute(100)
                .maxMessagesPerHour(1000)
                .maxMessagesPerDay(10000)
                .burstSize(10)
                .build());

        return channel;
    }

    /**
     * Activates the channel
     */
    public void activate() {
        this.status = ChannelStatus.ACTIVE;
    }

    /**
     * Deactivates the channel
     */
    public void deactivate() {
        this.status = ChannelStatus.INACTIVE;
    }

    /**
     * Suspends the channel
     */
    public void suspend() {
        this.status = ChannelStatus.SUSPENDED;
    }

    /**
     * Sets as maintenance mode
     */
    public void setMaintenanceMode() {
        this.status = ChannelStatus.MAINTENANCE;
    }

    /**
     * Sets as default channel
     */
    public void setAsDefault() {
        this.isDefault = true;
    }

    /**
     * Removes default status
     */
    public void removeDefault() {
        this.isDefault = false;
    }

    /**
     * Updates the configuration
     */
    public void updateConfig(ConfigParameters config) {
        this.config = config;
    }

    /**
     * Adds an allowed sender
     */
    public void addAllowedSender(String senderId) {
        if (this.allowedSenders == null) {
            this.allowedSenders = new ArrayList<>();
        }
        if (!this.allowedSenders.contains(senderId)) {
            this.allowedSenders.add(senderId);
        }
    }

    /**
     * Removes an allowed sender
     */
    public void removeAllowedSender(String senderId) {
        if (this.allowedSenders != null) {
            this.allowedSenders.remove(senderId);
        }
    }

    /**
     * Adds a blocked recipient
     */
    public void addBlockedRecipient(String recipientId) {
        if (this.blockedRecipients == null) {
            this.blockedRecipients = new ArrayList<>();
        }
        if (!this.blockedRecipients.contains(recipientId)) {
            this.blockedRecipients.add(recipientId);
        }
    }

    /**
     * Removes a blocked recipient
     */
    public void removeBlockedRecipient(String recipientId) {
        if (this.blockedRecipients != null) {
            this.blockedRecipients.remove(recipientId);
        }
    }

    /**
     * Checks if a sender is allowed
     */
    public boolean isSenderAllowed(String senderId) {
        return this.allowedSenders == null || this.allowedSenders.isEmpty()
                || this.allowedSenders.contains(senderId);
    }

    /**
     * Checks if a recipient is blocked
     */
    public boolean isRecipientBlocked(String recipientId) {
        return this.blockedRecipients != null && this.blockedRecipients.contains(recipientId);
    }

    /**
     * Checks if the channel is available
     */
    public boolean isAvailable() {
        return this.status == ChannelStatus.ACTIVE;
    }

    /**
     * Records a sent message
     */
    public void recordSent() {
        this.totalSent++;
        this.lastUsedAt = Instant.now();
    }

    /**
     * Records a delivered message
     */
    public void recordDelivered() {
        this.totalDelivered++;
    }

    /**
     * Records a failed message
     */
    public void recordFailed() {
        this.totalFailed++;
    }

    /**
     * Gets the delivery rate
     */
    public double getDeliveryRate() {
        if (this.totalSent == null || this.totalSent == 0) {
            return 0.0;
        }
        long delivered = this.totalDelivered != null ? this.totalDelivered : 0;
        return (double) delivered / this.totalSent;
    }

    /**
     * Checks if rate limiting allows sending
     */
    public boolean canSend() {
        if (this.rateLimiting == null || !this.rateLimiting.getIsEnabled()) {
            return true;
        }
        // Simplified check - actual implementation would use Redis counter
        return true;
    }

    /**
     * Adds metadata
     */
    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }
}
