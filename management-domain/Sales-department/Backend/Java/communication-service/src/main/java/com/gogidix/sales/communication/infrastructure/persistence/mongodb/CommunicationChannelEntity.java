package com.gogidix.sales.communication.infrastructure.persistence.mongodb;

import com.gogidix.sales.communication.domain.model.CommunicationChannel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MongoDB Entity for CommunicationChannel
 * Separate from domain model for clean architecture
 */
@Document(collection = "communication_channels")
public class CommunicationChannelEntity {

    @Id
    private String id;

    @Indexed
    private String channelId;

    @Indexed
    private String tenantId;

    private String name;
    private String description;

    @Indexed
    private CommunicationChannel.ChannelType type;

    private CommunicationChannel.ChannelStatus status;
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
    private Instant createdAt;
    private Instant updatedAt;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getChannelId() { return channelId; }
    public void setChannelId(String channelId) { this.channelId = channelId; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public CommunicationChannel.ChannelType getType() { return type; }
    public void setType(CommunicationChannel.ChannelType type) { this.type = type; }

    public CommunicationChannel.ChannelStatus getStatus() { return status; }
    public void setStatus(CommunicationChannel.ChannelStatus status) { this.status = status; }

    public Boolean getIsDefault() { return isDefault; }
    public void setIsDefault(Boolean isDefault) { this.isDefault = isDefault; }

    public ConfigParameters getConfig() { return config; }
    public void setConfig(ConfigParameters config) { this.config = config; }

    public WebhookConfig getWebhookConfig() { return webhookConfig; }
    public void setWebhookConfig(WebhookConfig webhookConfig) { this.webhookConfig = webhookConfig; }

    public TemplateSettings getTemplateSettings() { return templateSettings; }
    public void setTemplateSettings(TemplateSettings templateSettings) { this.templateSettings = templateSettings; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    public RateLimiting getRateLimiting() { return rateLimiting; }
    public void setRateLimiting(RateLimiting rateLimiting) { this.rateLimiting = rateLimiting; }

    public RetryPolicy getRetryPolicy() { return retryPolicy; }
    public void setRetryPolicy(RetryPolicy retryPolicy) { this.retryPolicy = retryPolicy; }

    public List<String> getAllowedSenders() { return allowedSenders; }
    public void setAllowedSenders(List<String> allowedSenders) { this.allowedSenders = allowedSenders; }

    public List<String> getBlockedRecipients() { return blockedRecipients; }
    public void setBlockedRecipients(List<String> blockedRecipients) { this.blockedRecipients = blockedRecipients; }

    public String getTimeZone() { return timeZone; }
    public void setTimeZone(String timeZone) { this.timeZone = timeZone; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    public Instant getLastUsedAt() { return lastUsedAt; }
    public void setLastUsedAt(Instant lastUsedAt) { this.lastUsedAt = lastUsedAt; }

    public Long getTotalSent() { return totalSent; }
    public void setTotalSent(Long totalSent) { this.totalSent = totalSent; }

    public Long getTotalDelivered() { return totalDelivered; }
    public void setTotalDelivered(Long totalDelivered) { this.totalDelivered = totalDelivered; }

    public Long getTotalFailed() { return totalFailed; }
    public void setTotalFailed(Long totalFailed) { this.totalFailed = totalFailed; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // Embedded classes for MongoDB
    public static class ConfigParameters {
        private String smtpHost;
        private Integer smtpPort;
        private String smtpUsername;
        private String smtpPassword;
        private Boolean useTls;
        private String fromEmail;
        private String fromName;
        private String replyToEmail;
        private String provider;
        private String apiKey;
        private String apiSecret;
        private String senderId;
        private String shortCode;
        private String businessAccountId;
        private String phoneNumberId;
        private String accessToken;
        private String templateNamespace;
        private String firebaseServerKey;
        private String apnsCertificate;
        private String apnsPrivateKey;
        private String webhookUrl;
        private String authenticationMethod;
        private String authUsername;
        private String authPassword;
        private String webhookApiKey;
        private List<String> headers;

        // Getters and setters
        public String getSmtpHost() { return smtpHost; }
        public void setSmtpHost(String smtpHost) { this.smtpHost = smtpHost; }
        public Integer getSmtpPort() { return smtpPort; }
        public void setSmtpPort(Integer smtpPort) { this.smtpPort = smtpPort; }
        public String getSmtpUsername() { return smtpUsername; }
        public void setSmtpUsername(String smtpUsername) { this.smtpUsername = smtpUsername; }
        public String getSmtpPassword() { return smtpPassword; }
        public void setSmtpPassword(String smtpPassword) { this.smtpPassword = smtpPassword; }
        public Boolean getUseTls() { return useTls; }
        public void setUseTls(Boolean useTls) { this.useTls = useTls; }
        public String getFromEmail() { return fromEmail; }
        public void setFromEmail(String fromEmail) { this.fromEmail = fromEmail; }
        public String getFromName() { return fromName; }
        public void setFromName(String fromName) { this.fromName = fromName; }
        public String getReplyToEmail() { return replyToEmail; }
        public void setReplyToEmail(String replyToEmail) { this.replyToEmail = replyToEmail; }
        public String getProvider() { return provider; }
        public void setProvider(String provider) { this.provider = provider; }
        public String getApiKey() { return apiKey; }
        public void setApiKey(String apiKey) { this.apiKey = apiKey; }
        public String getApiSecret() { return apiSecret; }
        public void setApiSecret(String apiSecret) { this.apiSecret = apiSecret; }
        public String getSenderId() { return senderId; }
        public void setSenderId(String senderId) { this.senderId = senderId; }
        public String getShortCode() { return shortCode; }
        public void setShortCode(String shortCode) { this.shortCode = shortCode; }
        public String getBusinessAccountId() { return businessAccountId; }
        public void setBusinessAccountId(String businessAccountId) { this.businessAccountId = businessAccountId; }
        public String getPhoneNumberId() { return phoneNumberId; }
        public void setPhoneNumberId(String phoneNumberId) { this.phoneNumberId = phoneNumberId; }
        public String getAccessToken() { return accessToken; }
        public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
        public String getTemplateNamespace() { return templateNamespace; }
        public void setTemplateNamespace(String templateNamespace) { this.templateNamespace = templateNamespace; }
        public String getFirebaseServerKey() { return firebaseServerKey; }
        public void setFirebaseServerKey(String firebaseServerKey) { this.firebaseServerKey = firebaseServerKey; }
        public String getApnsCertificate() { return apnsCertificate; }
        public void setApnsCertificate(String apnsCertificate) { this.apnsCertificate = apnsCertificate; }
        public String getApnsPrivateKey() { return apnsPrivateKey; }
        public void setApnsPrivateKey(String apnsPrivateKey) { this.apnsPrivateKey = apnsPrivateKey; }
        public String getWebhookUrl() { return webhookUrl; }
        public void setWebhookUrl(String webhookUrl) { this.webhookUrl = webhookUrl; }
        public String getAuthenticationMethod() { return authenticationMethod; }
        public void setAuthenticationMethod(String authenticationMethod) { this.authenticationMethod = authenticationMethod; }
        public String getAuthUsername() { return authUsername; }
        public void setAuthUsername(String authUsername) { this.authUsername = authUsername; }
        public String getAuthPassword() { return authPassword; }
        public void setAuthPassword(String authPassword) { this.authPassword = authPassword; }
        public String getWebhookApiKey() { return webhookApiKey; }
        public void setWebhookApiKey(String webhookApiKey) { this.webhookApiKey = webhookApiKey; }
        public List<String> getHeaders() { return headers; }
        public void setHeaders(List<String> headers) { this.headers = headers; }
    }

    public static class WebhookConfig {
        private String webhookUrl;
        private Boolean isEnabled;
        private List<String> subscribedEvents;
        private String secretKey;
        private Integer retryAttempts;
        private Integer timeoutSeconds;

        // Getters and setters
        public String getWebhookUrl() { return webhookUrl; }
        public void setWebhookUrl(String webhookUrl) { this.webhookUrl = webhookUrl; }
        public Boolean getIsEnabled() { return isEnabled; }
        public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }
        public List<String> getSubscribedEvents() { return subscribedEvents; }
        public void setSubscribedEvents(List<String> subscribedEvents) { this.subscribedEvents = subscribedEvents; }
        public String getSecretKey() { return secretKey; }
        public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
        public Integer getRetryAttempts() { return retryAttempts; }
        public void setRetryAttempts(Integer retryAttempts) { this.retryAttempts = retryAttempts; }
        public Integer getTimeoutSeconds() { return timeoutSeconds; }
        public void setTimeoutSeconds(Integer timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
    }

    public static class TemplateSettings {
        private Boolean isEnabled;
        private String templateEngine;
        private String templateLocation;
        private Map<String, String> defaultTemplates;

        // Getters and setters
        public Boolean getIsEnabled() { return isEnabled; }
        public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }
        public String getTemplateEngine() { return templateEngine; }
        public void setTemplateEngine(String templateEngine) { this.templateEngine = templateEngine; }
        public String getTemplateLocation() { return templateLocation; }
        public void setTemplateLocation(String templateLocation) { this.templateLocation = templateLocation; }
        public Map<String, String> getDefaultTemplates() { return defaultTemplates; }
        public void setDefaultTemplates(Map<String, String> defaultTemplates) { this.defaultTemplates = defaultTemplates; }
    }

    public static class RateLimiting {
        private Boolean isEnabled;
        private Integer maxMessagesPerMinute;
        private Integer maxMessagesPerHour;
        private Integer maxMessagesPerDay;
        private Integer burstSize;

        // Getters and setters
        public Boolean getIsEnabled() { return isEnabled; }
        public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }
        public Integer getMaxMessagesPerMinute() { return maxMessagesPerMinute; }
        public void setMaxMessagesPerMinute(Integer maxMessagesPerMinute) { this.maxMessagesPerMinute = maxMessagesPerMinute; }
        public Integer getMaxMessagesPerHour() { return maxMessagesPerHour; }
        public void setMaxMessagesPerHour(Integer maxMessagesPerHour) { this.maxMessagesPerHour = maxMessagesPerHour; }
        public Integer getMaxMessagesPerDay() { return maxMessagesPerDay; }
        public void setMaxMessagesPerDay(Integer maxMessagesPerDay) { this.maxMessagesPerDay = maxMessagesPerDay; }
        public Integer getBurstSize() { return burstSize; }
        public void setBurstSize(Integer burstSize) { this.burstSize = burstSize; }
    }

    public static class RetryPolicy {
        private Boolean isEnabled;
        private Integer maxRetries;
        private Integer retryIntervalSeconds;
        private Double backoffMultiplier;
        private List<String> retryableErrors;

        // Getters and setters
        public Boolean getIsEnabled() { return isEnabled; }
        public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }
        public Integer getMaxRetries() { return maxRetries; }
        public void setMaxRetries(Integer maxRetries) { this.maxRetries = maxRetries; }
        public Integer getRetryIntervalSeconds() { return retryIntervalSeconds; }
        public void setRetryIntervalSeconds(Integer retryIntervalSeconds) { this.retryIntervalSeconds = retryIntervalSeconds; }
        public Double getBackoffMultiplier() { return backoffMultiplier; }
        public void setBackoffMultiplier(Double backoffMultiplier) { this.backoffMultiplier = backoffMultiplier; }
        public List<String> getRetryableErrors() { return retryableErrors; }
        public void setRetryableErrors(List<String> retryableErrors) { this.retryableErrors = retryableErrors; }
    }
}
