package com.gogidix.shared.messaging.domain.model;

import com.gogidix.shared.messaging.domain.valueobject.MessagePriority;
import com.gogidix.shared.messaging.domain.valueobject.MessageStatus;
import com.gogidix.shared.messaging.domain.valueobject.MessageType;
import lombok.Getter;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

/**
 * Rich Domain Model for Message - HEXAGONAL ARCHITECTURE TEMPLATE
 * 
 * This serves as the comprehensive messaging domain model for Agent B services.
 * Demonstrates:
 * - Rich business logic for messaging and event processing
 * - Zero infrastructure dependencies (NO Lombok, JPA, etc.)
 * - Immutable design with builder pattern
 * - Kafka/messaging routing and delivery management
 * - Message lifecycle and retry logic
 * 
 * NOTE: @Getter added to generate accessors for final fields
 * 
 * @author Agent A - Foundation Lead
 * @template-for Agent B Messaging Integration
 * @version 1.0.0
 */
@Getter
public class Message {
    
    // Core identifiers (immutable)
    private final String messageId;
    private final String correlationId;
    private final String threadId;
    private final String replyToMessageId;
    private final String parentMessageId;
    
    // Message metadata (immutable)
    private final MessageType type;
    private final MessagePriority priority;
    private final String subject;
    private final String content;
    private final String contentType;
    private final String encoding;
    
    // Routing information (immutable)
    private final String sender;
    private final String recipient;
    private final Set<String> recipients; // For broadcast messages
    private final String topic;
    private final String routingKey;
    private final String exchange;
    
    // Status and lifecycle (immutable)
    private final MessageStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime scheduledAt;
    private final LocalDateTime sentAt;
    private final LocalDateTime deliveredAt;
    private final LocalDateTime readAt;
    private final LocalDateTime expiresAt;
    
    // Delivery and retry (immutable)
    private final boolean requiresAck;
    private final boolean requiresDeliveryConfirmation;
    private final int retryCount;
    private final int maxRetries;
    private final Duration retryDelay;
    private final String errorMessage;
    private final List<String> deliveryAttempts;
    
    // Business context (immutable)
    private final Map<String, Object> headers;
    private final Map<String, Object> metadata;
    private final Set<String> tags;
    private final String businessDomain;
    private final String eventType;
    
    // Security and validation (immutable)
    private final boolean encrypted;
    private final boolean signed;
    private final String checksum;
    private final MessageSecurityLevel securityLevel;
    
    // Basic constructor for essential fields
    public Message(String messageId, MessageType type, String subject, String content,
                  String sender, String recipient) {
        this.messageId = Objects.requireNonNull(messageId, "Message ID cannot be null");
        this.type = Objects.requireNonNull(type, "Message type cannot be null");
        this.subject = Objects.requireNonNull(subject, "Subject cannot be null");
        this.content = Objects.requireNonNull(content, "Content cannot be null");
        this.sender = Objects.requireNonNull(sender, "Sender cannot be null");
        this.recipient = Objects.requireNonNull(recipient, "Recipient cannot be null");
        
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.status = MessageStatus.PENDING;
        
        // Defaults
        this.correlationId = null;
        this.threadId = messageId; // Default thread is itself
        this.replyToMessageId = null;
        this.parentMessageId = null;
        this.priority = MessagePriority.NORMAL;
        this.contentType = "text/plain";
        this.encoding = "UTF-8";
        this.recipients = new HashSet<>();
        this.topic = null;
        this.routingKey = generateDefaultRoutingKey();
        this.exchange = "default";
        this.scheduledAt = null;
        this.sentAt = null;
        this.deliveredAt = null;
        this.readAt = null;
        this.expiresAt = null;
        this.requiresAck = false;
        this.requiresDeliveryConfirmation = false;
        this.retryCount = 0;
        this.maxRetries = 3;
        this.retryDelay = Duration.ofSeconds(30);
        this.errorMessage = null;
        this.deliveryAttempts = new ArrayList<>();
        this.headers = new HashMap<>();
        this.metadata = new HashMap<>();
        this.tags = new HashSet<>();
        this.businessDomain = "general";
        this.eventType = null;
        this.encrypted = false;
        this.signed = false;
        this.checksum = null;
        this.securityLevel = MessageSecurityLevel.STANDARD;
    }
    
