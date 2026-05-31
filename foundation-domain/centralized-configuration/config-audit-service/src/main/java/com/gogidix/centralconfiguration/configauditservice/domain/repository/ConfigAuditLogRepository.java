package com.gogidix.centralconfiguration.configauditservice.domain.repository;

import com.gogidix.centralconfiguration.configauditservice.domain.model.AuditAction;
import com.gogidix.centralconfiguration.configauditservice.domain.model.ConfigAuditLog;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for ConfigAuditLog aggregate.
 */
public interface ConfigAuditLogRepository {

    ConfigAuditLog save(ConfigAuditLog auditLog);

    List<ConfigAuditLog> findByTenantId(String tenantId);

    List<ConfigAuditLog> findByTenantIdAndEntityType(String tenantId, String entityType);

    List<ConfigAuditLog> findByTenantIdAndEntityTypeAndEntityId(
            String tenantId, String entityType, String entityId);

    List<ConfigAuditLog> findByTenantIdAndAction(String tenantId, AuditAction action);

    List<ConfigAuditLog> findByTenantIdAndDateRange(
            String tenantId, LocalDateTime startDate, LocalDateTime endDate);

    List<ConfigAuditLog> findByChangedBy(String changedBy);

    void delete(ConfigAuditLog auditLog);

    List<ConfigAuditLog> findAll(int page, int size);
}
