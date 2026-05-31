package com.gogidix.sales.communication.infrastructure.persistence.mongo;

import com.gogidix.sales.communication.domain.model.Message;
import com.gogidix.sales.communication.domain.repository.MessageRepository;
import com.gogidix.sales.communication.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
 * MongoDB Repository Implementation - Message
 * Implements message persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoMessageRepository implements MessageRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Message save(Message message) {
        log.debug("Saving message: {} for tenant: {}", message.getMessageId(), message.getTenantId());
        return mongoTemplate.save(message);
    }

    @Override
    public List<Message> saveAll(List<Message> messages) {
        return messages.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<Message> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Message.class));
    }

    @Override
    public Optional<Message> findByMessageIdAndTenantId(String messageId, String tenantId) {
        Query query = Query.query(
                Criteria.where("messageId").is(messageId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Message.class));
    }

    @Override
    public List<Message> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Message.class);
    }

    @Override
    public List<Message> findByConversationIdAndTenantId(String conversationId, String tenantId) {
        Query query = Query.query(
                Criteria.where("conversationId").is(conversationId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, Message.class);
    }

    @Override
    public List<Message> findByConversationIdAndTenantIdOrderBySentAtAsc(String conversationId, String tenantId) {
        Query query = Query.query(
                Criteria.where("conversationId").is(conversationId)
                        .and("tenantId").is(tenantId)
        ).with(org.springframework.data.domain.Sort.by(
                org.springframework.data.domain.Sort.Order.asc("sentAt"),
                org.springframework.data.domain.Sort.Order.asc("createdAt")
        ));
        return mongoTemplate.find(query, Message.class);
    }

    @Override
    public List<Message> findBySenderIdAndTenantId(String senderId, String tenantId) {
        Query query = Query.query(
                Criteria.where("senderId").is(senderId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, Message.class);
    }

    @Override
    public List<Message> findByRecipientIdsContainingAndTenantId(String recipientId, String tenantId) {
        Query query = Query.query(
                Criteria.where("recipientIds").is(recipientId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, Message.class);
    }

    @Override
    public List<Message> findByTenantIdAndStatus(String tenantId, Message.MessageStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, Message.class);
    }

    @Override
    public List<Message> findByTenantIdAndChannel(String tenantId, Message.ChannelType channel) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("channel").is(channel)
        );
        return mongoTemplate.find(query, Message.class);
    }

    @Override
    public List<Message> findByScheduledAtBeforeAndStatus(Instant scheduledAt, Message.MessageStatus status) {
        Query query = Query.query(
                Criteria.where("scheduledAt").lte(scheduledAt)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, Message.class);
    }

    @Override
    public List<Message> findByTenantIdAndParentMessageId(String tenantId, String parentMessageId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("parentMessageId").is(parentMessageId)
        );
        return mongoTemplate.find(query, Message.class);
    }

    @Override
    public List<Message> findByTemplateIdAndTenantId(String templateId, String tenantId) {
        Query query = Query.query(
                Criteria.where("templateId").is(templateId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, Message.class);
    }

    @Override
    public List<Message> findByRelatedEntityTypeAndRelatedEntityIdAndTenantId(
            String relatedEntityType, String relatedEntityId, String tenantId) {
        Query query = Query.query(
                Criteria.where("relatedEntityType").is(relatedEntityType)
                        .and("relatedEntityId").is(relatedEntityId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, Message.class);
    }

    @Override
    public List<Message> findByIsReadFalseAndRecipientIdsContaining(String recipientId) {
        Query query = Query.query(
                Criteria.where("isRead").is(false)
                        .and("recipientIds").is(recipientId)
        );
        return mongoTemplate.find(query, Message.class);
    }

    @Override
    public boolean existsByMessageIdAndTenantId(String messageId, String tenantId) {
        Query query = Query.query(
                Criteria.where("messageId").is(messageId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Message.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Message.class);
    }

    @Override
    public void deleteByMessageIdAndTenantId(String messageId, String tenantId) {
        Query query = Query.query(
                Criteria.where("messageId").is(messageId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Message.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, Message.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, Message.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, Message.MessageStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.count(query, Message.class);
    }

    @Override
    public long countByConversationIdAndTenantId(String conversationId, String tenantId) {
        Query query = Query.query(
                Criteria.where("conversationId").is(conversationId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.count(query, Message.class);
    }

    @Override
    public long countUnreadByRecipientId(String recipientId) {
        Query query = Query.query(
                Criteria.where("isRead").is(false)
                        .and("recipientIds").is(recipientId)
        );
        return mongoTemplate.count(query, Message.class);
    }

    @Override
    public List<Message> searchByContent(String tenantId, String searchTerm) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(searchTerm);
        Query query = TextQuery.queryText(textCriteria)
                .addCriteria(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Message.class);
    }

    @Override
    public List<Message> findByTenantIdAndCreatedAtBetween(String tenantId, Instant startDate, Instant endDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("createdAt").gte(startDate).lte(endDate)
        );
        return mongoTemplate.find(query, Message.class);
    }
}
