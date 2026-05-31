package com.gogidix.sales.notification.infrastructure.persistence.mongo;

import com.gogidix.sales.notification.domain.model.Notification;
import com.gogidix.sales.notification.domain.repository.NotificationRepository;
import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;
import com.gogidix.sales.notification.domain.valueobject.NotificationStatus;
import com.gogidix.sales.notification.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
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
 * MongoDB Repository Implementation - Notification
 * Implements notification persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoNotificationRepository implements NotificationRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Notification save(Notification notification) {
        log.debug("Saving notification: {} for tenant: {}", notification.getNotificationId(), notification.getTenantId());
        return mongoTemplate.save(notification);
    }

    @Override
    public List<Notification> saveAll(List<Notification> notifications) {
        return notifications.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<Notification> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Notification.class));
    }

    @Override
    public Optional<Notification> findByNotificationIdAndTenantId(String notificationId, String tenantId) {
        Query query = Query.query(
                Criteria.where("notificationId").is(notificationId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Notification.class));
    }

    @Override
    public List<Notification> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Notification.class);
    }

    @Override
    public List<Notification> findByUserIdAndTenantId(String userId, String tenantId) {
        Query query = Query.query(
                Criteria.where("userId").is(userId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, Notification.class);
    }

    @Override
    public List<Notification> findByUserIdAndTenantIdOrderByCreatedAtDesc(String userId, String tenantId) {
        Query query = Query.query(
                Criteria.where("userId").is(userId)
                        .and("tenantId").is(tenantId)
        ).with(Sort.by(Sort.Direction.DESC, "createdAt"));
        return mongoTemplate.find(query, Notification.class);
    }

    @Override
    public List<Notification> findByRecipientIdsContainingAndTenantId(String recipientId, String tenantId) {
        Query query = Query.query(
                Criteria.where("recipientIds").is(recipientId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, Notification.class);
    }

    @Override
    public List<Notification> findByTenantIdAndStatus(String tenantId, NotificationStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, Notification.class);
    }

    @Override
    public List<Notification> findByTenantIdAndChannel(String tenantId, NotificationChannel channel) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("channel").is(channel)
        );
        return mongoTemplate.find(query, Notification.class);
    }

    @Override
    public List<Notification> findByScheduledAtBeforeAndStatus(Instant scheduledAt, NotificationStatus status) {
        Query query = Query.query(
                Criteria.where("scheduledAt").lte(scheduledAt)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, Notification.class);
    }

    @Override
    public List<Notification> findByGroupIdAndTenantId(String groupId, String tenantId) {
        Query query = Query.query(
                Criteria.where("groupId").is(groupId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, Notification.class);
    }

    @Override
    public boolean existsByNotificationIdAndTenantId(String notificationId, String tenantId) {
        Query query = Query.query(
                Criteria.where("notificationId").is(notificationId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Notification.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Notification.class);
    }

    @Override
    public void deleteByNotificationIdAndTenantId(String notificationId, String tenantId) {
        Query query = Query.query(
                Criteria.where("notificationId").is(notificationId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Notification.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, Notification.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, Notification.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, NotificationStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.count(query, Notification.class);
    }

    @Override
    public long countUnreadByUserId(String userId) {
        Query query = Query.query(
                Criteria.where("userId").is(userId)
                        .and("isRead").is(false)
        );
        return mongoTemplate.count(query, Notification.class);
    }

    @Override
    public long countUnreadByRecipientId(String recipientId) {
        Query query = Query.query(
                Criteria.where("recipientIds").is(recipientId)
                        .and("isRead").is(false)
        );
        return mongoTemplate.count(query, Notification.class);
    }

    @Override
    public List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(String userId) {
        Query query = Query.query(
                Criteria.where("userId").is(userId)
                        .and("isRead").is(false)
        ).with(Sort.by(Sort.Direction.DESC, "createdAt"));
        return mongoTemplate.find(query, Notification.class);
    }

    @Override
    public List<Notification> findByUserIdAndTenantIdAndIsReadFalseOrderByCreatedAtDesc(String userId, String tenantId) {
        Query query = Query.query(
                Criteria.where("userId").is(userId)
                        .and("tenantId").is(tenantId)
                        .and("isRead").is(false)
        ).with(Sort.by(Sort.Direction.DESC, "createdAt"));
        return mongoTemplate.find(query, Notification.class);
    }

    @Override
    public List<Notification> findByExpiresAtBeforeAndStatusNot(Instant expiresAt, NotificationStatus status) {
        Query query = Query.query(
                Criteria.where("expiresAt").lt(expiresAt)
                        .and("status").ne(status)
        );
        return mongoTemplate.find(query, Notification.class);
    }

    @Override
    public List<Notification> findByTenantIdAndCreatedAtBetween(String tenantId, Instant startDate, Instant endDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("createdAt").gte(startDate).lte(endDate)
        );
        return mongoTemplate.find(query, Notification.class);
    }

    @Override
    public List<Notification> searchByContent(String tenantId, String searchTerm) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(searchTerm);
        Query query = TextQuery.queryText(textCriteria)
                .addCriteria(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Notification.class);
    }
}
