package com.gogidix.sales.notification.infrastructure.persistence.mongo;

import com.gogidix.sales.notification.domain.model.NotificationTemplate;
import com.gogidix.sales.notification.domain.repository.NotificationTemplateRepository;
import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Notification Template
 * Implements template persistence with tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoNotificationTemplateRepository implements NotificationTemplateRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public NotificationTemplate save(NotificationTemplate template) {
        log.debug("Saving template: {} for tenant: {}", template.getTemplateId(), template.getTenantId());
        return mongoTemplate.save(template);
    }

    @Override
    public List<NotificationTemplate> saveAll(List<NotificationTemplate> templates) {
        return templates.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<NotificationTemplate> findById(String id) {
        String tenantId = com.gogidix.sales.notification.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, NotificationTemplate.class));
    }

    @Override
    public Optional<NotificationTemplate> findByTemplateIdAndTenantId(String templateId, String tenantId) {
        Query query = Query.query(
                Criteria.where("templateId").is(templateId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, NotificationTemplate.class));
    }

    @Override
    public Optional<NotificationTemplate> findByCodeAndTenantId(String code, String tenantId) {
        Query query = Query.query(
                Criteria.where("code").is(code)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, NotificationTemplate.class));
    }

    @Override
    public List<NotificationTemplate> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, NotificationTemplate.class);
    }

    @Override
    public List<NotificationTemplate> findByTenantIdAndChannel(String tenantId, NotificationChannel channel) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("channel").is(channel)
        );
        return mongoTemplate.find(query, NotificationTemplate.class);
    }

    @Override
    public List<NotificationTemplate> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isActive").is(isActive)
        );
        return mongoTemplate.find(query, NotificationTemplate.class);
    }

    @Override
    public List<NotificationTemplate> findByTenantIdAndLocale(String tenantId, String locale) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("locale").is(locale)
        );
        return mongoTemplate.find(query, NotificationTemplate.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), NotificationTemplate.class);
    }

    @Override
    public void deleteByTemplateIdAndTenantId(String templateId, String tenantId) {
        Query query = Query.query(
                Criteria.where("templateId").is(templateId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, NotificationTemplate.class);
    }

    @Override
    public boolean existsByCodeAndTenantId(String code, String tenantId) {
        Query query = Query.query(
                Criteria.where("code").is(code)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, NotificationTemplate.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, NotificationTemplate.class);
    }
}
