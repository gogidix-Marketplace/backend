package com.gogidix.analytics.bi.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing an execution of a report.
 */
@Entity
@Table(name = "report_executions", indexes = {
    @Index(name = "idx_execution_report", columnList = "report_definition_id"),
    @Index(name = "idx_execution_status", columnList = "status"),
    @Index(name = "idx_execution_started_at", columnList = "started_at")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    @Builder.Default
    private ExecutionStatus status = ExecutionStatus.PENDING;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "file_path", length = 500)
    private String filePath;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "record_count")
    private Integer recordCount;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "requested_by", length = 100)
    private String requestedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_definition_id", nullable = false)
    private ReportDefinition reportDefinition;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
        if (startedAt == null && status == ExecutionStatus.RUNNING) {
            startedAt = LocalDateTime.now();
        }
    }

    public void markAsRunning() {
        this.status = ExecutionStatus.RUNNING;
        this.startedAt = LocalDateTime.now();
    }

    public void markAsCompleted(String filePath, Long fileSize, Integer recordCount) {
        this.status = ExecutionStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.filePath = filePath;
        this.fileSizeBytes = fileSize;
        this.recordCount = recordCount;
    }

    public void markAsFailed(String errorMessage) {
        this.status = ExecutionStatus.FAILED;
        this.completedAt = LocalDateTime.now();
        this.errorMessage = errorMessage;
    }

    public enum ExecutionStatus {
        PENDING,
        RUNNING,
        COMPLETED,
        FAILED,
        CANCELLED
    }
}
