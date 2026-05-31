package com.gogidix.sales.notification.infrastructure.persistence.mongo;

import com.gogidix.sales.notification.domain.model.NotificationDelivery;
import com.gogidix.sales.notification.domain.repository.NotificationDeliveryRepository;
import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;
import com.gogidix.sales.notification.domain.valueobject.NotificationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Notification Delivery
 * Implements delivery tracking persistence with tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoNotificationDeliveryRepository implements NotificationDeliveryRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public NotificationDelivery save(NotificationDelivery delivery) {
        log.debug("Saving delivery: {} for notification: {}", delivery.getDeliveryId(), delivery.getNotificationId());
        return mongoTemplate.save(delivery);
    }

    @Override
    public List<NotificationDelivery> saveAll(List<NotificationDelivery> deliveries) {
        return deliveries.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<NotificationDelivery> findById(String id) {
        String tenantId = com.gogidix.sales.notification.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, NotificationDelivery.class));
    }

    @Override
    public Optional<NotificationDelivery> findByDeliveryIdAndTenantId(String deliveryId, String tenantId) {
        Query query = Query.query(
                Criteria.where("deliveryId").is(deliveryId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, NotificationDelivery.class));
    }

    @Override
    public List<NotificationDelivery> findByNotificationIdAndTenantId(String notificationId, String tenantId) {
        Query query = Query.query(
                Criteria.where("notificationId").is(notificationId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, NotificationDelivery.class);
    }

    @Override
    public List<NotificationDelivery> findByRecipientIdAndTenantId(String recipientId, String tenantId) {
        Query query = Query.query(
                Criteria.where("recipientId").is(recipientId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, NotificationDelivery.class);
    }

    @Override
    public List<NotificationDelivery> findByRecipientIdAndTenantIdOrderByCreatedAtDesc(String recipientId, String tenantId) {
        Query query = Query.query(
                Criteria.where("recipientId").is(recipientId)
                        .and("tenantId").is(tenantId)
        ).with(Sort.by(Sort.Direction.DESC, "createdAt"));
        return mongoTemplate.find(query, NotificationDelivery.class);
    }

    @Override
    public List<NotificationDelivery> findByNotificationIdAndRecipientId(String notificationId, String recipientId) {
        Query query = Query.query(
                Criteria.where("notificationId").is(notificationId)
                        .and("recipientId").is(recipientId)
        );
        return mongoTemplate.find(query, NotificationDelivery.class);
    }

    @Override
    public List<NotificationDelivery> findByTenantIdAndStatus(String tenantId, NotificationStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, NotificationDelivery.class);
    }

    @Override
    public List<NotificationDelivery> findByTenantIdAndChannel(String tenantId, NotificationChannel channel) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("channel").is(channel)
        );
        return mongoTemplate.find(query, NotificationDelivery.class);
    }

    @Override
    public List<NotificationDelivery> findByStatusAndNextRetryAtBefore(NotificationStatus status, Instant retryAt) {
        Query query = Query.query(
                Criteria.where("status").is(status)
                        .and("nextRetryAt").lte(retryAt)
        );
        return mongoTemplate.find(query, NotificationDelivery.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), NotificationDelivery.class);
    }

    @Override
    public void deleteByNotificationIdAndTenantId(String notificationId, String tenantId) {
        Query query = Query.query(
                Criteria.where("notificationId").is(notificationId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, NotificationDelivery.class);
    }

    @Override
    public void deleteByDeliveryIdAndTenantId(String deliveryId, String tenantId) {
        Query query = Query.query(
                Criteria.where("deliveryId").is(deliveryId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, NotificationDelivery.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, NotificationDelivery.class);
    }

    @Override
    public long countByNotificationId(String notificationId) {
        Query query = Query.query(Criteria.where("notificationId").is(notificationId));
        return mongoTemplate.count(query, NotificationDelivery.class);
    }

    @Override
    public long countByRecipientIdAndStatus(String recipientId, NotificationStatus status) {
        Query query = Query.query(
                Criteria.where("recipientId").is(recipientId)
                        .and("status").is(status)
        );
        return mongoTemplate.count(query, NotificationDelivery.class);
    }

    @Override
    public long countFailedDeliveriesByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(NotificationStatus.FAILED)
        );
        return mongoTemplate.count(query, NotificationDelivery.class);
    }
}
