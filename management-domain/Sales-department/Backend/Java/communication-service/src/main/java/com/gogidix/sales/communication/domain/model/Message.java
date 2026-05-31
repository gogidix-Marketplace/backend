package com.gogidix.sales.communication.domain.model;

import com.gogidix.sales.communication.domain.event.MessageSentEvent;
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
 * Message Domain Entity
 * Multi-tenant message with read/unread tracking and attachment support
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "messages")
public class Message extends AuditableEntity {

    @Indexed
    private String messageId;

    @Indexed
    private String tenantId;

    @Indexed
    private String conversationId;

    @Indexed
    private String senderId;

    private String senderName;

    private String senderType; // USER, SYSTEM, BOT

    @Indexed
    private List<String> recipientIds;

    private List<RecipientInfo> recipients;

    private ChannelType channel;

    private String subject;

    private String content;

    private String templateId;

    private Map<String, Object> templateVariables;

    private MessageStatus status;

    @Indexed
    private Boolean isRead;

    private Instant readAt;

    private String readBy;

    private List<Attachment> attachments;

    private String parentMessageId; // For threaded conversations

    private Boolean isSystemMessage;

    private Integer priority; // 1=low, 2=normal, 3=high, 4=urgent

    private Instant scheduledAt;

    private Instant sentAt;

    private Instant deliveredAt;

    private String externalMessageId; // ID from external provider (email, SMS, etc.)

    private Map<String, String> metadata;

    private Integer retryCount;

    private String errorMessage;

    @Builder.Default
    private List<MessageSentEvent> domainEvents = new ArrayList<>();

    public enum ChannelType {
        EMAIL,
        SMS,
        IN_APP,
        WHATSAPP,
        PUSH_NOTIFICATION
    }

