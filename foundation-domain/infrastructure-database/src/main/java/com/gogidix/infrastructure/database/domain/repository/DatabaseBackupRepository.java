package com.gogidix.infrastructure.database.domain.repository;

import com.gogidix.infrastructure.database.domain.model.DatabaseBackup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for managing database backups.
 */
@Repository
public interface DatabaseBackupRepository extends MongoRepository<DatabaseBackup, String> {

    /**
     * Find by tenant ID.
     */
    List<DatabaseBackup> findAllByTenantId(String tenantId);

    /**
     * Find by tenant ID paginated.
     */
    Page<DatabaseBackup> findAllByTenantId(String tenantId, Pageable pageable);

    /**
     * Find by tenant ID and target database.
     */
    List<DatabaseBackup> findAllByTenantIdAndTargetDatabase(String tenantId, String targetDatabase);

    /**
     * Find by tenant ID and environment.
     */
    List<DatabaseBackup> findAllByTenantIdAndEnvironment(String tenantId,
            DatabaseBackup.Environment environment);

    /**
     * Find by status.
     */
    List<DatabaseBackup> findAllByStatus(DatabaseBackup.BackupStatus status);

    /**
     * Find by tenant ID and status.
     */
    List<DatabaseBackup> findAllByTenantIdAndStatus(String tenantId,
            DatabaseBackup.BackupStatus status);

    /**
     * Find by tenant ID, target database, and status.
     */
    List<DatabaseBackup> findAllByTenantIdAndTargetDatabaseAndStatus(
            String tenantId,
            String targetDatabase,
            DatabaseBackup.BackupStatus status);

    /**
     * Find by backup type.
     */
    List<DatabaseBackup> findAllByTenantIdAndBackupType(String tenantId,
            DatabaseBackup.BackupType backupType);

    /**
     * Find scheduled backups ready for execution.
     */
    @Query("{'status': 'SCHEDULED', 'scheduledFor': {$lte: ?0}}")
    List<DatabaseBackup> findScheduledBackupsReadyForExecution(LocalDateTime now);

    /**
     * Find pending backups.
     */
    List<DatabaseBackup> findAllByTenantIdAndStatusAndScheduledForBefore(
            String tenantId,
            DatabaseBackup.BackupStatus status,
            LocalDateTime scheduledFor);

    /**
     * Find failed backups that can be retried.
     */
    @Query("{'status': 'FAILED', 'retryCount': {$lt: '$maxRetries'}}")
    List<DatabaseBackup> findFailedBackupsForRetry();

    /**
     * Find expired backups.
     */
    @Query("{'expiresAt': {$lt: ?0}, 'status': {$ne: 'DELETED'}}")
    List<DatabaseBackup> findExpiredBackups(LocalDateTime now);

    /**
     * Find backups eligible for deletion.
     */
    @Query("{'$or': [{'expiresAt': {$lt: ?0}}, {'status': 'DELETED'}], 'immutable': false}")
    List<DatabaseBackup> findBackupsEligibleForDeletion(LocalDateTime now);

    /**
     * Find latest successful backup for database.
     */
    Optional<DatabaseBackup> findFirstByTenantIdAndTargetDatabaseAndStatusOrderByCreatedAtDesc(
            String tenantId,
            String targetDatabase,
            DatabaseBackup.BackupStatus status);

    /**
     * Find incremental backups by parent backup ID.
     */
    List<DatabaseBackup> findAllByParentBackupId(String parentBackupId);

    /**
     * Find by checksum.
     */
    Optional<DatabaseBackup> findByChecksum(String checksum);

    /**
     * Find by validation status.
     */
    List<DatabaseBackup> findAllByValidationStatus(DatabaseBackup.ValidationStatus validationStatus);

    /**
     * Count by tenant ID and status.
     */
    Long countByTenantIdAndStatus(String tenantId, DatabaseBackup.BackupStatus status);

    /**
     * Count by tenant ID and backup type.
     */
    Long countByTenantIdAndBackupType(String tenantId, DatabaseBackup.BackupType backupType);

    /**
     * Delete by tenant ID and target database.
     */
    void deleteByTenantIdAndTargetDatabase(String tenantId, String targetDatabase);

    /**
     * Delete expired backups.
     */
    void deleteByExpiresAtBefore(LocalDateTime expiresAt);

    /**
     * Find by backup name.
     */
    List<DatabaseBackup> findAllByBackupName(String backupName);

    /**
     * Find by tags containing.
     */
    @Query("{'tags': {$in: ?0}}")
    List<DatabaseBackup> findByTagsContaining(List<String> tags);

    /**
     * Find by executed by.
     */
    List<DatabaseBackup> findAllByExecutedBy(String executedBy);

    /**
     * Get backup count per database.
     */
    @Query(value = "{'tenantId': ?0, 'status': 'SUCCESS'}", count = true)
    Long countSuccessfulBackupsByTenant(String tenantId);

    /**
     * Find backups within date range.
     */
    List<DatabaseBackup> findAllByTenantIdAndCreatedAtBetween(
            String tenantId,
            LocalDateTime startDate,
            LocalDateTime endDate);

    /**
     * Find differential backups.
     */
    @Query("{'backupType': 'DIFFERENTIAL', 'parentBackupId': ?0}")
    List<DatabaseBackup> findDifferentialBackups(String parentBackupId);

    /**
     * Calculate total backup size for tenant.
     */
    @Query("{'tenantId': ?0, 'fileSizeBytes': {$ne: null}}")
    List<DatabaseBackup> findAllWithSizeByTenant(String tenantId);
}
