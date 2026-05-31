package com.gogidix.shared.infrastructure.services.infrastructure.caching.infrastructure.persistence;

import com.gogidix.shared.infrastructure.services.infrastructure.caching.domain.model.CacheEntry;
import com.gogidix.shared.infrastructure.services.infrastructure.caching.domain.port.out.CacheRepositoryPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * MongoDB repository adapter implementing CacheRepositoryPort.
 */
@Component
public class CacheRepositoryAdapter implements CacheRepositoryPort {

    private final MongoTemplate mongoTemplate;

    public CacheRepositoryAdapter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public CacheEntry save(CacheEntry entry) {
        return mongoTemplate.save(entry);
    }

    @Override
    public Optional<CacheEntry> findByTenantIdAndKey(String tenantId, String key) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("key").is(key));
        return Optional.ofNullable(mongoTemplate.findOne(query, CacheEntry.class));
    }

    @Override
    public List<CacheEntry> findByTenantId(String tenantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, CacheEntry.class);
    }

    @Override
    public List<CacheEntry> findExpiredEntries(String tenantId, LocalDateTime now) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("expiresAt").lt(now));
        return mongoTemplate.find(query, CacheEntry.class);
    }

    @Override
    public List<CacheEntry> findByTenantIdAndKeyIn(String tenantId, Set<String> keys) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("key").in(keys));
        return mongoTemplate.find(query, CacheEntry.class);
    }

    @Override
    public void deleteByTenantIdAndKey(String tenantId, String key) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("key").is(key));
        mongoTemplate.remove(query, CacheEntry.class);
    }

    @Override
    public void deleteByTenantId(String tenantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, CacheEntry.class);
    }

    @Override
    public void deleteByTenantIdAndKeyIn(String tenantId, Set<String> keys) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("key").in(keys));
        mongoTemplate.remove(query, CacheEntry.class);
    }

    @Override
    public long deleteExpiredEntries(String tenantId, LocalDateTime now) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("expiresAt").lt(now));
        return mongoTemplate.count(query, CacheEntry.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, CacheEntry.class);
    }

    @Override
    public boolean existsByTenantIdAndKey(String tenantId, String key) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("key").is(key));
        return mongoTemplate.exists(query, CacheEntry.class);
    }
}
