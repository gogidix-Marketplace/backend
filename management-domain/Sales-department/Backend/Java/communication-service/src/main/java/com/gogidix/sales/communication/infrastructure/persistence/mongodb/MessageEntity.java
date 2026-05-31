package com.gogidix.sales.communication.infrastructure.persistence.mongodb;

import com.gogidix.sales.communication.domain.model.Message;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MongoDB Entity for Message
 * Separate from domain model for clean architecture
 */
@Document(collection = "messages")
public class MessageEntity {

    @Id
    private String id;

    @Indexed
    private String messageId;

    @Indexed
    private String tenantId;

    @Indexed
    private String conversationId;

    @Indexed
    private String senderId;

    private String senderName;
    private String senderType;
    private List<String> recipientIds;
    private Message.ChannelType channel;
    private String subject;
    private String content;
    private String templateId;
    private Map<String, Object> templateVariables;
    private Message.MessageStatus status;
    private Boolean isRead;
    private Instant readAt;
    private String readBy;
    private List<RecipientInfo> recipients;
    private List<Attachment> attachments;
    private String parentMessageId;
    private Boolean isSystemMessage;
    private Integer priority;
    private Instant scheduledAt;
    private Instant sentAt;
    private Instant deliveredAt;
    private String externalMessageId;
    private Map<String, String> metadata;
    private Integer retryCount;
    private String errorMessage;
    private Instant createdAt;
    private Instant updatedAt;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getConversationId() { return conversationId; }
    public void setConversationId(String conversationId) { this.conversationId = conversationId; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getSenderType() { return senderType; }
    public void setSenderType(String senderType) { this.senderType = senderType; }

    public List<String> getRecipientIds() { return recipientIds; }
    public void setRecipientIds(List<String> recipientIds) { this.recipientIds = recipientIds; }

    public Message.ChannelType getChannel() { return channel; }
    public void setChannel(Message.ChannelType channel) { this.channel = channel; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }

    public Map<String, Object> getTemplateVariables() { return templateVariables; }
    public void setTemplateVariables(Map<String, Object> templateVariables) { this.templateVariables = templateVariables; }

    public Message.MessageStatus getStatus() { return status; }
    public void setStatus(Message.MessageStatus status) { this.status = status; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

    public Instant getReadAt() { return readAt; }
    public void setReadAt(Instant readAt) { this.readAt = readAt; }

    public String getReadBy() { return readBy; }
    public void setReadBy(String readBy) { this.readBy = readBy; }

    public List<RecipientInfo> getRecipients() { return recipients; }
    public void setRecipients(List<RecipientInfo> recipients) { this.recipients = recipients; }

    public List<Attachment> getAttachments() { return attachments; }
    public void setAttachments(List<Attachment> attachments) { this.attachments = attachments; }

    public String getParentMessageId() { return parentMessageId; }
    public void setParentMessageId(String parentMessageId) { this.parentMessageId = parentMessageId; }

    public Boolean getIsSystemMessage() { return isSystemMessage; }
    public void setIsSystemMessage(Boolean isSystemMessage) { this.isSystemMessage = isSystemMessage; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    public Instant getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(Instant scheduledAt) { this.scheduledAt = scheduledAt; }

    public Instant getSentAt() { return sentAt; }
    public void setSentAt(Instant sentAt) { this.sentAt = sentAt; }

    public Instant getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(Instant deliveredAt) { this.deliveredAt = deliveredAt; }

    public String getExternalMessageId() { return externalMessageId; }
    public void setExternalMessageId(String externalMessageId) { this.externalMessageId = externalMessageId; }

    public Map<String, String> getMetadata() { return metadata; }
    public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }

    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // Embedded classes for MongoDB
    public static class RecipientInfo {
        private String recipientId;
        private String recipientName;
        private String recipientType;
        private String emailAddress;
        private String phoneNumber;
        private Boolean isRead;
        private Instant readAt;
        private String deliveryStatus;
        private Instant deliveredAt;

        // Getters and setters
        public String getRecipientId() { return recipientId; }
        public void setRecipientId(String recipientId) { this.recipientId = recipientId; }

        public String getRecipientName() { return recipientName; }
        public void setRecipientName(String recipientName) { this.recipientName = recipientName; }

        public String getRecipientType() { return recipientType; }
        public void setRecipientType(String recipientType) { this.recipientType = recipientType; }

        public String getEmailAddress() { return emailAddress; }
        public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

        public Boolean getIsRead() { return isRead; }
        public void setIsRead(Boolean isRead) { this.isRead = isRead; }

        public Instant getReadAt() { return readAt; }
        public void setReadAt(Instant readAt) { this.readAt = readAt; }

        public String getDeliveryStatus() { return deliveryStatus; }
        public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }

        public Instant getDeliveredAt() { return deliveredAt; }
        public void setDeliveredAt(Instant deliveredAt) { this.deliveredAt = deliveredAt; }
    }

    public static class Attachment {
        private String attachmentId;
        private String fileName;
        private String fileType;
        private Long fileSize;
        private String fileUrl;
        private String storageProvider;
        private String contentType;

        // Getters and setters
        public String getAttachmentId() { return attachmentId; }
        public void setAttachmentId(String attachmentId) { this.attachmentId = attachmentId; }

        public String getFileName() { return fileName; }
        public void setFileName(String fileName) { this.fileName = fileName; }

        public String getFileType() { return fileType; }
        public void setFileType(String fileType) { this.fileType = fileType; }

        public Long getFileSize() { return fileSize; }
        public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

        public String getFileUrl() { return fileUrl; }
        public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

        public String getStorageProvider() { return storageProvider; }
        public void setStorageProvider(String storageProvider) { this.storageProvider = storageProvider; }

        public String getContentType() { return contentType; }
        public void setContentType(String contentType) { this.contentType = contentType; }
    }

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
}
