package com.gogidix.aiservices.aianalyticsdashboard.domain.port.out;

import com.gogidix.aiservices.aianalyticsdashboard.domain.aggregate.MetricAggregate;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface MetricRepository {
    MetricAggregate save(MetricAggregate metric);
    Optional<MetricAggregate> findById(String metricId);
    Optional<MetricAggregate> findByName(String name);
    List<MetricAggregate> findByTimeRange(Instant start, Instant end);
    List<MetricAggregate> findByTag(String key, String value);
    void delete(String metricId);
}
