package com.gogidix.transaction.audit.domain.repository;

import com.gogidix.transaction.audit.domain.model.AuditLog;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for AuditLog aggregate root.
 * This is the domain-level repository interface.
 */
public interface AuditLogRepository {

    /**
     * Save an audit log entry
     */
    AuditLog save(AuditLog auditLog);

    /**
     * Find audit log by ID
     */
    Optional<AuditLog> findById(UUID id);

    /**
     * Delete an audit log entry
     */
    void delete(AuditLog auditLog);

    /**
     * Check if audit log exists
     */
    boolean existsById(UUID id);
}
