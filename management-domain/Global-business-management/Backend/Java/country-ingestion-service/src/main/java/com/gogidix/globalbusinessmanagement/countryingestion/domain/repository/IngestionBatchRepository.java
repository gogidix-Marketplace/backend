package com.gogidix.globalbusinessmanagement.countryingestion.domain.repository;

import com.gogidix.globalbusinessmanagement.countryingestion.domain.model.IngestionBatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for IngestionBatch entity.
 * Provides data access operations for batch tracking and management.
 */
@Repository
public interface IngestionBatchRepository extends MongoRepository<IngestionBatch, String> {

    /**
     * Find ingestion batch by batch ID.
     */
    Optional<IngestionBatch> findByBatchId(String batchId);

    /**
     * Check if batch exists by batch ID.
     */
    boolean existsByBatchId(String batchId);

    /**
     * Find all batches by status.
     */
    List<IngestionBatch> findByStatus(IngestionBatch.BatchStatus status);

    /**
     * Find all batches by status with pagination.
     */
    Page<IngestionBatch> findByStatus(IngestionBatch.BatchStatus status, Pageable pageable);

    /**
     * Find all batches by batch type.
     */
    List<IngestionBatch> findByBatchType(IngestionBatch.BatchType batchType);

    /**
     * Find all batches by source.
     */
    List<IngestionBatch> findBySource(String source);

    /**
     * Find batches created between dates.
     */
    List<IngestionBatch> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find active (not archived) batches.
     */
    List<IngestionBatch> findByArchivedFalse();

    /**
     * Find archived batches.
     */
    List<IngestionBatch> findByArchivedTrue();

    /**
     * Find batches by created by user.
     */
    List<IngestionBatch> findByCreatedBy(String createdBy);

    /**
     * Find batches by organization ID.
     */
    List<IngestionBatch> findByOrganizationId(String organizationId);

    /**
     * Find batches by schema ID.
     */
    List<IngestionBatch> findBySchemaId(String schemaId);

    /**
     * Find pending or in-progress batches.
     */
    @Query("{'status': {$in: ['PENDING', 'IN_PROGRESS', 'VALIDATING', 'PROCESSING', 'RETRYING']}}")
    List<IngestionBatch> findActiveBatches();

    /**
     * Find completed batches.
     */
    @Query("{'status': {$in: ['COMPLETED', 'COMPLETED_WITH_ERRORS']}}")
    List<IngestionBatch> findCompletedBatches();

    /**
     * Find failed batches.
     */
    @Query("{'status': 'FAILED'}")
    List<IngestionBatch> findFailedBatches();

    /**
     * Find batches that can be retried.
     */
    @Query("{$and: [" +
            "{'status': {$in: ['FAILED', 'COMPLETED_WITH_ERRORS']}}, " +
            "{$where: 'this.retryCount < this.maxRetries'}" +
            "]}")
    List<IngestionBatch> findRetryableBatches();

    /**
     * Find batches by multiple status values.
     */
    List<IngestionBatch> findByStatusIn(List<IngestionBatch.BatchStatus> statuses);

    /**
     * Find recent batches ordered by created date.
     */
    List<IngestionBatch> findByArchivedFalseOrderByCreatedDateDesc(Pageable pageable);

    /**
     * Count batches by status.
     */
    Long countByStatus(IngestionBatch.BatchStatus status);

    /**
     * Count active batches.
     */
    @Query("{'status': {$in: ['PENDING', 'IN_PROGRESS', 'VALIDATING', 'PROCESSING', 'RETRYING']}, 'archived': false}")
    Long countActiveBatches();

    /**
     * Count batches by batch type.
     */
    Long countByBatchType(IngestionBatch.BatchType batchType);

    /**
     * Find batches by parent batch ID.
     */
    List<IngestionBatch> findByParentBatchId(String parentBatchId);

    /**
     * Find batches created after a specific date.
     */
    List<IngestionBatch> findByCreatedDateAfter(LocalDateTime date);

    /**
     * Find batches completed between dates.
     */
    @Query("{'endTime': {$gte: ?0, $lte: ?1}}")
    List<IngestionBatch> findByEndTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find batches by validation enabled flag.
     */
    List<IngestionBatch> findByValidationEnabled(Boolean validationEnabled);

    /**
     * Search batches by batch ID or source name.
     */
    @Query("{$or: [" +
            "{'batchId': {$regex: ?0, $options: 'i'}}, " +
            "{'source': {$regex: ?0, $options: 'i'}}, " +
            "{'fileName': {$regex: ?0, $options: 'i'}}" +
            "]}")
    Page<IngestionBatch> searchBatches(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find batches with processing time greater than specified milliseconds.
     */
    @Query("{'processingTimeMs': {$gt: ?0}}")
    List<IngestionBatch> findByProcessingTimeMsGreaterThan(Long processingTimeMs);

    /**
     * Get batch statistics by status.
     */
    @Query("{$group: {_id: '$status', count: {$sum: 1}, " +
            "avgProcessingTime: {$avg: '$processingTimeMs'}}}")
    List<BatchStats> getBatchStatsByStatus();

    /**
     * Find batches that need to be cleaned up (old and completed).
     */
    @Query("{$and: [" +
            "{'status': {$in: ['COMPLETED', 'COMPLETED_WITH_ERRORS', 'FAILED', 'CANCELLED']}}, " +
            "{'endTime': {$lt: ?0}}, " +
            "{'archived': false}, " +
            "{'deleteOnCompletion': true}" +
            "]}")
    List<IngestionBatch> findBatchesForCleanup(LocalDateTime cutoffDate);

    /**
     * Update batch status.
     */
    @Query("{'batchId': ?0}, {$set: {'status': ?1}}")
    void updateBatchStatus(String batchId, IngestionBatch.BatchStatus status);

    /**
     * Interface for batch statistics aggregation result.
     */
    interface BatchStats {
        String getId();
        Integer getCount();
        Double getAvgProcessingTime();
    }
}