    public enum MessageStatus {
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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipientInfo {
        private String recipientId;
        private String recipientName;
        private String recipientType; // USER, GROUP, EXTERNAL
        private String emailAddress;
        private String phoneNumber;
        private Boolean isRead;
        private Instant readAt;
        private String deliveryStatus; // PENDING, DELIVERED, FAILED, BOUNCED
        private Instant deliveredAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Attachment {
        private String attachmentId;
        private String fileName;
        private String fileType;
        private Long fileSize;
        private String fileUrl;
        private String storageProvider; // S3, AZURE, LOCAL
        private String contentType;
    }

    /**
     * Creates a new message
     */
    public static Message create(String tenantId, String conversationId, String senderId,
                                  String senderName, List<RecipientInfo> recipients,
                                  ChannelType channel, String subject, String content) {
        Message message = Message.builder()
                .tenantId(tenantId)
                .conversationId(conversationId)
                .senderId(senderId)
                .senderName(senderName)
                .senderType("USER")
                .recipients(recipients != null ? recipients : new ArrayList<>())
                .channel(channel)
                .subject(subject)
                .content(content)
                .status(MessageStatus.DRAFT)
                .isRead(false)
                .isSystemMessage(false)
                .priority(2)
                .retryCount(0)
                .templateVariables(new HashMap<>())
                .metadata(new HashMap<>())
                .attachments(new ArrayList<>())
                .build();

        // Extract recipient IDs
        if (recipients != null) {
            message.setRecipientIds(recipients.stream()
                    .map(RecipientInfo::getRecipientId)
                    .toList());
        }

        message.addDomainEvent(MessageSentEvent.builder()
                .messageId(message.getMessageId())
                .tenantId(tenantId)
                .conversationId(conversationId)
                .senderId(senderId)
                .channel(channel.name())
                .subject(subject)
                .timestamp(Instant.now())
                .eventType("MESSAGE_CREATED")
                .build());

        return message;
    }

    /**
     * Creates a message from template
     */
    public static Message createFromTemplate(String tenantId, String conversationId,
                                              String senderId, String templateId,
                                              Map<String, Object> templateVariables,
                                              List<RecipientInfo> recipients,
                                              ChannelType channel) {
        Message message = create(tenantId, conversationId, senderId, null,
                recipients, channel, null, null);
        message.setTemplateId(templateId);
        message.setTemplateVariables(templateVariables);
        return message;
    }

    /**
     * Sends the message
     */
    public void send() {
        if (this.status != MessageStatus.DRAFT && this.status != MessageStatus.SCHEDULED) {
            throw new IllegalStateException("Can only send draft or scheduled messages");
        }

        if (this.recipients == null || this.recipients.isEmpty()) {
            throw new IllegalStateException("Message must have at least one recipient");
        }

        if (this.content == null && this.templateId == null) {
            throw new IllegalStateException("Message must have content or a template");
        }

        this.status = MessageStatus.SENDING;
        this.sentAt = Instant.now();

        addDomainEvent(MessageSentEvent.builder()
                .messageId(this.messageId)
                .tenantId(this.tenantId)
                .conversationId(this.conversationId)
                .senderId(this.senderId)
                .channel(this.channel.name())
                .subject(this.subject)
                .recipientCount(this.recipients.size())
                .timestamp(Instant.now())
                .eventType("MESSAGE_SENDING")
                .build());
    }

    /**
     * Marks the message as sent
     */
    public void markAsSent(String externalMessageId) {
        this.status = MessageStatus.SENT;
        this.sentAt = Instant.now();
        this.externalMessageId = externalMessageId;

        addDomainEvent(MessageSentEvent.builder()
                .messageId(this.messageId)
                .tenantId(this.tenantId)
                .conversationId(this.conversationId)
                .senderId(this.senderId)
                .channel(this.channel.name())
                .externalMessageId(externalMessageId)
                .timestamp(Instant.now())
                .eventType("MESSAGE_SENT")
                .build());
    }

    /**
     * Marks the message as delivered
     */
    public void markAsDelivered() {
        this.status = MessageStatus.DELIVERED;
        this.deliveredAt = Instant.now();

        addDomainEvent(MessageSentEvent.builder()
                .messageId(this.messageId)
                .tenantId(this.tenantId)
                .conversationId(this.conversationId)
                .timestamp(Instant.now())
                .eventType("MESSAGE_DELIVERED")
                .build());
    }

    /**
     * Marks the message as failed
     */
    public void markAsFailed(String errorMessage) {
        this.status = MessageStatus.FAILED;
        this.errorMessage = errorMessage;
        this.retryCount++;

        addDomainEvent(MessageSentEvent.builder()
                .messageId(this.messageId)
                .tenantId(this.tenantId)
                .conversationId(this.conversationId)
                .errorMessage(errorMessage)
                .retryCount(this.retryCount)
                .timestamp(Instant.now())
                .eventType("MESSAGE_FAILED")
                .build());
    }

    /**
     * Marks the message as read
     */
    public void markAsRead(String readBy) {
        this.isRead = true;
        this.readAt = Instant.now();
        this.readBy = readBy;
        this.status = MessageStatus.READ;

        addDomainEvent(MessageSentEvent.builder()
                .messageId(this.messageId)
                .tenantId(this.tenantId)
                .conversationId(this.conversationId)
                .readBy(readBy)
                .timestamp(Instant.now())
                .eventType("MESSAGE_READ")
                .build());
    }

    /**
     * Marks a recipient as read
     */
    public void markRecipientAsRead(String recipientId) {
        if (this.recipients != null) {
            this.recipients.stream()
                    .filter(r -> r.getRecipientId().equals(recipientId))
                    .forEach(r -> {
                        r.setIsRead(true);
                        r.setReadAt(Instant.now());
                    });
        }
    }

    /**
     * Adds an attachment to the message
     */
    public void addAttachment(Attachment attachment) {
        if (this.attachments == null) {
            this.attachments = new ArrayList<>();
        }
        this.attachments.add(attachment);
    }

    /**
     * Removes an attachment from the message
     */
    public void removeAttachment(String attachmentId) {
        if (this.attachments != null) {
            this.attachments.removeIf(a -> a.getAttachmentId().equals(attachmentId));
        }
    }

    /**
     * Schedules the message for later sending
     */
    public void schedule(Instant scheduledAt) {
        if (scheduledAt.isBefore(Instant.now())) {
            throw new IllegalStateException("Scheduled time must be in the future");
        }
        this.status = MessageStatus.SCHEDULED;
        this.scheduledAt = scheduledAt;
    }

    /**
     * Adds metadata to the message
     */
    public void addMetadata(String key, String value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }

    /**
     * Sets the priority of the message
     */
    public void setPriorityValue(Integer priority) {
        if (priority < 1 || priority > 4) {
            throw new IllegalArgumentException("Priority must be between 1 and 4");
        }
        this.priority = priority;
    }

    public void addDomainEvent(MessageSentEvent event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }
}
