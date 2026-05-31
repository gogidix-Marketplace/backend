package com.gogidix.digitalmarketing.leadgeneration.domain.repository;

import com.gogidix.digitalmarketing.leadgeneration.domain.model.LeadQualification;
import com.gogidix.digitalmarketing.shared.infrastructure.persistence.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.CountQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * LeadQualification Repository - Data access for LeadQualification entities
 *
 * <p>All queries automatically filter by current tenant via BaseRepository.</p>
 *
 * <p>Uses compound indexes for efficient queries:</p>
 * <ul>
 *   <li>qual_tenant_active_idx on (tenantId, active)</li>
 *   <li>qual_tenant_priority_idx on (tenantId, priority)</li>
 * </ul>
 */
@Repository
public interface LeadQualificationRepository extends BaseRepository<LeadQualification> {

    /**
     * Find qualification rules by name.
     *
     * @param name the rule name
     * @return list of rules
     */
    List<LeadQualification> findByName(String name);

    /**
     * Find active qualification rules.
     *
     * @return list of active rules
     */
    List<LeadQualification> findByActiveTrue();

    /**
     * Find active qualification rules with pagination.
     *
     * @param pageable pagination parameters
     * @return page of active rules
     */
    Page<LeadQualification> findByActiveTrue(Pageable pageable);

    /**
     * Find inactive qualification rules.
     *
     * @return list of inactive rules
     */
    List<LeadQualification> findByActiveFalse();

    /**
     * Find qualification rules by priority.
     *
     * @param priority the priority level
     * @return list of rules
     */
    List<LeadQualification> findByPriority(Integer priority);

    /**
     * Find qualification rules by priority greater than or equal to.
     *
     * @param priority the minimum priority
     * @return list of rules
     */
    @Query("{ 'priority': { $gte: ?0 } }")
    List<LeadQualification> findByPriorityGreaterThanEqual(Integer priority);

    /**
     * Find active qualification rules sorted by priority (descending).
     *
     * @return list of active rules sorted by priority
     */
    @Query("{ 'active': true }")
    List<LeadQualification> findActiveRulesSortedByPriority();

    /**
     * Find qualification rules by score threshold.
     *
     * @param threshold the score threshold
     * @return list of rules
     */
    List<LeadQualification> findByScoreThreshold(Integer threshold);

    /**
     * Find qualification rules by score threshold greater than.
     *
     * @param threshold the threshold value
     * @return list of rules
     */
    @Query("{ 'scoreThreshold': { $gt: ?0 } }")
    List<LeadQualification> findByScoreThresholdGreaterThan(Integer threshold);

    /**
     * Find qualification rules by score threshold less than.
     *
     * @param threshold the threshold value
     * @return list of rules
     */
    @Query("{ 'scoreThreshold': { $lt: ?0 } }")
    List<LeadQualification> findByScoreThresholdLessThan(Integer threshold);

    /**
     * Find qualification rules with auto-assign enabled.
     *
     * @return list of rules with auto-assign
     */
    List<LeadQualification> findByAutoAssignOnQualifyTrue();

    /**
     * Find qualification rules by default sales representative.
     *
     * @param salesRepId the sales rep ID
     * @return list of rules
     */
    List<LeadQualification> findByDefaultSalesRepId(String salesRepId);

