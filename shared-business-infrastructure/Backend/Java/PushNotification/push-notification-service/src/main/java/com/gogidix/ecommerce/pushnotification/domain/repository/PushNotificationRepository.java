package com.gogidix.ecommerce.pushnotification.domain.repository;

import com.gogidix.ecommerce.pushnotification.domain.model.PushNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PushNotificationRepository extends MongoRepository<PushNotification, String> {

    Optional<PushNotification findByIdAndTenantId(String id, String tenantId);

    Page<PushNotification> findByTenantId(String tenantId, Pageable pageable);
}