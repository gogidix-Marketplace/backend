package com.gogidix.digitalmarketing.leadgeneration.domain.repository;

import com.gogidix.digitalmarketing.leadgeneration.domain.model.Lead;
import com.gogidix.digitalmarketing.shared.infrastructure.persistence.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.CountQuery;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Lead Repository - Data access for Lead entities
 *
 * <p>All queries automatically filter by current tenant via BaseRepository.</p>
 *
 * <p>Uses compound indexes for efficient queries:</p>
 * <ul>
 *   <li>lead_tenant_email_idx on (tenantId, email)</li>
 *   <li>lead_tenant_status_idx on (tenantId, status, createdAt)</li>
 *   <li>lead_tenant_source_idx on (tenantId, source, createdAt)</li>
 * </ul>
 */
@Repository
public interface LeadRepository extends BaseRepository<Lead> {

    // ========== Basic Queries ==========

    /**
     * Find leads by email.
     *
     * @param email the lead email
     * @return list of leads with matching email
     */
    List<Lead> findByEmail(String email);

    /**
     * Find lead by email for a specific tenant.
     *
     * @param tenantId the tenant ID
     * @param email the lead email
     * @return optional lead
     */
    List<Lead> findByTenantIdAndEmail(String tenantId, String email);

    /**
     * Find leads by status.
     *
     * @param status the lead status
     * @return list of leads
     */
    List<Lead> findByStatus(String status);

    /**
     * Find leads by status with pagination.
     *
     * @param status the lead status
     * @param pageable pagination parameters
     * @return page of leads
     */
    Page<Lead> findByStatus(String status, Pageable pageable);

    /**
     * Find leads by source.
     *
     * @param source the lead source
     * @return list of leads
     */
    List<Lead> findBySource(String source);

    List<Lead> findByTenantIdAndSource(String tenantId, String source);

    /**
     * Find leads by source with pagination.
     *
     * @param source the lead source
     * @param pageable pagination parameters
     * @return page of leads
     */
    Page<Lead> findBySource(String source, Pageable pageable);

    /**
     * Find leads by country.
     *
     * @param country the country code
     * @return list of leads
     */
    List<Lead> findByCountry(String country);

    /**
     * Find leads by company.
     *
     * @param company the company name
     * @return list of leads
     */
    List<Lead> findByCompany(String company);

    /**
     * Find leads by assigned sales representative.
     *
     * @param assignedTo the sales rep ID
     * @return list of leads
     */
    List<Lead> findByAssignedTo(String assignedTo);

    /**
     * Find leads by assigned sales representative with pagination.
     *
     * @param assignedTo the sales rep ID
     * @param pageable pagination parameters
     * @return page of leads
     */
    Page<Lead> findByAssignedTo(String assignedTo, Pageable pageable);

    /**
     * Find unassigned leads.
     *
     * @return list of leads without assigned rep
     */
    @Query("{ 'assignedTo': { $exists: false } }")
    List<Lead> findUnassigned();

    /**
     * Find leads by temperature.
     *
     * @param temperature the lead temperature
     * @return list of leads
     */
    List<Lead> findByTemperature(String temperature);

    /**
     * Find hot leads.
     *
     * @return list of hot leads
     */

    /**
     * Find qualified leads (score >= threshold).
     *
     * @param score the minimum score
     * @return list of qualified leads
     */
    List<Lead> findByScoreGreaterThanEqual(Integer score);

    /**
     * Find leads with score above threshold.
     *
     * @param threshold the score threshold
     * @return list of leads
     */
    @Query("{ 'score': { $gte: ?0 } }")
    List<Lead> findQualifiedLeads(Integer threshold);

    /**
     * Find leads by campaign ID.
     *
     * @param campaignId the campaign ID
     * @return list of leads
     */
    List<Lead> findByCampaignId(String campaignId);

    /**
     * Find leads by campaign ID with pagination.
     *
     * @param campaignId the campaign ID
     * @param pageable pagination parameters
     * @return page of leads
     */
    Page<Lead> findByCampaignId(String campaignId, Pageable pageable);

    /**
     * Find leads by status and source.
     *
     * @param status the lead status
     * @param source the lead source
     * @return list of leads
     */
    List<Lead> findByStatusAndSource(String status, String source);

    /**
     * Find new leads (status = NEW).
     *
     * @return list of new leads
     */
    @Query("{ 'status': 'NEW' }")
    List<Lead> findNewLeads();

    /**
     * Find qualified leads (status = QUALIFIED).
     *
     * @return list of qualified leads
     */
    @Query("{ 'status': 'QUALIFIED' }")
    List<Lead> findQualifiedLeads();

