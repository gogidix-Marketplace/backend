package com.gogidix.sales.notification.infrastructure.persistence.mongo;

import com.gogidix.sales.notification.domain.model.NotificationPreference;
import com.gogidix.sales.notification.domain.repository.NotificationPreferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Notification Preference
 * Implements preference persistence with tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoNotificationPreferenceRepository implements NotificationPreferenceRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public NotificationPreference save(NotificationPreference preference) {
        log.debug("Saving preference for user: {} in tenant: {}", preference.getUserId(), preference.getTenantId());
        return mongoTemplate.save(preference);
    }

    @Override
    public List<NotificationPreference> saveAll(List<NotificationPreference> preferences) {
        return preferences.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<NotificationPreference> findById(String id) {
        String tenantId = com.gogidix.sales.notification.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, NotificationPreference.class));
    }

    @Override
    public Optional<NotificationPreference> findByUserIdAndTenantId(String userId, String tenantId) {
        Query query = Query.query(
                Criteria.where("userId").is(userId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, NotificationPreference.class));
    }

    @Override
    public List<NotificationPreference> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, NotificationPreference.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), NotificationPreference.class);
    }

    @Override
    public void deleteByUserIdAndTenantId(String userId, String tenantId) {
        Query query = Query.query(
                Criteria.where("userId").is(userId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, NotificationPreference.class);
    }

    @Override
    public boolean existsByUserIdAndTenantId(String userId, String tenantId) {
        Query query = Query.query(
                Criteria.where("userId").is(userId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, NotificationPreference.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, NotificationPreference.class);
    }
}
