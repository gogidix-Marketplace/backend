package com.gogidix.platform.platform.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Platform configuration entity.
 *
 * Manages centralized configuration for all platform services including:
 * - Feature flags
 * - Service settings
 * - Platform-wide parameters
 * - Configuration versioning and rollback
 */
@Entity
@Table(name = "platform_configurations", indexes = {
    @Index(name = "idx_platform_config_tenant", columnList = "tenant_id"),
    @Index(name = "idx_platform_config_key", columnList = "config_key"),
    @Index(name = "idx_platform_config_env", columnList = "environment"),
    @Index(name = "idx_platform_config_status", columnList = "status")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    /**
     * Tenant ID for multi-tenancy
     */
    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    /**
     * Unique configuration key
     */
    @Column(name = "config_key", nullable = false, unique = true, length = 255)
    private String configKey;

    /**
     * Configuration value
     */
    @Lob
    @Column(name = "config_value", nullable = false)
    private String configValue;

    /**
     * Configuration data type
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "config_type", nullable = false, length = 50)
    private ConfigType configType;

    /**
     * Human-readable description
     */
    @Lob
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "description")
    private String description;

    /**
     * Whether this config contains sensitive data
     */
    @Column(name = "is_sensitive", nullable = false)
    @Builder.Default
    private boolean isSensitive = false;

    /**
     * Whether value is encrypted at rest
     */
    @Column(name = "is_encrypted", nullable = false)
    @Builder.Default
    private boolean isEncrypted = false;

    /**
     * Environment this config applies to
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "environment", length = 50)
    private Environment environment;

    /**
     * Configuration version for history tracking
     */
    @Column(name = "version", nullable = false)
    @Builder.Default
    private Integer version = 1;

    /**
     * When this config becomes effective
     */
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "effective_from")
    private LocalDateTime effectiveFrom;

    /**
     * When this config expires
     */
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "effective_until")
    private LocalDateTime effectiveUntil;

    /**
     * Tags for grouping and filtering
     */
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "tags")
    private String[] tags;

    /**
     * Additional metadata
     */
    @Column(name = "metadata", columnDefinition = "JSONB")
    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    /**
     * Current configuration status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    @Builder.Default
    private ConfigStatus status = ConfigStatus.ACTIVE;

    /**
     * Check if configuration is currently effective
     */
    public boolean isEffective() {
        LocalDateTime now = LocalDateTime.now();
        boolean effective = true;

        if (effectiveFrom != null && now.isBefore(effectiveFrom)) {
            effective = false;
        }

        if (effectiveUntil != null && now.isAfter(effectiveUntil)) {
            effective = false;
        }

        return effective && status == ConfigStatus.ACTIVE;
    }

    /**
     * Increment version for new change
     */
    public void incrementVersion() {
        this.version++;
    }

    /**
     * Archive this configuration
     */
    public void archive() {
        this.status = ConfigStatus.ARCHIVED;
    }

    /**
     * Audit fields
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 255)
    private String createdBy;

    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum ConfigType {
        STRING, JSON, NUMBER, BOOLEAN
    }

    public enum Environment {
        DEV, STAGING, PROD, ALL
    }

    public enum ConfigStatus {
        ACTIVE, INACTIVE, ARCHIVED
    }
}
