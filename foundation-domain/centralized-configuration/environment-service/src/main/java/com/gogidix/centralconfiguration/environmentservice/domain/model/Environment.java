package com.gogidix.centralconfiguration.environmentservice.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Environment aggregate root for environment configuration.
 */
@Entity
@Table(name = "environments", indexes = {
    @Index(name = "idx_env_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_env_name", columnList = "environment_name")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Environment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    @Column(name = "environment_name", nullable = false, unique = true, length = 100)
    private String environmentName;

    @Column(name = "display_name", nullable = false, length = 255)
    private String displayName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "environment_type", nullable = false, length = 50)
    @Builder.Default
    private EnvironmentType environmentType = EnvironmentType.DEVELOPMENT;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "config_base_uri", length = 500)
    private String configBaseUri;

    @Column(name = "priority", nullable = false)
    @Builder.Default
    private Integer priority = 100;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "environment", cascade = CascadeType.ALL)
    private List<EnvironmentVariable> variables;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }
}
