package com.gogidix.transaction.audit.domain.port.out;

import com.gogidix.transaction.audit.domain.model.AuditLog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Output port for audit log persistence operations.
 */
public interface AuditLogRepositoryPort {

    /**
     * Save an audit log entry
     */
    AuditLog save(AuditLog auditLog);

    /**
     * Find audit log by ID
     */
    Optional<AuditLog> findById(String id);

    /**
     * Find all audit logs for a specific entity
     */
    List<AuditLog> findByEntityTypeAndEntityId(String entityType, String entityId);

    /**
     * Find audit logs by tenant and entity
     */
    List<AuditLog> findByTenantIdAndEntityTypeAndEntityId(String tenantId, String entityType, String entityId);

    /**
     * Find audit logs by correlation ID
     */
    List<AuditLog> findByCorrelationId(String correlationId);

    /**
     * Find audit logs by tenant with date range
     */
    List<AuditLog> findByTenantIdAndTimestampBetween(String tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find audit logs by actor
     */
    List<AuditLog> findByActorId(String actorId);

    /**
     * Search audit logs with multiple filters
     */
    List<AuditLog> searchAuditLogs(String tenantId, String entityType, String entityId,
                                    String action, String actorId, String severity,
                                    String category, String status,
                                    LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Count audit logs by tenant
     */
    Long countByTenantId(String tenantId);

    /**
     * Delete audit logs older than specified date
     */
    Long deleteOlderThan(LocalDateTime date);
}
