package com.gogidix.sales.notification.domain.repository;

import com.gogidix.sales.notification.domain.model.NotificationPreference;

import java.util.List;
import java.util.Optional;

/**
 * Notification Preference Repository Interface (Port)
 * Defines the contract for preference persistence operations
 */
public interface NotificationPreferenceRepository {

    NotificationPreference save(NotificationPreference preference);

    List<NotificationPreference> saveAll(List<NotificationPreference> preferences);

    Optional<NotificationPreference> findById(String id);

    Optional<NotificationPreference> findByUserIdAndTenantId(String userId, String tenantId);

    List<NotificationPreference> findByTenantId(String tenantId);

    void deleteById(String id);

    void deleteByUserIdAndTenantId(String userId, String tenantId);

    boolean existsByUserIdAndTenantId(String userId, String tenantId);

    long countByTenantId(String tenantId);
}
