package com.gogidix.centralconfiguration.configserver.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Configuration History entity representing audit trail for configuration changes.
 * Stores all historical versions of configuration entries.
 */
@Entity
@Table(name = "configuration_history", indexes = {
    @Index(name = "idx_config_hist_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_config_hist_config_id", columnList = "configuration_id"),
    @Index(name = "idx_config_hist_created_at", columnList = "created_at")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "configuration_id", nullable = false)
    private Long configurationId;

    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    @Column(name = "application_name", nullable = false, length = 255)
    private String applicationName;

    @Column(name = "profile", nullable = false, length = 100)
    private String profile;

    @Column(name = "config_key", nullable = false, length = 500)
    private String configKey;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "change_type", nullable = false, length = 20)
    private String changeType; // CREATE, UPDATE, DELETE

    @Column(name = "changed_by", length = 100)
    private String changedBy;

    @Column(name = "change_reason", columnDefinition = "TEXT")
    private String changeReason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
