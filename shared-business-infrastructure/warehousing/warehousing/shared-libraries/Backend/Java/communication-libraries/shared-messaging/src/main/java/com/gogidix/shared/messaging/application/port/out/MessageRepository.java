package com.gogidix.shared.messaging.application.port.out;

import com.gogidix.shared.messaging.domain.model.Message;
import com.gogidix.shared.messaging.domain.valueobject.MessageStatus;
import com.gogidix.shared.messaging.domain.valueobject.MessageType;
import com.gogidix.shared.messaging.domain.valueobject.MessagePriority;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Output port for message persistence operations.
 * Defines the contract for message repository implementations.
 */
public interface MessageRepository {
    
    /**
     * Saves a message to persistent storage
     */
    Message save(Message message);
    
    /**
     * Finds a message by its ID
     */
    Optional<Message> findById(String messageId);
    
    /**
     * Finds messages by recipient
     */
    List<Message> findByRecipient(String recipient, int limit);
    
    /**
     * Finds messages by sender
     */
    List<Message> findBySender(String sender, int limit);
    
    /**
     * Finds messages by status
     */
    List<Message> findByStatus(MessageStatus status, int limit);
    
    /**
     * Finds messages by type and status
     */
    List<Message> findByTypeAndStatus(MessageType type, MessageStatus status, int limit);
    
    /**
     * Finds messages by priority
     */
    List<Message> findByPriority(MessagePriority priority, int limit);
    
    /**
     * Finds unread messages for a recipient
     */
    List<Message> findUnreadMessages(String recipient, int limit);
    
    /**
     * Finds messages in date range
     */
    List<Message> findByDateRange(LocalDateTime from, LocalDateTime to, int limit);
    
    /**
     * Finds messages by thread ID
     */
    List<Message> findByThreadId(String threadId);
    
    /**
     * Finds failed messages that can be retried
     */
    List<Message> findRetriableMessages(int limit);
    
    /**
     * Finds expired messages
     */
    List<Message> findExpiredMessages(int limit);
    
    /**
     * Updates message status
     */
    Message updateStatus(String messageId, MessageStatus status);
    
    /**
     * Marks message as read
     */
    Message markAsRead(String messageId);
    
    /**
     * Archives a message
     */
    Message archiveMessage(String messageId);
    
    /**
     * Deletes a message
     */
    void deleteById(String messageId);
    
    /**
     * Deletes expired messages
     */
    int deleteExpiredMessages();
    
    /**
     * Counts messages by recipient
     */
    long countByRecipient(String recipient);
    
    /**
     * Counts unread messages by recipient
     */
    long countUnreadByRecipient(String recipient);
    
    /**
     * Counts messages by sender
     */
    long countBySender(String sender);
    
    /**
     * Counts messages by status
     */
    long countByStatus(MessageStatus status);
    
    /**
     * Counts messages by type
     */
    long countByType(MessageType type);
    
    /**
     * Counts messages by priority
     */
    long countByPriority(MessagePriority priority);
    
    /**
     * Gets message statistics for a user
     */
    MessageStatistics getStatisticsForUser(String userId);
    
    /**
     * Gets global message statistics
     */
    MessageStatistics getGlobalStatistics();
    
    /**
     * Checks if message exists
     */
    boolean existsById(String messageId);
    
    /**
     * Finds messages with custom filter criteria
     */
    List<Message> findWithFilter(MessageFilter filter, int limit);
    
    /**
     * Message statistics data class
     */
    class MessageStatistics {
        private final long totalMessages;
        private final long unreadMessages;
        private final long sentMessages;
        private final long failedMessages;
        private final long archivedMessages;
        private final double averageResponseTimeMs;
        
        public MessageStatistics(long totalMessages, long unreadMessages, long sentMessages,
                               long failedMessages, long archivedMessages, double averageResponseTimeMs) {
            this.totalMessages = totalMessages;
            this.unreadMessages = unreadMessages;
            this.sentMessages = sentMessages;
            this.failedMessages = failedMessages;
            this.archivedMessages = archivedMessages;
            this.averageResponseTimeMs = averageResponseTimeMs;
        }
        
        public long getTotalMessages() { return totalMessages; }
        public long getUnreadMessages() { return unreadMessages; }
        public long getSentMessages() { return sentMessages; }
        public long getFailedMessages() { return failedMessages; }
        public long getArchivedMessages() { return archivedMessages; }
        public double getAverageResponseTimeMs() { return averageResponseTimeMs; }
        
        public double getReadRate() {
            return totalMessages > 0 ? (double) (totalMessages - unreadMessages) / totalMessages : 0.0;
        }
        
        public double getSuccessRate() {
            return totalMessages > 0 ? (double) sentMessages / totalMessages : 0.0;
        }
    }
    
    /**
     * Message filter for complex queries
     */
    class MessageFilter {
        private final String recipient;
        private final String sender;
        private final MessageType type;
        private final MessageStatus status;
        private final MessagePriority priority;
        private final LocalDateTime fromDate;
        private final LocalDateTime toDate;
        private final Boolean readStatus;
        private final String threadId;
        private final String contentSearch;
        
        public MessageFilter(String recipient, String sender, MessageType type, MessageStatus status,
                           MessagePriority priority, LocalDateTime fromDate, LocalDateTime toDate,
                           Boolean readStatus, String threadId, String contentSearch) {
            this.recipient = recipient;
            this.sender = sender;
            this.type = type;
            this.status = status;
            this.priority = priority;
            this.fromDate = fromDate;
            this.toDate = toDate;
            this.readStatus = readStatus;
            this.threadId = threadId;
            this.contentSearch = contentSearch;
        }
        
        public Optional<String> getRecipient() { return Optional.ofNullable(recipient); }
        public Optional<String> getSender() { return Optional.ofNullable(sender); }
        public Optional<MessageType> getType() { return Optional.ofNullable(type); }
        public Optional<MessageStatus> getStatus() { return Optional.ofNullable(status); }
        public Optional<MessagePriority> getPriority() { return Optional.ofNullable(priority); }
        public Optional<LocalDateTime> getFromDate() { return Optional.ofNullable(fromDate); }
        public Optional<LocalDateTime> getToDate() { return Optional.ofNullable(toDate); }
        public Optional<Boolean> getReadStatus() { return Optional.ofNullable(readStatus); }
        public Optional<String> getThreadId() { return Optional.ofNullable(threadId); }
        public Optional<String> getContentSearch() { return Optional.ofNullable(contentSearch); }
    }
}