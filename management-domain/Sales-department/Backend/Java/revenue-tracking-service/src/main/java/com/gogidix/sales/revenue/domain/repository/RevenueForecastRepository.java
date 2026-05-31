package com.gogidix.sales.revenue.domain.repository;

import com.gogidix.sales.revenue.domain.model.RevenueForecast;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * Revenue Forecast Repository Interface (Port)
 * Defines the contract for revenue forecast persistence operations
 */
public interface RevenueForecastRepository {

    RevenueForecast save(RevenueForecast forecast);

    List<RevenueForecast> saveAll(List<RevenueForecast> forecasts);

    Optional<RevenueForecast> findById(String id);

    Optional<RevenueForecast> findByForecastIdAndTenantId(String forecastId, String tenantId);

    List<RevenueForecast> findByTenantId(String tenantId);

    List<RevenueForecast> findByTenantIdAndStatus(String tenantId, RevenueForecast.ForecastStatus status);

    List<RevenueForecast> findByTenantIdAndForecastType(String tenantId, RevenueForecast.ForecastType forecastType);

    List<RevenueForecast> findByTenantIdAndForecastPeriod(String tenantId, YearMonth forecastPeriod);

    List<RevenueForecast> findByTenantIdAndDepartment(String tenantId, String department);

    List<RevenueForecast> findByTenantIdAndTerritory(String tenantId, String territory);

    List<RevenueForecast> findLatestForecastsByTenantId(String tenantId, int limit);

    Optional<RevenueForecast> findLatestApprovedForecastByTenantId(String tenantId);

    List<RevenueForecast> findByTenantIdAndCreatedDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    boolean existsByForecastIdAndTenantId(String forecastId, String tenantId);

    void deleteById(String id);

    void deleteByForecastIdAndTenantId(String forecastId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, RevenueForecast.ForecastStatus status);
}
