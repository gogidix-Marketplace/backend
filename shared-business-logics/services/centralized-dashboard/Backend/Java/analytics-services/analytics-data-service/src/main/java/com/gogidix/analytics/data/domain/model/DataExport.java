package com.gogidix.analytics.data.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "data_exports", indexes = {
    @Index(name = "idx_export_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_export_requested_by", columnList = "requested_by"),
    @Index(name = "idx_export_status", columnList = "status"),
    @Index(name = "idx_export_created_at", columnList = "created_at")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataExport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "export_name", length = 255)
    private String exportName;

    @Column(name = "query_definition", columnDefinition = "TEXT")
    private String queryDefinition;

    @Column(name = "query_id", length = 100)
    private String queryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "export_format", nullable = false, length = 20)
    private ExportFormat exportFormat;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    @Builder.Default
    private ExportStatus status = ExportStatus.PENDING;

    @Column(name = "file_path", length = 500)
    private String filePath;

    @Column(name = "file_url", length = 500)
    private String fileUrl;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "row_count")
    private Integer rowCount;

    @Column(name = "column_count")
    private Integer columnCount;

    @Column(name = "compression_type", length = 20)
    private CompressionType compressionType;

    @Column(name = "include_headers", nullable = false)
    @Builder.Default
    private Boolean includeHeaders = true;

    @Column(name = "filters", columnDefinition = "JSONB")
    private String filters;

    @Column(name = "requested_by", length = 100)
    private String requestedBy;

    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "progress_percentage")
    @Builder.Default
    private Integer progressPercentage = 0;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
        setDefaultExpiration();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void markAsProcessing() {
        this.status = ExportStatus.PROCESSING;
        this.startedAt = LocalDateTime.now();
    }

    public void markAsCompleted(String filePath, String fileUrl, Long fileSize, Integer rowCount, Integer columnCount) {
        this.status = ExportStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.filePath = filePath;
        this.fileUrl = fileUrl;
        this.fileSizeBytes = fileSize;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.progressPercentage = 100;
    }

    public void markAsFailed(String errorMessage) {
        this.status = ExportStatus.FAILED;
        this.completedAt = LocalDateTime.now();
        this.errorMessage = errorMessage;
    }

    public void updateProgress(int progress) {
        this.progressPercentage = Math.min(100, Math.max(0, progress));
    }

    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    private void setDefaultExpiration() {
        if (expiresAt == null) {
            expiresAt = LocalDateTime.now().plusHours(24);
        }
    }

    public enum ExportFormat {
        CSV,
        EXCEL,
        JSON,
        PDF,
        PARQUET,
        XML
    }

    public enum ExportStatus {
        PENDING,
        PROCESSING,
        COMPLETED,
        FAILED,
        CANCELLED,
        EXPIRED
    }

    public enum CompressionType {
        NONE,
        ZIP,
        GZIP,
        SNAPPY
    }
}
