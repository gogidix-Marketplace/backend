package com.gogidix.transaction.audit.infrastructure.persistence.postgres;

import com.gogidix.transaction.audit.domain.model.AuditLog;
import com.gogidix.transaction.audit.domain.port.out.AuditLogRepositoryPort;
import com.gogidix.transaction.audit.domain.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * PostgreSQL implementation of AuditLogRepository.
 * Bridges between JPA and domain repository.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PostgresAuditLogRepository implements AuditLogRepository, AuditLogRepositoryPort {

    private final AuditJpaRepository jpaRepository;

    @Override
    @Transactional
    public AuditLog save(AuditLog auditLog) {
        log.debug("Saving audit log: entity={}, action={}, entity={}",
            auditLog.getEntityType(), auditLog.getAction(), auditLog.getEntityId());
        return jpaRepository.save(auditLog);
    }

    @Override
    public Optional<AuditLog> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    @Transactional
    public void delete(AuditLog auditLog) {
        log.debug("Deleting audit log: id={}", auditLog.getId());
        jpaRepository.delete(auditLog);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    // AuditLogRepositoryPort implementation

    @Override
    public Optional<AuditLog> findById(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return jpaRepository.findById(uuid);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid UUID format: {}", id);
            return Optional.empty();
        }
    }

    @Override
    public List<AuditLog> findByEntityTypeAndEntityId(String entityType, String entityId) {
        return jpaRepository.findByEntityTypeAndEntityId(entityType, entityId);
    }

    @Override
    public List<AuditLog> findByTenantIdAndEntityTypeAndEntityId(String tenantId, String entityType, String entityId) {
        return jpaRepository.findByTenantIdAndEntityTypeAndEntityId(tenantId, entityType, entityId);
    }

    @Override
    public List<AuditLog> findByCorrelationId(String correlationId) {
        return jpaRepository.findByCorrelationId(correlationId);
    }

    @Override
    public List<AuditLog> findByTenantIdAndTimestampBetween(String tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.findByTenantIdAndTimestampBetween(tenantId, startDate, endDate);
    }

    @Override
    public List<AuditLog> findByActorId(String actorId) {
        return jpaRepository.findByActorId(actorId);
    }

    @Override
    public List<AuditLog> searchAuditLogs(String tenantId, String entityType, String entityId,
                                          String action, String actorId, String severity,
                                          String category, String status,
                                          LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.searchAuditLogs(
            tenantId, entityType, entityId, action, actorId,
            severity, category, status, startDate, endDate
        );
    }

    @Override
    public Long countByTenantId(String tenantId) {
        return jpaRepository.countByTenantId(tenantId);
    }

    @Override
    @Transactional
    public Long deleteOlderThan(LocalDateTime date) {
        long count = jpaRepository.countByTimestampBefore(date);
        jpaRepository.deleteByTimestampBefore(date);
        log.info("Deleted {} audit logs older than {}", count, date);
        return count;
    }
}
