package com.gogidix.customersupport.livechat.application.dto;

import com.gogidix.customersupport.livechat.domain.model.ChatMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequestDto {

    @NotBlank(message = "Session ID is required")
    private String sessionId;

    @NotBlank(message = "Sender ID is required")
    private String senderId;

    @NotBlank(message = "Sender name is required")
    private String senderName;

    @NotBlank(message = "Message content is required")
    private String messageContent;

    private SenderTypeDto senderType;

    private MessageTypeDto messageType;

    private List<MessageAttachmentDto> attachments;

    private String replyToMessageId;

    private String ipAddress;

    private String userAgent;

    public enum SenderTypeDto {
        CUSTOMER, AGENT, SYSTEM, BOT
    }

    public enum MessageTypeDto {
        TEXT, IMAGE, FILE, SYSTEM, RATING, TYPING_INDICATOR
    }

    public ChatMessage.SenderType toEntitySenderType() {
        return senderType != null ? ChatMessage.SenderType.valueOf(senderType.name()) : ChatMessage.SenderType.CUSTOMER;
    }

    public ChatMessage.MessageType toEntityMessageType() {
        return messageType != null ? ChatMessage.MessageType.valueOf(messageType.name()) : ChatMessage.MessageType.TEXT;
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
}
