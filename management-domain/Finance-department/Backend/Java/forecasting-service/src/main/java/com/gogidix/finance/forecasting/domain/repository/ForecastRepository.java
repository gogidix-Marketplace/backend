package com.gogidix.finance.forecasting.domain.repository;

import com.gogidix.finance.forecasting.domain.model.Forecast;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Forecast Repository Interface (Port)
 * Defines the contract for forecast persistence operations
 */
public interface ForecastRepository {

    /**
     * Save a forecast
     *
     * @param forecast the forecast to save
     * @return the saved forecast
     */
    Forecast save(Forecast forecast);

    /**
     * Save multiple forecasts
     *
     * @param forecasts the list of forecasts to save
     * @return the list of saved forecasts
     */
    List<Forecast> saveAll(List<Forecast> forecasts);

    /**
     * Find forecast by ID
     *
     * @param id the forecast ID
     * @return optional containing the forecast if found
     */
    Optional<Forecast> findById(String id);

    /**
     * Find forecast by forecast ID and tenant ID
     *
     * @param forecastId the forecast ID
     * @param tenantId the tenant ID
     * @return optional containing the forecast if found
     */
    Optional<Forecast> findByForecastIdAndTenantId(String forecastId, String tenantId);

    /**
     * Find all forecasts by tenant ID
     *
     * @param tenantId the tenant ID
     * @return list of forecasts
     */
    List<Forecast> findByTenantId(String tenantId);

    /**
     * Find forecasts by tenant ID and status
     *
     * @param tenantId the tenant ID
     * @param status the forecast status
     * @return list of forecasts
     */
    List<Forecast> findByTenantIdAndStatus(String tenantId, Forecast.ForecastStatus status);

    /**
     * Find forecasts by tenant ID and forecast type
     *
     * @param tenantId the tenant ID
     * @param forecastType the forecast type
     * @return list of forecasts
     */
    List<Forecast> findByTenantIdAndForecastType(String tenantId, Forecast.ForecastType forecastType);

    /**
     * Find forecasts by tenant ID and forecast horizon
     *
     * @param tenantId the tenant ID
     * @param forecastHorizon the forecast horizon
     * @return list of forecasts
     */
    List<Forecast> findByTenantIdAndForecastHorizon(String tenantId, Forecast.ForecastHorizon forecastHorizon);

    /**
     * Find forecasts by tenant ID and date range
     *
     * @param tenantId the tenant ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of forecasts
     */
    List<Forecast> findByTenantIdAndStartDateBetween(String tenantId, Instant startDate, Instant endDate);

    /**
     * Find forecasts by tenant ID, status, and forecast type
     *
     * @param tenantId the tenant ID
     * @param status the forecast status
     * @param forecastType the forecast type
     * @return list of forecasts
     */
    List<Forecast> findByTenantIdAndStatusAndForecastType(String tenantId, Forecast.ForecastStatus status,
                                                           Forecast.ForecastType forecastType);

    /**
     * Find forecasts by tenant ID and department
     *
     * @param tenantId the tenant ID
     * @param department the department
     * @return list of forecasts
     */
    List<Forecast> findByTenantIdAndDepartment(String tenantId, String department);

    /**
     * Find forecasts by tenant ID and scenario
     *
     * @param tenantId the tenant ID
     * @param scenario the scenario identifier
     * @return list of forecasts
     */
    List<Forecast> findByTenantIdAndScenario(String tenantId, String scenario);

    /**
     * Find forecasts by tenant ID and created by
     *
     * @param tenantId the tenant ID
     * @param createdBy the creator user ID
     * @return list of forecasts
     */
    List<Forecast> findByTenantIdAndCreatedBy(String tenantId, String createdBy);

    /**
     * Find pending approval forecasts by tenant ID
     *
     * @param tenantId the tenant ID
     * @return list of pending approval forecasts
     */
    List<Forecast> findPendingApprovalByTenantId(String tenantId);

    /**
     * Find pending approval forecasts by tenant ID and department
     *
     * @param tenantId the tenant ID
     * @param department the department
     * @return list of pending approval forecasts
     */
    List<Forecast> findPendingApprovalByTenantIdAndDepartment(String tenantId, String department);

    /**
     * Check if forecast exists by forecast ID and tenant ID
     *
     * @param forecastId the forecast ID
     * @param tenantId the tenant ID
     * @return true if forecast exists
     */
    boolean existsByForecastIdAndTenantId(String forecastId, String tenantId);

    /**
     * Delete forecast by ID
     *
     * @param id the forecast ID
     */
    void deleteById(String id);

    /**
     * Delete forecast by forecast ID and tenant ID
     *
     * @param forecastId the forecast ID
     * @param tenantId the tenant ID
     */
    void deleteByForecastIdAndTenantId(String forecastId, String tenantId);

    /**
     * Delete all forecasts by tenant ID
     *
     * @param tenantId the tenant ID
     */
    void deleteAllByTenantId(String tenantId);

    /**
     * Count forecasts by tenant ID
     *
     * @param tenantId the tenant ID
     * @return count of forecasts
     */
    long countByTenantId(String tenantId);

    /**
     * Count forecasts by tenant ID and status
     *
     * @param tenantId the tenant ID
     * @param status the forecast status
     * @return count of forecasts
     */
    long countByTenantIdAndStatus(String tenantId, Forecast.ForecastStatus status);

    /**
     * Sum of forecast amounts by tenant ID and status
     *
     * @param tenantId the tenant ID
     * @param status the forecast status
     * @return total amount
     */
    BigDecimal sumTotalForecastAmountByTenantIdAndStatus(String tenantId, Forecast.ForecastStatus status);

    /**
     * Find archived forecasts by tenant ID
     *
     * @param tenantId the tenant ID
     * @return list of archived forecasts
     */
    List<Forecast> findArchivedByTenantId(String tenantId);

    /**
     * Find latest forecasts by tenant ID
     *
     * @param tenantId the tenant ID
     * @param limit the maximum number of results
     * @return list of latest forecasts
     */
    List<Forecast> findLatestByTenantId(String tenantId, int limit);

    /**
     * Find forecasts by tenant ID with search
     *
     * @param tenantId the tenant ID
     * @param searchTerm the search term
     * @return list of matching forecasts
     */
    List<Forecast> searchByTenantId(String tenantId, String searchTerm);

    /**
     * Find forecasts by tenant ID, type, and date range
     *
     * @param tenantId the tenant ID
     * @param forecastType the forecast type
     * @param startDate the start date
     * @param endDate the end date
     * @return list of forecasts
     */
    List<Forecast> findByTenantIdAndForecastTypeAndStartDateBetween(
            String tenantId, Forecast.ForecastType forecastType, Instant startDate, Instant endDate);

    /**
     * Find forecasts by tenant ID and category
     *
     * @param tenantId the tenant ID
     * @param category the category
     * @return list of forecasts
     */
    List<Forecast> findByTenantIdAndCategory(String tenantId, String category);

    /**
     * Find forecasts by multiple forecast IDs
     *
     * @param tenantId the tenant ID
     * @param forecastIds the list of forecast IDs
     * @return list of forecasts
     */
    List<Forecast> findByTenantIdAndForecastIdIn(String tenantId, List<String> forecastIds);

    /**
     * Find forecasts by tenant ID and confidence level greater than
     *
     * @param tenantId the tenant ID
     * @param confidenceLevel the minimum confidence level
     * @return list of forecasts
     */
    List<Forecast> findByTenantIdAndConfidenceLevelGreaterThanEqual(String tenantId, Integer confidenceLevel);
}
