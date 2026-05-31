package com.gogidix.marketing.campaign.domain.repository;

import com.gogidix.marketing.campaign.domain.model.Campaign;
import com.gogidix.marketing.campaign.shared.infrastructure.persistence.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.CountQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Campaign Repository - Data access for Campaign entities
 *
 * <p>All queries automatically filter by current tenant via BaseRepository.</p>
 *
 * <p>Uses compound indexes for efficient queries:</p>
 * <ul>
 *   <li>campaign_tenant_status_idx on (tenantId, status, startDate)</li>
 *   <li>campaign_tenant_type_idx on (tenantId, campaignType, status)</li>
 * </ul>
 */
@Repository
public interface CampaignRepository extends BaseRepository<Campaign> {

    // ========== Basic Queries ==========

    /**
     * Find campaigns by name.
     *
     * @param name the campaign name
     * @return list of campaigns
     */
    List<Campaign> findByName(String name);

    /**
     * Find campaigns by status.
     *
     * @param status the campaign status
     * @return list of campaigns
     */
    List<Campaign> findByStatus(String status);

    /**
     * Find campaigns by status with pagination.
     *
     * @param status   the campaign status
     * @param pageable pagination parameters
     * @return page of campaigns
     */
    Page<Campaign> findByStatus(String status, Pageable pageable);

    /**
     * Find campaigns by campaign type.
     *
     * @param campaignType the campaign type
     * @return list of campaigns
     */
    List<Campaign> findByCampaignType(String campaignType);

    /**
     * Find campaigns by campaign type and status.
     *
     * @param campaignType the campaign type
     * @param status       the campaign status
     * @return list of campaigns
     */
    List<Campaign> findByCampaignTypeAndStatus(String campaignType, String status);

    /**
     * Find campaigns by scope.
     *
     * @param scope the campaign scope (GLOBAL, REGIONAL, LOCAL)
     * @return list of campaigns
     */
    List<Campaign> findByScope(String scope);

    /**
     * Find active campaigns.
     *
     * @return list of active campaigns
     */
    List<Campaign> findByStatusAndActiveTrue(String status);

    // ========== Date Range Queries ==========

    /**
     * Find campaigns starting after date.
     *
     * @param date the date
     * @return list of campaigns
     */
    List<Campaign> findByStartDateAfter(Instant date);

    /**
     * Find campaigns starting before date.
     *
     * @param date the date
     * @return list of campaigns
     */
    List<Campaign> findByStartDateBefore(Instant date);

    /**
     * Find campaigns by date range.
     *
     * @param startDate start date
     * @param endDate   end date
     * @return list of campaigns
     */
    List<Campaign> findByStartDateBetween(Instant startDate, Instant endDate);

    /**
     * Find campaigns ending before date.
     *
     * @param date the date
     * @return list of campaigns
     */
    List<Campaign> findByEndDateBefore(Instant date);

    /**
     * Find campaigns ending after date.
     *
     * @param date the date
     * @return list of campaigns
     */
    List<Campaign> findByEndDateAfter(Instant date);

    /**
     * Find currently running campaigns.
     *
     * @param now current date
     * @return list of campaigns
     */
    @Query("{ 'startDate': { $lte: ?0 }, 'endDate': { $gte: ?0 }, 'status': 'ACTIVE' }")
    List<Campaign> findCurrentlyRunning(Instant now);

    // ========== Budget Queries ==========

    /**
     * Find campaigns with budget greater than.
     *
     * @param amount the amount
     * @return list of campaigns
     */
    List<Campaign> findByTotalBudgetGreaterThan(BigDecimal amount);

    /**
     * Find campaigns with budget between.
     *
     * @param min minimum amount
     * @param max maximum amount
     * @return list of campaigns
     */
    List<Campaign> findByTotalBudgetBetween(BigDecimal min, BigDecimal max);

    /**
     * Find campaigns that have exceeded budget.
     *
     * @return list of campaigns
     */
    @Query("{ $expr: { $gt: ['$spentAmount', '$totalBudget'] } }")
    List<Campaign> findOverBudget();

