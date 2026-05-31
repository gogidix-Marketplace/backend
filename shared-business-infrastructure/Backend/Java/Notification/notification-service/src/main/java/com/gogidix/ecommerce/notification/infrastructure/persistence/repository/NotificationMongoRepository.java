package com.gogidix.ecommerce.notification.infrastructure.persistence.repository;

import com.gogidix.ecommerce.notification.domain.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationMongoRepository extends MongoRepository<Notification, String> {
    List<Notification> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
