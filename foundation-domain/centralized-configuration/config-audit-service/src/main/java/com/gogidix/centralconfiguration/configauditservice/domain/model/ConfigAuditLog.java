package com.gogidix.centralconfiguration.configauditservice.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Config Audit Log aggregate root for configuration audit trail.
 */
@Entity
@Table(name = "config_audit_logs", indexes = {
    @Index(name = "idx_audit_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_audit_type", columnList = "entity_type"),
    @Index(name = "idx_audit_action", columnList = "action"),
    @Index(name = "idx_audit_created_at", columnList = "created_at")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    @Column(name = "entity_type", nullable = false, length = 100)
    private String entityType; // CONFIGURATION, FEATURE_FLAG, ENVIRONMENT

    @Column(name = "entity_id", length = 100)
    private String entityId;

    @Column(name = "action", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private AuditAction action;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    @Column(name = "changed_by", length = 100)
    private String changedBy;

    @Column(name = "user_id", length = 100)
    private String userId;

    @Column(name = "user_name", length = 255)
    private String userName;

    @Column(name = "user_email", length = 255)
    private String userEmail;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "change_reason", columnDefinition = "TEXT")
    private String changeReason;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata; // JSON metadata

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public String getSummary() {
        return String.format("%s %s %s by %s at %s",
            action, entityType, entityId, changedBy, createdAt);
    }
}
