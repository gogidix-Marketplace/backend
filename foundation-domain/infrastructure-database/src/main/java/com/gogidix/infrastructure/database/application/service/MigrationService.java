package com.gogidix.infrastructure.database.application.service;

import com.gogidix.infrastructure.database.domain.model.DatabaseMigration;
import com.gogidix.infrastructure.database.domain.repository.DatabaseMigrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing database migrations.
 *
 * <p>Handles migration execution, scheduling, and tracking
 * for Flyway and Liquibase migrations.</p>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MigrationService {

    private final DatabaseMigrationRepository repository;
    private final ConnectionPoolService connectionPoolService;

    private final Map<String, Boolean> runningMigrations = new ConcurrentHashMap<>();

    /**
     * Create a new migration record.
     */
    @Transactional
    @CacheEvict(value = "migrations", allEntries = true)
    public DatabaseMigration createMigration(@Valid DatabaseMigration migration) {
        log.info("Creating migration: {} - {}", migration.getVersion(), migration.getDescription());

        // Check if migration already exists
        Optional<DatabaseMigration> existing = repository.findByTenantIdAndTargetDatabaseAndVersion(
                migration.getTenantId(),
                migration.getTargetDatabase(),
                migration.getVersion());

        if (existing.isPresent()) {
            throw new IllegalArgumentException(
                    "Migration already exists: " + migration.getVersion());
        }

        migration.setStatus(DatabaseMigration.MigrationStatus.PENDING);
        migration.setCreatedAt(LocalDateTime.now());

        return repository.save(migration);
    }

    /**
     * Get migration by ID.
     */
    @Cacheable(value = "migrations", key = "#id")
    public Optional<DatabaseMigration> getMigration(String id) {
        return repository.findById(id);
    }

    /**
     * Get all migrations for a tenant and database.
     */
    public List<DatabaseMigration> getMigrations(String tenantId, String targetDatabase) {
        return repository.findAllByTenantIdAndTargetDatabase(tenantId, targetDatabase);
    }

    /**
     * Get pending migrations for a database.
     */
    public List<DatabaseMigration> getPendingMigrations(String tenantId, String targetDatabase) {
        return repository.findPendingMigrations(tenantId, targetDatabase);
    }

    /**
     * Get migration history for a database.
     */
    public List<DatabaseMigration> getMigrationHistory(String tenantId, String targetDatabase) {
        return repository.findAllByTenantIdAndTargetDatabase(tenantId, targetDatabase)
                .stream()
                .sorted(Comparator.comparing(DatabaseMigration::getCreatedAt).reversed())
                .toList();
    }

    /**
     * Execute a migration.
     */
    @Transactional
    @CacheEvict(value = "migrations", allEntries = true)
    public DatabaseMigration executeMigration(String id) {
        DatabaseMigration migration = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Migration not found: " + id));

        if (!migration.canExecute()) {
            throw new IllegalStateException("Migration cannot be executed: " + migration.getStatus());
        }

        // Check if already running
        String runningKey = getMigrationKey(migration);
        if (runningMigrations.containsKey(runningKey)) {
            throw new IllegalStateException("Migration is already running: " + runningKey);
        }

        runningMigrations.put(runningKey, true);

        try {
            migration.setStatus(DatabaseMigration.MigrationStatus.RUNNING);
            migration.setExecutionStartTime(LocalDateTime.now());
            repository.save(migration);

            log.info("Executing migration: {} - {}", migration.getVersion(), migration.getDescription());

            // Execute migration based on type
            switch (migration.getMigrationType()) {
                case FLYWAY:
                    executeFlywayMigration(migration);
                    break;
                case LIQUIBASE:
                    executeLiquibaseMigration(migration);
                    break;
                case CUSTOM:
                    executeCustomMigration(migration);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported migration type: " + migration.getMigrationType());
            }

            migration.setStatus(DatabaseMigration.MigrationStatus.SUCCESS);
            migration.setExecutionEndTime(LocalDateTime.now());
            migration.calculateDuration();

            log.info("Migration completed successfully: {} - {}", migration.getVersion(), migration.getDescription());

        } catch (Exception e) {
            migration.setStatus(DatabaseMigration.MigrationStatus.FAILED);
            migration.setErrorMessage(e.getMessage());
            migration.setErrorStackTrace(getStackTrace(e));
            migration.setExecutionEndTime(LocalDateTime.now());
            migration.calculateDuration();
            migration.setRetryCount(migration.getRetryCount() + 1);

            log.error("Migration failed: {} - {}: {}",
                    migration.getVersion(), migration.getDescription(), e.getMessage());
        } finally {
            runningMigrations.remove(runningKey);
        }

        return repository.save(migration);
    }

    /**
     * Execute Flyway migration.
     */
    private void executeFlywayMigration(DatabaseMigration migration) {
        log.info("Executing Flyway migration: {}", migration.getScriptName());

        // TODO: Implement actual Flyway migration execution
        // This would involve:
        // 1. Getting the data source for the target database
        // 2. Configuring Flyway with the data source
        // 3. Running the migration
        // 4. Capturing the output

        // Placeholder implementation
        try {
            Thread.sleep(100); // Simulate migration execution
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Execute Liquibase migration.
     */
    private void executeLiquibaseMigration(DatabaseMigration migration) {
        log.info("Executing Liquibase migration: {}", migration.getScriptName());

        // TODO: Implement actual Liquibase migration execution
        // This would involve:
        // 1. Getting the data source for the target database
        // 2. Configuring Liquibase with the data source
        // 3. Running the changeset
        // 4. Capturing the output

        // Placeholder implementation
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Execute custom migration script.
     */
    private void executeCustomMigration(DatabaseMigration migration) {
        log.info("Executing custom migration: {}", migration.getScriptName());

        // TODO: Implement custom script execution
        // This would involve:
        // 1. Getting the data source for the target database
        // 2. Executing the SQL script
        // 3. Capturing the output

        // Placeholder implementation
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Rollback a migration.
     */
    @Transactional
    @CacheEvict(value = "migrations", allEntries = true)
    public DatabaseMigration rollbackMigration(String id) {
        DatabaseMigration migration = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Migration not found: " + id));

        if (!migration.canRollback()) {
            throw new IllegalStateException("Migration cannot be rolled back");
        }

        log.info("Rolling back migration: {} - {}", migration.getVersion(), migration.getDescription());

        try {
            // Execute rollback based on type
            switch (migration.getMigrationType()) {
                case FLYWAY:
                    rollbackFlywayMigration(migration);
                    break;
                case LIQUIBASE:
                    rollbackLiquibaseMigration(migration);
                    break;
                case CUSTOM:
                    rollbackCustomMigration(migration);
                    break;
            }

            migration.setStatus(DatabaseMigration.MigrationStatus.ROLLED_BACK);
            log.info("Migration rolled back successfully: {}", migration.getVersion());

        } catch (Exception e) {
            migration.setStatus(DatabaseMigration.MigrationStatus.FAILED);
            migration.setErrorMessage("Rollback failed: " + e.getMessage());
            log.error("Rollback failed for migration {}: {}", migration.getVersion(), e.getMessage());
        }

        return repository.save(migration);
    }

    /**
     * Rollback Flyway migration.
     */
    private void rollbackFlywayMigration(DatabaseMigration migration) {
        // TODO: Implement Flyway rollback
        log.info("Rolling back Flyway migration: {}", migration.getScriptName());
    }

    /**
     * Rollback Liquibase migration.
     */
    private void rollbackLiquibaseMigration(DatabaseMigration migration) {
        // TODO: Implement Liquibase rollback
        log.info("Rolling back Liquibase migration: {}", migration.getScriptName());
    }

    /**
     * Rollback custom migration.
     */
    private void rollbackCustomMigration(DatabaseMigration migration) {
        // TODO: Execute custom rollback script
        log.info("Executing custom rollback: {}", migration.getRollbackScriptName());
    }

    /**
     * Schedule a migration.
     */
    @Transactional
    public DatabaseMigration scheduleMigration(String id, LocalDateTime scheduledFor) {
        DatabaseMigration migration = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Migration not found: " + id));

        migration.setStatus(DatabaseMigration.MigrationStatus.SCHEDULED);
        migration.setScheduledFor(scheduledFor);
        migration.setExecutionType(DatabaseMigration.ExecutionType.SCHEDULED);

        return repository.save(migration);
    }

    /**
     * Approve a migration for execution.
     */
    @Transactional
    public DatabaseMigration approveMigration(String id, String approvedBy) {
        DatabaseMigration migration = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Migration not found: " + id));

        migration.setApproved(true);
        migration.setApprovedBy(approvedBy);
        migration.setApprovedAt(LocalDateTime.now());

        return repository.save(migration);
    }

    /**
     * Delete a migration.
     */
    @Transactional
    @CacheEvict(value = "migrations", allEntries = true)
    public void deleteMigration(String id) {
        DatabaseMigration migration = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Migration not found: " + id));

        if (migration.getStatus() == DatabaseMigration.MigrationStatus.RUNNING) {
            throw new IllegalStateException("Cannot delete running migration");
        }

        repository.delete(migration);
    }

    /**
     * Get migration status summary.
     */
    public Map<String, Long> getMigrationStatusSummary(String tenantId, String targetDatabase) {
        Map<String, Long> summary = new HashMap<>();

        List<DatabaseMigration> migrations = repository.findAllByTenantIdAndTargetDatabase(
                tenantId, targetDatabase);

        for (DatabaseMigration.MigrationStatus status : DatabaseMigration.MigrationStatus.values()) {
            long count = migrations.stream()
                    .filter(m -> m.getStatus() == status)
                    .count();
            summary.put(status.name(), count);
        }

        return summary;
    }

    /**
     * Scheduled task to check for migrations ready to execute.
     */
    @Scheduled(fixedDelay = 60000)
    public void checkScheduledMigrations() {
        List<DatabaseMigration> readyMigrations = repository.findScheduledMigrationsReadyForExecution(
                LocalDateTime.now());

        log.debug("Found {} migrations ready for execution", readyMigrations.size());

        for (DatabaseMigration migration : readyMigrations) {
            try {
                if (migration.canExecute()) {
                    executeMigration(migration.getId());
                }
            } catch (Exception e) {
                log.error("Failed to execute scheduled migration {}: {}",
                        migration.getId(), e.getMessage());
            }
        }
    }

    /**
     * Get migration key for tracking.
     */
    private String getMigrationKey(DatabaseMigration migration) {
        return migration.getTenantId() + ":" + migration.getTargetDatabase() + ":" + migration.getVersion();
    }

    /**
     * Get stack trace as string.
     */
    private String getStackTrace(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
