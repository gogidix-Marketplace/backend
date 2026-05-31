package com.gogidix.shared.messaging.adapter.out.persistence;

import com.gogidix.shared.messaging.application.port.out.MessageRepository;
import com.gogidix.shared.messaging.domain.model.Message;
import com.gogidix.shared.messaging.domain.valueobject.MessageStatus;
import com.gogidix.shared.messaging.domain.valueobject.MessageType;
import com.gogidix.shared.messaging.domain.valueobject.MessagePriority;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * JPA repository implementation for message persistence.
 * Adapts JPA operations to domain requirements.
 */
@Repository
public class MessageJpaRepository implements MessageRepository {
    
    private final SpringDataMessageRepository springRepository;
    
    public MessageJpaRepository(SpringDataMessageRepository springRepository) {
        this.springRepository = springRepository;
    }
    
    @Override
    public Message save(Message message) {
        MessageEntity entity = MessageEntity.fromDomain(message);
        MessageEntity savedEntity = springRepository.save(entity);
        return savedEntity.toDomain();
    }
    
    @Override
    public Optional<Message> findById(String messageId) {
        return springRepository.findById(messageId)
                              .map(MessageEntity::toDomain);
    }
    
    @Override
    public List<Message> findByRecipient(String recipient, int limit) {
        return springRepository.findByRecipientOrderByCreatedAtDesc(recipient, 
                PageRequest.of(0, limit))
                .stream()
                .map(MessageEntity::toDomain)
                .toList();
    }
    
    @Override
    public List<Message> findBySender(String sender, int limit) {
        return springRepository.findBySenderOrderByCreatedAtDesc(sender, 
                PageRequest.of(0, limit))
                .stream()
                .map(MessageEntity::toDomain)
                .toList();
    }
    
    @Override
    public List<Message> findByStatus(MessageStatus status, int limit) {
        return springRepository.findByStatusOrderByCreatedAtDesc(status, 
                PageRequest.of(0, limit))
                .stream()
                .map(MessageEntity::toDomain)
                .toList();
    }
    
    @Override
    public List<Message> findByTypeAndStatus(MessageType type, MessageStatus status, int limit) {
        return springRepository.findByTypeAndStatusOrderByCreatedAtDesc(type, status, 
                PageRequest.of(0, limit))
                .stream()
                .map(MessageEntity::toDomain)
                .toList();
    }
    
    @Override
    public List<Message> findByPriority(MessagePriority priority, int limit) {
        return springRepository.findByPriorityOrderByCreatedAtDesc(priority, 
                PageRequest.of(0, limit))
                .stream()
                .map(MessageEntity::toDomain)
                .toList();
    }
    
    @Override
    public List<Message> findUnreadMessages(String recipient, int limit) {
        return springRepository.findByRecipientAndReadFalseOrderByCreatedAtDesc(recipient, 
                PageRequest.of(0, limit))
                .stream()
                .map(MessageEntity::toDomain)
                .toList();
    }
    
    @Override
    public List<Message> findByDateRange(LocalDateTime from, LocalDateTime to, int limit) {
        return springRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(from, to, 
                PageRequest.of(0, limit))
                .stream()
                .map(MessageEntity::toDomain)
                .toList();
    }
    
    @Override
    public List<Message> findByThreadId(String threadId) {
        return springRepository.findByThreadIdOrderByCreatedAtAsc(threadId)
                .stream()
                .map(MessageEntity::toDomain)
                .toList();
    }
    
    @Override
    public List<Message> findRetriableMessages(int limit) {
        return springRepository.findRetriableMessages(PageRequest.of(0, limit))
                .stream()
                .map(MessageEntity::toDomain)
                .toList();
    }
    
    @Override
    public List<Message> findExpiredMessages(int limit) {
        LocalDateTime now = LocalDateTime.now();
        return springRepository.findByExpiresAtBeforeAndStatusNot(now, MessageStatus.EXPIRED, 
                PageRequest.of(0, limit))
                .stream()
                .map(MessageEntity::toDomain)
                .toList();
    }
    
