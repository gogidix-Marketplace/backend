package com.gogidix.customersupport.livechat.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "chat_messages")
public class ChatMessage extends BaseEntity {

    @Field("session_id")
    @Indexed
    private String sessionId;

    @Field("message_id")
    @Indexed
    private String messageId;

    @Field("sender_type")
    @Indexed
    private SenderType senderType;

    @Field("sender_id")
    private String senderId;

    @Field("sender_name")
    private String senderName;

    @Field("message_content")
    private String messageContent;

    @Field("message_type")
    private MessageType messageType;

    @Field("attachments")
    private List<MessageAttachment> attachments;

    @Field("sent_at")
    private Instant sentAt;

    @Field("read_at")
    private Instant readAt;

    @Field("is_deleted")
    private Boolean isDeleted;

    @Field("edited_at")
    private Instant editedAt;

    @Field("reply_to_message_id")
    private String replyToMessageId;

    @Field("metadata")
    private MessageMetadata metadata;

    public static ChatMessage create(String tenantId, String sessionId, SenderType senderType,
                                     String senderId, String senderName, String messageContent) {
        ChatMessage message = new ChatMessage();
        message.setId(java.util.UUID.randomUUID().toString());
        message.setTenantId(tenantId);
        message.setSessionId(sessionId);
        message.setMessageId(java.util.UUID.randomUUID().toString());
        message.setSenderType(senderType);
        message.setSenderId(senderId);
        message.setSenderName(senderName);
        message.setMessageContent(messageContent);
        message.setMessageType(MessageType.TEXT);
        message.setSentAt(Instant.now());
        message.setIsDeleted(false);
        message.setCreatedAt(Instant.now());
        message.setUpdatedAt(Instant.now());
        return message;
    }

    public void markAsRead() {
        this.readAt = Instant.now();
        this.updateTimestamp();
    }

    public void editMessage(String newContent) {
        this.messageContent = newContent;
        this.editedAt = Instant.now();
        this.updateTimestamp();
    }

    public void deleteMessage() {
        this.isDeleted = true;
        this.messageContent = "[Message deleted]";
        this.updateTimestamp();
    }

    public enum SenderType {
        CUSTOMER, AGENT, SYSTEM, BOT
    }

    public enum MessageType {
        TEXT, IMAGE, FILE, SYSTEM, RATING, TYPING_INDICATOR
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageAttachment {
        private String fileName;
        private String fileUrl;
        private String fileSize;
        private String contentType;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageMetadata {
        private String ipAddress;
        private String userAgent;
        private String browser;
        private String os;
        private String location;
    }
}
