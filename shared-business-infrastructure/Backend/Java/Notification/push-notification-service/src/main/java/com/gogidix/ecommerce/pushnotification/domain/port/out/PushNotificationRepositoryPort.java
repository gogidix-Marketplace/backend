package com.gogidix.ecommerce.pushnotification.domain.port.out;

import com.gogidix.ecommerce.pushnotification.domain.model.PushNotification;
import java.util.List;
import java.util.Optional;

public interface PushNotificationRepositoryPort {
    PushNotification save(PushNotification entity);
    Optional<PushNotification> findById(String id);
    List<PushNotification> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}