    @Override
    public Message updateStatus(String messageId, MessageStatus status) {
        Optional<MessageEntity> entityOpt = springRepository.findById(messageId);
        if (entityOpt.isEmpty()) {
            throw new IllegalArgumentException("Message not found: " + messageId);
        }
        
        MessageEntity entity = entityOpt.get();
        entity.setStatus(status);
        MessageEntity updatedEntity = springRepository.save(entity);
        return updatedEntity.toDomain();
    }
    
    @Override
    public Message markAsRead(String messageId) {
        Optional<MessageEntity> entityOpt = springRepository.findById(messageId);
        if (entityOpt.isEmpty()) {
            throw new IllegalArgumentException("Message not found: " + messageId);
        }
        
        MessageEntity entity = entityOpt.get();
        entity.setRead(true);
        entity.setReadAt(LocalDateTime.now());
        entity.setStatus(MessageStatus.read);
        MessageEntity updatedEntity = springRepository.save(entity);
        return updatedEntity.toDomain();
    }
    
    @Override
    public Message archiveMessage(String messageId) {
        Optional<MessageEntity> entityOpt = springRepository.findById(messageId);
        if (entityOpt.isEmpty()) {
            throw new IllegalArgumentException("Message not found: " + messageId);
        }
        
        MessageEntity entity = entityOpt.get();
        entity.setArchived(true);
        entity.setStatus(MessageStatus.ARCHIVED);
        MessageEntity updatedEntity = springRepository.save(entity);
        return updatedEntity.toDomain();
    }
    
    @Override
    public void deleteById(String messageId) {
        springRepository.deleteById(messageId);
    }
    
    @Override
    public int deleteExpiredMessages() {
        LocalDateTime now = LocalDateTime.now();
        return springRepository.deleteExpiredMessages(now);
    }
    
    @Override
    public long countByRecipient(String recipient) {
        return springRepository.countByRecipient(recipient);
    }
    
    @Override
    public long countUnreadByRecipient(String recipient) {
        return springRepository.countByRecipientAndReadFalse(recipient);
    }
    
    @Override
    public long countBySender(String sender) {
        return springRepository.countBySender(sender);
    }
    
    @Override
    public long countByStatus(MessageStatus status) {
        return springRepository.countByStatus(status);
    }
    
    @Override
    public long countByType(MessageType type) {
        return springRepository.countByType(type);
    }
    
    @Override
    public long countByPriority(MessagePriority priority) {
        return springRepository.countByPriority(priority);
    }
    
    @Override
    public MessageStatistics getStatisticsForUser(String userId) {
        long totalMessages = springRepository.countByRecipient(userId);
        long unreadMessages = springRepository.countByRecipientAndReadFalse(userId);
        long sentMessages = springRepository.countByRecipientAndStatus(userId, MessageStatus.SENT);
        long failedMessages = springRepository.countByRecipientAndStatus(userId, MessageStatus.FAILED);
        long archivedMessages = springRepository.countByRecipientAndArchived(userId, true);
        double avgResponseTime = springRepository.getAverageResponseTimeForUser(userId);
        
        return new MessageStatistics(totalMessages, unreadMessages, sentMessages,
                                   failedMessages, archivedMessages, avgResponseTime);
    }
    
    @Override
    public MessageStatistics getGlobalStatistics() {
        long totalMessages = springRepository.count();
        long unreadMessages = springRepository.countByReadFalse();
        long sentMessages = springRepository.countByStatus(MessageStatus.SENT);
        long failedMessages = springRepository.countByStatus(MessageStatus.FAILED);
        long archivedMessages = springRepository.countByArchived(true);
        double avgResponseTime = springRepository.getGlobalAverageResponseTime();
        
        return new MessageStatistics(totalMessages, unreadMessages, sentMessages,
                                   failedMessages, archivedMessages, avgResponseTime);
    }
    
    @Override
    public boolean existsById(String messageId) {
        return springRepository.existsById(messageId);
    }
    
