package com.gogidix.dashboard.shared.adapter.audit;

import com.gogidix.shared.audit.service.AuditService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Enterprise adapter for audit service providing a simplified, service-friendly API.
 * 
 * <p>This adapter wraps the Foundation shared-audit library's AuditService,
 * providing a clean interface for dashboard services to record audit events.
 * 
 * <p><strong>Usage Example:</strong>
 * <pre>{@code
 * @Autowired
 * private DashboardAuditService auditService;
 * 
 * public void createDashboard(Dashboard dashboard) {
 *     // Business logic...
 *     auditService.auditCreate("Dashboard", dashboard.getId(), "Dashboard created");
 * }
 * 
 * public void deleteDashboard(String dashboardId) {
 *     // Business logic...
 *     auditService.auditDelete("Dashboard", dashboardId, "Dashboard deleted");
 * }
 * }</pre>
 * 
 * @author Gogidix Dashboard Team
 * @since 1.0.0
 */
@Service
public class DashboardAuditService {

    private final AuditService delegate;

    public DashboardAuditService(AuditService delegate) {
        this.delegate = delegate;
    }

    /**
     * Records an audit event for a create operation.
     * 
     * @param entityType the type of entity created
     * @param entityId the ID of the entity
     * @param description the audit description
     */
    public void auditCreate(String entityType, String entityId, String description) {
        audit("CREATE", entityType, entityId, description);
    }

    /**
     * Records an audit event for an update operation.
     * 
     * @param entityType the type of entity updated
     * @param entityId the ID of the entity
     * @param description the audit description
     */
    public void auditUpdate(String entityType, String entityId, String description) {
        audit("UPDATE", entityType, entityId, description);
    }

    /**
     * Records an audit event for a delete operation.
     * 
     * @param entityType the type of entity deleted
     * @param entityId the ID of the entity
     * @param description the audit description
     */
    public void auditDelete(String entityType, String entityId, String description) {
        audit("DELETE", entityType, entityId, description);
    }

    /**
     * Records an audit event for a read operation.
     * 
     * @param entityType the type of entity read
     * @param entityId the ID of the entity
     * @param description the audit description
     */
    public void auditRead(String entityType, String entityId, String description) {
        audit("READ", entityType, entityId, description);
    }

    /**
     * Records an audit event for an export operation.
     * 
     * @param entityType the type of entity exported
     * @param entityId the ID of the entity or export
     * @param description the audit description
     */
    public void auditExport(String entityType, String entityId, String description) {
        audit("EXPORT", entityType, entityId, description);
    }

    /**
     * Records a generic audit event.
     * 
     * <p>This is the primary audit method that all specialized methods delegate to.
     * 
     * @param action the action performed (CREATE, UPDATE, DELETE, READ, EXPORT, etc.)
     * @param entityType the type of entity
     * @param entityId the ID of the entity
     * @param description the audit description
     */
    public void audit(String action, String entityType, String entityId, String description) {
        Map<String, Object> details = new HashMap<>();
        details.put("description", description);
        details.put("timestamp", LocalDateTime.now().toString());
        details.put("correlationId", UUID.randomUUID().toString());
        
        delegate.logEvent(
            "system",
            action,
            entityType,
            entityId
        );
    }

    /**
     * Records an audit event with additional metadata.
     * 
     * @param action the action performed
     * @param entityType the type of entity
     * @param entityId the ID of the entity
     * @param description the audit description
     * @param metadata additional metadata as key-value pairs
     */
    public void auditWithMetadata(String action, String entityType, String entityId, 
                                   String description, Map<String, Object> metadata) {
        Map<String, Object> details = new HashMap<>(metadata);
        details.put("description", description);
        details.put("timestamp", LocalDateTime.now().toString());
        
        delegate.logEvent(
            "system",
            action,
            entityType,
            entityId
        );
    }

    /**
     * Records a security-related audit event.
     * 
     * @param action the security action (LOGIN, LOGOUT, ACCESS_DENIED, etc.)
     * @param userId the user ID involved
     * @param description the audit description
     */
    public void auditSecurity(String action, String userId, String description) {
        audit(action, "User", userId, description);
    }

    /**
     * Records a data query audit event.
     * 
     * @param queryType the type of query executed
     * @param queryId the query identifier
     * @param description the audit description
     */
    public void auditQuery(String queryType, String queryId, String description) {
        audit("QUERY", queryType, queryId, description);
    }
}
