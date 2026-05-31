package com.gogidix.ecommerce.pushnotification.domain.repository;

import com.gogidix.ecommerce.pushnotification.domain.model.PushNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PushNotificationRepository extends MongoRepository<PushNotification, String> {
    List<PushNotification> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