    @Override
    public List<Message> findWithFilter(MessageFilter filter, int limit) {
        // Build dynamic query based on filter criteria
        return springRepository.findWithCustomFilter(
            filter.getRecipient().orElse(null),
            filter.getSender().orElse(null),
            filter.getType().orElse(null),
            filter.getStatus().orElse(null),
            filter.getPriority().orElse(null),
            filter.getFromDate().orElse(null),
            filter.getToDate().orElse(null),
            filter.getReadStatus().orElse(null),
            filter.getThreadId().orElse(null),
            PageRequest.of(0, limit)
        ).stream()
        .map(MessageEntity::toDomain)
        .toList();
    }
    
    /**
     * Spring Data JPA repository interface
     */
    interface SpringDataMessageRepository extends JpaRepository<MessageEntity, String> {
        
        List<MessageEntity> findByRecipientOrderByCreatedAtDesc(String recipient, Pageable pageable);
        
        List<MessageEntity> findBySenderOrderByCreatedAtDesc(String sender, Pageable pageable);
        
        List<MessageEntity> findByStatusOrderByCreatedAtDesc(MessageStatus status, Pageable pageable);
        
        List<MessageEntity> findByTypeAndStatusOrderByCreatedAtDesc(MessageType type, MessageStatus status, Pageable pageable);
        
        List<MessageEntity> findByPriorityOrderByCreatedAtDesc(MessagePriority priority, Pageable pageable);
        
        List<MessageEntity> findByRecipientAndReadFalseOrderByCreatedAtDesc(String recipient, Pageable pageable);
        
        List<MessageEntity> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime from, LocalDateTime to, Pageable pageable);
        
        List<MessageEntity> findByThreadIdOrderByCreatedAtAsc(String threadId);
        
        @Query("SELECT m FROM MessageEntity m WHERE m.status = 'FAILED' AND m.retryCount < m.maxRetries")
        List<MessageEntity> findRetriableMessages(Pageable pageable);
        
        List<MessageEntity> findByExpiresAtBeforeAndStatusNot(LocalDateTime expiresBefore, MessageStatus status, Pageable pageable);
        
        @Modifying
        @Query("DELETE FROM MessageEntity m WHERE m.expiresAt < :now")
        int deleteExpiredMessages(@Param("now") LocalDateTime now);
        
        long countByRecipient(String recipient);
        
        long countByRecipientAndReadFalse(String recipient);
        
        long countBySender(String sender);
        
        long countByStatus(MessageStatus status);
        
        long countByType(MessageType type);
        
        long countByPriority(MessagePriority priority);
        
        long countByRecipientAndStatus(String recipient, MessageStatus status);
        
        long countByRecipientAndArchived(String recipient, boolean archived);
        
        long countByReadFalse();
        
        long countByArchived(boolean archived);
        
        @Query("SELECT AVG(TIMESTAMPDIFF(MICROSECOND, m.createdAt, m.readAt)) FROM MessageEntity m WHERE m.recipient = :userId AND m.readAt IS NOT NULL")
        double getAverageResponseTimeForUser(@Param("userId") String userId);
        
        @Query("SELECT AVG(TIMESTAMPDIFF(MICROSECOND, m.createdAt, m.readAt)) FROM MessageEntity m WHERE m.readAt IS NOT NULL")
        double getGlobalAverageResponseTime();
        
        @Query("""
            SELECT m FROM MessageEntity m WHERE
            (:recipient IS NULL OR m.recipient = :recipient) AND
            (:sender IS NULL OR m.sender = :sender) AND
            (:type IS NULL OR m.type = :type) AND
            (:status IS NULL OR m.status = :status) AND
            (:priority IS NULL OR m.priority = :priority) AND
            (:fromDate IS NULL OR m.createdAt >= :fromDate) AND
            (:toDate IS NULL OR m.createdAt <= :toDate) AND
            (:readStatus IS NULL OR m.read = :readStatus) AND
            (:threadId IS NULL OR m.threadId = :threadId)
            ORDER BY m.createdAt DESC
            """)
        List<MessageEntity> findWithCustomFilter(
            @Param("recipient") String recipient,
            @Param("sender") String sender,
            @Param("type") MessageType type,
            @Param("status") MessageStatus status,
            @Param("priority") MessagePriority priority,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("readStatus") Boolean readStatus,
            @Param("threadId") String threadId,
            Pageable pageable
        );
    }
}