    // Full constructor for complete message creation
    public Message(String messageId, String correlationId, String threadId, String replyToMessageId, String parentMessageId,
                  MessageType type, MessagePriority priority, String subject, String content, String contentType, String encoding,
                  String sender, String recipient, Set<String> recipients, String topic, String routingKey, String exchange,
                  MessageStatus status, LocalDateTime createdAt, LocalDateTime scheduledAt, LocalDateTime sentAt,
                  LocalDateTime deliveredAt, LocalDateTime readAt, LocalDateTime expiresAt,
                  boolean requiresAck, boolean requiresDeliveryConfirmation, int retryCount, int maxRetries,
                  Duration retryDelay, String errorMessage, List<String> deliveryAttempts,
                  Map<String, Object> headers, Map<String, Object> metadata, Set<String> tags,
                  String businessDomain, String eventType, boolean encrypted, boolean signed,
                  String checksum, MessageSecurityLevel securityLevel) {
        this.messageId = messageId;
        this.correlationId = correlationId;
        this.threadId = threadId;
        this.replyToMessageId = replyToMessageId;
        this.parentMessageId = parentMessageId;
        this.type = type;
        this.priority = priority;
        this.subject = subject;
        this.content = content;
        this.contentType = contentType;
        this.encoding = encoding;
        this.sender = sender;
        this.recipient = recipient;
        this.recipients = recipients != null ? new HashSet<>(recipients) : new HashSet<>();
        this.topic = topic;
        this.routingKey = routingKey;
        this.exchange = exchange;
        this.status = status;
        this.createdAt = createdAt;
        this.scheduledAt = scheduledAt;
        this.sentAt = sentAt;
        this.deliveredAt = deliveredAt;
        this.readAt = readAt;
        this.expiresAt = expiresAt;
        this.requiresAck = requiresAck;
        this.requiresDeliveryConfirmation = requiresDeliveryConfirmation;
        this.retryCount = retryCount;
        this.maxRetries = maxRetries;
        this.retryDelay = retryDelay;
        this.errorMessage = errorMessage;
        this.deliveryAttempts = deliveryAttempts != null ? new ArrayList<>(deliveryAttempts) : new ArrayList<>();
        this.headers = headers != null ? new HashMap<>(headers) : new HashMap<>();
        this.metadata = metadata != null ? new HashMap<>(metadata) : new HashMap<>();
        this.tags = tags != null ? new HashSet<>(tags) : new HashSet<>();
        this.businessDomain = businessDomain;
        this.eventType = eventType;
        this.encrypted = encrypted;
        this.signed = signed;
        this.checksum = checksum;
        this.securityLevel = securityLevel != null ? securityLevel : MessageSecurityLevel.STANDARD;
    }
    
    // ==============================================
    // MESSAGING BUSINESS LOGIC METHODS - Template
    // ==============================================
    
    /**
     * Business Rule: Check if message is valid for sending
     */
    public boolean isValid() {
        return messageId != null && !messageId.trim().isEmpty() &&
               type != null &&
               subject != null && !subject.trim().isEmpty() &&
               content != null && !content.trim().isEmpty() &&
               sender != null && !sender.trim().isEmpty() &&
               (recipient != null || !recipients.isEmpty()) &&
               hasValidStructure() &&
               !isExpired();
    }
    
    /**
     * Business Rule: Check if message has expired
     */
    public boolean isExpired() {
        if (expiresAt == null) return false;
        return LocalDateTime.now().isAfter(expiresAt);
    }
    
    /**
     * Business Rule: Check if message is high priority
     */
    public boolean isHighPriority() {
        return priority == MessagePriority.HIGH || 
               priority == MessagePriority.URGENT ||
               priority == MessagePriority.CRITICAL;
    }
    
    /**
     * Business Rule: Check if message is ready to send
     */
    public boolean isReadyToSend() {
        return status == MessageStatus.PENDING &&
               isValid() &&
               !isScheduledForLater() &&
               !isExpired();
    }
    
    /**
     * Business Rule: Check if message is scheduled for later delivery
     */
    public boolean isScheduledForLater() {
        if (scheduledAt == null) return false;
        return LocalDateTime.now().isBefore(scheduledAt);
    }
    
    /**
     * Business Rule: Check if message can be retried
     */
    public boolean canRetry() {
        return status == MessageStatus.FAILED &&
               retryCount < maxRetries &&
               !isExpired() &&
               hasRetryableError();
    }
    