    /**
     * Find converted leads (status = CONVERTED).
     *
     * @return list of converted leads
     */
    @Query("{ 'status': 'CONVERTED' }")
    List<Lead> findConvertedLeads();

    /**
     * Find lost leads (status = LOST).
     *
     * @return list of lost leads
     */
    @Query("{ 'status': 'LOST' }")
    List<Lead> findLostLeads();

    /**
     * Find leads that need attention (NEW, CONTACTED with high score).
     *
     * @return list of leads needing attention
     */
    @Query("{ $or: [ " +
            "{ 'status': 'NEW' }, " +
            "{ 'status': 'CONTACTED', 'score': { $gte: 60 } } " +
            "] }")
    List<Lead> findLeadsNeedingAttention();

    /**
     * Find leads by opt-out status.
     *
     * @param optOut the opt-out status
     * @return list of leads
     */
    List<Lead> findByOptOut(Boolean optOut);

    /**
     * Find leads who granted consent.
     *
     * @return list of leads with consent
     */
    List<Lead> findByConsentGrantedTrue();

    /**
     * Find leads by industry.
     *
     * @param industry the industry
     * @return list of leads
     */
    List<Lead> findByIndustry(String industry);

    /**
     * Find leads by job title.
     *
     * @param jobTitle the job title
     * @return list of leads
     */
    List<Lead> findByJobTitle(String jobTitle);

    /**
     * Find leads by company size.
     *
     * @param companySize the company size
     * @return list of leads
     */
    List<Lead> findByCompanySize(String companySize);

    /**
     * Find leads created after date.
     *
     * @param date the creation date threshold
     * @return list of leads
     */
    @Query("{ 'createdAt': { $gte: ?0 } }")
    List<Lead> findByCreatedAtAfter(Instant date);

    /**
     * Find leads created before date.
     *
     * @param date the creation date threshold
     * @return list of leads
     */
    @Query("{ 'createdAt': { $lte: ?0 } }")
    List<Lead> findByCreatedAtBefore(Instant date);

    /**
     * Find leads created between dates.
     *
     * @param startDate start date
     * @param endDate end date
     * @return list of leads
     */
    @Query("{ 'createdAt': { $gte: ?0, $lte: ?1 } }")
    List<Lead> findByCreatedAtBetween(Instant startDate, Instant endDate);

    /**
     * Find leads with last activity after date.
     *
     * @param date the activity date threshold
     * @return list of leads
     */
    @Query("{ 'lastActivityAt': { $gte: ?0 } }")
    List<Lead> findByLastActivityAfter(Instant date);

    /**
     * Find inactive leads (no activity since date).
     *
     * @param date the inactivity threshold
     * @return list of inactive leads
     */
    @Query("{ 'lastActivityAt': { $lt: ?0 } }")
    List<Lead> findInactiveLeads(Instant date);

    /**
     * Find leads by assigned sales rep and status.
     *
     * @param assignedTo the sales rep ID
     * @param status the lead status
     * @return list of leads
     */
    List<Lead> findByAssignedToAndStatus(String assignedTo, String status);

    /**
     * Find leads by assigned sales rep and status with pagination.
     *
     * @param assignedTo the sales rep ID
     * @param status the lead status
     * @param pageable pagination parameters
     * @return page of leads
     */
    Page<Lead> findByAssignedToAndStatus(String assignedTo, String status, Pageable pageable);

    /**
     * Find leads by multiple statuses.
     *
     * @param statuses list of statuses
     * @return list of leads
     */
    @Query("{ 'status': { $in: ?0 } }")
    List<Lead> findByStatusIn(List<String> statuses);

    /**
     * Find leads by multiple sources.
     *
     * @param sources list of sources
     * @return list of leads
     */
    @Query("{ 'source': { $in: ?0 } }")
    List<Lead> findBySourceIn(List<String> sources);