    // ========== Owner/Team Queries ==========

    /**
     * Find campaigns by owner.
     *
     * @param owner the owner ID
     * @return list of campaigns
     */
    List<Campaign> findByOwner(String owner);

    /**
     * Find campaigns where user is team member.
     *
     * @param userId the user ID
     * @return list of campaigns
     */
    @Query("{ 'teamMembers': { $in: [?0] } }")
    List<Campaign> findByTeamMember(String userId);

    /**
     * Find campaigns by owner or team member.
     *
     * @param userId the user ID
     * @return list of campaigns
     */
    @Query("{ $or: [ { 'owner': ?0 }, { 'teamMembers': { $in: [?0] } } ] }")
    List<Campaign> findByOwnerOrTeamMember(String userId);

    // ========== Template Queries ==========

    /**
     * Find template campaigns.
     *
     * @return list of template campaigns
     */
    List<Campaign> findByIsTemplateTrue();

    /**
     * Find campaigns created from template.
     *
     * @param templateId the template ID
     * @return list of campaigns
     */
    List<Campaign> findByTemplateId(String templateId);

    /**
     * Find sub-campaigns by parent.
     *
     * @param parentCampaignId the parent campaign ID
     * @return list of sub-campaigns
     */
    List<Campaign> findByParentCampaignId(String parentCampaignId);

    // ========== Target Audience Queries ==========

    /**
     * Find campaigns by target audience.
     *
     * @param targetAudienceId the target audience ID
     * @return list of campaigns
     */
    List<Campaign> findByTargetAudienceId(String targetAudienceId);

    // ========== Region/Country Queries ==========

    /**
     * Find campaigns targeting region.
     *
     * @param region the region
     * @return list of campaigns
     */
    @Query("{ 'regions': { $in: [?0] } }")
    List<Campaign> findByRegion(String region);

    /**
     * Find campaigns targeting country.
     *
     * @param country the country
     * @return list of campaigns
     */
    @Query("{ 'countries': { $in: [?0] } }")
    List<Campaign> findByCountry(String country);

    /**
     * Find campaigns by multiple regions.
     *
     * @param regions list of regions
     * @return list of campaigns
     */
    @Query("{ 'regions': { $in: ?0 } }")
    List<Campaign> findByRegionsIn(List<String> regions);

    // ========== Tag Queries ==========

    /**
     * Find campaigns by tag.
     *
     * @param tag the tag
     * @return list of campaigns
     */
    @Query("{ 'tags': { $in: [?0] } }")
    List<Campaign> findByTag(String tag);

    /**
     * Find campaigns by multiple tags.
     *
     * @param tags list of tags
     * @return list of campaigns
     */
    @Query("{ 'tags': { $all: ?0 } }")
    List<Campaign> findByTagsIn(List<String> tags);

    // ========== Priority Queries ==========

    /**
     * Find campaigns by priority.
     *
     * @param priority the priority level
     * @return list of campaigns
     */
    List<Campaign> findByPriority(String priority);

    /**
     * Find high priority campaigns.
     *
     * @return list of high priority campaigns
     */
    @Query("{ 'priority': { $in: ['HIGH', 'URGENT'] } }")
    List<Campaign> findHighPriority();

    // ========== Approval Queries ==========

    /**
     * Find campaigns by approval status.
     *
     * @param approvalStatus the approval status
     * @return list of campaigns
     */
    List<Campaign> findByApprovalStatus(String approvalStatus);

    /**
     * Find pending approval campaigns.
     *
     * @return list of pending campaigns
     */
    @Query("{ $or: [ { 'approvalStatus': 'PENDING' }, { 'approvalStatus': null } ] }")
    List<Campaign> findPendingApproval();

    // ========== Count Queries ==========

    /**
     * Count campaigns by status.
     *
     * @param status the campaign status
     * @return count of campaigns
     */
    @CountQuery("{ 'status': ?0 }")
    long countByStatus(String status);

