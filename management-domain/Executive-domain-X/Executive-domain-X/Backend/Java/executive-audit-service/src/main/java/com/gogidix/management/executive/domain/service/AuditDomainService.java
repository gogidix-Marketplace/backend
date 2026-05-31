package com.gogidix.management.executive.audit.domain.service;

import com.gogidix.management.executive.audit.domain.model.Audit;

/**
 * Domain service for Audit business logic
 */
public interface AuditDomainService {

    /**
     * Record layout class
     */
    static record AuditLayout(int columns, int rows, int itemCount) {}

    /**
     * Record health metrics class
     */
    static record AuditHealthMetrics(int score, boolean healthy, String recommendation) {}

    /**
     * Validate audit for creation
     */
    void validateAuditForCreation(Audit audit);

    /**
     * Validate audit for update
     */
    void validateAuditForUpdate(Audit audit);

    /**
     * Check if audit can be published
     */
    boolean canPublish(Audit audit);

    /**
     * Check if audit can be deleted
     */
    boolean canDelete(Audit audit);

    /**
     * Calculate audit layout
     */
    AuditLayout calculateLayout(Audit audit);

    /**
     * Validate audit layout
     */
    void validateLayout(Audit audit);

    /**
     * Get audit health metrics
     */
    AuditHealthMetrics getHealthMetrics(Audit audit);
}
