package com.gogidix.globalbusinessmanagement.countryingestion.domain.repository;

import com.gogidix.globalbusinessmanagement.countryingestion.domain.model.ValidationError;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for ValidationError entity.
 * Provides data access operations for validation error tracking.
 */
@Repository
public interface ValidationErrorRepository extends MongoRepository<ValidationError, String> {

    /**
     * Find validation error by error ID.
     */
    ValidationError findByErrorId(String errorId);

    /**
     * Check if error exists by error ID.
     */
    boolean existsByErrorId(String errorId);

    /**
     * Find all errors by batch ID.
     */
    List<ValidationError> findByBatchId(String batchId);

    /**
     * Find all errors by batch ID with pagination.
     */
    Page<ValidationError> findByBatchId(String batchId, Pageable pageable);

    /**
     * Find all errors by status.
     */
    List<ValidationError> findByStatus(ValidationError.ErrorStatus status);

    /**
     * Find all errors by status with pagination.
     */
    Page<ValidationError> findByStatus(ValidationError.ErrorStatus status, Pageable pageable);

    /**
     * Find all errors by error level.
     */
    List<ValidationError> findByErrorLevel(ValidationError.ErrorLevel errorLevel);

    /**
     * Find all errors by error level with pagination.
     */
    Page<ValidationError> findByErrorLevel(ValidationError.ErrorLevel errorLevel, Pageable pageable);

    /**
     * Find all errors by error type.
     */
    List<ValidationError> findByErrorType(ValidationError.ErrorType errorType);

    /**
     * Find errors by country code.
     */
    List<ValidationError> findByCountryCode(String countryCode);

    /**
     * Find errors by field name.
     */
    List<ValidationError> findByFieldName(String fieldName);

    /**
     * Find errors by error code.
     */
    List<ValidationError> findByErrorCode(String errorCode);

    /**
     * Find errors between timestamps.
     */
    List<ValidationError> findByErrorTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find errors created after a specific date.
     */
    List<ValidationError> findByErrorTimestampAfter(LocalDateTime date);

    /**
     * Find errors by batch ID and status.
     */
    List<ValidationError> findByBatchIdAndStatus(String batchId, ValidationError.ErrorStatus status);

    /**
     * Find errors by batch ID and error level.
     */
    List<ValidationError> findByBatchIdAndErrorLevel(String batchId, ValidationError.ErrorLevel errorLevel);

    /**
     * Find critical and high priority errors.
     */
    @Query("{'errorLevel': {$in: ['CRITICAL', 'HIGH']}, 'status': {$ne: 'RESOLVED'}}")
    List<ValidationError> findCriticalErrors();

    /**
     * Find open errors (not resolved).
     */
    @Query("{'status': {$in: ['OPEN', 'IN_PROGRESS']}}")
    List<ValidationError> findOpenErrors();

    /**
     * Find errors assigned to a specific user.
     */
    List<ValidationError> findByAssignedTo(String assignedTo);

    /**
     * Find errors by organization ID.
     */
    List<ValidationError> findByOrganizationId(String organizationId);

    /**
     * Find suppressed errors.
     */
    List<ValidationError> findBySuppressTrue();

    /**
     * Find auto-corrected errors.
     */
    List<ValidationError> findByAutoCorrectedTrue();

    /**
     * Find errors that are not suppressed.
     */
    List<ValidationError> findBySuppressFalse();

    /**
     * Find errors by priority.
     */
    List<ValidationError> findByPriority(Integer priority);

    /**
     * Find errors with priority less than or equal to.
     */
    List<ValidationError> findByPriorityLessThanEqual(Integer maxPriority);

    /**
     * Find errors by multiple status values.
     */
    List<ValidationError> findByStatusIn(List<ValidationError.ErrorStatus> statuses);

    /**
     * Search errors by error message or field name.
     */
    @Query("{$or: [" +
            "{'errorMessage': {$regex: ?0, $options: 'i'}}, " +
            "{'fieldName': {$regex: ?0, $options: 'i'}}, " +
            "{'errorCode': {$regex: ?0, $options: 'i'}}" +
            "]}")
    Page<ValidationError> searchErrors(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find errors by batch ID ordered by priority.
     */
    List<ValidationError> findByBatchIdOrderByPriorityAsc(String batchId);

    /**
     * Find most recent errors.
     */
    List<ValidationError> findBySuppressFalseOrderByErrorTimestampDesc(Pageable pageable);

    /**
     * Count errors by batch ID.
     */
    Long countByBatchId(String batchId);

    /**
     * Count errors by status.
     */
    Long countByStatus(ValidationError.ErrorStatus status);

    /**
     * Count errors by error level.
     */
    Long countByErrorLevel(ValidationError.ErrorLevel errorLevel);

    /**
     * Count errors by batch ID and status.
     */
    Long countByBatchIdAndStatus(String batchId, ValidationError.ErrorStatus status);

    /**
     * Count open errors.
     */
    @Query("{'status': {$in: ['OPEN', 'IN_PROGRESS']}, 'suppress': false}")
    Long countOpenErrors();

    /**
     * Count critical errors.
     */
    Long countByErrorLevelAndStatus(ValidationError.ErrorLevel errorLevel, ValidationError.ErrorStatus status);

    /**
     * Get error statistics by batch ID.
     */
    @Query("{'batchId': ?0}")
    List<ValidationError> findAllByBatchIdForStats(String batchId);

    /**
     * Get error statistics by type.
     */
    @Query("{$group: {_id: '$errorType', count: {$sum: 1}}}")
    List<ErrorStatsByType> getErrorStatsByType();

    /**
     * Get error statistics by level.
     */
    @Query("{$group: {_id: '$errorLevel', count: {$sum: 1}}}")
    List<ErrorStatsByLevel> getErrorStatsByLevel();

    /**
     * Get error statistics by field.
     */
    @Query("{$group: {_id: '$fieldName', count: {$sum: 1}}}")
    List<ErrorStatsByField> getErrorStatsByField();

    /**
     * Find errors with high occurrence count.
     */
    @Query("{'occurrences': {$gt: ?0}}")
    List<ValidationError> findByOccurrencesGreaterThan(Integer minOccurrences);

    /**
     * Find errors for a specific record ID.
     */
    List<ValidationError> findByRecordId(String recordId);

    /**
     * Find errors by validation rule.
     */
    List<ValidationError> findByValidationRule(String validationRule);

    /**
     * Delete errors by batch ID.
     */
    void deleteByBatchId(String batchId);

    /**
     * Delete resolved errors older than specified date.
     */
    @Query("{$and: [" +
            "{'status': {$in: ['RESOLVED', 'AUTO_CORRECTED', 'SUPPRESSED']}}, " +
            "{'resolvedDate': {$lt: ?0}}" +
            "]}")
    void deleteOldResolvedErrors(LocalDateTime cutoffDate);

    /**
     * Interface for error statistics by type aggregation result.
     */
    interface ErrorStatsByType {
        String getId();
        Integer getCount();
    }

    /**
     * Interface for error statistics by level aggregation result.
     */
    interface ErrorStatsByLevel {
        String getId();
        Integer getCount();
    }

    /**
     * Interface for error statistics by field aggregation result.
     */
    interface ErrorStatsByField {
        String getId();
        Integer getCount();
    }
}
