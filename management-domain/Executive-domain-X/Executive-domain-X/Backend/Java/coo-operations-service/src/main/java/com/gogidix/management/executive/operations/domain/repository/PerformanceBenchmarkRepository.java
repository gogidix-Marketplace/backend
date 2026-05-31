package com.gogidix.management.executive.operations.domain.repository;

import com.gogidix.management.executive.operations.domain.model.PerformanceBenchmark;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for PerformanceBenchmark entities
 */
@Repository
public interface PerformanceBenchmarkRepository extends MongoRepository<PerformanceBenchmark, String> {

    /**
     * Find benchmarks by tenant ID
     */
    List<PerformanceBenchmark> findByTenantId(String tenantId);

    /**
     * Find benchmarks by tenant ID and not deleted
     */
    List<PerformanceBenchmark> findByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find benchmarks by tenant ID and metric type
     */
    List<PerformanceBenchmark> findByTenantIdAndMetricType(String tenantId, String metricType);

    /**
     * Find benchmark by ID and tenant ID
     */
    Optional<PerformanceBenchmark> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find latest benchmark for metric
     */
    Optional<PerformanceBenchmark> findFirstByTenantIdAndMetricTypeOrderByUpdatedAtDesc(String tenantId, String metricType);
}
