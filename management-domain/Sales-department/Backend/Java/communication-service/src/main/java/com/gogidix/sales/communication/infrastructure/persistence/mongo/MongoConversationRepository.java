package com.gogidix.sales.communication.infrastructure.persistence.mongo;

import com.gogidix.sales.communication.domain.model.Conversation;
import com.gogidix.sales.communication.domain.repository.ConversationRepository;
import com.gogidix.sales.communication.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Conversation
 * Implements conversation persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoConversationRepository implements ConversationRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Conversation save(Conversation conversation) {
        log.debug("Saving conversation: {} for tenant: {}", conversation.getConversationId(), conversation.getTenantId());
        return mongoTemplate.save(conversation);
    }

    @Override
    public List<Conversation> saveAll(List<Conversation> conversations) {
        return conversations.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<Conversation> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Conversation.class));
    }

    @Override
    public Optional<Conversation> findByConversationIdAndTenantId(String conversationId, String tenantId) {
        Query query = Query.query(
                Criteria.where("conversationId").is(conversationId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Conversation.class));
    }

    @Override
    public List<Conversation> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Conversation.class);
    }

    @Override
    public List<Conversation> findByTenantIdAndStatus(String tenantId, Conversation.ConversationStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, Conversation.class);
    }

    @Override
    public List<Conversation> findByTenantIdAndType(String tenantId, Conversation.ConversationType type) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("type").is(type)
        );
        return mongoTemplate.find(query, Conversation.class);
    }

    @Override
    public List<Conversation> findByParticipantIdsContainingAndTenantId(String participantId, String tenantId) {
        Query query = Query.query(
                Criteria.where("participantIds").is(participantId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, Conversation.class);
    }

    @Override
    public List<Conversation> findByOwnerIdAndTenantId(String ownerId, String tenantId) {
        Query query = Query.query(
                Criteria.where("ownerId").is(ownerId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, Conversation.class);
    }

    @Override
    public List<Conversation> findByAssignedToAndTenantId(String assignedTo, String tenantId) {
        Query query = Query.query(
                Criteria.where("assignedTo").is(assignedTo)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, Conversation.class);
    }

    @Override
    public List<Conversation> findByTenantIdAndIsArchived(String tenantId, Boolean isArchived) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isArchived").is(isArchived)
        );
        return mongoTemplate.find(query, Conversation.class);
    }

    @Override
    public List<Conversation> findByTenantIdAndIsPinned(String tenantId, Boolean isPinned) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isPinned").is(isPinned)
        );
        return mongoTemplate.find(query, Conversation.class);
    }

    @Override
    public List<Conversation> findByTenantIdAndTagsContaining(String tenantId, String tag) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("tags").is(tag)
        );
        return mongoTemplate.find(query, Conversation.class);
    }

    @Override
    public List<Conversation> findByRelatedEntityTypeAndRelatedEntityIdAndTenantId(
            String relatedEntityType, String relatedEntityId, String tenantId) {
        Query query = Query.query(
                Criteria.where("relatedEntityType").is(relatedEntityType)
                        .and("relatedEntityId").is(relatedEntityId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, Conversation.class);
    }

    @Override
    public List<Conversation> findByTenantIdAndSlaDeadlineBeforeAndSlaBreachFalse(String tenantId, Instant deadline) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("slaDeadline").lte(deadline)
                        .and("slaBreach").is(false)
        );
        return mongoTemplate.find(query, Conversation.class);
    }

    @Override
    public boolean existsByConversationIdAndTenantId(String conversationId, String tenantId) {
        Query query = Query.query(
                Criteria.where("conversationId").is(conversationId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Conversation.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Conversation.class);
    }

    @Override
    public void deleteByConversationIdAndTenantId(String conversationId, String tenantId) {
        Query query = Query.query(
                Criteria.where("conversationId").is(conversationId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Conversation.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, Conversation.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, Conversation.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, Conversation.ConversationStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.count(query, Conversation.class);
    }

    @Override
    public long countByParticipantIdsContainingAndTenantId(String participantId, String tenantId) {
        Query query = Query.query(
                Criteria.where("participantIds").is(participantId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.count(query, Conversation.class);
    }

    @Override
    public long countUnreadByParticipantId(String participantId) {
        Query query = Query.query(
                Criteria.where("participantIds").is(participantId)
                        .and("unreadCount").gt(0)
        );
        return mongoTemplate.count(query, Conversation.class);
    }

    @Override
    public List<Conversation> searchByTitleOrDescription(String tenantId, String searchTerm) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(searchTerm);
        Query query = TextQuery.queryText(textCriteria)
                .addCriteria(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Conversation.class);
    }
}
