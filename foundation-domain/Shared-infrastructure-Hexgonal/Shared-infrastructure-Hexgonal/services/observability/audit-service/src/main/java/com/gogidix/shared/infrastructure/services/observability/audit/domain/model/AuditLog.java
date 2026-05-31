package com.gogidix.shared.infrastructure.services.observability.audit.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Audit Log domain entity.
 * <p>
 * Records all user and system activities for compliance and debugging.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "audit_logs")
public class AuditLog {

    @Id
    private String id;

    @Indexed
    private TenantId tenantId;

    @Indexed
    private String userId;

    private String username;

    @Indexed
    private AuditAction action;

    private AuditEntityType entityType;

    private String entityId;

    private String entityName;

    private String description;

    @Indexed
    private String ipAddress;

    private String userAgent;

    private boolean success;

    private String errorMessage;

    private Map<String, Object> changes;

    private Map<String, Object> metadata;

    @Indexed
    private LocalDateTime timestamp;

    /**
     * Audit actions.
     */
    public enum AuditAction {
        CREATE,
        READ,
        UPDATE,
        DELETE,
        LOGIN,
        LOGOUT,
        EXPORT,
        IMPORT,
        APPROVE,
        REJECT,
        ASSIGN,
        UNASSIGN,
        ARCHIVE,
        RESTORE
    }

    /**
     * Entity types that can be audited.
     */
    public enum AuditEntityType {
        USER,
        ROLE,
        PERMISSION,
        TENANT,
        FILE,
        NOTIFICATION,
        CONFIGURATION,
        SYSTEM
    }
}
