package com.gogidix.globalbusinessmanagement.regionalaggregation.domain.repository;

import com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model.AggregatedMetrics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AggregatedMetricsRepository extends MongoRepository<AggregatedMetrics, String> {

    Optional<AggregatedMetrics> findByRegionCodeAndPeriodId(String regionCode, String periodId);

    void deleteByRegionCodeAndPeriodId(String regionCode, String periodId);
}
