package com.gogidix.shared.messaging.application.port.in;

import com.gogidix.shared.messaging.domain.model.Message;
import com.gogidix.shared.messaging.domain.valueobject.MessageType;
import com.gogidix.shared.messaging.adapter.in.web.MessageController.MessageTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Input port for message handling use cases.
 * Defines the contract for messaging operations.
 */
public interface MessageHandlingUseCase {
    
    /**
     * Sends a new message
     */
    MessageResult sendMessage(Message message);
    
    /**
     * Retrieves messages by filter criteria
     */
    List<Message> getMessages(MessageFilter filter, int limit);
    
    /**
     * Gets a specific message by ID
     */
    Optional<Message> getMessage(String messageId);
    
    /**
     * Marks a message as read
     */
    MessageResult markAsRead(String messageId);
    
    /**
     * Deletes a message
     */
    MessageResult deleteMessage(String messageId);
    
    /**
     * Gets message statistics for a user
     */
    MessageStatistics getMessageStatistics(String userId);
    
    /**
     * Broadcasts a message to multiple recipients
     */
    BroadcastResult broadcastMessage(String subject, String content, List<String> recipients, 
                                   MessageType type, String sender);
    
    /**
     * Gets available message templates
     */
    List<MessageTemplate> getMessageTemplates(String category);
    
    /**
     * Processes message queue
     */
    ProcessingResult processMessageQueue();
    
    /**
     * Retries failed messages
     */
    RetryResult retryFailedMessages();
    
    /**
     * Message operation result
     */
    class MessageResult {
        private final boolean success;
        private final String messageId;
        private final String message;
        private final Exception error;
        
        public MessageResult(boolean success, String messageId, String message, Exception error) {
            this.success = success;
            this.messageId = messageId;
            this.message = message;
            this.error = error;
        }
        
        public static MessageResult success(String messageId, String message) {
            return new MessageResult(true, messageId, message, null);
        }
        
        public static MessageResult failure(String message, Exception error) {
            return new MessageResult(false, null, message, error);
        }
        
        public boolean isSuccess() { return success; }
        public String getMessageId() { return messageId; }
        public String getMessage() { return message; }
        public Optional<Exception> getError() { return Optional.ofNullable(error); }
    }
    
    /**
     * Message filter criteria
     */
    class MessageFilter {
        private final String recipient;
        private final String sender;
        private final String type;
        private final String status;
        private final String priority;
        private final Long fromTimestamp;
        private final Long toTimestamp;
        private final Boolean readStatus;
        
        public MessageFilter(String recipient, String sender, String type, String status, 
                           String priority, Long fromTimestamp, Long toTimestamp, Boolean readStatus) {
            this.recipient = recipient;
            this.sender = sender;
            this.type = type;
            this.status = status;
            this.priority = priority;
            this.fromTimestamp = fromTimestamp;
            this.toTimestamp = toTimestamp;
            this.readStatus = readStatus;
        }
        
        public static MessageFilter all() {
            return new MessageFilter(null, null, null, null, null, null, null, null);
        }
        
        public static MessageFilter forRecipient(String recipient, String status) {
            return new MessageFilter(recipient, null, null, status, null, null, null, null);
        }
        
        public static MessageFilter forSender(String sender) {
            return new MessageFilter(null, sender, null, null, null, null, null, null);
        }
        
        public static MessageFilter unread(String recipient) {
            return new MessageFilter(recipient, null, null, null, null, null, null, false);
        }
        
        public Optional<String> getRecipient() { return Optional.ofNullable(recipient); }
        public Optional<String> getSender() { return Optional.ofNullable(sender); }
        public Optional<String> getType() { return Optional.ofNullable(type); }
        public Optional<String> getStatus() { return Optional.ofNullable(status); }
        public Optional<String> getPriority() { return Optional.ofNullable(priority); }
        public Optional<Long> getFromTimestamp() { return Optional.ofNullable(fromTimestamp); }
        public Optional<Long> getToTimestamp() { return Optional.ofNullable(toTimestamp); }
        public Optional<Boolean> getReadStatus() { return Optional.ofNullable(readStatus); }
    }
    
