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
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * Domain model representing a database migration record.
 *
 * <p>Tracks database migrations including:</p>
 * <ul>
 *   <li>Flyway migrations</li>
 *   <li>Liquibase changesets</li>
 *   <li>Custom migration scripts</li>
 * </ul>
 *
 * <p>Provides version tracking, execution status, and rollback capabilities.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "database_migrations")
public class DatabaseMigration {

    /**
     * Unique identifier for the migration record.
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
     * Environment for this migration (dev, staging, prod).
     */
    @Indexed
    @NotNull(message = "Environment is required")
    private Environment environment;

    /**
     * Target database/pool identifier.
     */
    @Indexed
    @NotBlank(message = "Target database is required")
    private String targetDatabase;

    /**
     * Migration type (FLYWAY, LIQUIBASE, CUSTOM).
     */
    @NotNull(message = "Migration type is required")
    private MigrationType migrationType;

    /**
     * Migration version number.
     */
    @Indexed
    @NotBlank(message = "Version is required")
    private String version;

    /**
     * Migration description.
     */
    @NotBlank(message = "Description is required")
    private String description;

    /**
     * Migration script name or checksum.
     */
    @NotBlank(message = "Script name is required")
    private String scriptName;

    /**
     * Script content or reference.
     */
    private String scriptContent;

    /**
     * Script checksum for validation.
     */
    private String checksum;

    /**
     * Current migration status.
     */
    @Indexed
    @Builder.Default
    private MigrationStatus status = MigrationStatus.PENDING;

    /**
     * Migration execution type.
     */
    @Builder.Default
    private ExecutionType executionType = ExecutionType.AUTO;

    /**
     * Whether this migration can be rolled back.
     */
    @Builder.Default
    private Boolean rollbackEnabled = false;

    /**
     * Rollback script name.
     */
    private String rollbackScriptName;

    /**
     * Rollback script content.
     */
    private String rollbackScriptContent;

    /**
     * Migration execution start time.
     */
    private LocalDateTime executionStartTime;

    /**
     * Migration execution end time.
     */
    private LocalDateTime executionEndTime;

    /**
     * Execution duration in milliseconds.
     */
    private Long executionDurationMs;

    /**
     * Executor information (user or system).
     */
    private String executedBy;

    /**
     * Execution log output.
     */
    private String executionLog;

    /**
     * Error message if execution failed.
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
     * Dependencies on other migrations.
     */
    private Set<String> dependencies;

    /**
     * Tags for categorization.
     */
    private Set<String> tags;

    /**
     * Category for grouping migrations.
     */
    private String category;

    /**
     * Priority for execution order.
     */
    @Builder.Default
    private Integer priority = 0;

    /**
     * Whether this is a baseline migration.
     */
    @Builder.Default
    private Boolean isBaseline = false;

    /**
     * Whether this migration repeats.
     */
    @Builder.Default
    private Boolean repeatable = false;

    /**
     * Repeat execution interval (for repeatable migrations).
     */
    private Long repeatIntervalMs;

    /**
     * Last repeat execution time.
     */
    private LocalDateTime lastRepeatExecution;

    /**
     * Metadata associated with this migration.
     */
    private Map<String, Object> metadata;

    /**
     * User who created this migration.
     */
    private String createdBy;

    /**
     * User who last updated this migration.
     */
    private String lastUpdatedBy;

    /**
     * Current version number.
     */
    @Builder.Default
    private Integer trackingVersion = 1;

    /**
     * Timestamp when this migration was created.
     */
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * Timestamp when this migration was last modified.
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * Scheduled execution time.
     */
    private LocalDateTime scheduledFor;

    /**
     * Whether migration is approved for execution.
     */
    @Builder.Default
    private Boolean approved = false;

    /**
     * Approver username.
     */
    private String approvedBy;

    /**
     * Approval timestamp.
     */
    private LocalDateTime approvedAt;

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
     * Migration type enumeration.
     */
    public enum MigrationType {
        FLYWAY,
        LIQUIBASE,
        CUSTOM,
        MANUAL
    }

    /**
     * Migration status enumeration.
     */
    public enum MigrationStatus {
        PENDING,
        SCHEDULED,
        RUNNING,
        SUCCESS,
        FAILED,
        ROLLED_BACK,
        SKIPPED,
        CANCELLED
    }

    /**
     * Execution type enumeration.
     */
    public enum ExecutionType {
        AUTO,
        MANUAL,
        SCHEDULED
    }

    /**
     * Checks if migration is in a terminal state.
     */
    public boolean isTerminalState() {
        return status == MigrationStatus.SUCCESS
                || status == MigrationStatus.FAILED
                || status == MigrationStatus.ROLLED_BACK
                || status == MigrationStatus.CANCELLED;
    }

    /**
     * Checks if migration can be executed.
     */
    public boolean canExecute() {
        return (status == MigrationStatus.PENDING || status == MigrationStatus.SCHEDULED)
                && (executionType == ExecutionType.AUTO || approved);
    }

    /**
     * Checks if migration can be retried.
     */
    public boolean canRetry() {
        return status == MigrationStatus.FAILED
                && retryCount < maxRetries;
    }

    /**
     * Checks if migration can be rolled back.
     */
    public boolean canRollback() {
        return rollbackEnabled
                && status == MigrationStatus.SUCCESS
                && rollbackScriptContent != null;
    }

    /**
     * Calculates execution duration if not already set.
     */
    public void calculateDuration() {
        if (executionStartTime != null && executionEndTime != null && executionDurationMs == null) {
            executionDurationMs = java.time.Duration.between(executionStartTime, executionEndTime).toMillis();
        }
    }

    /**
     * Checks if dependencies are satisfied.
     */
    public boolean areDependenciesSatisfied(Set<String> completedMigrations) {
        if (dependencies == null || dependencies.isEmpty()) {
            return true;
        }
        return completedMigrations.containsAll(dependencies);
    }
}
