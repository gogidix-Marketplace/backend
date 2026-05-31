package com.gogidix.infrastructure.database.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * Domain model representing database backup records.
 *
 * <p>Manages database backups including:</p>
 * <ul>
 *   <li>Full backups</li>
 *   <li>Incremental backups</li>
 *   <li>Differential backups</li>
 *   <li>Backup scheduling</li>
 *   <li>Restore operations</li>
 * </ul>
 *
 * <p>Provides comprehensive backup lifecycle management.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "database_backups")
@CompoundIndex(def = "{'tenantId': 1, 'targetDatabase': 1, 'createdAt': -1}")
@CompoundIndex(def = "{'status': 1, 'scheduledFor': 1}")
public class DatabaseBackup {

    /**
     * Unique identifier for the backup record.
     */
    @Id
    private String id;

    /**
     * Tenant identifier for multi-tenancy support.
     */
    @Indexed
    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    /**
     * Environment for this backup (dev, staging, prod).
     */
    @NotNull(message = "Environment is required")
    private Environment environment;

    /**
     * Target database identifier.
     */
    @Indexed
    @NotBlank(message = "Target database is required")
    private String targetDatabase;

    /**
     * Target connection pool name.
     */
    private String connectionPoolName;

    /**
     * Backup type.
     */
    @NotNull(message = "Backup type is required")
    private BackupType backupType;

    /**
     * Backup method.
     */
    @NotNull(message = "Backup method is required")
    private BackupMethod backupMethod;

    /**
     * Backup name/description.
     */
    @NotBlank(message = "Backup name is required")
    private String backupName;

    /**
     * Backup description.
     */
    private String description;

    /**
     * Current backup status.
     */
    @Indexed
    @Builder.Default
    private BackupStatus status = BackupStatus.PENDING;

    /**
     * Backup execution start time.
     */
    private LocalDateTime executionStartTime;

    /**
     * Backup execution end time.
     */
    private LocalDateTime executionEndTime;

    /**
     * Scheduled execution time.
     */
    @Indexed
    private LocalDateTime scheduledFor;

    /**
     * Backup duration in milliseconds.
     */
    private Long durationMs;

    /**
     * Backup file location.
     */
    private String backupLocation;

    /**
     * Backup file name.
     */
    private String backupFileName;

    /**
     * Backup file size in bytes.
     */
    private Long fileSizeBytes;

    /**
     * Backup file checksum.
     */
    private String checksum;

    /**
     * Checksum algorithm used.
     */
    private String checksumAlgorithm;

    /**
     * Whether backup is compressed.
     */
    @Builder.Default
    private Boolean compressed = true;

    /**
     * Compression algorithm.
     */
    private String compressionAlgorithm;

    /**
     * Compression ratio.
     */
    private Double compressionRatio;

    /**
     * Whether backup is encrypted.
     */
    @Builder.Default
    private Boolean encrypted = false;

    /**
     * Encryption algorithm.
     */
    private String encryptionAlgorithm;

    /**
     * Previous backup ID for incremental/differential backups.
     */
    private String parentBackupId;

    /**
     * Child backup IDs.
     */
    private Set<String> childBackupIds;

    /**
     * Included tables/collections.
     */
    private Set<String> includedTables;

    /**
     * Excluded tables/collections.
     */
    private Set<String> excludedTables;

    /**
     * Backup tags.
     */
    private Set<String> tags;

    /**
     * Backup category.
     */
    private String category;

    /**
     * Retention days.
     */
    @Builder.Default
    private Integer retentionDays = 30;

    /**
     * Expiration date.
     */
    private LocalDateTime expiresAt;

    /**
     * Whether backup is immutable (cannot be deleted until expiration).
     */
    @Builder.Default
    private Boolean immutable = false;

    /**
     * Executor information.
     */
    private String executedBy;

    /**
     * Execution log output.
     */
    private String executionLog;

    /**
     * Error message if backup failed.
     */
    private String errorMessage;

    /**
     * Error stack trace.
     */
    private String errorStackTrace;

    /**
     * Number of retry attempts.
     */
    @Builder.Default
    private Integer retryCount = 0;