    /**
     * Business Rule: Check if message requires acknowledgment
     */
    public boolean requiresAcknowledgment() {
        return requiresAck ||
               type == MessageType.SYSTEM_ALERT ||
               type == MessageType.SECURITY_NOTIFICATION ||
               priority == MessagePriority.URGENT ||
               priority == MessagePriority.CRITICAL;
    }
    
    /**
     * Business Rule: Check if message has been read
     */
    public boolean isRead() {
        return readAt != null;
    }

    /**
     * Business Rule: Check if message should be persisted
     */
    public boolean shouldPersist() {
        return type != MessageType.TRANSIENT &&
               !isExpired() &&
               (type.requiresPersistence() || isHighPriority());
    }
    
    /**
     * Business Rule: Check if message is a broadcast
     */
    public boolean isBroadcast() {
        return type == MessageType.BROADCAST ||
               (recipients != null && recipients.size() > 1);
    }
    
    /**
     * Business Rule: Check if message is a reply
     */
    public boolean isReply() {
        return replyToMessageId != null && !replyToMessageId.trim().isEmpty();
    }
    
    /**
     * Business Rule: Check if message is part of a thread
     */
    public boolean isPartOfThread() {
        return threadId != null && !threadId.equals(messageId);
    }
    
    /**
     * Business Rule: Check if message requires encryption
     */
    public boolean requiresEncryption() {
        return securityLevel.requiresEncryption() ||
               type == MessageType.CONFIDENTIAL ||
               hasSecurityTag("encrypt") ||
               containsSensitiveData();
    }
    
    /**
     * Business Rule: Check if message requires digital signature
     */
    public boolean requiresDigitalSignature() {
        return securityLevel.requiresSignature() ||
               type == MessageType.LEGAL_DOCUMENT ||
               hasSecurityTag("sign") ||
               isFinancialMessage();
    }
    
    /**
     * Business Calculation: Get message age
     */
    public Duration getAge() {
        if (createdAt == null) return Duration.ZERO;
        return Duration.between(createdAt, LocalDateTime.now());
    }
    
    /**
     * Business Calculation: Get delivery time (if delivered)
     */
    public Duration getDeliveryTime() {
        if (sentAt == null || deliveredAt == null) return null;
        return Duration.between(sentAt, deliveredAt);
    }
    
    /**
     * Business Calculation: Get time until expiration
     */
    public Duration getTimeUntilExpiration() {
        if (expiresAt == null) return Duration.ofDays(365); // Never expires
        
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(expiresAt)) return Duration.ZERO;
        
