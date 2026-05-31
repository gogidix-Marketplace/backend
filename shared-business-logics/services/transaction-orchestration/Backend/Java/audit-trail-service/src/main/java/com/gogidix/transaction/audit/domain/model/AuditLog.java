package com.gogidix.transaction.audit.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Audit Log aggregate root for tracking all transaction events.
 * Provides comprehensive audit trail for compliance and debugging.
 */
@Entity
@Table(name = "audit_logs", indexes = {
    @Index(name = "idx_audit_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_audit_entity_type", columnList = "entity_type"),
    @Index(name = "idx_audit_entity_id", columnList = "entity_id"),
    @Index(name = "idx_audit_action", columnList = "action"),
    @Index(name = "idx_audit_actor", columnList = "actor_id"),
    @Index(name = "idx_audit_timestamp", columnList = "timestamp"),
    @Index(name = "idx_audit_correlation_id", columnList = "correlation_id")
})
@Data
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    /**
     * Unique identifier for this audit log entry
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Tenant ID for multi-tenancy isolation
     */
    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    /**
     * Date and time when the entity was created
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Date and time when the entity was last updated
     */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * User who created this entity
     */
    @CreatedBy
    @Column(name = "created_by", length = 100)
    private String createdBy;

    /**
     * User who last updated this entity
     */
    @LastModifiedBy
    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    /**
     * Version field for optimistic locking
     */
    @Version
    @Column(name = "version")
    private Long version;

    /**
     * Indicates whether the entity is active
     */
    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Indicates whether the entity is deleted (soft delete)
     */
    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    /**
     * Type of entity being audited (e.g., "Transaction", "SagaInstance", "Payment")
     */
    @Column(name = "entity_type", nullable = false, length = 100)
    private String entityType;

    /**
     * Unique identifier of the entity being audited
     */
    @Column(name = "entity_id", nullable = false, length = 100)
    private String entityId;

    /**
     * Action performed on the entity (e.g., "CREATE", "UPDATE", "DELETE", "EXECUTE")
     */
    @Column(name = "action", nullable = false, length = 50)
    private String action;

    /**
     * Actor who performed the action (user ID or system service)
     */
    @Column(name = "actor_id", length = 100)
    private String actorId;

    /**
     * Actor type (USER, SYSTEM, SERVICE)
     */
    @Column(name = "actor_type", length = 20)
    private String actorType;

    /**
     * IP address of the actor
     */
    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    /**
     * User agent of the actor
     */
    @Column(name = "user_agent", length = 500)
    private String userAgent;

    /**
     * Correlation ID for tracking across services
     */
    @Column(name = "correlation_id", length = 100)
    private String correlationId;

    /**
     * Timestamp when the action occurred
     */
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    /**
     * Old state before the action (JSON serialized)
     */
    @Column(name = "old_state", columnDefinition = "TEXT")
    private String oldState;

    /**
     * New state after the action (JSON serialized)
     */
    @Column(name = "new_state", columnDefinition = "TEXT")
    private String newState;

    /**
     * Changed fields (JSON serialized array)
     */
    @Column(name = "changed_fields", columnDefinition = "TEXT")
    private String changedFields;

    /**
     * Business context information
     */
    @Column(name = "business_context", columnDefinition = "TEXT")
    private String businessContext;

    /**
     * Severity level (INFO, WARNING, ERROR, CRITICAL)
     */
    @Column(name = "severity", length = 20)
    private String severity;

    /**
     * Category of the audit event (e.g., "AUTHENTICATION", "AUTHORIZATION", "BUSINESS", "SYSTEM")
     */
    @Column(name = "category", length = 50)
    private String category;

    /**
     * Description of the action
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Status of the action (SUCCESS, FAILURE, PENDING)
     */
    @Column(name = "status", length = 20)
    private String status;

    /**
     * Error message if the action failed
     */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    /**
     * Session ID of the actor
     */
    @Column(name = "session_id", length = 100)
    private String sessionId;

    /**
     * Request ID for HTTP requests
     */
    @Column(name = "request_id", length = 100)
    private String requestId;

    /**
     * Lifecycle callback before persist
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Lifecycle callback before update
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Check if this is a critical audit event
     */
    public boolean isCritical() {
        return "CRITICAL".equalsIgnoreCase(severity);
    }

    /**
     * Check if this action was successful
     */
    public boolean isSuccess() {
        return "SUCCESS".equalsIgnoreCase(status);
    }

    /**
     * Check if this action was performed by a system
     */
    public boolean isSystemActor() {
        return "SYSTEM".equalsIgnoreCase(actorType) || "SERVICE".equalsIgnoreCase(actorType);
    }

    /**
     * Check if this action was performed by a user
     */
    public boolean isUserActor() {
        return "USER".equalsIgnoreCase(actorType);
    }

    /**
     * Create a summary of this audit log entry
     */
    public String getSummary() {
        return String.format("[%s] %s %s %s by %s at %s",
            severity != null ? severity : "INFO",
            entityType,
            action,
            entityId,
            actorId != null ? actorId : "SYSTEM",
            timestamp != null ? timestamp : LocalDateTime.now()
        );
    }
}
