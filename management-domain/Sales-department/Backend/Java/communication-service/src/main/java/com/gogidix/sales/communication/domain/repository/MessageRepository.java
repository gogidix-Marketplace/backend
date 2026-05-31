package com.gogidix.sales.communication.domain.repository;

import com.gogidix.sales.communication.domain.model.Message;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Message Repository Interface (Port)
 * Defines the contract for message persistence operations
 */
public interface MessageRepository {

    Message save(Message message);

    List<Message> saveAll(List<Message> messages);

    Optional<Message> findById(String id);

    Optional<Message> findByMessageIdAndTenantId(String messageId, String tenantId);

    List<Message> findByTenantId(String tenantId);

    List<Message> findByConversationIdAndTenantId(String conversationId, String tenantId);

    List<Message> findByConversationIdAndTenantIdOrderBySentAtAsc(
            String conversationId, String tenantId);

    List<Message> findBySenderIdAndTenantId(String senderId, String tenantId);

    List<Message> findByRecipientIdsContainingAndTenantId(String recipientId, String tenantId);

    List<Message> findByTenantIdAndStatus(String tenantId, Message.MessageStatus status);

    List<Message> findByTenantIdAndChannel(String tenantId, Message.ChannelType channel);

    List<Message> findByScheduledAtBeforeAndStatus(Instant scheduledAt, Message.MessageStatus status);

    List<Message> findByTenantIdAndParentMessageId(String tenantId, String parentMessageId);

    List<Message> findByTemplateIdAndTenantId(String templateId, String tenantId);

    List<Message> findByRelatedEntityTypeAndRelatedEntityIdAndTenantId(
            String relatedEntityType, String relatedEntityId, String tenantId);

    List<Message> findByIsReadFalseAndRecipientIdsContaining(String recipientId);

    boolean existsByMessageIdAndTenantId(String messageId, String tenantId);

    void deleteById(String id);

    void deleteByMessageIdAndTenantId(String messageId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, Message.MessageStatus status);

    long countByConversationIdAndTenantId(String conversationId, String tenantId);

    long countUnreadByRecipientId(String recipientId);

    List<Message> searchByContent(String tenantId, String searchTerm);

    List<Message> findByTenantIdAndCreatedAtBetween(String tenantId, Instant startDate, Instant endDate);
}
