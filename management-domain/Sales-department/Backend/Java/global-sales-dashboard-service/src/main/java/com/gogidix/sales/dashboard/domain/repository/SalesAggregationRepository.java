package com.gogidix.sales.dashboard.domain.repository;

import com.gogidix.sales.dashboard.domain.model.SalesAggregation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Sales Aggregation Repository Interface (Port)
 * Defines the contract for aggregation persistence operations
 */
public interface SalesAggregationRepository {

    SalesAggregation save(SalesAggregation aggregation);

    List<SalesAggregation> saveAll(List<SalesAggregation> aggregations);

    Optional<SalesAggregation> findById(String id);

    Optional<SalesAggregation> findByAggregationIdAndTenantId(String aggregationId, String tenantId);

    List<SalesAggregation> findByTenantId(String tenantId);

    List<SalesAggregation> findByTenantIdAndAggregationType(String tenantId,
                                                               SalesAggregation.AggregationType type);

    List<SalesAggregation> findByTenantIdAndDimension(String tenantId,
                                                        SalesAggregation.AggregationDimension dimension);

    List<SalesAggregation> findByTenantIdAndDimensionAndValue(String tenantId,
                                                                 SalesAggregation.AggregationDimension dimension,
                                                                 String dimensionValue);

    List<SalesAggregation> findByTenantIdAndTimePeriod(String tenantId,
                                                          SalesAggregation.TimePeriod timePeriod);

    List<SalesAggregation> findByTenantIdAndAggregationTypeAndTimePeriod(
            String tenantId,
            SalesAggregation.AggregationType type,
            SalesAggregation.TimePeriod timePeriod);

    List<SalesAggregation> findByTenantIdAndDateRange(String tenantId,
                                                         LocalDate startDate,
                                                         LocalDate endDate);

    List<SalesAggregation> findLatestByTenantIdAndType(String tenantId,
                                                         SalesAggregation.AggregationType type,
                                                         int limit);

    List<SalesAggregation> findByTenantIdAndIsComplete(String tenantId, Boolean isComplete);

    boolean existsByAggregationIdAndTenantId(String aggregationId, String tenantId);

    void deleteById(String id);

    void deleteByAggregationIdAndTenantId(String aggregationId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    void deleteOldAggregations(String tenantId, LocalDate beforeDate);

    long countByTenantId(String tenantId);

    long countByTenantIdAndAggregationType(String tenantId, SalesAggregation.AggregationType type);
}
