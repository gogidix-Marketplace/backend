package com.gogidix.sales.communication.application.dto.response;

import com.gogidix.sales.communication.domain.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Message Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDto {

    private String id;
    private String messageId;
    private String tenantId;
    private String conversationId;
    private String senderId;
    private String senderName;
    private String senderType;
    private List<String> recipientIds;
    private List<RecipientInfoDto> recipients;
    private ChannelTypeDto channel;
    private String subject;
    private String content;
    private String templateId;
    private MessageStatusDto status;
    private Boolean isRead;
    private Instant readAt;
    private String readBy;
    private List<AttachmentDto> attachments;
    private String parentMessageId;
    private Boolean isSystemMessage;
    private Integer priority;
    private Instant scheduledAt;
    private Instant sentAt;
    private Instant deliveredAt;
    private String externalMessageId;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipientInfoDto {
        private String recipientId;
        private String recipientName;
        private String recipientType;
        private String emailAddress;
        private String phoneNumber;
        private Boolean isRead;
        private Instant readAt;
        private String deliveryStatus;
        private Instant deliveredAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttachmentDto {
        private String attachmentId;
        private String fileName;
        private String fileType;
        private Long fileSize;
        private String fileUrl;
    }

    public enum ChannelTypeDto {
        EMAIL,
        SMS,
        IN_APP,
        WHATSAPP,
        PUSH_NOTIFICATION
    }

    public enum MessageStatusDto {
        DRAFT,
        SCHEDULED,
        SENDING,
        SENT,
        DELIVERED,
        FAILED,
        BOUNCED,
        READ,
        ARCHIVED
    }

    public static ChannelTypeDto mapChannelType(Message.ChannelType channelType) {
        return channelType != null ? ChannelTypeDto.valueOf(channelType.name()) : null;
    }

    public static MessageStatusDto mapMessageStatus(Message.MessageStatus status) {
        return status != null ? MessageStatusDto.valueOf(status.name()) : null;
    }
}
