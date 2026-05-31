package com.gogidix.infrastructure.database.application.service;

import com.gogidix.infrastructure.database.domain.model.DatabaseBackup;
import com.gogidix.infrastructure.database.domain.repository.DatabaseBackupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing database backups.
 *
 * <p>Handles backup creation, scheduling, restoration, and retention management.</p>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BackupService {

    private final DatabaseBackupRepository repository;
    private final ConnectionPoolService connectionPoolService;

    private final Map<String, Boolean> runningBackups = new ConcurrentHashMap<>();

    /**
     * Create a backup configuration.
     */
    @Transactional
    @CacheEvict(value = "backups", allEntries = true)
    public DatabaseBackup createBackup(@Valid DatabaseBackup backup) {
        log.info("Creating backup configuration: {}", backup.getBackupName());

        backup.setStatus(DatabaseBackup.BackupStatus.PENDING);
        backup.setCreatedAt(LocalDateTime.now());

        if (backup.getRetentionDays() != null && backup.getExpiresAt() == null) {
            backup.setExpiresAt(LocalDateTime.now().plusDays(backup.getRetentionDays()));
        }

        return repository.save(backup);
    }

    /**
     * Execute a backup.
     */
    @Transactional
    @CacheEvict(value = "backups", allEntries = true)
    public DatabaseBackup executeBackup(String id) {
        DatabaseBackup backup = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Backup not found: " + id));

        if (!backup.canExecute()) {
            throw new IllegalStateException("Backup cannot be executed: " + backup.getStatus());
        }

        String runningKey = getBackupKey(backup);
        if (runningBackups.containsKey(runningKey)) {
            throw new IllegalStateException("Backup is already running: " + runningKey);
        }

        runningBackups.put(runningKey, true);

        try {
            backup.setStatus(DatabaseBackup.BackupStatus.RUNNING);
            backup.setExecutionStartTime(LocalDateTime.now());
            repository.save(backup);

            log.info("Executing backup: {} for database: {}",
                    backup.getBackupName(), backup.getTargetDatabase());

            // Execute backup based on type
            performBackup(backup);

            backup.setStatus(DatabaseBackup.BackupStatus.SUCCESS);
            backup.setExecutionEndTime(LocalDateTime.now());
            backup.calculateDuration();

            log.info("Backup completed successfully: {}", backup.getBackupName());

        } catch (Exception e) {
            backup.setStatus(DatabaseBackup.BackupStatus.FAILED);
            backup.setErrorMessage(e.getMessage());
            backup.setExecutionEndTime(LocalDateTime.now());
            backup.calculateDuration();
            backup.setRetryCount(backup.getRetryCount() + 1);

            log.error("Backup failed: {}: {}", backup.getBackupName(), e.getMessage());
        } finally {
            runningBackups.remove(runningKey);
        }

        return repository.save(backup);
    }

    /**
     * Perform the actual backup operation.
     */
    private void performBackup(DatabaseBackup backup) {
        switch (backup.getBackupMethod()) {
            case LOGICAL:
                performLogicalBackup(backup);
                break;
            case PHYSICAL:
                performPhysicalBackup(backup);
                break;
            case SNAPSHOT:
                performSnapshotBackup(backup);
                break;
            case STREAMING:
                performStreamingBackup(backup);
                break;
        }
    }

    /**
     * Perform logical backup (SQL dump).
     */
    private void performLogicalBackup(DatabaseBackup backup) {
        log.info("Performing logical backup: {}", backup.getBackupName());

        // TODO: Implement actual logical backup
        // 1. Get data source for target database
        // 2. Run pg_dump/mysqldump equivalent
        // 3. Store backup file
        // 4. Calculate checksum
        // 5. Compress if enabled

        try {
            Thread.sleep(500); // Placeholder
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Perform physical backup.
     */
    private void performPhysicalBackup(DatabaseBackup backup) {
        log.info("Performing physical backup: {}", backup.getBackupName());
        // TODO: Implement physical backup
    }

    /**
     * Perform snapshot backup.
     */
    private void performSnapshotBackup(DatabaseBackup backup) {
        log.info("Performing snapshot backup: {}", backup.getBackupName());
        // TODO: Implement snapshot backup
    }

    /**
     * Perform streaming backup.
     */
    private void performStreamingBackup(DatabaseBackup backup) {
        log.info("Performing streaming backup: {}", backup.getBackupName());
        // TODO: Implement streaming backup
    }

    /**
     * Restore from backup.
     */
    @Transactional
    @CacheEvict(value = "backups", allEntries = true)
    public DatabaseBackup restoreFromBackup(String id) {
        DatabaseBackup backup = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Backup not found: " + id));

        if (!backup.canRestore()) {
            throw new IllegalStateException("Backup cannot be used for restore: " + backup.getStatus());
        }

        log.info("Restoring from backup: {}", backup.getBackupName());

        try {
            performRestore(backup);

            backup.setRestoreCount(backup.getRestoreCount() + 1);
            backup.setLastRestoreAt(LocalDateTime.now());

            log.info("Restore completed successfully from backup: {}", backup.getBackupName());

        } catch (Exception e) {
            log.error("Restore failed from backup {}: {}", backup.getBackupName(), e.getMessage());
            throw new RuntimeException("Restore failed: " + e.getMessage(), e);
        }

        return repository.save(backup);
    }

    /**
     * Perform restore operation.
     */
    private void performRestore(DatabaseBackup backup) {
        log.info("Performing restore from: {}", backup.getBackupLocation());

        // TODO: Implement actual restore
        // 1. Get data source for target database
        // 2. Execute restore command (psql/mysql etc.)
        // 3. Verify restore completed successfully
    }

    /**
     * Schedule a backup.
     */
    @Transactional
    public DatabaseBackup scheduleBackup(String id, LocalDateTime scheduledFor) {
        DatabaseBackup backup = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Backup not found: " + id));

        backup.setStatus(DatabaseBackup.BackupStatus.SCHEDULED);
        backup.setScheduledFor(scheduledFor);

        return repository.save(backup);
    }

    /**
     * Get backup by ID.
     */
    @Cacheable(value = "backups", key = "#id")
    public Optional<DatabaseBackup> getBackup(String id) {
        return repository.findById(id);
    }

    /**
     * Get all backups for a tenant.
     */
    public List<DatabaseBackup> getBackupsByTenant(String tenantId) {
        return repository.findAllByTenantId(tenantId);
    }

    /**
     * Get backups for a tenant and database.
     */
    public List<DatabaseBackup> getBackupsByTenantAndDatabase(String tenantId, String targetDatabase) {
        return repository.findAllByTenantIdAndTargetDatabase(tenantId, targetDatabase);
    }

    /**
     * Get latest successful backup.
     */
    public Optional<DatabaseBackup> getLatestSuccessfulBackup(String tenantId, String targetDatabase) {
        return repository.findFirstByTenantIdAndTargetDatabaseAndStatusOrderByCreatedAtDesc(
                tenantId, targetDatabase, DatabaseBackup.BackupStatus.SUCCESS);
    }

    /**
     * Delete a backup.
     */
    @Transactional
    @CacheEvict(value = "backups", allEntries = true)
    public void deleteBackup(String id) {
        DatabaseBackup backup = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Backup not found: " + id));

        if (backup.getImmutable() && !backup.isExpired()) {
            throw new IllegalStateException("Cannot delete immutable backup");
        }

        // Delete actual backup file
        deleteBackupFile(backup);

        repository.delete(backup);
        log.info("Backup deleted: {}", backup.getBackupName());
    }

    /**
     * Delete backup file from storage.
     */
    private void deleteBackupFile(DatabaseBackup backup) {
        log.info("Deleting backup file: {}", backup.getBackupLocation());
        // TODO: Implement file deletion
    }

    /**
     * Validate a backup.
     */
    @Transactional
    public DatabaseBackup validateBackup(String id) {
        DatabaseBackup backup = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Backup not found: " + id));

        backup.setValidationStatus(DatabaseBackup.ValidationStatus.VALIDATING);

        try {
            performValidation(backup);
            backup.setValidationStatus(DatabaseBackup.ValidationStatus.VALIDATED);
            backup.setValidatedAt(LocalDateTime.now());
            log.info("Backup validated successfully: {}", backup.getBackupName());

        } catch (Exception e) {
            backup.setValidationStatus(DatabaseBackup.ValidationStatus.VALIDATION_FAILED);
            log.error("Backup validation failed: {}: {}", backup.getBackupName(), e.getMessage());
        }

        return repository.save(backup);
    }

    /**
     * Perform backup validation.
     */
    private void performValidation(DatabaseBackup backup) {
        log.info("Validating backup: {}", backup.getBackupName());
        // TODO: Implement backup validation
        // 1. Verify file exists
        // 2. Verify checksum
        // 3. Possibly test restore
    }

    /**
     * Get backup statistics.
     */
    public Map<String, Object> getBackupStatistics(String tenantId) {
        List<DatabaseBackup> backups = repository.findAllByTenantId(tenantId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBackups", backups.size());
        stats.put("tenantId", tenantId);

        if (backups.isEmpty()) {
            return stats;
        }

        long successfulCount = backups.stream()
                .filter(b -> b.getStatus() == DatabaseBackup.BackupStatus.SUCCESS)
                .count();
        stats.put("successfulBackups", successfulCount);

        long failedCount = backups.stream()
                .filter(b -> b.getStatus() == DatabaseBackup.BackupStatus.FAILED)
                .count();
        stats.put("failedBackups", failedCount);

        long totalSize = backups.stream()
                .filter(b -> b.getFileSizeBytes() != null)
                .mapToLong(DatabaseBackup::getFileSizeBytes)
                .sum();
        stats.put("totalSizeBytes", totalSize);
        stats.put("totalSizeMB", totalSize / (1024.0 * 1024.0));

        Map<String, Long> countsByType = new HashMap<>();
        for (DatabaseBackup.BackupType type : DatabaseBackup.BackupType.values()) {
            long count = backups.stream()
                    .filter(b -> b.getBackupType() == type)
                    .count();
            countsByType.put(type.name(), count);
        }
        stats.put("byType", countsByType);

        return stats;
    }

    /**
     * Scheduled task to check for backups ready to execute.
     */
    @Scheduled(fixedDelay = 60000)
    public void checkScheduledBackups() {
        List<DatabaseBackup> readyBackups = repository.findScheduledBackupsReadyForExecution(
                LocalDateTime.now());

        log.debug("Found {} backups ready for execution", readyBackups.size());

        for (DatabaseBackup backup : readyBackups) {
            try {
                if (backup.canExecute()) {
                    executeBackup(backup.getId());
                }
            } catch (Exception e) {
                log.error("Failed to execute scheduled backup {}: {}",
                        backup.getId(), e.getMessage());
            }
        }
    }

    /**
     * Scheduled task to clean up expired backups.
     */
    @Scheduled(cron = "0 0 3 * * ?") // Run at 3 AM daily
    public void cleanupExpiredBackups() {
        log.info("Checking for expired backups to clean up");

        List<DatabaseBackup> expiredBackups = repository.findExpiredBackups(LocalDateTime.now());

        for (DatabaseBackup backup : expiredBackups) {
            if (!backup.getImmutable()) {
                try {
                    deleteBackup(backup.getId());
                } catch (Exception e) {
                    log.error("Failed to delete expired backup {}: {}",
                            backup.getId(), e.getMessage());
                }
            }
        }
    }

    /**
     * Get backup key for tracking.
     */
    private String getBackupKey(DatabaseBackup backup) {
        return backup.getTenantId() + ":" + backup.getTargetDatabase() + ":" + backup.getId();
    }
}
