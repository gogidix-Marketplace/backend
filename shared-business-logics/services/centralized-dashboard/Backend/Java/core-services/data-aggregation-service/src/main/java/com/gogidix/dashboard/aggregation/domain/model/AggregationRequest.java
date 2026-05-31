package com.gogidix.dashboard.aggregation.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Aggregation Request aggregate root.
 * Manages cross-service data aggregation requests.
 */
@Entity
@Table(name = "aggregation_requests", indexes = {
    @Index(name = "idx_agg_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_agg_status", columnList = "status"),
    @Index(name = "idxagg_created_at", columnList = "created_at")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AggregationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    @Builder.Default
    private AggregationStatus status = AggregationStatus.PENDING;

    @Column(name = "source_domains", columnDefinition = "TEXT")
    private String sourceDomains;

    @Column(name = "kpi_codes", columnDefinition = "TEXT")
    private String kpiCodes;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "aggregation_type", length = 50)
    private String aggregationType;

    @Column(name = "group_by", length = 100)
    private String groupBy;

    @Column(name = "filters", columnDefinition = "TEXT")
    private String filters;

    @Column(name = "result", columnDefinition = "TEXT")
    private String result;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void markAsProcessing() {
        this.status = AggregationStatus.PROCESSING;
        this.processedAt = LocalDateTime.now();
    }

    public void markAsCompleted(String result) {
        this.status = AggregationStatus.COMPLETED;
        this.result = result;
        this.completedAt = LocalDateTime.now();
    }

    public void markAsFailed(String errorMessage) {
        this.status = AggregationStatus.FAILED;
        this.errorMessage = errorMessage;
        this.completedAt = LocalDateTime.now();
    }

    public boolean isPending() {
        return this.status == AggregationStatus.PENDING;
    }

    public boolean isProcessing() {
        return this.status == AggregationStatus.PROCESSING;
    }

    public boolean isCompleted() {
        return this.status == AggregationStatus.COMPLETED;
    }

    public boolean isFailed() {
        return this.status == AggregationStatus.FAILED;
    }
}
