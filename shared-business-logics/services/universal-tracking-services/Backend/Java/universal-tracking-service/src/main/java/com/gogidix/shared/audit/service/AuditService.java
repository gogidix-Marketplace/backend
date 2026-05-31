package com.gogidix.shared.audit.service;

import org.springframework.stereotype.Service;

/**
 * Audit service for logging audit events.
 */
@Service
public class AuditService {

    /**
     * Log an audit event
     */
    public void audit(String action, String entityType, String entityId, String description) {
        // In a real implementation, this would persist to an audit log
        // For now, just log to console
        System.out.println(String.format("[AUDIT] action=%s, entityType=%s, entityId=%s, description=%s",
            action, entityType, entityId, description));
    }
}
