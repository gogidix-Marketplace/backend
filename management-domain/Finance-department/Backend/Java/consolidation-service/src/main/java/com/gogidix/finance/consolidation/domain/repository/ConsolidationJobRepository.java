package com.gogidix.finance.consolidation.domain.repository;

import com.gogidix.finance.consolidation.domain.model.ConsolidationJob;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Consolidation Job Repository Interface (Port)
 * Defines the contract for consolidation job persistence operations
 */
public interface ConsolidationJobRepository {

    ConsolidationJob save(ConsolidationJob job);

    List<ConsolidationJob> saveAll(List<ConsolidationJob> jobs);

    Optional<ConsolidationJob> findById(String id);

    Optional<ConsolidationJob> findByJobIdAndTenantId(String jobId, String tenantId);

    List<ConsolidationJob> findByTenantId(String tenantId);

    List<ConsolidationJob> findByTenantIdAndStatus(String tenantId, ConsolidationJob.JobStatus status);

    List<ConsolidationJob> findByTenantIdAndJobType(String tenantId, ConsolidationJob.JobType jobType);

    List<ConsolidationJob> findByTenantIdAndStatusAndJobType(String tenantId,
                                                              ConsolidationJob.JobStatus status,
                                                              ConsolidationJob.JobType jobType);

    List<ConsolidationJob> findByTenantIdAndInitiatedBy(String tenantId, String initiatedBy);

    List<ConsolidationJob> findByTenantIdAndPeriodEndBetween(String tenantId,
                                                              LocalDate periodStart,
                                                              LocalDate periodEnd);

    List<ConsolidationJob> findByTenantIdAndStartedAtBetween(String tenantId,
                                                              Instant startedAtFrom,
                                                              Instant startedAtTo);

    List<ConsolidationJob> findByTenantIdAndRuleId(String tenantId, String ruleId);

    List<ConsolidationJob> findPendingJobsByTenantId(String tenantId);

    List<ConsolidationJob> findRunningJobsByTenantId(String tenantId);

    List<ConsolidationJob> findFailedJobsByTenantId(String tenantId);

    List<ConsolidationJob> findJobsToRetryByTenantId(String tenantId);

    boolean existsByJobIdAndTenantId(String jobId, String tenantId);

    void deleteById(String id);

    void deleteByJobIdAndTenantId(String jobId, String tenantId);

    void deleteCompletedJobsBefore(String tenantId, Instant beforeDate);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, ConsolidationJob.JobStatus status);

    long countByTenantIdAndJobType(String tenantId, ConsolidationJob.JobType jobType);

    List<ConsolidationJob> findByTenantIdAndCorrelationId(String tenantId, String correlationId);

    List<ConsolidationJob> findByParentJobIdAndTenantId(String parentJobId, String tenantId);

    Optional<ConsolidationJob> findLatestJobByTenantIdAndJobType(String tenantId,
                                                                  ConsolidationJob.JobType jobType);
}
