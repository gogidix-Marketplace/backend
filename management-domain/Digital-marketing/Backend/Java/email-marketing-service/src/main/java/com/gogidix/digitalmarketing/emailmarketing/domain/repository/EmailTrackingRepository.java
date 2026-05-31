package com.gogidix.digitalmarketing.emailmarketing.domain.repository;

import com.gogidix.digitalmarketing.emailmarketing.domain.model.EmailMetrics;
import com.gogidix.digitalmarketing.shared.infrastructure.persistence.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.CountQuery;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * EmailMetrics Repository - Data access for Email Metrics entities
 *
 * <p>All queries automatically filter by current tenant via BaseRepository.</p>
 */
@Repository
public interface EmailTrackingRepository extends BaseRepository<EmailMetrics> {

    // Basic Queries

    /**
     * Find metrics by entity type and ID.
     */
    List<EmailMetrics> findByEntityTypeAndEntityId(String entityType, String entityId);

    /**
     * Find metrics by entity type.
     */
    List<EmailMetrics> findByEntityType(String entityType);

    /**
     * Find metrics by campaign ID.
     */
    List<EmailMetrics> findByCampaignId(String campaignId);

    /**
     * Find metrics by list ID.
     */
    List<EmailMetrics> findByListId(String listId);

    /**
     * Find metrics by template ID.
     */
    List<EmailMetrics> findByTemplateId(String templateId);

    // Date Queries

    /**
     * Find metrics by metric date.
     */
    List<EmailMetrics> findByMetricDate(Instant metricDate);

    /**
     * Find metrics between dates.
     */
    @Query("{ 'metricDate': { $gte: ?0, $lte: ?1 } }")
    List<EmailMetrics> findByMetricDateBetween(Instant start, Instant end);

    /**
     * Find metrics by campaign between dates.
     */
    @Query("{ 'campaignId': ?0, 'metricDate': { $gte: ?1, $lte: ?2 } }")
    List<EmailMetrics> findByCampaignIdAndMetricDateBetween(String campaignId, Instant start, Instant end);

    /**
     * Find latest metrics by campaign.
     */
    @Query("{ 'campaignId': ?0 }")
    List<EmailMetrics> findLatestByCampaignId(String campaignId);

    // Period Type Queries

    /**
     * Find metrics by period type.
     */
    List<EmailMetrics> findByPeriodType(String periodType);

    /**
     * Find lifetime metrics by campaign.
     */
    @Query("{ 'campaignId': ?0, 'periodType': 'LIFETIME' }")
    List<EmailMetrics> findLifetimeMetricsByCampaignId(String campaignId);

    /**
     * Find daily metrics by campaign.
     */
    @Query("{ 'campaignId': ?0, 'periodType': 'DAILY' }")
    List<EmailMetrics> findDailyMetricsByCampaignId(String campaignId);

    /**
     * Find hourly metrics by campaign.
     */
    @Query("{ 'campaignId': ?0, 'periodType': 'HOURLY' }")
    List<EmailMetrics> findHourlyMetricsByCampaignId(String campaignId);

    // Combined Entity Queries

    /**
     * Find metrics by entity type with pagination.
     */
    Page<EmailMetrics> findByEntityType(String entityType, Pageable pageable);

    /**
     * Find metrics by campaign with pagination.
     */
    Page<EmailMetrics> findByCampaignId(String campaignId, Pageable pageable);

    // Aggregation Queries

    /**
     * Aggregate metrics for all campaigns.
     */
    @Query("{ 'entityType': 'CAMPAIGN' }")
    List<EmailMetrics> findAllCampaignMetrics();

    /**
     * Aggregate metrics for all lists.
     */
    @Query("{ 'entityType': 'LIST' }")
    List<EmailMetrics> findAllListMetrics();

    /**
     * Aggregate metrics for all templates.
     */
    @Query("{ 'entityType': 'TEMPLATE' }")
    List<EmailMetrics> findAllTemplateMetrics();

    // Global Metrics

    /**
     * Find global metrics.
     */
    @Query("{ 'entityType': 'GLOBAL' }")
    List<EmailMetrics> findGlobalMetrics();

    /**
     * Find latest global metrics.
     */
    @Query("{ 'entityType': 'GLOBAL', 'periodType': 'LIFETIME' }")
    List<EmailMetrics> findLatestGlobalMetrics();

    // Performance Queries

    /**
     * Find metrics with minimum open rate.
     */
    @Query("{ 'openRate': { $gte: ?0 } }")
    List<EmailMetrics> findByMinOpenRate(Double minRate);

    /**
     * Find metrics with minimum click rate.
     */
    @Query("{ 'clickRate': { $gte: ?0 } }")
    List<EmailMetrics> findByMinClickRate(Double minRate);

    /**
     * Find top performing campaigns by open rate.
     */
    @Query("{ 'entityType': 'CAMPAIGN', 'sentCount': { $gt: 0 } }")
    List<EmailMetrics> findTopCampaignsByOpenRate(Pageable pageable);

    /**
     * Find top performing campaigns by click rate.
     */
    @Query("{ 'entityType': 'CAMPAIGN', 'sentCount': { $gt: 0 } }")
    List<EmailMetrics> findTopCampaignsByClickRate(Pageable pageable);

    // Count Queries

    /**
     * Count metrics by entity type.
     */
    @CountQuery("{ 'entityType': ?0 }")
    long countByEntityType(String entityType);

