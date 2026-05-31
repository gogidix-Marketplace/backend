package com.gogidix.finance.cashflow.domain.repository;

import com.gogidix.finance.cashflow.domain.model.CashflowForecast;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Cashflow Forecast Repository Interface (Port)
 * Defines the contract for cashflow forecast persistence operations
 */
public interface CashflowForecastRepository {

    CashflowForecast save(CashflowForecast forecast);

    List<CashflowForecast> saveAll(List<CashflowForecast> forecasts);

    Optional<CashflowForecast> findById(String id);

    Optional<CashflowForecast> findByForecastIdAndTenantId(String forecastId, String tenantId);

    List<CashflowForecast> findByTenantId(String tenantId);

    List<CashflowForecast> findByTenantIdAndScenario(String tenantId, CashflowForecast.ForecastScenario scenario);

    List<CashflowForecast> findByTenantIdAndStatus(String tenantId, CashflowForecast.ForecastStatus status);

    List<CashflowForecast> findByTenantIdAndDateRange(
            String tenantId, LocalDate startDate, LocalDate endDate);

    List<CashflowForecast> findByTenantIdAndIsBaselineTrue(String tenantId);

    List<CashflowForecast> findByTenantIdAndParentForecastId(String tenantId, String parentForecastId);

    List<CashflowForecast> findByTenantIdOrderByVersionDesc(String tenantId);

    Optional<CashflowForecast> findLatestByTenantIdAndScenario(
            String tenantId, CashflowForecast.ForecastScenario scenario);

    List<CashflowForecast> findByTenantIdAndGeneratedBy(String tenantId, String generatedBy);

    List<CashflowForecast> findByTenantIdAndStartDateBeforeAndEndDateAfter(
            String tenantId, LocalDate startDate, LocalDate endDate);

    List<CashflowForecast> findByTenantIdAndScenarioIn(
            String tenantId, List<CashflowForecast.ForecastScenario> scenarios);

    List<CashflowForecast> findByTenantIdAndStatusIn(
            String tenantId, List<CashflowForecast.ForecastStatus> statuses);

    boolean existsByForecastIdAndTenantId(String forecastId, String tenantId);

    void deleteById(String id);

    void deleteByForecastIdAndTenantId(String forecastId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, CashflowForecast.ForecastStatus status);

    long countByTenantIdAndScenario(String tenantId, CashflowForecast.ForecastScenario scenario);

    List<CashflowForecast> findByTenantIdAndTagsContaining(String tenantId, String tag);

    List<CashflowForecast> findActiveForecastsByTenantId(String tenantId);
}
