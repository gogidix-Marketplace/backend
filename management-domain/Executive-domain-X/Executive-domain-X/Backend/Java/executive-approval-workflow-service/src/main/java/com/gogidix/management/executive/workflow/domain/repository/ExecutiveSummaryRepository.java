package com.gogidix.management.executive.workflow.domain.repository;

import com.gogidix.management.executive.workflow.domain.model.ExecutiveSummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ExecutiveSummary entities
 */
@Repository
public interface ExecutiveSummaryRepository extends MongoRepository<ExecutiveSummary, String> {

    /**
     * Find summaries by tenant ID
     */
    List<ExecutiveSummary> findByTenantId(String tenantId);

    /**
     * Find summaries by tenant ID and not deleted
     */
    List<ExecutiveSummary> findByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find latest summary for tenant
     */
    Optional<ExecutiveSummary> findFirstByTenantIdOrderByGeneratedAtDesc(String tenantId);

    /**
     * Find summaries by tenant ID and date range
     */
    List<ExecutiveSummary> findByTenantIdAndGeneratedAtBetween(String tenantId, Instant start, Instant end);

    /**
     * Find summary by ID and tenant ID
     */
    Optional<ExecutiveSummary> findByIdAndTenantId(String id, String tenantId);
}