    /**
     * Search qualification rules by name or description.
     *
     * @param searchTerm the search term
     * @return list of matching rules
     */
    @Query("{ $or: [ " +
            "{ 'name': { $regex: ?0, $options: 'i' } }, " +
            "{ 'description': { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<LeadQualification> search(String searchTerm);

    /**
     * Find qualification rules updated after date.
     *
     * @param date the update date threshold
     * @return list of recently updated rules
     */
    @Query("{ 'rulesUpdatedAt': { $gte: ?0 } }")
    List<LeadQualification> findByRulesUpdatedAtAfter(java.time.Instant date);

    /**
     * Find qualification rules with specific qualification tags.
     *
     * @param tag the tag to search for
     * @return list of rules with the tag
     */
    @Query("{ 'qualificationTags': { $in: [?0] } }")
    List<LeadQualification> findByQualificationTag(String tag);

    /**
     * Find qualification rules with any of the tags.
     *
     * @param tags list of tags
     * @return list of rules with any of the tags
     */
    @Query("{ 'qualificationTags': { $in: ?0 } }")
    List<LeadQualification> findByQualificationTagsIn(List<String> tags);

    /**
     * Count active qualification rules.
     *
     * @return count of active rules
     */
    @CountQuery("{ 'active': true }")
    long countActiveRules();

    /**
     * Count qualification rules by priority.
     *
     * @param priority the priority level
     * @return count of rules
     */
    @CountQuery("{ 'priority': ?0 }")
    long countByPriority(Integer priority);

    /**
     * Find all unique qualification tags.
     *
     * @return list of unique tags
     */
    @Query("{ 'qualificationTags': { $exists: true } }")
    List<String> findDistinctQualificationTags();

    /**
     * Find rules with company size criteria configured.
     *
     * @return list of rules with company size criteria
     */
    @Query("{ 'companySizeCriteria': { $exists: true, $ne: {} } }")
    List<LeadQualification> findRulesWithCompanySizeCriteria();

    /**
     * Find rules with budget criteria configured.
     *
     * @return list of rules with budget criteria
     */
    @Query("{ 'budgetCriteria': { $exists: true, $ne: {} } }")
    List<LeadQualification> findRulesWithBudgetCriteria();

    /**
     * Find rules with timeline criteria configured.
     *
     * @return list of rules with timeline criteria
     */
    @Query("{ 'timelineCriteria': { $exists: true, $ne: {} } }")
    List<LeadQualification> findRulesWithTimelineCriteria();

    /**
     * Find rules with job title criteria configured.
     *
     * @return list of rules with job title criteria
     */
    @Query("{ 'jobTitleCriteria': { $exists: true, $ne: {} } }")
    List<LeadQualification> findRulesWithJobTitleCriteria();

    /**
     * Find rules with industry criteria configured.
     *
     * @return list of rules with industry criteria
     */
    @Query("{ 'industryCriteria': { $exists: true, $ne: {} } }")
    List<LeadQualification> findRulesWithIndustryCriteria();

    /**
     * Find rules with geographic criteria configured.
     *
     * @return list of rules with geographic criteria
     */
    @Query("{ 'geographicCriteria': { $exists: true, $ne: {} } }")
    List<LeadQualification> findRulesWithGeographicCriteria();

    /**
     * Find rules with source criteria configured.
     *
     * @return list of rules with source criteria
     */
    @Query("{ 'sourceCriteria': { $exists: true, $ne: {} } }")
    List<LeadQualification> findRulesWithSourceCriteria();

    /**
     * Find rules with engagement criteria configured.
     *
     * @return list of rules with engagement criteria
     */
    @Query("{ 'engagementCriteria': { $exists: true, $ne: {} } }")
    List<LeadQualification> findRulesWithEngagementCriteria();

    /**
     * Find rules with custom rules configured.
     *
     * @return list of rules with custom rules
     */
    @Query("{ 'customRules': { $exists: true, $ne: {} } }")
    List<LeadQualification> findRulesWithCustomRules();

    /**
     * Find rules with disqualification criteria configured.
     *
     * @return list of rules with disqualification criteria
     */
    @Query("{ 'disqualificationCriteria': { $exists: true, $ne: {} } }")
    List<LeadQualification> findRulesWithDisqualificationCriteria();

    /**
     * Find rules by scoring weights configuration.
     *
     * @return list of rules with scoring weights
     */
    @Query("{ 'scoringWeights': { $exists: true, $ne: {} } }")
    List<LeadQualification> findRulesWithScoringWeights();

    /**
     * Find active and auto-assign rules.
     *
     * @return list of rules that are active and auto-assign
     */
    @Query("{ 'active': true, 'autoAssignOnQualify': true }")
    List<LeadQualification> findActiveAutoAssignRules();

    /**
     * Find rules with notification settings.
     *
     * @return list of rules with notification settings
     */
    @Query("{ 'notificationSettings': { $exists: true, $ne: {} } }")
    List<LeadQualification> findRulesWithNotificationSettings();

    /**
     * Find rules with max score greater than.
     *
     * @param score the max score threshold
     * @return list of rules
     */
    @Query("{ 'maxScore': { $gt: ?0 } }")
    List<LeadQualification> findByMaxScoreGreaterThan(Integer score);

    /**
     * Find rules by default score.
     *
     * @param defaultScore the default score
     * @return list of rules
     */
    List<LeadQualification> findByDefaultScore(Integer defaultScore);

    /**
     * Find rules with default score greater than.
     *
     * @param score the minimum default score
     * @return list of rules
     */
    @Query("{ 'defaultScore': { $gte: ?0 } }")
    List<LeadQualification> findByDefaultScoreGreaterThanEqual(Integer score);

    /**
     * Find rules by priority range.
     *
     * @param minPriority minimum priority
     * @param maxPriority maximum priority
     * @return list of rules
     */
    @Query("{ 'priority': { $gte: ?0, $lte: ?1 } }")
    List<LeadQualification> findByPriorityBetween(Integer minPriority, Integer maxPriority);

    /**
     * Find rules with specific custom rule key.
     *
     * @param customRuleKey the custom rule key
     * @return list of rules
     */
    @Query("{ 'customRules.?0': { $exists: true } }")
    List<LeadQualification> findByCustomRuleKey(String customRuleKey);

    /**
     * Find active rules with priority above threshold.
     *
     * @param priorityThreshold the priority threshold
     * @return list of high priority active rules
     */
    @Query("{ 'active': true, 'priority': { $gte: ?0 } }")
    List<LeadQualification> findActiveRulesWithPriorityAbove(Integer priorityThreshold);

    /**
     * Find rules by name containing text.
     *
     * @param nameText the text to search for in name
     * @return list of matching rules
     */
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<LeadQualification> findByNameContaining(String nameText);

    /**
     * Find qualification rules for specific company size.
     *
     * @param companySize the company size category
     * @return list of rules that have this company size in criteria
     */
    @Query("{ 'companySizeCriteria.?0': { $exists: true } }")
    List<LeadQualification> findByCompanySizeInCriteria(String companySize);

    /**
     * Find qualification rules for specific industry.
     *
     * @param industry the industry
     * @return list of rules that have this industry in criteria
     */
    @Query("{ 'industryCriteria.?0': { $exists: true } }")
    List<LeadQualification> findByIndustryInCriteria(String industry);

    /**
     * Find qualification rules for specific job title.
     *
     * @param jobTitle the job title
     * @return list of rules that have this job title in criteria
     */
    @Query("{ 'jobTitleCriteria.?0': { $exists: true } }")
    List<LeadQualification> findByJobTitleInCriteria(String jobTitle);

    /**
     * Find qualification rules for specific source.
     *
     * @param source the lead source
     * @return list of rules that have this source in criteria
     */
    @Query("{ 'sourceCriteria.?0': { $exists: true } }")
    List<LeadQualification> findBySourceInCriteria(String source);

    /**
     * Find recently updated rules (within last N days).
     *
     * @param sinceDate the date threshold
     * @return list of recently updated rules
     */
    @Query("{ 'rulesUpdatedAt': { $gte: ?0 } }")
    List<LeadQualification> findRecentlyUpdatedRules(java.time.Instant sinceDate);

    /**
     * Count rules with auto-assign enabled.
     *
     * @return count of auto-assign rules
     */
    @CountQuery("{ 'autoAssignOnQualify': true }")
    long countAutoAssignRules();

    /**
     * Find rules sorted by priority descending.
     *
     * @param pageable pagination parameters
     * @return page of rules sorted by priority
     */
    Page<LeadQualification> findAllByOrderByPriorityDesc(Pageable pageable);

    /**
     * Find active rules sorted by priority descending.
     *
     * @param pageable pagination parameters
     * @return page of active rules sorted by priority
     */
    @Query(value = "{ 'active': true }", sort = "{ 'priority': -1 }")
    Page<LeadQualification> findActiveRulesByOrderByPriorityDesc(Pageable pageable);
}
