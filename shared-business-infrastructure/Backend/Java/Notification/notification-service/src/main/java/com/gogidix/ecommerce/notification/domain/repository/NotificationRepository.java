package com.gogidix.ecommerce.notification.domain.repository;

import com.gogidix.ecommerce.notification.domain.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
