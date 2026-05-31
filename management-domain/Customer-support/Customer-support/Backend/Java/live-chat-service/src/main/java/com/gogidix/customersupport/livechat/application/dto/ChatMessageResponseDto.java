package com.gogidix.customersupport.livechat.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.customersupport.livechat.domain.model.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponseDto {

    private String id;
    private String tenantId;
    private String sessionId;
    private String messageId;
    private SenderTypeDto senderType;
    private String senderId;
    private String senderName;
    private String messageContent;
    private MessageTypeDto messageType;
    private List<MessageAttachmentDto> attachments;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant sentAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant readAt;

    private Boolean isDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant editedAt;

    private String replyToMessageId;

    private MessageMetadataDto metadata;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum SenderTypeDto {
        CUSTOMER, AGENT, SYSTEM, BOT
    }

    public enum MessageTypeDto {
        TEXT, IMAGE, FILE, SYSTEM, RATING, TYPING_INDICATOR
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageAttachmentDto {
        private String fileName;
        private String fileUrl;
        private String fileSize;
        private String contentType;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageMetadataDto {
        private String ipAddress;
        private String userAgent;
        private String browser;
        private String os;
        private String location;
    }

    public static SenderTypeDto fromEntitySenderType(ChatMessage.SenderType senderType) {
        return SenderTypeDto.valueOf(senderType.name());
    }

    public static MessageTypeDto fromEntityMessageType(ChatMessage.MessageType messageType) {
        return MessageTypeDto.valueOf(messageType.name());
    }
}
