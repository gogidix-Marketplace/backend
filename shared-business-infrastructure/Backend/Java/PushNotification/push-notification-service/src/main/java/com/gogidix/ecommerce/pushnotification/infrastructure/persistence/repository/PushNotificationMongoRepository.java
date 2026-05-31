package com.gogidix.ecommerce.pushnotification.infrastructure.persistence.repository;

import com.gogidix.ecommerce.pushnotification.domain.model.PushNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PushNotificationMongoRepository extends MongoRepository<PushNotification, String> {

    Optional<PushNotification> findByIdAndTenantId(String id, String tenantId);

    Page<PushNotification> findByTenantId(String tenantId, Pageable pageable);
}