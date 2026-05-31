package com.gogidix.sales.forecast.domain.repository;

import com.gogidix.sales.forecast.domain.model.Forecast;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * Forecast Repository Interface (Port)
 * Defines the contract for forecast persistence operations
 */
public interface ForecastRepository {

    Forecast save(Forecast forecast);

    List<Forecast> saveAll(List<Forecast> forecasts);

    Optional<Forecast> findById(String id);

    Optional<Forecast> findByForecastIdAndTenantId(String forecastId, String tenantId);

    List<Forecast> findByTenantId(String tenantId);

    List<Forecast> findByTenantIdAndStatus(String tenantId, Forecast.ForecastStatus status);

    List<Forecast> findByTenantIdAndPeriod(String tenantId, Forecast.ForecastPeriod period);

    List<Forecast> findByTenantIdAndStartDateBetween(String tenantId, YearMonth startDate, YearMonth endDate);

    List<Forecast> findByTenantIdAndRegion(String tenantId, String region);

    List<Forecast> findByTenantIdAndTerritory(String tenantId, String territory);

    List<Forecast> findByTenantIdAndBusinessUnit(String tenantId, String businessUnit);

    List<Forecast> findByTenantIdAndCreatedBy(String tenantId, String createdBy);

    List<Forecast> findByParentForecastIdAndTenantId(String parentForecastId, String tenantId);

    List<Forecast> findVersionsByForecastIdAndTenantId(String forecastId, String tenantId);

    List<Forecast> findPendingApprovalByTenantIdAndApproverLevel(String tenantId,
                                                                   Forecast.ApprovalLevel approverLevel);

    boolean existsByForecastIdAndTenantId(String forecastId, String tenantId);

    void deleteById(String id);

    void deleteByForecastIdAndTenantId(String forecastId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, Forecast.ForecastStatus status);

    List<Forecast> findActiveByTenantId(String tenantId);

    List<Forecast> findPublishedByTenantIdAndDateRange(String tenantId, YearMonth startDate, YearMonth endDate);

    Optional<Forecast> findLatestPublishedByTenantIdAndPeriod(String tenantId, Forecast.ForecastPeriod period);
}
