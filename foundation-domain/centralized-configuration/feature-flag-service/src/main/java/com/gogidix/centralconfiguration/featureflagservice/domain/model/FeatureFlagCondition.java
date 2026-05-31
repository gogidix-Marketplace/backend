package com.gogidix.centralconfiguration.featureflagservice.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Feature Flag Condition entity for advanced targeting rules.
 * Allows defining conditions for feature flag evaluation.
 */
@Entity
@Table(name = "feature_flag_conditions", indexes = {
    @Index(name = "idx_cond_flag_id", columnList = "feature_flag_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureFlagCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "feature_flag_id", nullable = false)
    private Long featureFlagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_flag_id", insertable = false, updatable = false)
    private FeatureFlag featureFlag;

    @Column(name = "attribute_name", nullable = false, length = 100)
    private String attributeName; // e.g., "country", "role", "tier"

    @Column(name = "operator", nullable = false, length = 20)
    private String operator; // e.g., "eq", "neq", "contains", "gt", "lt"

    @Column(name = "attribute_value", columnDefinition = "TEXT")
    private String attributeValue;

    @Column(name = "priority", nullable = false)
    @Builder.Default
    private Integer priority = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
