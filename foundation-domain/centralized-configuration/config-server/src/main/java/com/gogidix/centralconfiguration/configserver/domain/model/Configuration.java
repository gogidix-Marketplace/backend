package com.gogidix.centralconfiguration.configserver.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Configuration aggregate root representing a configuration entry.
 * Manages centralized configuration with version control and encryption support.
 *
 * Multi-tenancy: Tenant isolation enforced through tenantId field.
 */
@Entity
@Table(name = "configurations", indexes = {
    @Index(name = "idx_config_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_config_application", columnList = "application_name"),
    @Index(name = "idx_config_profile", columnList = "profile"),
    @Index(name = "idx_config_key", columnList = "config_key"),
    @Index(name = "idx_config_created_at", columnList = "created_at")
})
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    @Column(name = "application_name", nullable = false, length = 255)
    private String applicationName;

    @Column(name = "profile", nullable = false, length = 100)
    private String profile;

    @Column(name = "config_key", nullable = false, length = 500)
    private String configKey;

    @Column(name = "config_value", columnDefinition = "TEXT")
    private String configValue;

    @Column(name = "is_encrypted", nullable = false)
    @Builder.Default
    private Boolean isEncrypted = false;

    @Column(name = "version", nullable = false)
    @Builder.Default
    private Integer version = 1;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        version++;
    }

    public void incrementVersion() {
        this.version++;
    }

    public void activate() {
        this.isActive = true;
    }

    @SuppressWarnings("unused")
    public void deactivate() {
        this.isActive = false;
    }
}