    /**
     * Maximum retry attempts allowed.
     */
    @Builder.Default
    private Integer maxRetries = 3;

    /**
     * Statistics backup.
     */
    private BackupStatistics statistics;

    /**
     * Validation status.
     */
    @Builder.Default
    private ValidationStatus validationStatus = ValidationStatus.NOT_VALIDATED;

    /**
     * Validation timestamp.
     */
    private LocalDateTime validatedAt;

    /**
     * Restore count.
     */
    @Builder.Default
    private Integer restoreCount = 0;

    /**
     * Last restore timestamp.
     */
    private LocalDateTime lastRestoreAt;

    /**
     * Metadata associated with this backup.
     */
    private Map<String, Object> metadata;

    /**
     * User who created this backup.
     */
    private String createdBy;

    /**
     * User who last updated this backup.
     */
    private String lastUpdatedBy;

    /**
     * Current version number.
     */
    @Builder.Default
    private Integer version = 1;

    /**
     * Timestamp when this backup was created.
     */
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * Timestamp when this backup was last modified.
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * Environment enumeration.
     */
    public enum Environment {
        DEV,
        STAGING,
        PROD,
        TEST
    }

    /**
     * Backup type enumeration.
     */
    public enum BackupType {
        FULL,
        INCREMENTAL,
        DIFFERENTIAL,
        TRANSACTION_LOG
    }

    /**
     * Backup method enumeration.
     */
    public enum BackupMethod {
        LOGICAL,
        PHYSICAL,
        SNAPSHOT,
        STREAMING
    }

    /**
     * Backup status enumeration.
     */
    public enum BackupStatus {
        PENDING,
        SCHEDULED,
        RUNNING,
        SUCCESS,
        FAILED,
        CANCELLED,
        EXPIRED,
        DELETED
    }

    /**
     * Validation status enumeration.
     */
    public enum ValidationStatus {
        NOT_VALIDATED,
        VALIDATING,
        VALIDATED,
        VALIDATION_FAILED
    }

    /**
     * Backup statistics.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BackupStatistics {
        private Long totalRows;
        private Long totalTables;
        private Long totalIndexes;
        private Long totalSizeBytes;
        private Double backupSpeedMBps;
        private Long throughputRowsPerSecond;
    }

    /**
     * Checks if backup is in a terminal state.
     */
    public boolean isTerminalState() {
        return status == BackupStatus.SUCCESS
                || status == BackupStatus.FAILED
                || status == BackupStatus.CANCELLED
                || status == BackupStatus.EXPIRED
                || status == BackupStatus.DELETED;
    }

    /**
     * Checks if backup can be executed.
     */
    public boolean canExecute() {
        return (status == BackupStatus.PENDING || status == BackupStatus.SCHEDULED)
                && (scheduledFor == null || scheduledFor.isBefore(LocalDateTime.now()));
    }

    /**
     * Checks if backup can be retried.
     */
    public boolean canRetry() {
        return status == BackupStatus.FAILED
                && retryCount < maxRetries;
    }

    /**
     * Checks if backup can be used for restore.
     */
    public boolean canRestore() {
        return status == BackupStatus.SUCCESS
                && backupLocation != null
                && !backupLocation.isEmpty()
                && (expiresAt == null || expiresAt.isAfter(LocalDateTime.now()));
    }

    /**
     * Checks if backup has expired.
     */
    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
    }

    /**
     * Calculates backup duration if not already set.
     */
    public void calculateDuration() {
        if (executionStartTime != null && executionEndTime != null && durationMs == null) {
            durationMs = java.time.Duration.between(executionStartTime, executionEndTime).toMillis();
        }
    }

    /**
     * Gets file size in MB.
     */
    public Double getFileSizeMB() {
        if (fileSizeBytes == null) {
            return null;
        }
        return fileSizeBytes / (1024.0 * 1024.0);
    }

    /**
     * Checks if backup is eligible for deletion.
     */
    public boolean isEligibleForDeletion() {
        return !immutable && (isExpired() || status == BackupStatus.DELETED);
    }
}
