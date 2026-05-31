package com.gogidix.management.executive.approval.domain.service;

import com.gogidix.management.executive.approval.domain.model.Approval;

/**
 * Domain service for Approval business logic
 */
public interface ApprovalDomainService {

    /**
     * Record layout class
     */
    static record ApprovalLayout(int columns, int rows, int itemCount) {}

    /**
     * Record health metrics class
     */
    static record ApprovalHealthMetrics(int score, boolean healthy, String recommendation) {}

    /**
     * Validate approval for creation
     */
    void validateApprovalForCreation(Approval approval);

    /**
     * Validate approval for update
     */
    void validateApprovalForUpdate(Approval approval);

    /**
     * Check if approval can be published
     */
    boolean canPublish(Approval approval);

    /**
     * Check if approval can be deleted
     */
    boolean canDelete(Approval approval);

    /**
     * Calculate approval layout
     */
    ApprovalLayout calculateLayout(Approval approval);

    /**
     * Validate approval layout
     */
    void validateLayout(Approval approval);

    /**
     * Get approval health metrics
     */
    ApprovalHealthMetrics getHealthMetrics(Approval approval);
}
