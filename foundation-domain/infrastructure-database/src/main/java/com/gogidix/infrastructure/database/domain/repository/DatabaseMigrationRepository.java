package com.gogidix.infrastructure.database.domain.repository;

import com.gogidix.infrastructure.database.domain.model.DatabaseMigration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for managing database migrations.
 */
@Repository
public interface DatabaseMigrationRepository extends MongoRepository<DatabaseMigration, String> {

    /**
     * Find by tenant ID and target database.
     */
    List<DatabaseMigration> findAllByTenantIdAndTargetDatabase(String tenantId, String targetDatabase);

    /**
     * Find by tenant ID and environment.
     */
    List<DatabaseMigration> findAllByTenantIdAndEnvironment(String tenantId,
            DatabaseMigration.Environment environment);

    /**
     * Find by tenant ID, environment, and status.
     */
    List<DatabaseMigration> findAllByTenantIdAndEnvironmentAndStatus(
            String tenantId,
            DatabaseMigration.Environment environment,
            DatabaseMigration.MigrationStatus status);

    /**
     * Find by tenant ID, target database, and status.
     */
    List<DatabaseMigration> findAllByTenantIdAndTargetDatabaseAndStatus(
            String tenantId,
            String targetDatabase,
            DatabaseMigration.MigrationStatus status);

    /**
     * Find by tenant ID, target database, and version.
     */
    Optional<DatabaseMigration> findByTenantIdAndTargetDatabaseAndVersion(
            String tenantId,
            String targetDatabase,
            String version);

    /**
     * Find pending migrations.
     */
    @Query("{'status': 'PENDING', 'tenantId': ?0, 'targetDatabase': ?1}")
    List<DatabaseMigration> findPendingMigrations(String tenantId, String targetDatabase);

    /**
     * Find migrations scheduled for execution.
     */
    @Query("{'status': 'SCHEDULED', 'scheduledFor': {$lte: ?0}}")
    List<DatabaseMigration> findScheduledMigrationsReadyForExecution(LocalDateTime now);

    /**
     * Find failed migrations that can be retried.
     */
    @Query("{'status': 'FAILED', 'retryCount': {$lt: '$maxRetries'}}")
    List<DatabaseMigration> findFailedMigrationsForRetry();

    /**
     * Find by migration type.
     */
    List<DatabaseMigration> findAllByMigrationType(DatabaseMigration.MigrationType migrationType);

    /**
     * Find latest successful migration.
     */
    Optional<DatabaseMigration> findFirstByTenantIdAndTargetDatabaseAndStatusOrderByCreatedAtDesc(
            String tenantId,
            String targetDatabase,
            DatabaseMigration.MigrationStatus status);

    /**
     * Count by tenant ID and status.
     */
    Long countByTenantIdAndStatus(String tenantId, DatabaseMigration.MigrationStatus status);

    /**
     * Find by checksum.
     */
    Optional<DatabaseMigration> findByTenantIdAndTargetDatabaseAndChecksum(
            String tenantId,
            String targetDatabase,
            String checksum);

    /**
     * Find all by script name.
     */
    List<DatabaseMigration> findAllByScriptName(String scriptName);

    /**
     * Find migrations with dependencies.
     */
    @Query("{'dependencies': {$ne: null}}")
    List<DatabaseMigration> findMigrationsWithDependencies();

    /**
     * Delete by tenant ID and target database.
     */
    void deleteByTenantIdAndTargetDatabase(String tenantId, String targetDatabase);

    /**
     * Find all by tenant ID paginated.
     */
    Page<DatabaseMigration> findAllByTenantId(String tenantId, Pageable pageable);

    /**
     * Find by status paginated.
     */
    Page<DatabaseMigration> findAllByStatus(DatabaseMigration.MigrationStatus status, Pageable pageable);
}