    /**
     * Count campaigns by type.
     *
     * @param campaignType the campaign type
     * @return count of campaigns
     */
    @CountQuery("{ 'campaignType': ?0 }")
    long countByCampaignType(String campaignType);

    /**
     * Count active campaigns.
     *
     * @return count of active campaigns
     */
    @CountQuery("{ 'status': 'ACTIVE' }")
    long countActive();

    // ========== Search/Filter Queries ==========

    /**
     * Search campaigns by name or description.
     *
     * @param searchTerm the search term
     * @return list of matching campaigns
     */
    @Query("{ $or: [ " +
            "{ 'name': { $regex: ?0, $options: 'i' } }, " +
            "{ 'description': { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<Campaign> search(String searchTerm);

    /**
     * Find campaigns with specific channel.
     *
     * @param channel the channel
     * @return list of campaigns
     */
    @Query("{ 'channels': { $in: [?0] } }")
    List<Campaign> findByChannel(String channel);

    /**
     * Find campaigns by multiple channels.
     *
     * @param channels list of channels
     * @return list of campaigns
     */
    @Query("{ 'channels': { $all: ?0 } }")
    List<Campaign> findByChannels(List<String> channels);

    // ========== Complex Queries ==========

    /**
     * Find campaigns needing budget alert.
     *
     * @param threshold percentage threshold
     * @return list of campaigns
     */
    @Query("{ $expr: { " +
            "$and: [" +
            "{ $ne: ['$totalBudget', null] }, " +
            "{ $ne: ['$spentAmount', null] }, " +
            "{ $gte: [ " +
            "{ $multiply: [ { $divide: ['$spentAmount', '$totalBudget'] }, 100 ] }, " +
            "?0 " +
            "] } " +
            "] " +
            "} }")
    List<Campaign> findNeedingBudgetAlert(BigDecimal threshold);

    /**
     * Find campaigns ending soon.
     *
     * @param days number of days
     * @return list of campaigns
     */
    @Query("{ 'endDate': { $lte: ?0 }, 'status': { $in: ['ACTIVE', 'SCHEDULED'] } }")
    List<Campaign> findEndingSoon(Instant threshold);

    /**
     * Find campaigns starting soon.
     *
     * @param threshold the date threshold
     * @return list of campaigns
     */
    @Query("{ 'startDate': { $lte: ?0, $gte: ?1 }, 'status': 'SCHEDULED' }")
    List<Campaign> findStartingSoon(Instant upperThreshold, Instant lowerThreshold);

    /**
     * Find unique campaign types.
     *
     * @return list of unique campaign types
     */
    @Query("{ 'campaignType': { $exists: true } }")
    List<String> findDistinctCampaignTypes();

    /**
     * Find unique statuses.
     *
     * @return list of unique statuses
     */
    @Query("{ 'status': { $exists: true } }")
    List<String> findDistinctStatuses();

    /**
     * Find unique scopes.
     *
     * @return list of unique scopes
     */
    @Query("{ 'scope': { $exists: true } }")
    List<String> findDistinctScopes();

    /**
     * Find campaigns by tenant, type, and status.
     *
     * @param tenantId     the tenant ID
     * @param campaignType the campaign type
     * @param status       the status
     * @return list of campaigns
     */
    List<Campaign> findByTenantIdAndCampaignTypeAndStatus(String tenantId, String campaignType, String status);

    /**
     * Find campaign by tenant and name.
     *
     * @param tenantId the tenant ID
     * @param name     the campaign name
     * @return optional campaign
     */
    Optional<Campaign> findByTenantIdAndName(String tenantId, String name);

    // ========== Additional Methods Needed by QueryService ==========

    List<Campaign> findByTenantIdAndStatus(String tenantId, String status);
    List<Campaign> findByTenantIdAndCampaignType(String tenantId, String campaignType);
    List<Campaign> findByTenantIdAndScope(String tenantId, String scope);
    List<Campaign> findByCountries(String country);
    List<Campaign> findByRegions(String region);
    List<Campaign> searchByNameOrDescription(String searchTerm);
    List<Campaign> findActiveCampaignsInDateRange(Instant startDate, Instant endDate);
}
