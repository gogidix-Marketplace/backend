package com.gogidix.globalbusinessmanagement.countryingestion.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for IngestionBatch entity.
 * Used for transferring batch information between layers.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Ingestion Batch DTO for tracking data ingestion operations")
public class IngestionBatchDto {

    @Schema(description = "Unique identifier")
    private String id;

    @Schema(description = "Batch identifier", example = "BATCH-2024-001")
    @NotBlank(message = "Batch ID is required")
    private String batchId;

    @Schema(description = "Type of batch")
    @NotNull(message = "Batch type is required")
    private BatchTypeDto batchType;

    @Schema(description = "Data source name", example = "World Bank API")
    @NotBlank(message = "Source is required")
    private String source;

    @Schema(description = "Source URL")
    private String sourceUrl;

    @Schema(description = "Current status of the batch")
    @NotNull(message = "Status is required")
    private BatchStatusDto status;

    @Schema(description = "Batch start time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(description = "Batch end time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;

    @Schema(description = "Total records to process")
    private Long totalRecords;

    @Schema(description = "Number of records processed")
    private Long processedRecords;

    @Schema(description = "Number of successfully processed records")
    private Long successfulRecords;

    @Schema(description = "Number of failed records")
    private Long failedRecords;

    @Schema(description = "Number of skipped records")
    private Long skippedRecords;

    @Schema(description = "Progress percentage (0-100)")
    private Integer progressPercentage;

    @Schema(description = "Schema ID used for validation")
    private String schemaId;

    @Schema(description = "File format", example = "CSV")
    private String format;

    @Schema(description = "File size in bytes")
    private Long fileSizeBytes;

    @Schema(description = "Original file name")
    private String fileName;

    @Schema(description = "File checksum for integrity verification")
    private String fileChecksum;

    @Schema(description = "User who created the batch")
    private String createdBy;

    @Schema(description = "Display name of creator")
    private String createdByName;

    @Schema(description = "Creation timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;

    @Schema(description = "Last modified by user")
    private String lastModifiedBy;

    @Schema(description = "Last modification timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastModifiedDate;

    @Schema(description = "Error message if batch failed")
    private String errorMessage;

    @Schema(description = "Detailed error messages")
    private List<String> errorMessages;

    @Schema(description = "Additional metadata")
    private Map<String, Object> metadata;

    @Schema(description = "Processing statistics")
    private Map<String, Object> statistics;

    @Schema(description = "Number of validation errors")
    private Long validationErrorCount;

    @Schema(description = "Delete batch data on completion")
    private Boolean deleteOnCompletion;

    @Schema(description = "User who completed the batch")
    private String completedBy;

    @Schema(description = "Completion timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime completedDate;

    @Schema(description = "Is batch archived")
    private Boolean archived;

    @Schema(description = "Archive timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime archivedDate;

    @Schema(description = "Number of retry attempts")
    private Integer retryCount;

    @Schema(description = "Maximum retry attempts allowed")
    private Integer maxRetries;

    @Schema(description = "Parent batch ID for retry batches")
    private String parentBatchId;

    @Schema(description = "Organization ID")
    private String organizationId;

    @Schema(description = "Tenant ID")
    private String tenantId;

    @Schema(description = "Validation enabled flag")
    private Boolean validationEnabled;

    @Schema(description = "Stop on error flag")
    private Boolean stopOnError;

    @Schema(description = "Processing time in milliseconds")
    private Long processingTimeMs;

    @Schema(description = "Average record processing time in milliseconds")
    private Double averageRecordProcessingTimeMs;

    /**
     * Batch type DTO enum.
     */
    @Schema(description = "Batch type enumeration")
    public enum BatchTypeDto {
        FULL_IMPORT,
        INCREMENTAL_UPDATE,
        MANUAL_UPLOAD,
        API_INGESTION,
        SYNC_FROM_SOURCE,
        BULK_CORRECTION,
        MIGRATION
    }

    /**
     * Batch status DTO enum.
     */
    @Schema(description = "Batch status enumeration")
    public enum BatchStatusDto {
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
     * Creates a minimal DTO for batch creation.
     */
    public static IngestionBatchDto createMinimal(String batchId, String source, BatchTypeDto type) {
        return IngestionBatchDto.builder()
                .batchId(batchId)
                .source(source)
                .batchType(type)
                .status(BatchStatusDto.PENDING)
                .totalRecords(0L)
                .processedRecords(0L)
                .successfulRecords(0L)
                .failedRecords(0L)
                .skippedRecords(0L)
                .progressPercentage(0)
                .validationEnabled(true)
                .stopOnError(false)
                .build();
    }

    /**
     * Checks if batch is in a terminal state.
     */
    public boolean isTerminalState() {
        return status == BatchStatusDto.COMPLETED ||
                status == BatchStatusDto.COMPLETED_WITH_ERRORS ||
                status == BatchStatusDto.FAILED ||
                status == BatchStatusDto.CANCELLED ||
                status == BatchStatusDto.ROLLED_BACK;
    }

    /**
     * Gets the success rate as percentage.
     */
    public double getSuccessRate() {
        if (processedRecords == null || processedRecords == 0) {
            return 0.0;
        }
        return (successfulRecords != null ? successfulRecords : 0) * 100.0 / processedRecords;
    }

    /**
     * Gets the failure rate as percentage.
     */
    public double getFailureRate() {
        if (processedRecords == null || processedRecords == 0) {
            return 0.0;
        }
        return (failedRecords != null ? failedRecords : 0) * 100.0 / processedRecords;
    }
}
