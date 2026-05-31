package com.gogidix.sales.communication.application.service;

import com.gogidix.sales.communication.domain.model.Conversation;
import com.gogidix.sales.communication.domain.repository.ConversationRepository;
import com.gogidix.sales.communication.domain.repository.MessageRepository;
import com.gogidix.sales.communication.shared.exception.NotFoundException;
import com.gogidix.sales.communication.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Conversation Query Service
 * Handles all read operations for conversations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ConversationQueryService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public Conversation getById(String conversationId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Getting conversation: {} for tenant: {}", conversationId, tenantId);

        return conversationRepository.findByConversationIdAndTenantId(conversationId, tenantId)
                .orElseThrow(() -> new NotFoundException("Conversation", conversationId));
    }

    public List<Conversation> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Getting all conversations for tenant: {}", tenantId);
        return conversationRepository.findByTenantId(tenantId);
    }

    public List<Conversation> getByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Getting conversations with status: {} for tenant: {}", status, tenantId);

        Conversation.ConversationStatus conversationStatus = Conversation.ConversationStatus.valueOf(status.toUpperCase());
        return conversationRepository.findByTenantIdAndStatus(tenantId, conversationStatus);
    }

    public List<Conversation> getByType(String type) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Getting conversations with type: {} for tenant: {}", type, tenantId);

        Conversation.ConversationType conversationType = Conversation.ConversationType.valueOf(type.toUpperCase());
        return conversationRepository.findByTenantIdAndType(tenantId, conversationType);
    }

    public List<Conversation> getByParticipant(String participantId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Getting conversations for participant: {} for tenant: {}", participantId, tenantId);

        return conversationRepository.findByParticipantIdsContainingAndTenantId(participantId, tenantId);
    }

    public List<Conversation> getByOwner(String ownerId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Getting conversations for owner: {} for tenant: {}", ownerId, tenantId);

        return conversationRepository.findByOwnerIdAndTenantId(ownerId, tenantId);
    }

    public List<Conversation> getByAssignedTo(String assignedTo) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Getting conversations assigned to: {} for tenant: {}", assignedTo, tenantId);

        return conversationRepository.findByAssignedToAndTenantId(assignedTo, tenantId);
    }

    public List<Conversation> getArchived(boolean isArchived) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Getting archived conversations: {} for tenant: {}", isArchived, tenantId);

        return conversationRepository.findByTenantIdAndIsArchived(tenantId, isArchived);
    }

    public List<Conversation> getPinned(boolean isPinned) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Getting pinned conversations: {} for tenant: {}", isPinned, tenantId);

        return conversationRepository.findByTenantIdAndIsPinned(tenantId, isPinned);
    }

    public List<Conversation> getByTag(String tag) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Getting conversations with tag: {} for tenant: {}", tag, tenantId);

        return conversationRepository.findByTenantIdAndTagsContaining(tenantId, tag);
    }

    public List<Conversation> getByRelatedEntity(String relatedEntityType, String relatedEntityId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Getting conversations for entity: {}:{} for tenant: {}", relatedEntityType, relatedEntityId, tenantId);

        return conversationRepository.findByRelatedEntityTypeAndRelatedEntityIdAndTenantId(
                relatedEntityType, relatedEntityId, tenantId);
    }

    public List<Conversation> getSlaBreachingConversations() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Getting SLA breaching conversations for tenant: {}", tenantId);

        return conversationRepository.findByTenantIdAndSlaDeadlineBeforeAndSlaBreachFalse(
                tenantId, Instant.now());
    }

    public Page<Conversation> search(String searchTerm, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Searching conversations for tenant: {} with term: {}", tenantId, searchTerm);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        List<Conversation> conversations = conversationRepository.searchByTitleOrDescription(tenantId, searchTerm);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), conversations.size());
        List<Conversation> pagedConversations = conversations.subList(start, end);

        return new PageImpl<>(pagedConversations, pageable, conversations.size());
    }

    public long countByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();
        Conversation.ConversationStatus conversationStatus = Conversation.ConversationStatus.valueOf(status.toUpperCase());
        return conversationRepository.countByTenantIdAndStatus(tenantId, conversationStatus);
    }

    public long countByParticipant(String participantId) {
        String tenantId = RequestContextHolder.getTenantId();
        return conversationRepository.countByParticipantIdsContainingAndTenantId(participantId, tenantId);
    }

    public ConversationSummary getSummary() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Getting conversation summary for tenant: {}", tenantId);

        long total = conversationRepository.countByTenantId(tenantId);
        long active = conversationRepository.countByTenantIdAndStatus(
                tenantId, Conversation.ConversationStatus.ACTIVE);
        long archived = conversationRepository.countByTenantIdAndStatus(
                tenantId, Conversation.ConversationStatus.ARCHIVED);
        long resolved = conversationRepository.countByTenantIdAndStatus(
                tenantId, Conversation.ConversationStatus.RESOLVED);

        return ConversationSummary.builder()
                .totalConversations(total)
                .activeConversations(active)
                .archivedConversations(archived)
                .resolvedConversations(resolved)
                .slaBreachingConversations(
                        conversationRepository.findByTenantIdAndSlaDeadlineBeforeAndSlaBreachFalse(
                                tenantId, Instant.now()).size())
                .build();
    }

    public ConversationSummary getSummaryForUser(String userId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Getting conversation summary for user: {} in tenant: {}", userId, tenantId);

        List<Conversation> userConversations = conversationRepository.findByParticipantIdsContainingAndTenantId(
                userId, tenantId);

        long active = userConversations.stream()
                .filter(c -> c.getStatus() == Conversation.ConversationStatus.ACTIVE)
                .count();
        long unread = userConversations.stream()
                .filter(c -> c.getUnreadCount() != null && c.getUnreadCount() > 0)
                .count();

        return ConversationSummary.builder()
                .totalConversations(userConversations.size())
                .activeConversations(active)
                .unreadConversations(unread)
                .build();
    }

    @lombok.Data
    @lombok.Builder
    public static class ConversationSummary {
        private long totalConversations;
        private long activeConversations;
        private long archivedConversations;
        private long resolvedConversations;
        private long unreadConversations;
        private long slaBreachingConversations;
    }
}
