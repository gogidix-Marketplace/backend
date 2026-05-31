package com.gogidix.monitoring.monitoringdataservice.infrastructure.persistence.repository;

import com.gogidix.monitoring.monitoringdataservice.infrastructure.persistence.document.MetricDataPointDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data MongoDB repository for metric data points.
 */
@Repository
public interface SpringDataMetricDataPointRepository extends MongoRepository<MetricDataPointDocument, String> {

    /**
     * Find by tenant, service, and time range.
     */
    List<MetricDataPointDocument> findByTenantIdAndServiceNameAndTimestampBetween(
            String tenantId,
            String serviceName,
            Instant startTime,
            Instant endTime
    );

    /**
     * Find by tenant, service, metric, and time range.
     */
    List<MetricDataPointDocument> findByTenantIdAndServiceNameAndMetricNameAndTimestampBetween(
            String tenantId,
            String serviceName,
            String metricName,
            Instant startTime,
            Instant endTime
    );

    /**
     * Find by tenant and time range.
     */
    List<MetricDataPointDocument> findByTenantIdAndTimestampBetween(
            String tenantId,
            Instant startTime,
            Instant endTime
    );

    /**
     * Delete documents older than timestamp.
     */
    long deleteByTimestampBefore(Instant timestamp);

    /**
     * Count by tenant.
     */
    long countByTenantId(String tenantId);

    /**
     * Find the latest timestamp for a service.
     */
    @Query(value = "{'tenantId': ?0, 'serviceName': ?1}", sort = "{'timestamp': -1}")
    List<MetricDataPointDocument> findLatestByTenantAndService(String tenantId, String serviceName);

    /**
     * Find by service name and timestamp range.
     */
    List<MetricDataPointDocument> findByServiceNameAndTimestampBetween(
            String serviceName,
            Instant startTime,
            Instant endTime
    );
}
