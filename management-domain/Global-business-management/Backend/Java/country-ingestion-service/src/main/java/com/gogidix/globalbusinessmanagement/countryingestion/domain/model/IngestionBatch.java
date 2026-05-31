package com.gogidix.globalbusinessmanagement.countryingestion.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Domain model representing an ingestion batch for tracking data ingestion operations.
 * Contains metadata about the batch processing including source, status, and statistics.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ingestion_batches")
public class IngestionBatch {

    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank(message = "Batch ID is required")
    private String batchId;

    @Indexed
    @NotNull(message = "Batch type is required")
    private BatchType batchType;

    @Indexed
    @NotBlank(message = "Source is required")
    private String source;

    private String sourceUrl;

    @Indexed
    @NotNull(message = "Status is required")
    @Builder.Default
    private BatchStatus status = BatchStatus.PENDING;

    @Indexed
    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Builder.Default
    private Long totalRecords = 0L;

    @Builder.Default
    private Long processedRecords = 0L;

    @Builder.Default
    private Long successfulRecords = 0L;

    @Builder.Default
    private Long failedRecords = 0L;

    @Builder.Default
    private Long skippedRecords = 0L;

    @Builder.Default
    private Integer progressPercentage = 0;

    @Indexed
    private String schemaId;

    @DBRef(lazy = true)
    private DataSchema dataSchema;

    private String format;

    private Long fileSizeBytes;

    private String fileName;

    private String fileChecksum;

    @Indexed
    private String createdBy;

    private String createdByName;

    @Indexed
    private LocalDateTime createdDate;

    private String lastModifiedBy;

    private LocalDateTime lastModifiedDate;

    private String errorMessage;

    @Builder.Default
    private List<String> errorMessages = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    @Builder.Default
    private Map<String, Object> statistics = new HashMap<>();

    @DBRef(lazy = true)
    private List<ValidationError> validationErrors;

    @Indexed
    @Builder.Default
    private Boolean deleteOnCompletion = false;

    private String completedBy;

    private LocalDateTime completedDate;

    @Indexed
    @Builder.Default
    private Boolean archived = false;

    private LocalDateTime archivedDate;

    @Builder.Default
    private Integer retryCount = 0;

    @Builder.Default
    private Integer maxRetries = 3;

    private String parentBatchId;

    @Indexed
    private String organizationId;

    private String tenantId;

    @Builder.Default
    private Boolean validationEnabled = true;

    @Builder.Default
    private Boolean stopOnError = false;

    @Builder.Default
    private Long processingTimeMs = 0L;

    @Builder.Default
    private Double averageRecordProcessingTimeMs = 0.0;

    /**
     * Batch type enumeration.
     */
    public enum BatchType {
        FULL_IMPORT,
        INCREMENTAL_UPDATE,
        MANUAL_UPLOAD,
        API_INGESTION,
        SYNC_FROM_SOURCE,
        BULK_CORRECTION,
        MIGRATION
    }

    /**
     * Batch status enumeration.
     */
    public enum BatchStatus {
        PENDING,
        IN_PROGRESS,
        VALIDATING,
        PROCESSING,
        COMPLETED,
        COMPLETED_WITH_ERRORS,
        FAILED,
        CANCELLED,
        ROLLING_BACK,
        ROLLED_BACK,
        PAUSED,
        RETRYING
    }

    /**
     * Calculates the current progress percentage.
     */
    public void calculateProgress() {
        if (totalRecords != null && totalRecords > 0) {
            this.progressPercentage = (int) ((processedRecords * 100) / totalRecords);
        }
    }

    /**
     * Checks if the batch is in a terminal state.
     */
    public boolean isTerminalState() {
        return status == BatchStatus.COMPLETED ||
                status == BatchStatus.COMPLETED_WITH_ERRORS ||
                status == BatchStatus.FAILED ||
                status == BatchStatus.CANCELLED ||
                status == BatchStatus.ROLLED_BACK;
    }

    /**
     * Checks if the batch can be retried.
     */
    public boolean canRetry() {
        return (status == BatchStatus.FAILED || status == BatchStatus.COMPLETED_WITH_ERRORS) &&
                retryCount < maxRetries;
    }

    /**
     * Marks the batch as started.
     */
    public void markAsStarted() {
        this.status = BatchStatus.IN_PROGRESS;
        this.startTime = LocalDateTime.now();
        if (this.createdDate == null) {
            this.createdDate = this.startTime;
        }
    }

    /**
     * Marks the batch as completed.
     */
    public void markAsCompleted() {
        if (failedRecords > 0) {
            this.status = BatchStatus.COMPLETED_WITH_ERRORS;
        } else {
            this.status = BatchStatus.COMPLETED;
        }
        this.endTime = LocalDateTime.now();
        this.completedDate = this.endTime;
        this.calculateProcessingTime();
    }

    /**
     * Marks the batch as failed.
     */
    public void markAsFailed(String errorMessage) {
        this.status = BatchStatus.FAILED;
        this.endTime = LocalDateTime.now();
        this.errorMessage = errorMessage;
        this.calculateProcessingTime();
    }

    /**
     * Increments the processed records count.
     */
    public void incrementProcessed(boolean success) {
        this.processedRecords++;
        if (success) {
            this.successfulRecords++;
        } else {
            this.failedRecords++;
        }
        calculateProgress();
    }

    /**
     * Calculates the total processing time.
     */
    public void calculateProcessingTime() {
        if (startTime != null && endTime != null) {
            this.processingTimeMs = java.time.Duration.between(startTime, endTime).toMillis();
            if (processedRecords != null && processedRecords > 0) {
                this.averageRecordProcessingTimeMs = (double) processingTimeMs / processedRecords;
            }
        }
    }

    /**
     * Gets the success rate as a percentage.
     */
    public double getSuccessRate() {
        if (processedRecords == null || processedRecords == 0) {
            return 0.0;
        }
        return (successfulRecords * 100.0) / processedRecords;
    }

    /**
     * Gets the failure rate as a percentage.
     */
    public double getFailureRate() {
        if (processedRecords == null || processedRecords == 0) {
            return 0.0;
        }
        return (failedRecords * 100.0) / processedRecords;
    }
}
