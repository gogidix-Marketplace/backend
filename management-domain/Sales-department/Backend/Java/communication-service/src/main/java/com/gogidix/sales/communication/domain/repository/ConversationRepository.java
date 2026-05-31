package com.gogidix.sales.communication.domain.repository;

import com.gogidix.sales.communication.domain.model.Conversation;

import java.util.List;
import java.util.Optional;

/**
 * Conversation Repository Interface (Port)
 * Defines the contract for conversation persistence operations
 */
public interface ConversationRepository {

    Conversation save(Conversation conversation);

    List<Conversation> saveAll(List<Conversation> conversations);

    Optional<Conversation> findById(String id);

    Optional<Conversation> findByConversationIdAndTenantId(String conversationId, String tenantId);

    List<Conversation> findByTenantId(String tenantId);

    List<Conversation> findByTenantIdAndStatus(String tenantId, Conversation.ConversationStatus status);

    List<Conversation> findByTenantIdAndType(String tenantId, Conversation.ConversationType type);

    List<Conversation> findByParticipantIdsContainingAndTenantId(String participantId, String tenantId);

    List<Conversation> findByOwnerIdAndTenantId(String ownerId, String tenantId);

    List<Conversation> findByAssignedToAndTenantId(String assignedTo, String tenantId);

    List<Conversation> findByTenantIdAndIsArchived(String tenantId, Boolean isArchived);

    List<Conversation> findByTenantIdAndIsPinned(String tenantId, Boolean isPinned);

    List<Conversation> findByTenantIdAndTagsContaining(String tenantId, String tag);

    List<Conversation> findByRelatedEntityTypeAndRelatedEntityIdAndTenantId(
            String relatedEntityType, String relatedEntityId, String tenantId);

    List<Conversation> findByTenantIdAndSlaDeadlineBeforeAndSlaBreachFalse(
            String tenantId, java.time.Instant deadline);

    boolean existsByConversationIdAndTenantId(String conversationId, String tenantId);

    void deleteById(String id);

    void deleteByConversationIdAndTenantId(String conversationId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, Conversation.ConversationStatus status);

    long countByParticipantIdsContainingAndTenantId(String participantId, String tenantId);

    long countUnreadByParticipantId(String participantId);

    List<Conversation> searchByTitleOrDescription(String tenantId, String searchTerm);
}
