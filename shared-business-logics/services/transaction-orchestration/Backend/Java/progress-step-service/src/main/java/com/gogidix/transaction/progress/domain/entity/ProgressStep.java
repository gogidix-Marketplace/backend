package com.gogidix.transaction.progress.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Progress Step entity for tracking transaction progress.
 * Converted from MongoDB to PostgreSQL JPA.
 */
@Entity
@Table(name = "progress_steps", indexes = {
    @Index(name = "idx_progress_transaction_id", columnList = "transaction_id"),
    @Index(name = "idx_progress_step_order", columnList = "step_order"),
    @Index(name = "idx_progress_step_type", columnList = "step_type"),
    @Index(name = "idx_progress_status", columnList = "status"),
    @Index(name = "idx_progress_parent_step", columnList = "parent_step_id"),
    @Index(name = "idx_progress_started_at", columnList = "started_at"),
    @Index(name = "idx_progress_created_at", columnList = "created_at")
})
@Data
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressStep {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Column(name = "transaction_id", nullable = false)
    private UUID transactionId;

    @Column(name = "step_order")
    private Integer stepOrder;

    @Column(name = "step_name", nullable = false, length = 255)
    private String stepName;

    @Enumerated(EnumType.STRING)
    @Column(name = "step_type", nullable = false, length = 50)
    private StepType stepType;

    @Column(name = "step_description", columnDefinition = "TEXT")
    private String stepDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    @Builder.Default
    private StepStatus status = StepStatus.PENDING;

    @Column(name = "parent_step_id")
    private UUID parentStepId;

    @Column(name = "can_execute_parallel")
    @Builder.Default
    private Boolean canExecuteParallel = false;

    @Column(name = "depends_on", length = 500)
    private String dependsOn;

    @Column(name = "execution_timeout_seconds")
    private Integer executionTimeoutSeconds;

    @Column(name = "retry_count")
    @Builder.Default
    private Integer retryCount = 0;

    @Column(name = "max_retries")
    @Builder.Default
    private Integer maxRetries = 3;

    @Column(name = "retry_delay_seconds")
    @Builder.Default
    private Integer retryDelaySeconds = 30;

    @Column(name = "compensation_action", length = 255)
    private String compensationAction;

    @Column(name = "input_data", columnDefinition = "TEXT")
    private String inputData;

    @Column(name = "output_data", columnDefinition = "TEXT")
    private String outputData;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "duration_milliseconds")
    private Long durationMilliseconds;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

    /**
     * Lifecycle callback before persist
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Lifecycle callback before update
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum StepType {
        INITIALIZATION,
        VALIDATION,
        DATA_PROCESSING,
        EXTERNAL_API_CALL,
        DATABASE_OPERATION,
        NOTIFICATION,
        COMPENSATION,
        VERIFICATION,
        FINALIZATION
    }

    public enum StepStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        SKIPPED,
        CANCELLED,
        RETRYING
    }

    /**
     * Check if step can be retried
     */
    public boolean canRetry() {
        return retryCount < maxRetries;
    }

    /**
     * Check if step has timed out
     */
    public boolean hasTimedOut() {
        if (startedAt == null || executionTimeoutSeconds == null) {
            return false;
        }
        LocalDateTime timeout = startedAt.plusSeconds(executionTimeoutSeconds);
        return LocalDateTime.now().isAfter(timeout);
    }

    /**
     * Calculate duration if not already set
     */
    public void calculateDuration() {
        if (startedAt != null && completedAt != null && durationMilliseconds == null) {
            durationMilliseconds = java.time.Duration.between(startedAt, completedAt).toMillis();
        }
    }

    /**
     * Check if this step is a terminal state
     */
    public boolean isTerminal() {
        return status == StepStatus.COMPLETED ||
               status == StepStatus.FAILED ||
               status == StepStatus.SKIPPED ||
               status == StepStatus.CANCELLED;
    }

    /**
     * Check if this step can execute
     */
    public boolean canExecute() {
        return status == StepStatus.PENDING || status == StepStatus.RETRYING;
    }
}
