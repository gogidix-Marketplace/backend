package com.gogidix.sales.communication.application.dto.response;

import com.gogidix.sales.communication.domain.model.CommunicationChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Communication Channel Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunicationChannelResponseDto {

    private String id;
    private String channelId;
    private String tenantId;
    private String name;
    private String description;
    private ChannelTypeDto type;
    private ChannelStatusDto status;
    private Boolean isDefault;
    private ConfigParametersDto config;
    private WebhookConfigDto webhookConfig;
    private Integer priority;
    private RateLimitingDto rateLimiting;
    private RetryPolicyDto retryPolicy;
    private List<String> allowedSenders;
    private List<String> blockedRecipients;
    private String timeZone;
    private Instant lastUsedAt;
    private Long totalSent;
    private Long totalDelivered;
    private Long totalFailed;
    private Double deliveryRate;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConfigParametersDto {
        private String smtpHost;
        private Integer smtpPort;
        private String fromEmail;
        private String fromName;
        private String replyToEmail;
        private String provider;
        private String senderId;
        private String businessAccountId;
        private String phoneNumberId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WebhookConfigDto {
        private String webhookUrl;
        private Boolean isEnabled;
        private List<String> subscribedEvents;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RateLimitingDto {
        private Boolean isEnabled;
        private Integer maxMessagesPerMinute;
        private Integer maxMessagesPerHour;
        private Integer maxMessagesPerDay;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RetryPolicyDto {
        private Boolean isEnabled;
        private Integer maxRetries;
        private Integer retryIntervalSeconds;
    }

    public enum ChannelTypeDto {
        EMAIL,
        SMS,
        IN_APP,
        WHATSAPP,
        PUSH_NOTIFICATION,
        WEBHOOK
    }

    public enum ChannelStatusDto {
        ACTIVE,
        INACTIVE,
        SUSPENDED,
        MAINTENANCE
    }

    public static ChannelTypeDto mapChannelType(CommunicationChannel.ChannelType type) {
        return type != null ? ChannelTypeDto.valueOf(type.name()) : null;
    }

    public static ChannelStatusDto mapChannelStatus(CommunicationChannel.ChannelStatus status) {
        return status != null ? ChannelStatusDto.valueOf(status.name()) : null;
    }
}
