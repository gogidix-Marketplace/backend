package com.gogidix.centralconfiguration.featureflagservice.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Feature Flag aggregate root representing a feature toggle.
 * Manages dynamic feature control with rollout strategies.
 *
 * Multi-tenancy: Tenant isolation enforced through tenantId field.
 */
@Entity
@Table(name = "feature_flags", indexes = {
    @Index(name = "idx_flag_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_flag_key", columnList = "flag_key"),
    @Index(name = "idx_flag_enabled", columnList = "is_enabled"),
    @Index(name = "idx_flag_created_at", columnList = "created_at")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureFlag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    @Column(name = "flag_key", nullable = false, unique = true, length = 255)
    private String flagKey;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_enabled", nullable = false)
    @Builder.Default
    private Boolean isEnabled = false;

    @Column(name = "rollout_percentage", nullable = false)
    @Builder.Default
    private Integer rolloutPercentage = 100;

    @Enumerated(EnumType.STRING)
    @Column(name = "rollout_strategy", nullable = false, length = 50)
    @Builder.Default
    private RolloutStrategy rolloutStrategy = RolloutStrategy.ALL_USERS;

    @Column(name = "whitelisted_users", columnDefinition = "TEXT")
    private String whitelistedUsers; // Comma-separated user IDs

    @Column(name = "is_sticky", nullable = false)
    @Builder.Default
    private Boolean isSticky = false; // Keep user experience consistent

    @Column(name = "tags", columnDefinition = "TEXT")
    private String tags; // Comma-separated tags

    @Column(name = "owner", length = 100)
    private String owner;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "featureFlag", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<FeatureFlagCondition> conditions = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isUserWhitelisted(String userId) {
        if (whitelistedUsers == null || whitelistedUsers.isEmpty()) {
            return false;
        }
        return whitelistedUsers.contains(userId);
    }

    public void enable() {
        this.isEnabled = true;
    }

    public void disable() {
        this.isEnabled = false;
    }

    public void addCondition(FeatureFlagCondition condition) {
        condition.setFeatureFlag(this);
        this.conditions.add(condition);
    }
}
