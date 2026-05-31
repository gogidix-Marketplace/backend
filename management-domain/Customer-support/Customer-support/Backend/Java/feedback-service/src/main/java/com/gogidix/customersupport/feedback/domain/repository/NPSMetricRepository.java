package com.gogidix.customersupport.feedback.domain.repository;

import com.gogidix.customersupport.feedback.domain.model.NPSMetric;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository for NPS Metric aggregate
 */
@Repository
public interface NPSMetricRepository extends MongoRepository<NPSMetric, String> {

    Optional<NPSMetric> findByMetricId(String metricId);

    List<NPSMetric> findByTenantId(String tenantId);

    List<NPSMetric> findByTenantIdAndPeriodType(String tenantId, NPSMetric.PeriodType periodType);

    List<NPSMetric> findByTenantIdAndPeriodStartAndPeriodEnd(String tenantId, Instant periodStart, Instant periodEnd);

    List<NPSMetric> findByTenantIdAndCountryCode(String tenantId, String countryCode);

    List<NPSMetric> findByTenantIdAndAgentId(String tenantId, String agentId);

    List<NPSMetric> findByTenantIdAndTeamId(String tenantId, String teamId);

    Optional<NPSMetric> findFirstByTenantIdAndPeriodTypeOrderByPeriodEndDesc(String tenantId, NPSMetric.PeriodType periodType);

    List<NPSMetric> findByTenantIdAndPeriodStartGreaterThanEqualOrderByPeriodStartAsc(String tenantId, Instant startDate);

    void deleteByTenantIdAndMetricId(String tenantId, String metricId);
}
