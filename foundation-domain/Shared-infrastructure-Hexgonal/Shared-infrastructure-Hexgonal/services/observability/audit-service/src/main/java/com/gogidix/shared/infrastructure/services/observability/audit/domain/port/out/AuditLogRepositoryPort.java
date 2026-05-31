package com.gogidix.shared.infrastructure.services.observability.audit.domain.port.out;

import com.gogidix.shared.infrastructure.services.observability.audit.domain.model.AuditLog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Audit Log repository output port.
 */
public interface AuditLogRepositoryPort {

    AuditLog save(AuditLog auditLog);

    Optional<AuditLog> findByIdAndTenantId(String id, String tenantId);

    List<AuditLog> findByTenantIdWithFilters(String tenantId, String userId, LocalDateTime startDate, LocalDateTime endDate, int limit);

    List<AuditLog> findByTenantIdAndEntity(String tenantId, String entityType, String entityId);
}