        return Duration.between(now, expiresAt);
    }
    
    /**
     * Business Calculation: Get message priority score (0-100)
     */
    public int getPriorityScore() {
        int score = priority.getScore();
        
        // Boost for urgent types
        if (type == MessageType.SYSTEM_ALERT) score += 20;
        if (type == MessageType.SECURITY_NOTIFICATION) score += 15;
        if (type == MessageType.ERROR_NOTIFICATION) score += 10;
        
        // Boost for security
        if (securityLevel.getLevel() >= MessageSecurityLevel.HIGH.getLevel()) score += 10;
        
        // Boost for retry attempts (indicates importance)
        score += Math.min(retryCount * 5, 15);
        
        return Math.min(100, score);
    }
    
    /**
     * Business Calculation: Get estimated message size in bytes
     */
    public long getEstimatedSize() {
        long size = 0;
        
        // Text content
        if (subject != null) size += subject.getBytes().length;
        if (content != null) size += content.getBytes().length;
        if (sender != null) size += sender.getBytes().length;
        if (recipient != null) size += recipient.getBytes().length;
        
        // Headers and metadata (estimate)
        size += headers.size() * 50; // Average header size
        size += metadata.size() * 100; // Average metadata size
        
        // Recipients for broadcast
        size += recipients.size() * 50;
        
        return size;
    }
    
    /**
     * Business Rule: Check if message is large
     */
    public boolean isLargeMessage() {
        return getEstimatedSize() > 1024 * 1024; // 1MB threshold
    }
    
    /**
     * Business Logic: Generate routing key for message queues
     */
    public String generateRoutingKey() {
        if (routingKey != null) return routingKey;
        
        StringBuilder key = new StringBuilder();
        key.append(businessDomain.toLowerCase());
        key.append(".");
        key.append(type.name().toLowerCase());
        key.append(".");
        key.append(priority.name().toLowerCase());
        
        if (eventType != null) {
            key.append(".").append(eventType.toLowerCase());
        }
        
        return key.toString();
    }
    
    /**
     * Business Logic: Get message classification
     */
    public MessageClassification getClassification() {
        if (isHighPriority() && requiresEncryption()) return MessageClassification.HIGH_SECURITY;
        if (isBroadcast()) return MessageClassification.BROADCAST;
        if (isReply()) return MessageClassification.REPLY;
        if (type == MessageType.DOMAIN_EVENT) return MessageClassification.DOMAIN_EVENT;
        if (type == MessageType.COMMAND) return MessageClassification.COMMAND;
        if (type == MessageType.QUERY) return MessageClassification.QUERY;
        return MessageClassification.STANDARD;
    }
    
    /**
     * Business Logic: Create sent version of message
     */
    public Message markAsSent() {
        if (status != MessageStatus.PENDING) {
            throw new IllegalStateException("Message must be PENDING to mark as sent");
        }
        if (!isValid()) {
            throw new IllegalStateException("Cannot send invalid message");
        }
        
        List<String> newAttempts = new ArrayList<>(this.deliveryAttempts);
        newAttempts.add("SENT:" + LocalDateTime.now());
        
        return new Message(
            this.messageId, this.correlationId, this.threadId, this.replyToMessageId, this.parentMessageId,
            this.type, this.priority, this.subject, this.content, this.contentType, this.encoding,
            this.sender, this.recipient, this.recipients, this.topic, this.routingKey, this.exchange,
            MessageStatus.SENT, this.createdAt, this.scheduledAt, LocalDateTime.now(),
            this.deliveredAt, this.readAt, this.expiresAt,
            this.requiresAck, this.requiresDeliveryConfirmation, this.retryCount, this.maxRetries,
            this.retryDelay, this.errorMessage, newAttempts,
            this.headers, this.metadata, this.tags,
            this.businessDomain, this.eventType, this.encrypted, this.signed,
            this.checksum, this.securityLevel
        );
    }
    
    /**
     * Business Logic: Create delivered version of message
     */
    public Message markAsDelivered() {
        if (status != MessageStatus.SENT) {
            throw new IllegalStateException("Message must be SENT to mark as delivered");
        }
        
        List<String> newAttempts = new ArrayList<>(this.deliveryAttempts);
        newAttempts.add("DELIVERED:" + LocalDateTime.now());
        
        return new Message(
            this.messageId, this.correlationId, this.threadId, this.replyToMessageId, this.parentMessageId,
            this.type, this.priority, this.subject, this.content, this.contentType, this.encoding,
            this.sender, this.recipient, this.recipients, this.topic, this.routingKey, this.exchange,
            MessageStatus.DELIVERED, this.createdAt, this.scheduledAt, this.sentAt,
            LocalDateTime.now(), this.readAt, this.expiresAt,
            this.requiresAck, this.requiresDeliveryConfirmation, this.retryCount, this.maxRetries,
            this.retryDelay, this.errorMessage, newAttempts,
            this.headers, this.metadata, this.tags,
            this.businessDomain, this.eventType, this.encrypted, this.signed,
            this.checksum, this.securityLevel
        );
    }
    
    /**
     * Business Logic: Create failed version of message
     */
    public Message markAsFailed(String errorMsg) {
        List<String> newAttempts = new ArrayList<>(this.deliveryAttempts);
        newAttempts.add("FAILED:" + LocalDateTime.now() + " - " + errorMsg);
        
        return new Message(
            this.messageId, this.correlationId, this.threadId, this.replyToMessageId, this.parentMessageId,
            this.type, this.priority, this.subject, this.content, this.contentType, this.encoding,
            this.sender, this.recipient, this.recipients, this.topic, this.routingKey, this.exchange,
            MessageStatus.FAILED, this.createdAt, this.scheduledAt, this.sentAt,
            this.deliveredAt, this.readAt, this.expiresAt,
            this.requiresAck, this.requiresDeliveryConfirmation, this.retryCount, this.maxRetries,
            this.retryDelay, errorMsg, newAttempts,
            this.headers, this.metadata, this.tags,
            this.businessDomain, this.eventType, this.encrypted, this.signed,
            this.checksum, this.securityLevel
        );
    }
    
    /**
     * Business Logic: Create retry version of message
     */
    public Message retry() {
        if (!canRetry()) {
            throw new IllegalStateException("Message cannot be retried: " + getRetryDenialReason());
        }

        List<String> newAttempts = new ArrayList<>(this.deliveryAttempts);
        newAttempts.add("RETRY:" + LocalDateTime.now());

        return new Message(
            this.messageId, this.correlationId, this.threadId, this.replyToMessageId, this.parentMessageId,
            this.type, this.priority, this.subject, this.content, this.contentType, this.encoding,
            this.sender, this.recipient, this.recipients, this.topic, this.routingKey, this.exchange,
            MessageStatus.PENDING, this.createdAt, this.scheduledAt, this.sentAt,
            this.deliveredAt, this.readAt, this.expiresAt,
            this.requiresAck, this.requiresDeliveryConfirmation, this.retryCount + 1, this.maxRetries,
            this.retryDelay, this.errorMessage, newAttempts,
            this.headers, this.metadata, this.tags,
            this.businessDomain, this.eventType, this.encrypted, this.signed,
            this.checksum, this.securityLevel
        );
    }
    
    /**
     * Alias for retry() method - increments retry count
     */
    public Message incrementRetry() {
        if (!canRetry()) {
            throw new IllegalStateException("Message cannot be retried: " + getRetryDenialReason());
        }
        
        List<String> newAttempts = new ArrayList<>(this.deliveryAttempts);
        newAttempts.add("RETRY:" + LocalDateTime.now());
        
        return new Message(
            this.messageId, this.correlationId, this.threadId, this.replyToMessageId, this.parentMessageId,
            this.type, this.priority, this.subject, this.content, this.contentType, this.encoding,
            this.sender, this.recipient, this.recipients, this.topic, this.routingKey, this.exchange,
            MessageStatus.PENDING, this.createdAt, this.scheduledAt, null,
            null, this.readAt, this.expiresAt,
            this.requiresAck, this.requiresDeliveryConfirmation, this.retryCount + 1, this.maxRetries,
            this.retryDelay, null, newAttempts,
            this.headers, this.metadata, this.tags,
            this.businessDomain, this.eventType, this.encrypted, this.signed,
            this.checksum, this.securityLevel
        );
    }
    
    /**
     * Business Logic: Create enhanced security version
     */
    public Message withEnhancedSecurity(MessageSecurityLevel newLevel, String reason) {
        Map<String, Object> newHeaders = new HashMap<>(this.headers);
        newHeaders.put("security_enhancement_reason", reason);
        newHeaders.put("enhanced_at", LocalDateTime.now().toString());
        newHeaders.put("previous_security_level", this.securityLevel.name());
        
        return new Message(
            this.messageId, this.correlationId, this.threadId, this.replyToMessageId, this.parentMessageId,
            this.type, this.priority, this.subject, this.content, this.contentType, this.encoding,
            this.sender, this.recipient, this.recipients, this.topic, this.routingKey, this.exchange,
            this.status, this.createdAt, this.scheduledAt, this.sentAt,
            this.deliveredAt, this.readAt, this.expiresAt,
            this.requiresAck, this.requiresDeliveryConfirmation, this.retryCount, this.maxRetries,
            this.retryDelay, this.errorMessage, this.deliveryAttempts,
            newHeaders, this.metadata, this.tags,
            this.businessDomain, this.eventType, true, true,
            this.checksum, newLevel
        );
    }
    
    // Private helper methods for business logic
    private boolean hasValidStructure() {
        return messageId != null && messageId.length() >= 8 &&
               contentType != null && !contentType.trim().isEmpty() &&
               encoding != null && !encoding.trim().isEmpty();
    }
    
    private boolean hasRetryableError() {
        if (errorMessage == null) return true;
        
        // Non-retryable errors
        String[] nonRetryableErrors = {"INVALID_RECIPIENT", "PERMISSION_DENIED", "MESSAGE_TOO_LARGE"};
        for (String error : nonRetryableErrors) {
            if (errorMessage.toUpperCase().contains(error)) return false;
        }
        
        return true;
    }
    
    private boolean containsSensitiveData() {
        String lowerContent = content.toLowerCase();
        return lowerContent.contains("password") ||
               lowerContent.contains("ssn") ||
               lowerContent.contains("credit card") ||
               lowerContent.contains("confidential") ||
               hasSecurityTag("sensitive");
    }
    
    private boolean isFinancialMessage() {
        return businessDomain.toLowerCase().contains("financial") ||
               businessDomain.toLowerCase().contains("payment") ||
               hasTag("financial");
    }
    
    private boolean hasSecurityTag(String tag) {
        return tags.stream().anyMatch(t -> t.toLowerCase().contains(tag));
    }
    
    private boolean hasTag(String tag) {
        return tags.contains(tag) || tags.contains(tag.toUpperCase()) || tags.contains(tag.toLowerCase());
    }
    
    private String generateDefaultRoutingKey() {
        return String.format("%s.%s.%s", 
                           "general",
                           type != null ? type.name().toLowerCase() : "message",
                           priority != null ? priority.name().toLowerCase() : "normal");
    }
    
    private String getRetryDenialReason() {
        if (retryCount >= maxRetries) return "Maximum retry count exceeded";
        if (status != MessageStatus.FAILED) return "Message not in failed state";
        if (isExpired()) return "Message expired";
        if (!hasRetryableError()) return "Non-retryable error";
        return "Unknown reason";
    }
    
    private String generateMessageId() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
    
    // Getters (immutable access)
    public String getMessageId() { return messageId; }
    public String getCorrelationId() { return correlationId; }
    public String getThreadId() { return threadId; }
    public String getReplyToMessageId() { return replyToMessageId; }
    public String getParentMessageId() { return parentMessageId; }
    public MessageType getType() { return type; }
    public MessagePriority getPriority() { return priority; }
    public String getSubject() { return subject; }
    public String getContent() { return content; }
    public String getContentType() { return contentType; }
    public String getEncoding() { return encoding; }
    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }
    public Set<String> getRecipients() { return new HashSet<>(recipients); }
    public String getTopic() { return topic; }
    public String getRoutingKey() { return routingKey != null ? routingKey : generateRoutingKey(); }
    public String getExchange() { return exchange; }
    public MessageStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public LocalDateTime getSentAt() { return sentAt; }
    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public LocalDateTime getReadAt() { return readAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public boolean isRequiresAck() { return requiresAck; }
    public boolean isRequiresDeliveryConfirmation() { return requiresDeliveryConfirmation; }
    public int getRetryCount() { return retryCount; }
    public int getMaxRetries() { return maxRetries; }
    public Duration getRetryDelay() { return retryDelay; }
    public String getErrorMessage() { return errorMessage; }
    public List<String> getDeliveryAttempts() { return new ArrayList<>(deliveryAttempts); }
    public Map<String, Object> getHeaders() { return new HashMap<>(headers); }
    public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }
    public Set<String> getTags() { return new HashSet<>(tags); }
    public String getBusinessDomain() { return businessDomain; }
    public String getEventType() { return eventType; }
    public boolean isEncrypted() { return encrypted; }
    public boolean isSigned() { return signed; }
    public String getChecksum() { return checksum; }
    public MessageSecurityLevel getSecurityLevel() { return securityLevel; }
    
    /**
     * Get specific header value
     */
    public Object getHeader(String headerName) {
        return headers.get(headerName);
    }
    
    public String getStringHeader(String headerName, String defaultValue) {
        Object header = getHeader(headerName);
        return header != null ? header.toString() : defaultValue;
    }
    
    /**
     * Get specific metadata value
     */
    public Object getMetadataValue(String key) {
        return metadata.get(key);
    }
    
    public String getStringMetadata(String key, String defaultValue) {
        Object value = getMetadataValue(key);
        return value != null ? value.toString() : defaultValue;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return Objects.equals(messageId, message.messageId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(messageId);
    }
    
    @Override
    public String toString() {
        return String.format("Message{id='%s', type=%s, priority=%s, status=%s, sender='%s', recipient='%s'}", 
                           messageId, type, priority, status, sender, recipient);
    }
    
    // Static factory methods for common message types
    public static Message createUserMessage(String sender, String recipient, String subject, String content) {
        String messageId = java.util.UUID.randomUUID().toString().replace("-", "");
        return new Message(messageId, MessageType.USER_MESSAGE, subject, content, sender, recipient);
    }
    
    public static Message createSystemNotification(String recipient, String subject, String content) {
        String messageId = java.util.UUID.randomUUID().toString().replace("-", "");
        return new Message(messageId, MessageType.SYSTEM_NOTIFICATION, subject, content, "SYSTEM", recipient);
    }
    
    public static Message createDomainEvent(String eventType, String content, String businessDomain) {
        String messageId = java.util.UUID.randomUUID().toString().replace("-", "");
        return new Message(
            messageId, null, messageId, null, null,
            MessageType.DOMAIN_EVENT, MessagePriority.HIGH, eventType, content, "application/json", "UTF-8",
            "DOMAIN_SERVICE", "EVENT_BUS", new HashSet<>(), businessDomain + ".events", null, "domain.exchange",
            MessageStatus.PENDING, LocalDateTime.now(), null, null,
            null, null, null,
            true, false, 0, 5,
            Duration.ofSeconds(10), null, new ArrayList<>(),
            new HashMap<>(), Map.of("business_domain", businessDomain, "event_type", eventType), new HashSet<>(),
            businessDomain, eventType, false, false,
            null, MessageSecurityLevel.STANDARD
        );
    }
    
    public static Message createBroadcast(String sender, Set<String> recipients, String subject, String content) {
        String messageId = java.util.UUID.randomUUID().toString().replace("-", "");
        return new Message(
            messageId, null, messageId, null, null,
            MessageType.BROADCAST, MessagePriority.NORMAL, subject, content, "text/plain", "UTF-8",
            sender, "BROADCAST", recipients, "broadcast", null, "broadcast.exchange",
            MessageStatus.PENDING, LocalDateTime.now(), null, null,
            null, null, null,
            false, true, 0, 3,
            Duration.ofSeconds(30), null, new ArrayList<>(),
            new HashMap<>(), Map.of("recipient_count", recipients.size()), Set.of("broadcast"),
            "general", "broadcast", false, false,
            null, MessageSecurityLevel.STANDARD
        );
    }
    
    // Builder pattern for complex message creation
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String messageId = java.util.UUID.randomUUID().toString().replace("-", "");
        private String correlationId;
        private String threadId;
        private String replyToMessageId;
        private String parentMessageId;
        private MessageType type = MessageType.USER_MESSAGE;
        private MessagePriority priority = MessagePriority.NORMAL;
        private String subject;
        private String content;
        private String contentType = "text/plain";
        private String encoding = "UTF-8";
        private String sender;
        private String recipient;
        private Set<String> recipients = new HashSet<>();
        private String topic;
        private String routingKey;
        private String exchange = "default";
        private MessageStatus status = MessageStatus.PENDING;
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime scheduledAt;
        private LocalDateTime sentAt;
        private LocalDateTime deliveredAt;
        private LocalDateTime readAt;
        private LocalDateTime expiresAt;
        private boolean requiresAck = false;
        private boolean requiresDeliveryConfirmation = false;
        private int retryCount = 0;
        private int maxRetries = 3;
        private Duration retryDelay = Duration.ofSeconds(30);
        private String errorMessage;
        private List<String> deliveryAttempts = new ArrayList<>();
        private Map<String, Object> headers = new HashMap<>();
        private Map<String, Object> metadata = new HashMap<>();
        private Set<String> tags = new HashSet<>();
        private String businessDomain = "general";
        private String eventType;
        private boolean encrypted = false;
        private boolean signed = false;
        private String checksum;
        private MessageSecurityLevel securityLevel = MessageSecurityLevel.STANDARD;
        
        public Builder messageId(String messageId) { this.messageId = messageId; return this; }
        public Builder correlationId(String correlationId) { this.correlationId = correlationId; return this; }
        public Builder threadId(String threadId) { this.threadId = threadId; return this; }
        public Builder replyToMessageId(String replyToMessageId) { this.replyToMessageId = replyToMessageId; return this; }
        public Builder parentMessageId(String parentMessageId) { this.parentMessageId = parentMessageId; return this; }
        public Builder type(MessageType type) { this.type = type; return this; }
        public Builder priority(MessagePriority priority) { this.priority = priority; return this; }
        public Builder subject(String subject) { this.subject = subject; return this; }
        public Builder content(String content) { this.content = content; return this; }
        public Builder contentType(String contentType) { this.contentType = contentType; return this; }
        public Builder encoding(String encoding) { this.encoding = encoding; return this; }
        public Builder sender(String sender) { this.sender = sender; return this; }
        public Builder recipient(String recipient) { this.recipient = recipient; return this; }
        public Builder recipients(Set<String> recipients) { this.recipients = recipients; return this; }
        public Builder topic(String topic) { this.topic = topic; return this; }
        public Builder routingKey(String routingKey) { this.routingKey = routingKey; return this; }
        public Builder exchange(String exchange) { this.exchange = exchange; return this; }
        public Builder status(MessageStatus status) { this.status = status; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder scheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; return this; }
        public Builder sentAt(LocalDateTime sentAt) { this.sentAt = sentAt; return this; }
        public Builder deliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; return this; }
        public Builder readAt(LocalDateTime readAt) { this.readAt = readAt; return this; }
        public Builder expiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; return this; }
        public Builder requiresAck(boolean requiresAck) { this.requiresAck = requiresAck; return this; }
        public Builder requiresDeliveryConfirmation(boolean requiresDeliveryConfirmation) { this.requiresDeliveryConfirmation = requiresDeliveryConfirmation; return this; }
        public Builder retryCount(int retryCount) { this.retryCount = retryCount; return this; }
        public Builder maxRetries(int maxRetries) { this.maxRetries = maxRetries; return this; }
        public Builder retryDelay(Duration retryDelay) { this.retryDelay = retryDelay; return this; }
        public Builder errorMessage(String errorMessage) { this.errorMessage = errorMessage; return this; }
        public Builder deliveryAttempts(List<String> deliveryAttempts) { this.deliveryAttempts = deliveryAttempts; return this; }
        public Builder headers(Map<String, Object> headers) { this.headers = headers; return this; }
        public Builder metadata(Map<String, Object> metadata) { this.metadata = metadata; return this; }
        public Builder tags(Set<String> tags) { this.tags = tags; return this; }
        public Builder businessDomain(String businessDomain) { this.businessDomain = businessDomain; return this; }
        public Builder eventType(String eventType) { this.eventType = eventType; return this; }
        public Builder encrypted(boolean encrypted) { this.encrypted = encrypted; return this; }
        public Builder signed(boolean signed) { this.signed = signed; return this; }
        public Builder checksum(String checksum) { this.checksum = checksum; return this; }
        public Builder securityLevel(MessageSecurityLevel securityLevel) { this.securityLevel = securityLevel; return this; }
        
        public Builder addRecipient(String recipient) { this.recipients.add(recipient); return this; }
        public Builder addHeader(String key, Object value) { this.headers.put(key, value); return this; }
        public Builder addMetadata(String key, Object value) { this.metadata.put(key, value); return this; }
        public Builder addTag(String tag) { this.tags.add(tag); return this; }
        public Builder expiresInMinutes(long minutes) { 
            this.expiresAt = this.createdAt.plusMinutes(minutes); 
            return this; 
        }
        public Builder expiresInHours(long hours) { 
            this.expiresAt = this.createdAt.plusHours(hours); 
            return this; 
        }
        public Builder scheduleInMinutes(long minutes) { 
            this.scheduledAt = LocalDateTime.now().plusMinutes(minutes); 
            return this; 
        }
        
        public Message build() {
            if (threadId == null) threadId = messageId;
            
            return new Message(messageId, correlationId, threadId, replyToMessageId, parentMessageId,
                             type, priority, subject, content, contentType, encoding,
                             sender, recipient, recipients, topic, routingKey, exchange,
                             status, createdAt, scheduledAt, sentAt, deliveredAt, readAt, expiresAt,
                             requiresAck, requiresDeliveryConfirmation, retryCount, maxRetries,
                             retryDelay, errorMessage, deliveryAttempts,
                             headers, metadata, tags, businessDomain, eventType,
                             encrypted, signed, checksum, securityLevel);
        }
    }
}

enum MessageClassification {
    STANDARD, HIGH_SECURITY, BROADCAST, REPLY, DOMAIN_EVENT, COMMAND, QUERY
}

enum MessageSecurityLevel {
    STANDARD(1), HIGH(2), CRITICAL(3);
    
    private final int level;
    
    MessageSecurityLevel(int level) {
        this.level = level;
    }
    
    public int getLevel() { return level; }
    
    public boolean requiresEncryption() {
        return level >= HIGH.level;
    }
    
    public boolean requiresSignature() {
        return level >= CRITICAL.level;
    }
}