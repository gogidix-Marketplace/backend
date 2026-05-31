package com.gogidix.shared.messaging.adapter.out.persistence;

import com.gogidix.shared.messaging.domain.model.Message;
import com.gogidix.shared.messaging.domain.valueobject.MessageStatus;
import com.gogidix.shared.messaging.domain.valueobject.MessageType;
import com.gogidix.shared.messaging.domain.valueobject.MessagePriority;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * JPA entity for message persistence.
 * Maps domain Message to database representation.
 */
@Entity
@Table(name = "messages")
@NamedQueries({
    @NamedQuery(
        name = "MessageEntity.findRetriableMessages",
        query = "SELECT m FROM MessageEntity m WHERE m.status = 'FAILED' AND m.retryCount < m.maxRetries"
    )
})
public class MessageEntity {
    
    @Id
    @Column(name = "message_id", nullable = false)
    private String messageId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private MessageType type;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private MessagePriority priority;
    
    @Column(name = "subject", nullable = false, length = 1000)
    private String subject;
    
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "sender", nullable = false, length = 500)
    private String sender;
    
    @Column(name = "recipient", nullable = false, length = 500)
    private String recipient;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MessageStatus status;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "sent_at")
    private LocalDateTime sentAt;
    
    @Column(name = "read_at")
    private LocalDateTime readAt;
    
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    
    @ElementCollection
    @CollectionTable(name = "message_metadata", joinColumns = @JoinColumn(name = "message_id"))
    @MapKeyColumn(name = "key_name")
    @Column(name = "key_value")
    private Map<String, String> metadata = new HashMap<>();
    
    @Column(name = "thread_id", length = 255)
    private String threadId;
    
    @Column(name = "reply_to_message_id", length = 255)
    private String replyToMessageId;
    
    @Column(name = "is_read", nullable = false)
    private boolean read = false;
    
    @Column(name = "is_archived", nullable = false)
    private boolean archived = false;
    
    @Column(name = "retry_count", nullable = false)
    private int retryCount = 0;
    
    @Column(name = "max_retries", nullable = false)
    private int maxRetries = 3;
    
    @Column(name = "error_message", length = 2000)
    private String errorMessage;
    
    @Version
    @Column(name = "version")
    private Long version;
    
    @Column(name = "created_by", length = 255)
    private String createdBy;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "updated_by", length = 255)
    private String updatedBy;
    
    // Default constructor for JPA
    protected MessageEntity() {}
    
    // Constructor for creating new entities
    public MessageEntity(String messageId, MessageType type, MessagePriority priority, 
                        String subject, String content, String sender, String recipient) {
        this.messageId = messageId;
        this.type = type;
        this.priority = priority;
        this.subject = subject;
        this.content = content;
        this.sender = sender;
        this.recipient = recipient;
        this.status = MessageStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.read = false;
        this.archived = false;
        this.retryCount = 0;
        this.maxRetries = 3;
    }
    
    // Conversion methods
    public static MessageEntity fromDomain(Message domainMessage) {
        MessageEntity entity = new MessageEntity();
        
        entity.messageId = domainMessage.getMessageId();
        entity.type = domainMessage.getType();
        entity.priority = domainMessage.getPriority();
        entity.subject = domainMessage.getSubject();
        entity.content = domainMessage.getContent();
        entity.sender = domainMessage.getSender();
        entity.recipient = domainMessage.getRecipient();
        entity.status = domainMessage.getStatus();
        entity.createdAt = domainMessage.getCreatedAt();
        entity.sentAt = domainMessage.getSentAt();
        entity.readAt = domainMessage.getReadAt();
        entity.expiresAt = domainMessage.getExpiresAt();
        entity.threadId = domainMessage.getThreadId();
        entity.replyToMessageId = domainMessage.getReplyToMessageId();
        entity.read = domainMessage.isRead();
        entity.archived = domainMessage.isArchived();
        entity.retryCount = domainMessage.getRetryCount();
        entity.maxRetries = domainMessage.getMaxRetries();
        entity.errorMessage = domainMessage.getErrorMessage();
        
        // Convert metadata - simplified string conversion
        if (domainMessage.getMetadata() != null) {
            domainMessage.getMetadata().forEach((key, value) -> 
                entity.metadata.put(key, value != null ? value.toString() : null));
        }
        
        return entity;
    }
    
    public Message toDomain() {
        // Convert string metadata back to Object map
        Map<String, Object> domainMetadata = new HashMap<>();
        if (metadata != null) {
            metadata.forEach((key, value) -> domainMetadata.put(key, value));
        }
        
        return Message.builder()
                .messageId(messageId)
                .type(type)
                .priority(priority)
                .subject(subject)
                .content(content)
                .sender(sender)
                .recipient(recipient)
                .status(status)
                .createdAt(createdAt)
                .sentAt(sentAt)
                .readAt(readAt)
                .expiresAt(expiresAt)
                .metadata(domainMetadata)
                .threadId(threadId)
                .replyToMessageId(replyToMessageId)
                .read(read)
                .archived(archived)
                .retryCount(retryCount)
                .maxRetries(maxRetries)
                .errorMessage(errorMessage)
                .build();
    }
    
    // JPA lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters and setters
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
    
    public MessageType getType() { return type; }
    public void setType(MessageType type) { this.type = type; }
    
    public MessagePriority getPriority() { return priority; }
    public void setPriority(MessagePriority priority) { this.priority = priority; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    
    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
    
    public MessageStatus getStatus() { return status; }
    public void setStatus(MessageStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
    
    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }
    
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    
    public Map<String, String> getMetadata() { return metadata; }
    public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }
    
    public String getThreadId() { return threadId; }
    public void setThreadId(String threadId) { this.threadId = threadId; }
    
    public String getReplyToMessageId() { return replyToMessageId; }
    public void setReplyToMessageId(String replyToMessageId) { this.replyToMessageId = replyToMessageId; }
    
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
    
    public boolean isArchived() { return archived; }
    public void setArchived(boolean archived) { this.archived = archived; }
    
    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
    
    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageEntity that = (MessageEntity) o;
        return messageId != null && messageId.equals(that.messageId);
    }
    
    @Override
    public int hashCode() {
        return messageId != null ? messageId.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "MessageEntity{" +
                "messageId='" + messageId + '\'' +
                ", type=" + type +
                ", priority=" + priority +
                ", subject='" + subject + '\'' +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}