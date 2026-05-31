package com.gogidix.monitoring.monitoringdataservice.infrastructure.persistence.repository;

import com.gogidix.monitoring.monitoringdataservice.infrastructure.persistence.document.MetricAggregationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data MongoDB repository for metric aggregations.
 */
@Repository
public interface SpringDataMetricAggregationRepository extends MongoRepository<MetricAggregationDocument, String> {

    /**
     * Find by tenant, service, metric, window, and time range.
     */
    List<MetricAggregationDocument> findByTenantIdAndServiceNameAndMetricNameAndWindowAndWindowStartBetween(
            String tenantId,
            String serviceName,
            String metricName,
            String window,
            Instant startTime,
            Instant endTime
    );

    /**
     * Find latest by tenant, service, metric, and window.
     */
    @Query(value = "{'tenantId': ?0, 'serviceName': ?1, 'metricName': ?2, 'window': ?3}", sort = "{'windowStart': -1}")
    List<MetricAggregationDocument> findLatest(String tenantId, String serviceName, String metricName, String window);

    /**
     * Delete by window and timestamp before.
     */
    long deleteByWindowAndWindowStartBefore(String window, Instant timestamp);

    /**
     * Delete by timestamp before.
     */
    long deleteByWindowStartBefore(Instant timestamp);

    /**
     * Find by tenant and window and time range.
     */
    List<MetricAggregationDocument> findByTenantIdAndWindowAndWindowStartBetween(
            String tenantId,
            String window,
            Instant startTime,
            Instant endTime
    );
}
