package com.gogidix.transaction.audit.infrastructure.persistence.postgres;

import com.gogidix.transaction.audit.domain.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for AuditLog entity.
 */
public interface AuditJpaRepository extends JpaRepository<AuditLog, UUID> {

    /**
     * Find audit logs by entity type and entity ID
     */
    List<AuditLog> findByEntityTypeAndEntityId(String entityType, String entityId);

    /**
     * Find audit logs by tenant, entity type, and entity ID
     */
    List<AuditLog> findByTenantIdAndEntityTypeAndEntityId(String tenantId, String entityType, String entityId);

    /**
     * Find audit logs by correlation ID
     */
    List<AuditLog> findByCorrelationId(String correlationId);

    /**
     * Find audit logs by tenant and timestamp range
     */
    List<AuditLog> findByTenantIdAndTimestampBetween(String tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find audit logs by actor ID
     */
    List<AuditLog> findByActorId(String actorId);

    /**
     * Count audit logs by tenant
     */
    Long countByTenantId(String tenantId);

    /**
     * Count audit logs older than date
     */
    Long countByTimestampBefore(LocalDateTime date);

    /**
     * Delete audit logs older than date
     */
    void deleteByTimestampBefore(LocalDateTime date);

    /**
     * Search audit logs with filters
     */
    @Query("SELECT a FROM AuditLog a WHERE " +
           "(:tenantId IS NULL OR a.tenantId = :tenantId) AND " +
           "(:entityType IS NULL OR a.entityType = :entityType) AND " +
           "(:entityId IS NULL OR a.entityId = :entityId) AND " +
           "(:action IS NULL OR a.action = :action) AND " +
           "(:actorId IS NULL OR a.actorId = :actorId) AND " +
           "(:severity IS NULL OR a.severity = :severity) AND " +
           "(:category IS NULL OR a.category = :category) AND " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:startDate IS NULL OR a.timestamp >= :startDate) AND " +
           "(:endDate IS NULL OR a.timestamp <= :endDate)")
    List<AuditLog> searchAuditLogs(
        @Param("tenantId") String tenantId,
        @Param("entityType") String entityType,
        @Param("entityId") String entityId,
        @Param("action") String action,
        @Param("actorId") String actorId,
        @Param("severity") String severity,
        @Param("category") String category,
        @Param("status") String status,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
}
