package com.gogidix.sales.communication.infrastructure.persistence.mongo;

import com.gogidix.sales.communication.domain.model.Message;
import com.gogidix.sales.communication.domain.model.MessageTemplate;
import com.gogidix.sales.communication.domain.repository.MessageTemplateRepository;
import com.gogidix.sales.communication.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - MessageTemplate
 * Implements message template persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoMessageTemplateRepository implements MessageTemplateRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public MessageTemplate save(MessageTemplate template) {
        log.debug("Saving template: {} for tenant: {}", template.getTemplateId(), template.getTenantId());
        return mongoTemplate.save(template);
    }

    @Override
    public List<MessageTemplate> saveAll(List<MessageTemplate> templates) {
        return templates.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<MessageTemplate> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, MessageTemplate.class));
    }

    @Override
    public Optional<MessageTemplate> findByTemplateIdAndTenantId(String templateId, String tenantId) {
        Query query = Query.query(
                Criteria.where("templateId").is(templateId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, MessageTemplate.class));
    }

    @Override
    public Optional<MessageTemplate> findByCodeAndTenantId(String code, String tenantId) {
        Query query = Query.query(
                Criteria.where("code").is(code)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, MessageTemplate.class));
    }

    @Override
    public List<MessageTemplate> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, MessageTemplate.class);
    }

    @Override
    public List<MessageTemplate> findByTenantIdAndStatus(String tenantId, MessageTemplate.TemplateStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, MessageTemplate.class);
    }

    @Override
    public List<MessageTemplate> findByTenantIdAndType(String tenantId, MessageTemplate.TemplateType type) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("type").is(type)
        );
        return mongoTemplate.find(query, MessageTemplate.class);
    }

    @Override
    public List<MessageTemplate> findByTenantIdAndChannelType(String tenantId, Message.ChannelType channelType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("channelType").is(channelType)
        );
        return mongoTemplate.find(query, MessageTemplate.class);
    }

    @Override
    public List<MessageTemplate> findByTenantIdAndCategory(String tenantId, String category) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("category").is(category)
        );
        return mongoTemplate.find(query, MessageTemplate.class);
    }

    @Override
    public List<MessageTemplate> findByTenantIdAndTagsContaining(String tenantId, String tag) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("tags").is(tag)
        );
        return mongoTemplate.find(query, MessageTemplate.class);
    }

    @Override
    public List<MessageTemplate> findByTenantIdAndLanguageAndLocale(String tenantId, String language, String locale) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("language").is(language)
                        .and("locale").is(locale)
        );
        return mongoTemplate.find(query, MessageTemplate.class);
    }

    @Override
    public List<MessageTemplate> findByParentTemplateIdAndTenantId(String parentTemplateId, String tenantId) {
        Query query = Query.query(
                Criteria.where("parentTemplateId").is(parentTemplateId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, MessageTemplate.class);
    }

    @Override
    public boolean existsByTemplateIdAndTenantId(String templateId, String tenantId) {
        Query query = Query.query(
                Criteria.where("templateId").is(templateId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, MessageTemplate.class);
    }

    @Override
    public boolean existsByCodeAndTenantId(String code, String tenantId) {
        Query query = Query.query(
                Criteria.where("code").is(code)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, MessageTemplate.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), MessageTemplate.class);
    }

    @Override
    public void deleteByTemplateIdAndTenantId(String templateId, String tenantId) {
        Query query = Query.query(
                Criteria.where("templateId").is(templateId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, MessageTemplate.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, MessageTemplate.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, MessageTemplate.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, MessageTemplate.TemplateStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.count(query, MessageTemplate.class);
    }

    @Override
    public List<MessageTemplate> searchByNameOrDescription(String tenantId, String searchTerm) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(searchTerm);
        Query query = TextQuery.queryText(textCriteria)
                .addCriteria(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, MessageTemplate.class);
    }
}
