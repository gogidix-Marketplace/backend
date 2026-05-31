package com.gogidix.centralconfiguration.notificationservice.domain.repository;

import com.gogidix.centralconfiguration.notificationservice.domain.model.Notification;
import com.gogidix.centralconfiguration.notificationservice.domain.model.NotificationStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Notification aggregate.
 */
public interface NotificationRepository {

    Notification save(Notification notification);

    Optional<Notification> findById(Long id);

    List<Notification> findByTenantId(String tenantId);

    List<Notification> findByTenantIdAndStatus(String tenantId, NotificationStatus status);

    List<Notification> findByRecipient(String recipient);

    void delete(Notification notification);

    List<Notification> findAll(int page, int size);
}
