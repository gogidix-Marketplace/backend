package com.gogidix.globalbusinessmanagement.datavalidation.domain.repository;

import com.gogidix.globalbusinessmanagement.datavalidation.domain.model.ValidationResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for ValidationResult
 */
@Repository
public interface ValidationResultRepository extends MongoRepository<ValidationResult, String> {

    /**
     * Find results by validation ID
     */
    List<ValidationResult> findByValidationId(String validationId);

    /**
     * Find results by entity type and entity ID
     */
    List<ValidationResult> findByEntityTypeAndEntityIdOrderByValidatedAtDesc(
            String entityType, String entityId);

    /**
     * Find results by rule code
     */
    List<ValidationResult> findByRuleCode(String ruleCode);

    /**
     * Find results by status
     */
    List<ValidationResult> findByStatus(ValidationResult.ValidationStatus status);

    /**
     * Find results by batch ID
     */
    List<ValidationResult> findByBatchId(String batchId);

    /**
     * Find results by tenant ID
     */
    Page<ValidationResult> findByTenantIdOrderByValidatedAtDesc(String tenantId, Pageable pageable);

    /**
     * Find failed results
     */
    List<ValidationResult> findByPassedFalseAndSeverity(
            ValidationResult.ValidationStatus status);

    /**
     * Find results within a date range
     */
    List<ValidationResult> findByValidatedAtBetweenOrderByValidatedAtDesc(
            LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find results by entity type, status, and date range
     */
    @Query("{ 'entityType': ?0, 'status': ?1, " +
           "'validatedAt': { '$gte': ?2, '$lte': ?3 } }")
    List<ValidationResult> findByEntityTypeAndStatusAndDateRange(
            String entityType,
            ValidationResult.ValidationStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate);

    /**
     * Count results by validation ID
     */
    long countByValidationId(String validationId);

    /**
     * Count passed results by validation ID
     */
    long countByValidationIdAndPassedTrue(String validationId);

    /**
     * Count failed results by validation ID
     */
    long countByValidationIdAndPassedFalse(String validationId);

    /**
     * Find recent results for an entity
     */
    List<ValidationResult> findTop10ByEntityTypeAndEntityIdOrderByValidatedAtDesc(
            String entityType, String entityId);

    /**
     * Find results by severity
     */
    List<ValidationResult> findBySeverity(ValidationResult.ValidationStatus status,
                                          com.gogidix.globalbusinessmanagement.datavalidation.domain.model.ValidationRule.SeverityLevel severity);

    /**
     * Delete results older than a date
     */
    long deleteByValidatedAtBefore(LocalDateTime date);
}
