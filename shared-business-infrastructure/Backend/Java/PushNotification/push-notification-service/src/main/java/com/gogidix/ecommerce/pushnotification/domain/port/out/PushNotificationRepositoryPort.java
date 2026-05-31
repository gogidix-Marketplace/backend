package com.gogidix.ecommerce.pushnotification.domain.port.out;

import com.gogidix.ecommerce.pushnotification.domain.model.PushNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PushNotificationRepositoryPort {

    PushNotification save(PushNotification entity);

    Optional<PushNotification> findByIdAndTenantId(String id, String tenantId);

    Page<PushNotification> findByTenantId(String tenantId, Pageable pageable);

    void deleteById(String id);
}