package com.gogidix.sales.notification.domain.repository;

import com.gogidix.sales.notification.domain.model.NotificationTemplate;
import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;

import java.util.List;
import java.util.Optional;

/**
 * Notification Template Repository Interface (Port)
 * Defines the contract for template persistence operations
 */
public interface NotificationTemplateRepository {

    NotificationTemplate save(NotificationTemplate template);

    List<NotificationTemplate> saveAll(List<NotificationTemplate> templates);

    Optional<NotificationTemplate> findById(String id);

    Optional<NotificationTemplate> findByTemplateIdAndTenantId(String templateId, String tenantId);

    Optional<NotificationTemplate> findByCodeAndTenantId(String code, String tenantId);

    List<NotificationTemplate> findByTenantId(String tenantId);

    List<NotificationTemplate> findByTenantIdAndChannel(String tenantId, NotificationChannel channel);

    List<NotificationTemplate> findByTenantIdAndIsActive(String tenantId, Boolean isActive);

    List<NotificationTemplate> findByTenantIdAndLocale(String tenantId, String locale);

    void deleteById(String id);

    void deleteByTemplateIdAndTenantId(String templateId, String tenantId);

    boolean existsByCodeAndTenantId(String code, String tenantId);

    long countByTenantId(String tenantId);
}
