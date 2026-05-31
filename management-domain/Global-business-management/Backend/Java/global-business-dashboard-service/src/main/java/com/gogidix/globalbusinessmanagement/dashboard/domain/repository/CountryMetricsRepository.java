package com.gogidix.globalbusinessmanagement.dashboard.domain.repository;

import com.gogidix.globalbusinessmanagement.dashboard.domain.model.CountryMetrics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CountryMetrics domain model.
 * Provides data access operations for country-specific business metrics.
 */
@Repository
public interface CountryMetricsRepository extends MongoRepository<CountryMetrics, String> {

    /**
     * Find country metrics by country code and period identifier.
     *
     * @param countryCode the country code (ISO 3166-1 alpha-2)
     * @param periodId    the period identifier
     * @return the country metrics
     */
    Optional<CountryMetrics> findByCountryCodeAndPeriodId(String countryCode, String periodId);

    /**
     * Find all country metrics by period identifier.
     *
     * @param periodId the period identifier
     * @return list of country metrics for the period
     */
    List<CountryMetrics> findByPeriodId(String periodId);

    /**
     * Find country metrics by region code and period identifier.
     *
     * @param regionCode the region code
     * @param periodId   the period identifier
     * @return list of country metrics for the region and period
     */
    List<CountryMetrics> findByRegionCodeAndPeriodId(String regionCode, String periodId);

    /**
     * Find country metrics by country code.
     *
     * @param countryCode the country code
     * @return list of country metrics ordered by end date
     */
    List<CountryMetrics> findByCountryCodeOrderByEndDateDesc(String countryCode);

    /**
     * Find country metrics by region code.
     *
     * @param regionCode the region code
     * @return list of country metrics for the region
     */
    List<CountryMetrics> findByRegionCodeOrderByEndDateDesc(String regionCode);

