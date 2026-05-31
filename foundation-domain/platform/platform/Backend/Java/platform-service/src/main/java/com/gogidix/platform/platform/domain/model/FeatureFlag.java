package com.gogidix.platform.platform.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

/**
 * Feature flag for controlled feature rollout.
 *
 * Supports:
 * - Progressive rollout (percentage-based)
 * - Targeted segments (whitelist/blacklist)
 * - Dependency management (flags that depend on other flags)
 * - A/B testing
 */
@Entity
@Table(name = "feature_flags", indexes = {
    @Index(name = "idx_feature_flags_tenant", columnList = "tenant_id"),
    @Index(name = "idx_feature_flags_key", columnList = "feature_key"),
    @Index(name = "idx_feature_flags_enabled", columnList = "is_enabled")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureFlag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    /**
     * Unique flag key (e.g., "new_dashboard_v2")
     */
    @Column(name = "feature_key", nullable = false, unique = true, length = 255)
    private String featureKey;

    /**
     * Human-readable flag name
     */
    @Column(name = "feature_name", nullable = false, length = 255)
    private String featureName;

    /**
     * Flag description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Feature type
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "feature_type", length = 50)
    private FeatureType featureType;

    /**
     * Whether flag is enabled
     */
    @Column(name = "is_enabled", nullable = false)
    @Builder.Default
    private boolean isEnabled = false;

    /**
     * Rollout percentage (0-100) for gradual rollout
     */
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "rollout_percentage")
    @Builder.Default
    private Integer rolloutPercentage = 0;

    /**
     * Target segments for this flag
     */
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "user_segments")
    private List<String> userSegments;

    /**
     * Whitelist of specific tenants
     */
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "allowed_tenants")
    private String[] allowedTenants;

    /**
     * Blacklist of excluded tenants
     */
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "denied_tenants")
    private String[] deniedTenants;

    /**
     * Rollout rules (JSON)
     */
    @Column(name = "rollout_rules", columnDefinition = "JSONB")
    @Builder.Default
    private Map<String, Object> rolloutRules = new HashMap<>();

    /**
     * Whether opt-in is required
     */
    @Column(name = "requires_opt_in", nullable = false)
    @Builder.Default
    private boolean requiresOptIn = false;

    /**
     * Other flag keys this flag depends on
     */
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "dependencies")
    private String[] dependencies;

    /**
     * Additional metadata
     */
    @Column(name = "metadata", columnDefinition = "JSONB")
    @Builder.Default
    private String metadata = "{}";

    /**
     * Flag status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    @Builder.Default
    private FlagStatus status = FlagStatus.ACTIVE;

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

    /**
     * Check if flag is enabled for user
     */
    public boolean isEnabledForUser(String userId, String[] userSegments) {
        if (!isEnabled || status != FlagStatus.ACTIVE) {
            return false;
        }

        // Check denied tenants
        if (deniedTenants != null && Arrays.asList(deniedTenants).contains(userId)) {
            return false;
        }

        // Check allowed tenants
        if (allowedTenants != null && allowedTenants.length > 0) {
            return Arrays.asList(allowedTenants).contains(userId);
        }

        // Check segments
        if (this.userSegments != null && !this.userSegments.isEmpty() && userSegments != null) {
            boolean inSegment = false;
            for (String segment : userSegments) {
                if (this.userSegments.contains(segment)) {
                    inSegment = true;
                    break;
                }
            }
            if (!inSegment) {
                return false;
            }
        }

        // Check rollout percentage
        if (rolloutPercentage != null && rolloutPercentage > 0) {
            // Simple hash-based rollout (deterministic per user)
            int userHash = Math.abs(userId.hashCode() % 100);
            return userHash < rolloutPercentage;
        }

        return true;
    }

    /**
     * Enable flag
     */
    public void enable() {
        this.isEnabled = true;
    }

    /**
     * Disable flag
     */
    public void disable() {
        this.isEnabled = false;
    }

    /**
     * Set rollout percentage
     */
    public void setRollout(Integer percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Rollout percentage must be between 0 and 100");
        }
        this.rolloutPercentage = percentage;
        this.isEnabled = percentage > 0;
    }

    public enum FlagStatus {
        ACTIVE, INACTIVE, ARCHIVED
    }

    public enum FeatureType {
        FEATURE,
        EXPERIMENT,
        PERMISSION,
        CONFIGURATION,
        INTEGRATION
    }
}
