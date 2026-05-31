package com.gogidix.digitalmarketing.leadgeneration.domain.repository;

import com.gogidix.digitalmarketing.leadgeneration.domain.model.LeadSource;
import com.gogidix.digitalmarketing.shared.infrastructure.persistence.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.CountQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * LeadSource Repository - Data access for LeadSource entities
 *
 * <p>All queries automatically filter by current tenant via BaseRepository.</p>
 *
 * <p>Uses compound indexes for efficient queries:</p>
 * <ul>
 *   <li>source_tenant_type_idx on (tenantId, type)</li>
 *   <li>source_tenant_active_idx on (tenantId, active)</li>
 * </ul>
 */
@Repository
public interface LeadSourceRepository extends BaseRepository<LeadSource> {

    /**
     * Find sources by name.
     *
     * @param name the source name
     * @return list of sources
     */
    List<LeadSource> findByName(String name);

    /**
     * Find sources by type.
     *
     * @param type the source type
     * @return list of sources
     */
    List<LeadSource> findByType(String type);

    /**
     * Find sources by type with pagination.
     *
     * @param type the source type
     * @param pageable pagination parameters
     * @return page of sources
     */
    Page<LeadSource> findByType(String type, Pageable pageable);

    /**
     * Find active sources.
     *
     * @return list of active sources
     */
    List<LeadSource> findByActiveTrue();

    /**
     * Find inactive sources.
     *
     * @return list of inactive sources
     */
    List<LeadSource> findByActiveFalse();

    /**
     * Find sources by default campaign ID.
     *
     * @param campaignId the campaign ID
     * @return list of sources
     */
    List<LeadSource> findByDefaultCampaignId(String campaignId);

    /**
     * Find sources by default sales representative.
     *
     * @param salesRepId the sales rep ID
     * @return list of sources
     */
    List<LeadSource> findByDefaultSalesRepId(String salesRepId);

    /**
     * Find sources by assignment priority.
     *
     * @param priority the priority level
     * @return list of sources
     */
    List<LeadSource> findByAssignmentPriority(Integer priority);

    /**
     * Find sources with priority greater than or equal to.
     *
     * @param priority the minimum priority
     * @return list of sources
     */
    @Query("{ 'assignmentPriority': { $gte: ?0 } }")
    List<LeadSource> findByAssignmentPriorityGreaterThanEqual(Integer priority);

    /**
     * Find sources sorted by priority (descending).
     *
     * @return list of sources sorted by priority
     */
    @Query("{ 'active': true }")
    List<LeadSource> findActiveSourcesSortedByPriority();

    /**
     * Find sources by type and active status.
     *
     * @param type the source type
     * @param active the active status
     * @return list of sources
     */
    List<LeadSource> findByTypeAndActive(String type, boolean active);

    /**
     * Find sources with auto-qualification enabled.
     *
     * @return list of sources with auto-qualification
     */
    List<LeadSource> findByAutoQualifyTrue();

    /**
     * Find sources by cost per lead range.
     *
     * @param minCost minimum cost
     * @param maxCost maximum cost
     * @return list of sources
     */
    @Query("{ 'costPerLead': { $gte: ?0, $lte: ?1 } }")
    List<LeadSource> findByCostPerLeadBetween(Double minCost, Double maxCost);

