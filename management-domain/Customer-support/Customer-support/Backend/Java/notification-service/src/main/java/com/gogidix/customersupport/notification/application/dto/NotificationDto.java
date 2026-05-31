package com.gogidix.customersupport.notification.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * DTO for Notification operations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationDto {

    private String id;
    private String tenantId;
    private String notificationId;

    // Basic Info
    private String type;
    private String recipientId;
    private String recipientEmail;
    private String recipientPhone;
    private String channel;
    private String status;

    // Content
    private String subject;
    private String content;
    private String templateId;
    private Map<String, Object> templateData;
    private String priority;

    // Timestamps
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant scheduledAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant sentAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant deliveredAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant readAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant failedAt;

    private String failureReason;
    private Integer retryCount;
    private Integer maxRetries;

    // Related Entity
    private String relatedEntityType;
    private String relatedEntityId;
    private Map<String, Object> metadata;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    /**
     * Request DTO for sending notification
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendNotificationRequest {
        @NotBlank(message = "Type is required")
        private String type;

        @NotBlank(message = "Recipient ID is required")
        private String recipientId;

        private String recipientEmail;
        private String recipientPhone;

        @NotBlank(message = "Channel is required")
        private String channel;

        @NotBlank(message = "Subject is required")
        private String subject;

        @NotBlank(message = "Content is required")
        private String content;

        private String tenantId;
        private String templateId;
        private Map<String, Object> templateData;
        private String priority;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant scheduledAt;

        private String relatedEntityType;
        private String relatedEntityId;
        private Map<String, Object> metadata;
    }

    /**
     * Request DTO for bulk notification
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BulkNotificationRequest {
        private String tenantId;

        @NotBlank(message = "Type is required")
        private String type;

        @NotNull(message = "Recipient IDs are required")
        private List<String> recipientIds;

        @NotBlank(message = "Channel is required")
        private String channel;

        @NotBlank(message = "Subject is required")
        private String subject;

        @NotBlank(message = "Content is required")
        private String content;

        private String priority;
        private String templateId;
        private Map<String, Object> templateData;
    }
}