    /**
     * Search leads by name or email.
     *
     * @param searchTerm the search term
     * @return list of matching leads
     */
    @Query("{ $or: [ " +
            "{ 'firstName': { $regex: ?0, $options: 'i' } }, " +
            "{ 'lastName': { $regex: ?0, $options: 'i' } }, " +
            "{ 'email': { $regex: ?0, $options: 'i' } }, " +
            "{ 'company': { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<Lead> search(String searchTerm);

    /**
     * Find leads by referral.
     *
     * @param referredBy the referring lead ID
     * @return list of referred leads
     */
    List<Lead> findByReferredBy(String referredBy);

    /**
     * Find leads referred by a specific lead.
     *
     * @param referredBy the referring lead ID
     * @return list of leads
     */
    @Query("{ 'referredBy': ?0 }")
    List<Lead> findReferrals(String referredBy);

    /**
     * Count leads by status.
     *
     * @param status the lead status
     * @return count of leads
     */
    @CountQuery("{ 'status': ?0 }")
    long countByStatus(String status);

    /**
     * Count leads by source.
     *
     * @param source the lead source
     * @return count of leads
     */
    @CountQuery("{ 'source': ?0 }")
    long countBySource(String source);

    /**
     * Count leads by campaign.
     *
     * @param campaignId the campaign ID
     * @return count of leads
     */
    @CountQuery("{ 'campaignId': ?0 }")
    long countByCampaignId(String campaignId);

    /**
     * Count leads by assigned sales rep.
     *
     * @param assignedTo the sales rep ID
     * @return count of leads
     */
    @CountQuery("{ 'assignedTo': ?0 }")
    long countByAssignedTo(String assignedTo);

    /**
     * Count qualified leads.
     *
     * @return count of qualified leads
     */
    @CountQuery("{ 'score': { $gte: 60 } }")
    long countQualifiedLeads();

    /**
     * Count converted leads.
     *
     * @return count of converted leads
     */
    @CountQuery("{ 'status': 'CONVERTED' }")
    long countConvertedLeads();

    /**
     * Find all unique countries.
     *
     * @return list of unique country codes
     */
    @Query("{ 'country': { $exists: true, $ne: null } }")
    List<String> findDistinctCountries();

    /**
     * Find all unique industries.
     *
     * @return list of unique industries
     */
    @Query("{ 'industry': { $exists: true, $ne: null } }")
    List<String> findDistinctIndustries();

    /**
     * Find all unique sources.
     *
     * @return list of unique sources
     */
    @Query("{ 'source': { $exists: true, $ne: null } }")
    List<String> findDistinctSources();

    /**
     * Find leads by engagement count range.
     *
     * @param minCount minimum engagement count
     * @return list of engaged leads
     */
    @Query("{ 'engagementCount': { $gte: ?0 } }")
    List<Lead> findEngagedLeads(Integer minCount);

    /**
     * Find leads by score range.
     *
     * @param minScore minimum score
     * @param maxScore maximum score
     * @return list of leads in score range
     */
    @Query("{ 'score': { $gte: ?0, $lte: ?1 } }")
    List<Lead> findByScoreBetween(Integer minScore, Integer maxScore);

    /**
     * Find leads with high engagement score.
     *
     * @param threshold the engagement score threshold
     * @return list of highly engaged leads
     */
    @Query("{ 'lastEngagementScore': { $gte: ?0 } }")
    List<Lead> findByHighEngagementScore(Double threshold);

    /**
     * Find leads by region.
     *
     * @param region the region code
     * @return list of leads
     */
    List<Lead> findByRegion(String region);

    /**
     * Find leads by country and region.
     *
     * @param country the country code
     * @param region the region code
     * @return list of leads
     */
    List<Lead> findByCountryAndRegion(String country, String region);

    /**
     * Find leads by status and country.
     *
     * @param status the lead status
     * @param country the country code
     * @return list of leads
     */
    List<Lead> findByStatusAndCountry(String status, String country);

    /**
     * Find leads by status and assigned sales rep with pagination.
     *
     * @param status the lead status
     * @param assignedTo the sales rep ID
     * @param pageable pagination parameters
     * @return page of leads
     */
    Page<Lead> findByStatusAndAssignedTo(String status, String assignedTo, Pageable pageable);

    /**
     * Find leads assigned to sales rep created after date.
     *
     * @param assignedTo the sales rep ID
     * @param date the creation date threshold
     * @return list of leads
     */
    @Query("{ 'assignedTo': ?0, 'createdAt': { $gte: ?1 } }")
    List<Lead> findByAssignedToAndCreatedAtAfter(String assignedTo, Instant date);

    /**
     * Find leads by budget range.
     *
     * @param budget the budget category
     * @return list of leads
     */
    List<Lead> findByBudget(String budget);

    /**
     * Find leads by timeline.
     *
     * @param timeline the timeline category
     * @return list of leads
     */
    List<Lead> findByTimeline(String timeline);

    /**
     * Find leads by budget and timeline.
     *
     * @param budget the budget category
     * @param timeline the timeline category
     * @return list of leads
     */
    List<Lead> findByBudgetAndTimeline(String budget, String timeline);
}
