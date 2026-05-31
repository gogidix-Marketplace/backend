package com.gogidix.management.executive.analytics.domain.repository;

import com.gogidix.management.executive.analytics.domain.model.Metric;
import com.gogidix.management.shared.infrastructure.persistence.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;

@Repository
public interface MetricRepository extends BaseRepository<Metric> {

    List<Metric> findByMetricName(String metricName);

    List<Metric> findBySourceDomain(String sourceDomain);

    List<Metric> findByMetricNameAndSourceDomain(String metricName, String sourceDomain);

    @Query("{ 'timestamp': { $gte: ?0, $lte: ?1 } }")
    List<Metric> findByTimestampRange(Instant startTime, Instant endTime);

    List<Metric> findByGranularity(String granularity);

    Page<Metric> findByMetricNameOrderByTimestampDesc(String metricName, Pageable pageable);

    @Query("{ 'metricName': ?0, 'sourceDomain': ?1 }")
    List<Metric> findLatestByNameAndSource(String metricName, String sourceDomain);

    @Query("{ 'qualityScore': { $gte: 0.9 } }")
    List<Metric> findHighQuality();

    List<Metric> findByIsAggregateTrue();

    List<Metric> findByIsAggregateFalse();

    @Query("{ 'dimensions.?0': ?1 }")
    List<Metric> findByDimension(String dimensionKey, String dimensionValue);

    @Query("{ 'timestamp': { $gte: ?0 } }")
    List<Metric> findRecent(Instant since);

    @Query("{ 'sourceDomain': { $in: ?0 } }")
    List<Metric> findBySourceDomainIn(List<String> sourceDomains);

    @Query(value = "{ 'metricName': { $exists: true } }", fields = "{ 'metricName' : 1 }")
    List<String> findDistinctMetricNames();

    @Query(value = "{ 'sourceDomain': { $exists: true } }", fields = "{ 'sourceDomain' : 1 }")
    List<String> findDistinctSourceDomains();

    @Query(value = "{ 'granularity': { $exists: true } }", fields = "{ 'granularity' : 1 }")
    List<String> findDistinctGranularities();

    @Query("{ $or: [ " +
            "{ 'metricName': { $regex: ?0, $options: 'i' } }, " +
            "{ 'sourceDomain': { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<Metric> search(String searchTerm);

    long countBySourceDomain(String sourceDomain);

    @Query(value = "{ 'timestamp': { $lt: ?0 } }", delete = true)
    long deleteOlderThan(Instant threshold);

    @Query("{ 'metricName': ?0, 'timestamp': { $gte: ?1, $lte: ?2 }, 'granularity': ?3 }")
    List<Metric> findForAggregation(String metricName, Instant startTime, Instant endTime, String granularity);

    List<Metric> findByParentMetricId(String parentMetricId);

    @Query("{ 'parentMetricId': { $exists: false } }")
    List<Metric> findTopLevel();

    List<Metric> findByMetricNameAndGranularity(String metricName, String granularity);
}