    /**
     * Find country metrics by country code and date range.
     *
     * @param countryCode the country code
     * @param startDate   the start date
     * @param endDate     the end date
     * @return list of country metrics within the date range
     */
    List<CountryMetrics> findByCountryCodeAndStartDateBetweenOrderByEndDateDesc(
        String countryCode, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find country metrics by status.
     *
     * @param status the metrics status
     * @return list of country metrics with the given status
     */
    List<CountryMetrics> findByStatus(CountryMetrics.MetricsStatus status);

    /**
     * Find country metrics by period and status.
     *
     * @param periodId the period identifier
     * @param status   the metrics status
     * @return list of country metrics
     */
    List<CountryMetrics> findByPeriodIdAndStatus(String periodId, CountryMetrics.MetricsStatus status);

    /**
     * Find top performing countries by revenue.
     *
     * @param periodId the period identifier
     * @param pageable the pagination information
     * @return page of country metrics ordered by revenue
     */
    Page<CountryMetrics> findByPeriodIdOrderByRevenueDesc(String periodId, Pageable pageable);

    /**
     * Find country metrics by growth rate threshold.
     *
     * @param minGrowthRate the minimum growth rate
     * @return list of country metrics with growth rate above threshold
     */
    List<CountryMetrics> findByGrowthRateGreaterThanEqualOrderByGrowthRateDesc(
        java.math.BigDecimal minGrowthRate);

    /**
     * Find country metrics by year-over-year growth threshold.
     *
     * @param minYoyGrowth the minimum year-over-year growth
     * @return list of country metrics with YoY growth above threshold
     */
    List<CountryMetrics> findByYearOverYearGrowthGreaterThanEqualOrderByYearOverYearGrowthDesc(
        java.math.BigDecimal minYoyGrowth);

    /**
     * Find country metrics within a date range with pagination.
     *
     * @param startDate the start date
     * @param endDate   the end date
     * @param pageable  the pagination information
     * @return page of country metrics within the date range
     */
    Page<CountryMetrics> findByStartDateBetweenOrderByEndDateDesc(
        LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Find country metrics by market share threshold.
     *
     * @param minMarketShare the minimum market share
     * @return list of country metrics with market share above threshold
     */
    List<CountryMetrics> findByMarketShareGreaterThanEqualOrderByMarketShareDesc(
        java.math.BigDecimal minMarketShare);

    /**
     * Find country metrics by profit margin threshold.
     *
     * @param minMargin the minimum profit margin
     * @return list of country metrics with profit margin above threshold
     */
    List<CountryMetrics> findByProfitMarginGreaterThanEqualOrderByProfitMarginDesc(
        java.math.BigDecimal minMargin);

    /**
     * Check if country metrics exist for country and period.
     *
     * @param countryCode the country code
     * @param periodId    the period identifier
     * @return true if metrics exist, false otherwise
     */
    boolean existsByCountryCodeAndPeriodId(String countryCode, String periodId);

    /**
     * Delete country metrics by country code and period.
     *
     * @param countryCode the country code
     * @param periodId    the period identifier
     */
    void deleteByCountryCodeAndPeriodId(String countryCode, String periodId);

    /**
     * Count country metrics by period.
     *
     * @param periodId the period identifier
     * @return count of metrics for the period
     */
    long countByPeriodId(String periodId);

    /**
     * Count country metrics by region.
     *
     * @param regionCode the region code
     * @return count of metrics for the region
     */
    long countByRegionCode(String regionCode);

    /**
     * Find country metrics with market penetration above threshold.
     *
     * @param minPenetration the minimum market penetration
     * @return list of country metrics
     */
    List<CountryMetrics> findByMarketPenetrationGreaterThanEqualOrderByMarketPenetrationDesc(
        java.math.BigDecimal minPenetration);

    /**
     * Find country metrics by base currency.
     *
     * @param currency the base currency code
     * @return list of country metrics using the specified currency
     */
    List<CountryMetrics> findByBaseCurrencyOrderByEndDateDesc(String currency);

    /**
     * Find countries with positive customer growth.
     *
     * @return list of country metrics with positive customer growth
     */
    @Query("{ 'newCustomers': { $gt: 0 } }")
    List<CountryMetrics> findWithPositiveCustomerGrowth();

    /**
     * Find countries with customer churn above threshold.
     *
     * @param maxChurned the maximum acceptable churned customers
     * @return list of country metrics with churn below threshold
     */
    List<CountryMetrics> findByChurnedCustomersLessThanOrderByChurnedCustomersAsc(Long maxChurned);

    /**
     * Find country metrics with LTV to CAC ratio above threshold.
     *
     * @param minRatio the minimum LTV/CAC ratio
     * @return list of country metrics (requires calculation in service layer)
     */
    List<CountryMetrics> findByCustomerLifetimeValueGreaterThanEqualAndCustomerAcquisitionCostGreaterThanEqual(
        java.math.BigDecimal minLtv, java.math.BigDecimal maxCac);

    /**
     * Find country metrics for a specific city.
     *
     * @param cityName the city name
     * @return list of country metrics containing the city
     */
    @Query("{ 'topCities.cityName': ?0 }")
    List<CountryMetrics> findByCityName(String cityName);

    /**
     * Get all unique country codes.
     *
     * @return list of distinct country codes
     */
    @Query("{ 'countryCode': { $exists: true } }")
    List<CountryMetrics> findAllDistinctCountryCodes();

    /**
     * Find country metrics with data quality score above threshold.
     *
     * @param minScore the minimum data quality score
     * @return list of country metrics with high data quality
     */
    @Query("{ 'dataQuality.overallScore': { $gte: ?0 } }")
    List<CountryMetrics> findByDataQualityScoreGreaterThanEqual(java.math.BigDecimal minScore);

    /**
     * Get country metrics trends across multiple periods.
     *
     * @param countryCode the country code
     * @param periodIds   list of period identifiers
     * @return list of country metrics for the periods
     */
    List<CountryMetrics> findByCountryCodeAndPeriodIdInOrderByPeriodIdAsc(
        String countryCode, List<String> periodIds);

    /**
     * Find country metrics with pagination.
     *
     * @param pageable the pagination information
     * @return page of country metrics
     */
    Page<CountryMetrics> findAllByOrderByEndDateDesc(Pageable pageable);

    /**
     * Find country metrics by competitive index threshold.
     *
     * @param minIndex the minimum competitive index
     * @return list of country metrics
     */
    List<CountryMetrics> findByCompetitiveIndexGreaterThanEqualOrderByCompetitiveIndexDesc(
        java.math.BigDecimal minIndex);
}
