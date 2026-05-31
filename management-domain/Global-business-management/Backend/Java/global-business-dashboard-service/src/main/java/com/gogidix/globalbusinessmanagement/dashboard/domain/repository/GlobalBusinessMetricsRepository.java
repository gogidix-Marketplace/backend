package com.gogidix.globalbusinessmanagement.dashboard.domain.repository;

import com.gogidix.globalbusinessmanagement.dashboard.domain.model.GlobalBusinessMetrics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for GlobalBusinessMetrics domain model.
 * Provides data access operations for global business metrics.
 */
@Repository
public interface GlobalBusinessMetricsRepository extends MongoRepository<GlobalBusinessMetrics, String> {

    /**
     * Find metrics by period identifier.
     *
     * @param periodId the period identifier
     * @return the global business metrics
     */
    Optional<GlobalBusinessMetrics> findByPeriodId(String periodId);

    /**
     * Find metrics by status.
     *
     * @param status the metrics status
     * @return list of metrics with the given status
     */
    List<GlobalBusinessMetrics> findByStatus(GlobalBusinessMetrics.MetricsStatus status);

    /**
     * Find metrics within a date range.
     *
     * @param startDate the start date
     * @param endDate   the end date
     * @return list of metrics within the date range
     */
    List<GlobalBusinessMetrics> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find metrics by period identifier and status.
     *
     * @param periodId the period identifier
     * @param status   the metrics status
     * @return the global business metrics
     */
    Optional<GlobalBusinessMetrics> findByPeriodIdAndStatus(String periodId, GlobalBusinessMetrics.MetricsStatus status);

    /**
     * Find the most recent published metrics.
     *
     * @return the most recent published metrics
     */
    @Query("{ 'status': 'PUBLISHED' }")
    List<GlobalBusinessMetrics> findMostRecentByStatusOrderByEndDateDesc(Pageable pageable);

    /**
     * Find metrics with total revenue greater than or equal to the specified amount.
     *
     * @param revenue the minimum revenue
     * @return list of metrics with revenue greater than or equal to the specified amount
     */
    List<GlobalBusinessMetrics> findByTotalRevenueGreaterThanEqualOrderByEndDateDesc(
        java.math.BigDecimal revenue);

    /**
     * Find metrics with profit margin greater than or equal to the specified percentage.
     *
     * @param margin the minimum profit margin percentage
     * @return list of metrics with profit margin greater than or equal to the specified percentage
     */
    List<GlobalBusinessMetrics> findByProfitMarginGreaterThanEqualOrderByEndDateDesc(
        java.math.BigDecimal margin);

    /**
     * Find metrics within a date range with pagination.
     *
     * @param startDate the start date
     * @param endDate   the end date
     * @param pageable  the pagination information
     * @return page of metrics within the date range
     */
    Page<GlobalBusinessMetrics> findByStartDateBetweenOrderByEndDateDesc(
        LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Find all metrics ordered by end date descending.
     *
     * @param pageable the pagination information
     * @return page of metrics ordered by end date
     */
    Page<GlobalBusinessMetrics> findAllByOrderByEndDateDesc(Pageable pageable);

    /**
     * Find published metrics by date range.
     *
     * @param startDate the start date
     * @param endDate   the end date
     * @return list of published metrics within the date range
     */
    List<GlobalBusinessMetrics> findByStatusAndStartDateBetweenOrderByEndDateDesc(
        GlobalBusinessMetrics.MetricsStatus status, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Check if metrics exist for a given period.
     *
     * @param periodId the period identifier
     * @return true if metrics exist, false otherwise
     */
    boolean existsByPeriodId(String periodId);

    /**
     * Delete metrics by period identifier.
     *
     * @param periodId the period identifier
     */
    void deleteByPeriodId(String periodId);

    /**
     * Count metrics by status.
     *
     * @param status the metrics status
     * @return count of metrics with the given status
     */
    long countByStatus(GlobalBusinessMetrics.MetricsStatus status);

    /**
     * Find metrics by data source.
     *
     * @param dataSource the data source
     * @return list of metrics from the specified data source
     */
    List<GlobalBusinessMetrics> findByDataSourceOrderByEndDateDesc(String dataSource);

    /**
     * Find metrics for a specific region by checking regional breakdown.
     *
     * @param regionCode the region code
     * @return list of metrics containing the specified region
     */
    @Query("{ 'regionalBreakdown.?0': { $exists: true } }")
    List<GlobalBusinessMetrics> findByRegionInRegionalBreakdown(String regionCode);

    /**
     * Find metrics with customer growth rate above threshold.
     *
     * @param threshold the growth rate threshold
     * @return list of metrics with customer growth rate above threshold
     */
    @Query("{ 'newCustomers': { $gt: 0 }, 'activeCustomers': { $gt: 0 } }")
    List<GlobalBusinessMetrics> findWithPositiveCustomerGrowth();

    /**
     * Get summary statistics for metrics in a date range.
     *
     * @param startDate the start date
     * @param endDate   the end date
     * @return aggregation of metrics statistics
     */
    @Query("{ 'startDate': { $gte: ?0, $lte: ?1 }, 'status': 'PUBLISHED' }")
    List<GlobalBusinessMetrics> findPublishedMetricsForStatistics(LocalDateTime startDate, LocalDateTime endDate);
}
