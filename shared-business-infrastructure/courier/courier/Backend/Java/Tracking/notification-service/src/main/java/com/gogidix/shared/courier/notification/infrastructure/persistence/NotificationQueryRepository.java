package com.gogidix.shared.courier.notification.infrastructure.persistence;

import com.gogidix.shared.courier.notification.domain.entity.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Custom query repository for notifications
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class NotificationQueryRepository {

    private final MongoTemplate mongoTemplate;

    /**
     * Find unread notifications for recipient
     */
    public List<Notification> findUnreadNotifications(String tenantId, String recipientId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("recipientId").is(recipientId));
        query.addCriteria(Criteria.where("status").is(Notification.NotificationStatus.SENT));
        query.limit(100);

        return mongoTemplate.find(query, Notification.class);
    }

    /**
     * Find notifications by type
     */
    public List<Notification> findByType(String tenantId, String notificationType, int limit) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("notificationType").is(notificationType));
        query.limit(limit);

        return mongoTemplate.find(query, Notification.class);
    }

    /**
     * Get notification statistics for tenant
     */
    public NotificationStatistics getStatistics(String tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("sentAt").gte(startDate).lte(endDate));

        List<Notification> results = mongoTemplate.find(query, Notification.class);

        if (results.isEmpty()) {
            return new NotificationStatistics(0, 0, 0);
        }

        long sentCount = results.stream()
                .filter(n -> n.getStatus() == Notification.NotificationStatus.SENT)
                .count();
        long readCount = results.stream()
                .filter(n -> n.getStatus() == Notification.NotificationStatus.READ)
                .count();

        return new NotificationStatistics(results.size(), (int) readCount, (int) sentCount);
    }

    /**
     * Statistics result class
     */
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class NotificationStatistics {
        private Integer totalSent;
        private Integer totalRead;
        private Integer totalUnread;
    }
}