    /**
     * Count metrics by campaign.
     */
    @CountQuery("{ 'campaignId': ?0 }")
    long countByCampaignId(String campaignId);

    /**
     * Count metrics by list.
     */
    @CountQuery("{ 'listId': ?0 }")
    long countByListId(String listId);

    /**
     * Count metrics by template.
     */
    @CountQuery("{ 'templateId': ?0 }")
    long countByTemplateId(String templateId);

    /**
     * Count metrics by period type.
     */
    @CountQuery("{ 'periodType': ?0 }")
    long countByPeriodType(String periodType);

    // Recalculation Queries

    /**
     * Find metrics needing recalculation.
     */
    @Query("{ $or: [ " +
            "{ 'calculatedAt': { $lt: ?0 } }, " +
            "{ 'calculatedAt': { $exists: false } } " +
            "] }")
    List<EmailMetrics> findNeedingRecalculation(Instant threshold);

    /**
     * Find metrics by campaign needing recalculation.
     */
    @Query("{ 'campaignId': ?0, $or: [ " +
            "{ 'calculatedAt': { $lt: ?1 } }, " +
            "{ 'calculatedAt': { $exists: false } } " +
            "] }")
    List<EmailMetrics> findCampaignMetricsNeedingRecalculation(String campaignId, Instant threshold);

    // Latest Metrics

    /**
     * Find latest metrics.
     */
    Page<EmailMetrics> findAllByOrderByCalculatedAtDesc(Pageable pageable);

    /**
     * Find latest metrics by entity.
     */
    Page<EmailMetrics> findByEntityTypeAndEntityIdOrderByCalculatedAtDesc(
        String entityType, String entityId, Pageable pageable);

    // Health Score Queries

    /**
     * Find metrics by minimum health score.
     */
    @Query("{ 'sentCount': { $gt: 0 } }")
    List<EmailMetrics> findByMinHealthScore();

    /**
     * Find low performing campaigns.
     */
    @Query("{ 'entityType': 'CAMPAIGN', 'sentCount': { $gt: 100 }, 'openRate': { $lt: 20 } }")
    List<EmailMetrics> findLowPerformingCampaigns();

    // Device Metrics Queries

    /**
     * Find metrics with mobile opens.
     */
    @Query("{ 'mobileOpenCount': { $gt: 0 } }")
    List<EmailMetrics> findWithMobileOpens();

    /**
     * Find metrics with desktop opens.
     */
    @Query("{ 'desktopOpenCount': { $gt: 0 } }")
    List<EmailMetrics> findWithDesktopOpens();

    // Version Queries

    /**
     * Find metrics by version.
     */
    List<EmailMetrics> findByVersion(String version);

    // Batch Operations

    /**
     * Find metrics by campaign IDs.
     */
    @Query("{ 'campaignId': { $in: ?0 } }")
    List<EmailMetrics> findByCampaignIdIn(List<String> campaignIds);

    /**
     * Find metrics by list IDs.
     */
    @Query("{ 'listId': { $in: ?0 } }")
    List<EmailMetrics> findByListIdIn(List<String> listIds);

    /**
     * Find metrics by template IDs.
     */
    @Query("{ 'templateId': { $in: ?0 } }")
    List<EmailMetrics> findByTemplateIdIn(List<String> templateIds);

    // Existence Checks

    /**
     * Check if metrics exist for campaign.
     */
    @Query(value = "{ 'campaignId': ?0, 'periodType': 'LIFETIME' }", exists = true)
    boolean existsLifetimeMetricsForCampaign(String campaignId);

    /**
     * Check if metrics exist for entity.
     */
    @Query(value = "{ 'entityType': ?0, 'entityId': ?1 }", exists = true)
    boolean existsMetricsForEntity(String entityType, String entityId);

    // Time Series Queries

    /**
     * Find daily metrics for date range.
     */
    @Query("{ 'periodType': 'DAILY', 'metricDate': { $gte: ?0, $lte: ?1 } }")
    List<EmailMetrics> findDailyMetricsBetween(Instant start, Instant end);

    /**
     * Find hourly metrics for date range.
     */
    @Query("{ 'periodType': 'HOURLY', 'metricDate': { $gte: ?0, $lte: ?1 } }")
    List<EmailMetrics> findHourlyMetricsBetween(Instant start, Instant end);

    /**
     * Find weekly metrics for date range.
     */
    @Query("{ 'periodType': 'WEEKLY', 'metricDate': { $gte: ?0, $lte: ?1 } }")
    List<EmailMetrics> findWeeklyMetricsBetween(Instant start, Instant end);

    /**
     * Find monthly metrics for date range.
     */
    @Query("{ 'periodType': 'MONTHLY', 'metricDate': { $gte: ?0, $lte: ?1 } }")
    List<EmailMetrics> findMonthlyMetricsBetween(Instant start, Instant end);

    // Sent Count Queries

    /**
     * Find metrics with minimum sent count.
     */
    @Query("{ 'sentCount': { $gte: ?0 } }")
    List<EmailMetrics> findByMinSentCount(int minSent);

    /**
     * Find metrics with sent count in range.
     */
    @Query("{ 'sentCount': { $gte: ?0, $lte: ?1 } }")
    List<EmailMetrics> findBySentCountBetween(int min, int max);
}