    /**
     * Search sources by name or description.
     *
     * @param searchTerm the search term
     * @return list of matching sources
     */
    @Query("{ $or: [ " +
            "{ 'name': { $regex: ?0, $options: 'i' } }, " +
            "{ 'description': { $regex: ?0, $options: 'i' } }, " +
            "{ 'type': { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<LeadSource> search(String searchTerm);

    /**
     * Find sources with monthly budget.
     *
     * @return list of sources with budget set
     */
    @Query("{ 'monthlyBudget': { $exists: true, $ne: null } }")
    List<LeadSource> findSourcesWithBudget();

    /**
     * Find sources without budget.
     *
     * @return list of sources without budget
     */
    @Query("{ 'monthlyBudget': { $exists: false } }")
    List<LeadSource> findSourcesWithoutBudget();

    /**
     * Find sources by qualification threshold.
     *
     * @param threshold the threshold value
     * @return list of sources
     */
    List<LeadSource> findByQualificationThreshold(Integer threshold);

    /**
     * Find sources with qualification threshold greater than.
     *
     * @param threshold the threshold value
     * @return list of sources
     */
    @Query("{ 'qualificationThreshold': { $gt: ?0 } }")
    List<LeadSource> findByQualificationThresholdGreaterThan(Integer threshold);

    /**
     * Count active sources.
     *
     * @return count of active sources
     */
    @CountQuery("{ 'active': true }")
    long countActiveSources();

    /**
     * Count sources by type.
     *
     * @param type the source type
     * @return count of sources
     */
    @CountQuery("{ 'type': ?0 }")
    long countByType(String type);

    /**
     * Find all unique source types.
     *
     * @return list of unique types
     */
    @Query("{ 'type': { $exists: true } }")
    List<String> findDistinctTypes();

    /**
     * Find sources by multiple types.
     *
     * @param types list of types
     * @return list of sources
     */
    @Query("{ 'type': { $in: ?0 } }")
    List<LeadSource> findByTypeIn(List<String> types);

    /**
     * Find sources with API key configured.
     *
     * @return list of sources with API key
     */
    @Query("{ 'apiKey': { $exists: true, $ne: null } }")
    List<LeadSource> findSourcesWithApiKey();

    /**
     * Find sources with webhook configured.
     *
     * @return list of sources with webhook
     */
    @Query("{ 'webhookUrl': { $exists: true, $ne: null } }")
    List<LeadSource> findSourcesWithWebhook();

    /**
     * Find sources by endpoint URL.
     *
     * @param endpointUrl the endpoint URL
     * @return list of sources
     */
    List<LeadSource> findByEndpointUrl(String endpointUrl);

    /**
     * Find sources with total leads greater than.
     *
     * @param count the minimum lead count
     * @return list of sources
     */
    @Query("{ 'totalLeads': { $gt: ?0 } }")
    List<LeadSource> findTopSourcesByLeadCount(Long count);

    /**
     * Find sources sorted by total leads (descending).
     *
     * @param pageable pagination parameters
     * @return page of sources sorted by lead count
     */
    Page<LeadSource> findAllByOrderByTotalLeadsDesc(Pageable pageable);

    /**
     * Find sources sorted by qualified leads (descending).
     *
     * @param pageable pagination parameters
     * @return page of sources sorted by qualified leads
     */
    Page<LeadSource> findAllByOrderByQualifiedLeadsDesc(Pageable pageable);

    /**
     * Find sources sorted by converted leads (descending).
     *
     * @param pageable pagination parameters
     * @return page of sources sorted by converted leads
     */
    Page<LeadSource> findAllByOrderByConvertedLeadsDesc(Pageable pageable);

    /**
     * Find sources by currency.
     *
     * @param currency the currency code
     * @return list of sources
     */
    List<LeadSource> findByCurrency(String currency);

    /**
     * Find sources with expected conversion rate.
     *
     * @return list of sources with conversion rate set
     */
    @Query("{ 'expectedConversionRate': { $exists: true, $ne: null } }")
    List<LeadSource> findSourcesWithConversionRate();

    /**
     * Find sources with expected conversion rate greater than.
     *
     * @param rate the minimum rate
     * @return list of sources
     */
    @Query("{ 'expectedConversionRate': { $gte: ?0 } }")
    List<LeadSource> findByExpectedConversionRateGreaterThanEqual(Double rate);

    /**
     * Find sources that need attention (low performance).
     *
     * @return list of underperforming sources
     */
    @Query("{ $or: [ " +
            "{ 'totalLeads': 0 }, " +
            "{ 'convertedLeads': 0 } " +
            "] }")
    List<LeadSource> findUnderperformingSources();

    /**
     * Find top performing sources.
     *
     * @param minConversionRate minimum conversion rate
     * @return list of top sources
     */
    @Query("{ 'convertedLeads': { $gt: 0 }, 'totalLeads': { $gt: 10 } }")
    List<LeadSource> findTopPerformingSources();

    /**
     * Find sources by last lead date after.
     *
     * @param date the date threshold
     * @return list of sources with recent leads
     */
    @Query("{ 'lastLeadAt': { $gte: ?0 } }")
    List<LeadSource> findSourcesWithRecentLeads(java.time.Instant date);

    /**
     * Find inactive sources (no recent leads).
     *
     * @param date the inactivity threshold
     * @return list of inactive sources
     */
    @Query("{ $or: [ " +
            "{ 'lastLeadAt': { $lt: ?0 } }, " +
            "{ 'lastLeadAt': { $exists: false } } " +
            "] }")
    List<LeadSource> findInactiveSources(java.time.Instant date);

    /**
     * Count sources with leads.
     *
     * @return count of sources with leads
     */
    @CountQuery("{ 'totalLeads': { $gt: 0 } }")
    long countSourcesWithLeads();

    /**
     * Count sources with qualified leads.
     *
     * @return count of sources with qualified leads
     */
    @CountQuery("{ 'qualifiedLeads': { $gt: 0 } }")
    long countSourcesWithQualifiedLeads();

    /**
     * Count sources with converted leads.
     *
     * @return count of sources with converted leads
     */
    @CountQuery("{ 'convertedLeads': { $gt: 0 } }")
    long countSourcesWithConvertedLeads();
}
