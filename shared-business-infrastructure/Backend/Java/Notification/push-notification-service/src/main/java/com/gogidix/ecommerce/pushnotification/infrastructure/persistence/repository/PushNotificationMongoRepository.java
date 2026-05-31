package com.gogidix.ecommerce.pushnotification.infrastructure.persistence.repository;

import com.gogidix.ecommerce.pushnotification.domain.model.PushNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PushNotificationMongoRepository extends MongoRepository<PushNotification, String> {
    List<PushNotification> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
