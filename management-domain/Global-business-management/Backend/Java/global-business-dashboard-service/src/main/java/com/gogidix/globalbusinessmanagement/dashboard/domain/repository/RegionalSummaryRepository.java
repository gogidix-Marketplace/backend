package com.gogidix.globalbusinessmanagement.dashboard.domain.repository;

import com.gogidix.globalbusinessmanagement.dashboard.domain.model.RegionalSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for RegionalSummary domain model.
 * Provides data access operations for regional business summaries.
 */
@Repository
public interface RegionalSummaryRepository extends MongoRepository<RegionalSummary, String> {

    /**
     * Find regional summary by region code and period identifier.
     *
     * @param regionCode the region code
     * @param periodId   the period identifier
     * @return the regional summary
     */
    Optional<RegionalSummary> findByRegionCodeAndPeriodId(String regionCode, String periodId);

    /**
     * Find all regional summaries by period identifier.
     *
     * @param periodId the period identifier
     * @return list of regional summaries for the period
     */
    List<RegionalSummary> findByPeriodId(String periodId);

    /**
     * Find regional summaries by region code.
     *
     * @param regionCode the region code
     * @return list of regional summaries for the region
     */
    List<RegionalSummary> findByRegionCodeOrderByEndDateDesc(String regionCode);

    /**
     * Find regional summaries by region code and date range.
     *
     * @param regionCode the region code
     * @param startDate  the start date
     * @param endDate    the end date
     * @return list of regional summaries within the date range
     */
    List<RegionalSummary> findByRegionCodeAndStartDateBetweenOrderByEndDateDesc(
        String regionCode, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find regional summaries by status.
     *
     * @param status the summary status
     * @return list of regional summaries with the given status
     */
    List<RegionalSummary> findByStatus(RegionalSummary.SummaryStatus status);

    /**
     * Find regional summaries by period and status.
     *
     * @param periodId the period identifier
     * @param status   the summary status
     * @return list of regional summaries
     */
    List<RegionalSummary> findByPeriodIdAndStatus(String periodId, RegionalSummary.SummaryStatus status);

    /**
     * Find top performing regions by revenue.
     *
     * @param periodId the period identifier
     * @param pageable the pagination information
     * @return page of regional summaries ordered by revenue
     */
    Page<RegionalSummary> findByPeriodIdOrderByRevenueDesc(String periodId, Pageable pageable);

    /**
     * Find regional summaries by growth rate threshold.
     *
     * @param minGrowthRate the minimum growth rate
     * @return list of regional summaries with growth rate above threshold
     */
    List<RegionalSummary> findByGrowthRateGreaterThanEqualOrderByGrowthRateDesc(
        java.math.BigDecimal minGrowthRate);

    /**
     * Find regional summaries within a date range with pagination.
     *
     * @param startDate the start date
     * @param endDate   the end date
     * @param pageable  the pagination information
     * @return page of regional summaries within the date range
     */
    Page<RegionalSummary> findByStartDateBetweenOrderByEndDateDesc(
        LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Find regional summaries by customer satisfaction score threshold.
     *
     * @param minScore the minimum satisfaction score
     * @return list of regional summaries with satisfaction score above threshold
     */
    List<RegionalSummary> findByCustomerSatisfactionScoreGreaterThanEqualOrderByCustomerSatisfactionScoreDesc(
        java.math.BigDecimal minScore);

    /**
     * Check if regional summary exists for region and period.
     *
     * @param regionCode the region code
     * @param periodId   the period identifier
     * @return true if summary exists, false otherwise
     */
    boolean existsByRegionCodeAndPeriodId(String regionCode, String periodId);

    /**
     * Delete regional summary by region code and period.
     *
     * @param regionCode the region code
     * @param periodId   the period identifier
     */
    void deleteByRegionCodeAndPeriodId(String regionCode, String periodId);

    /**
     * Count regional summaries by period.
     *
     * @param periodId the period identifier
     * @return count of summaries for the period
     */
    long countByPeriodId(String periodId);

    /**
     * Find all regions with market penetration above threshold.
     *
     * @param minPenetration the minimum market penetration
     * @return list of regional summaries
     */
    List<RegionalSummary> findByMarketPenetrationGreaterThanEqualOrderByMarketPenetrationDesc(
        java.math.BigDecimal minPenetration);

    /**
     * Find regional summaries for a specific country.
     *
     * @param countryCode the country code
     * @return list of regional summaries containing the country
     */
    @Query("{ 'countries.countryCode': ?0 }")
    List<RegionalSummary> findByCountryCode(String countryCode);

    /**
     * Get all unique region codes.
     *
     * @return list of distinct region codes
     */
    @Query("{ 'regionCode': { $exists: true } }")
    List<RegionalSummary> findAllDistinctRegionCodes();

    /**
     * Find regional summaries with positive customer growth.
     *
     * @return list of regional summaries with positive customer growth
     */
    @Query("{ 'newCustomers': { $gt: 0 } }")
    List<RegionalSummary> findWithPositiveCustomerGrowth();

    /**
     * Find regions with customer churn above threshold.
     *
     * @param maxChurnRate the maximum acceptable churn rate
     * @return list of regional summaries with churn below threshold
     */
    List<RegionalSummary> findByChurnedCustomersLessThanOrderByChurnedCustomersAsc(Long maxChurnRate);

    /**
     * Find regional summaries by product performance.
     *
     * @param productCode the product code
     * @return list of regional summaries containing the product
     */
    @Query("{ 'topPerformingProducts.productCode': ?0 }")
    List<RegionalSummary> findByProductCode(String productCode);

    /**
     * Get regional growth trends for multiple periods.
     *
     * @param periodIds list of period identifiers
     * @return list of regional summaries for the periods
     */
    List<RegionalSummary> findByPeriodIdInOrderByPeriodIdAsc(List<String> periodIds);

    /**
     * Find regional summaries with pagination.
     *
     * @param pageable the pagination information
     * @return page of regional summaries
     */
    Page<RegionalSummary> findAllByOrderByEndDateDesc(Pageable pageable);
}
