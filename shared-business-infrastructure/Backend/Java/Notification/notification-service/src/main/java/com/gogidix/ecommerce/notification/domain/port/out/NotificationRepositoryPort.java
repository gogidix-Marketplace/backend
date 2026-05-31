package com.gogidix.ecommerce.notification.domain.port.out;

import com.gogidix.ecommerce.notification.domain.model.Notification;
import java.util.List;
import java.util.Optional;

public interface NotificationRepositoryPort {
    Notification save(Notification entity);
    Optional<Notification> findById(String id);
    List<Notification> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}
