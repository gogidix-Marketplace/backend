package com.gogidix.analytics.data.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "analytics_datasets", indexes = {
    @Index(name = "idx_dataset_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_dataset_owner", columnList = "owner_id"),
    @Index(name = "idx_dataset_is_public", columnList = "is_public"),
    @Index(name = "idx_dataset_active", columnList = "is_active")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsDataset {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "dataset_name", nullable = false, length = 255)
    private String datasetName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "table_name", length = 255)
    private String tableName;

    @Column(name = "view_name", length = 255)
    private String viewName;

    @Column(name = "schema_definition", columnDefinition = "JSONB")
    private String schemaDefinition;

    @Column(name = "column_mappings", columnDefinition = "JSONB")
    private String columnMappings;

    @Column(name = "filters", columnDefinition = "JSONB")
    private String filters;

    @Column(name = "relationships", columnDefinition = "JSONB")
    private String relationships;

    @Column(name = "data_source", length = 100)
    private String dataSource;

    @Enumerated(EnumType.STRING)
    @Column(name = "dataset_type", length = 50)
    @Builder.Default
    private DatasetType datasetType = DatasetType.CUSTOM;

    @Column(name = "refresh_strategy", length = 50)
    @Builder.Default
    private String refreshStrategy = "MANUAL";

    @Column(name = "refresh_interval_seconds")
    private Integer refreshIntervalSeconds;

    @Column(name = "last_refreshed_at")
    private LocalDateTime lastRefreshedAt;

    @Column(name = "owner_id", length = 100)
    private String ownerId;

    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    @Column(name = "is_public", nullable = false)
    @Builder.Default
    private Boolean isPublic = false;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "tags", columnDefinition = "TEXT")
    private String tags;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void markAsRefreshed() {
        this.lastRefreshedAt = LocalDateTime.now();
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public enum DatasetType {
        TABLE,
        VIEW,
        MATERIALIZED_VIEW,
        CUSTOM,
        JOINED,
        AGGREGATED
    }
}
