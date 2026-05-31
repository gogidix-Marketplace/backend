package com.gogidix.analytics.data.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "data_queries", indexes = {
    @Index(name = "idx_query_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_query_owner", columnList = "owner_id"),
    @Index(name = "idx_query_is_public", columnList = "is_public"),
    @Index(name = "idx_query_category", columnList = "category")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "query_name", nullable = false, length = 255)
    private String queryName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "query_definition", columnDefinition = "TEXT", nullable = false)
    private String queryDefinition;

    @Enumerated(EnumType.STRING)
    @Column(name = "query_type", nullable = false, length = 50)
    private QueryType queryType;

    @Column(name = "data_source", length = 100)
    private String dataSource;

    @Column(name = "parameters_schema", columnDefinition = "JSONB")
    private String parametersSchema;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "tags", columnDefinition = "TEXT")
    private String tags;

    @Column(name = "owner_id", nullable = false, length = 100)
    private String ownerId;

    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    @Column(name = "is_public", nullable = false)
    @Builder.Default
    private Boolean isPublic = false;

    @Column(name = "is_favorite", nullable = false)
    @Builder.Default
    private Boolean isFavorite = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_executed_at")
    private LocalDateTime lastExecutedAt;

    @Column(name = "execution_count", nullable = false)
    @Builder.Default
    private Integer executionCount = 0;

    @Column(name = "avg_execution_time_ms")
    private Long avgExecutionTimeMs;

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

    public void recordExecution(Long executionTimeMs) {
        this.executionCount++;
        this.lastExecutedAt = LocalDateTime.now();
        if (this.avgExecutionTimeMs == null) {
            this.avgExecutionTimeMs = executionTimeMs;
        } else {
            this.avgExecutionTimeMs = (this.avgExecutionTimeMs + executionTimeMs) / 2;
        }
    }

    public enum QueryType {
        SQL,
        AGGREGATION,
        METRIC,
        CUSTOM,
        JOIN
    }
}