    /**
     * Message statistics
     */
    class MessageStatistics {
        private final long totalMessages;
        private final long unreadMessages;
        private final long sentMessages;
        private final Map<String, Long> messagesByType;
        private final Map<String, Long> messagesByPriority;
        private final Map<String, Long> messagesByStatus;
        private final double averageResponseTime;
        
        public MessageStatistics(long totalMessages, long unreadMessages, long sentMessages,
                               Map<String, Long> messagesByType, Map<String, Long> messagesByPriority,
                               Map<String, Long> messagesByStatus, double averageResponseTime) {
            this.totalMessages = totalMessages;
            this.unreadMessages = unreadMessages;
            this.sentMessages = sentMessages;
            this.messagesByType = messagesByType;
            this.messagesByPriority = messagesByPriority;
            this.messagesByStatus = messagesByStatus;
            this.averageResponseTime = averageResponseTime;
        }
        
        public long getTotalMessages() { return totalMessages; }
        public long getUnreadMessages() { return unreadMessages; }
        public long getSentMessages() { return sentMessages; }
        public Map<String, Long> getMessagesByType() { return messagesByType; }
        public Map<String, Long> getMessagesByPriority() { return messagesByPriority; }
        public Map<String, Long> getMessagesByStatus() { return messagesByStatus; }
        public double getAverageResponseTime() { return averageResponseTime; }
        
        public double getReadRate() {
            return totalMessages > 0 ? 
                   (double) (totalMessages - unreadMessages) / totalMessages : 0.0;
        }
    }
    
    /**
     * Broadcast operation result
     */
    class BroadcastResult {
        private final boolean success;
        private final int totalRecipients;
        private final int successfulDeliveries;
        private final int failedDeliveries;
        private final List<String> failedRecipients;
        private final String message;
        
        public BroadcastResult(boolean success, int totalRecipients, int successfulDeliveries,
                             int failedDeliveries, List<String> failedRecipients, String message) {
            this.success = success;
            this.totalRecipients = totalRecipients;
            this.successfulDeliveries = successfulDeliveries;
            this.failedDeliveries = failedDeliveries;
            this.failedRecipients = failedRecipients;
            this.message = message;
        }
        
        public boolean isSuccess() { return success; }
        public int getTotalRecipients() { return totalRecipients; }
        public int getSuccessfulDeliveries() { return successfulDeliveries; }
        public int getFailedDeliveries() { return failedDeliveries; }
        public List<String> getFailedRecipients() { return failedRecipients; }
        public String getMessage() { return message; }
        
        public double getDeliveryRate() {
            return totalRecipients > 0 ? 
                   (double) successfulDeliveries / totalRecipients : 0.0;
        }
    }
    
    /**
     * Processing operation result
     */
    class ProcessingResult {
        private final int processedCount;
        private final int failedCount;
        private final long processingTimeMs;
        private final String summary;
        
        public ProcessingResult(int processedCount, int failedCount, long processingTimeMs, String summary) {
            this.processedCount = processedCount;
            this.failedCount = failedCount;
            this.processingTimeMs = processingTimeMs;
            this.summary = summary;
        }
        
        public int getProcessedCount() { return processedCount; }
        public int getFailedCount() { return failedCount; }
        public long getProcessingTimeMs() { return processingTimeMs; }
        public String getSummary() { return summary; }
        
        public boolean hasFailures() { return failedCount > 0; }
        public double getSuccessRate() {
            int total = processedCount + failedCount;
            return total > 0 ? (double) processedCount / total : 0.0;
        }
    }
    
    /**
     * Retry operation result
     */
    class RetryResult {
        private final int retriedCount;
        private final int successfulRetries;
        private final int permanentFailures;
        private final String message;
        
        public RetryResult(int retriedCount, int successfulRetries, int permanentFailures, String message) {
            this.retriedCount = retriedCount;
            this.successfulRetries = successfulRetries;
            this.permanentFailures = permanentFailures;
            this.message = message;
        }
        
        public int getRetriedCount() { return retriedCount; }
        public int getSuccessfulRetries() { return successfulRetries; }
        public int getPermanentFailures() { return permanentFailures; }
        public String getMessage() { return message; }
        
        public double getRetrySuccessRate() {
            return retriedCount > 0 ? (double) successfulRetries / retriedCount : 0.0;
        }
    }
}