package com.gogidix.digitalmarketing.leadgeneration.domain.repository;

import com.gogidix.digitalmarketing.leadgeneration.domain.model.LeadActivity;
import com.gogidix.digitalmarketing.shared.infrastructure.persistence.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.CountQuery;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * LeadActivity Repository - Data access for LeadActivity entities
 *
 * <p>All queries automatically filter by current tenant via BaseRepository.</p>
 *
 * <p>Uses compound indexes for efficient queries:</p>
 * <ul>
 *   <li>activity_tenant_lead_idx on (tenantId, leadId, timestamp)</li>
 *   <li>activity_tenant_type_idx on (tenantId, type, timestamp)</li>
 *   <li>activity_tenant_user_idx on (tenantId, performedBy, timestamp)</li>
 * </ul>
 */
@Repository
public interface LeadActivityRepository extends BaseRepository<LeadActivity> {

    /**
     * Find activities by lead ID.
     *
     * @param leadId the lead ID
     * @return list of activities
     */
    @Query("{ 'leadId': ?0 }")
    List<LeadActivity> findByLeadId(String leadId);

    /**
     * Find activities by lead ID with pagination.
     *
     * @param leadId the lead ID
     * @param pageable pagination parameters
     * @return page of activities
     */
    @Query("{ 'leadId': ?0 }")
    Page<LeadActivity> findByLeadId(String leadId, Pageable pageable);

    /**
     * Find activities by lead ID sorted by timestamp (newest first).
     *
     * @param leadId the lead ID
     * @param pageable pagination parameters
     * @return page of activities sorted newest first
     */
    @Query(value = "{ 'leadId': ?0 }", sort = "{ 'timestamp': -1 }")
    Page<LeadActivity> findByLeadIdOrderByTimestampDesc(String leadId, Pageable pageable);

    /**
     * Find activities by type.
     *
     * @param type the activity type
     * @return list of activities
     */
    List<LeadActivity> findByType(String type);

    /**
     * Find activities by type with pagination.
     *
     * @param type the activity type
     * @param pageable pagination parameters
     * @return page of activities
     */
    Page<LeadActivity> findByType(String type, Pageable pageable);

    /**
     * Find activities by performed by user.
     *
     * @param performedBy the user ID
     * @return list of activities
     */
    List<LeadActivity> findByPerformedBy(String performedBy);

    /**
     * Find activities by performed by user with pagination.
     *
     * @param performedBy the user ID
     * @param pageable pagination parameters
     * @return page of activities
     */
    Page<LeadActivity> findByPerformedBy(String performedBy, Pageable pageable);

    /**
     * Find activities by status.
     *
     * @param status the activity status
     * @return list of activities
     */
    List<LeadActivity> findByStatus(String status);

    /**
     * Find pending activities.
     *
     * @return list of pending activities
     */
    @Query("{ 'status': 'PENDING' }")
    List<LeadActivity> findPendingActivities();

    /**
     * Find completed activities.
     *
     * @return list of completed activities
     */
    @Query("{ 'status': 'COMPLETED' }")
    List<LeadActivity> findCompletedActivities();

    /**
     * Find failed activities.
     *
     * @return list of failed activities
     */
    @Query("{ 'status': 'FAILED' }")
    List<LeadActivity> findFailedActivities();

    /**
     * Find activities by channel.
     *
     * @param channel the activity channel
     * @return list of activities
     */
    List<LeadActivity> findByChannel(String channel);

    /**
     * Find activities by direction.
     *
     * @param direction the activity direction
     * @return list of activities
     */
    List<LeadActivity> findByDirection(String direction);

    /**
     * Find inbound activities.
     *
     * @return list of inbound activities
     */
    @Query("{ 'direction': 'INBOUND' }")
    List<LeadActivity> findInboundActivities();

    /**
     * Find outbound activities.
     *
     * @return list of outbound activities
     */
    @Query("{ 'direction': 'OUTBOUND' }")
    List<LeadActivity> findOutboundActivities();

    /**
     * Find automated activities.
     *
     * @return list of automated activities
     */
    List<LeadActivity> findByAutomatedTrue();

    /**
     * Find manual activities.
     *
     * @return list of manual activities
     */
    List<LeadActivity> findByAutomatedFalse();

    /**
     * Find activities by campaign ID.
     *
     * @param campaignId the campaign ID
     * @return list of activities
     */
    List<LeadActivity> findByCampaignId(String campaignId);

    /**
     * Find activities by lead ID and type.
     *
     * @param leadId the lead ID
     * @param type the activity type
     * @return list of activities
     */
    @Query("{ 'leadId': ?0, 'type': ?1 }")
    List<LeadActivity> findByLeadIdAndType(String leadId, String type);

    /**
     * Find activities by lead ID and status.
     *
     * @param leadId the lead ID
     * @param status the activity status
     * @return list of activities
     */
    @Query("{ 'leadId': ?0, 'status': ?1 }")
    List<LeadActivity> findByLeadIdAndStatus(String leadId, String status);

    /**
     * Find activities by lead ID and performed by.
     *
     * @param leadId the lead ID
     * @param performedBy the user ID
     * @return list of activities
     */
    @Query("{ 'leadId': ?0, 'performedBy': ?1 }")
    List<LeadActivity> findByLeadIdAndPerformedBy(String leadId, String performedBy);

    /**
     * Find activities between timestamps.
     *
     * @param startDate start timestamp
     * @param endDate end timestamp
     * @return list of activities
     */
    @Query("{ 'timestamp': { $gte: ?0, $lte: ?1 } }")
    List<LeadActivity> findByTimestampBetween(Instant startDate, Instant endDate);

    /**
     * Find activities after timestamp.
     *
     * @param timestamp the timestamp threshold
     * @return list of activities
     */
    @Query("{ 'timestamp': { $gte: ?0 } }")
    List<LeadActivity> findByTimestampAfter(Instant timestamp);

    /**
     * Find activities before timestamp.
     *
     * @param timestamp the timestamp threshold
     * @return list of activities
     */
    @Query("{ 'timestamp': { $lte: ?0 } }")
    List<LeadActivity> findByTimestampBefore(Instant timestamp);

    /**
     * Find recent activities for a lead.
     *
     * @param leadId the lead ID
     * @param limit the maximum number of activities
     * @return list of recent activities
     */
    @Query(value = "{ 'leadId': ?0 }", sort = "{ 'timestamp': -1 }")
    List<LeadActivity> findRecentActivitiesByLeadId(String leadId, Pageable pageable);

    /**
     * Find activities by multiple types.
     *
     * @param types list of activity types
     * @return list of activities
     */
    @Query("{ 'type': { $in: ?0 } }")
    List<LeadActivity> findByTypeIn(List<String> types);

    /**
     * Find activities by priority.
     *
     * @param priority the priority level
     * @return list of activities
     */
    List<LeadActivity> findByPriority(String priority);

    /**
     * Find high priority activities.
     *
     * @return list of high priority activities
     */
    @Query("{ 'priority': 'HIGH' }")
    List<LeadActivity> findHighPriorityActivities();

    /**
     * Find activities by sentiment.
     *
     * @param sentiment the sentiment value
     * @return list of activities
     */
    List<LeadActivity> findBySentiment(String sentiment);

    /**
     * Find activities with positive sentiment.
     *
     * @return list of positive sentiment activities
     */
    @Query("{ 'sentiment': 'POSITIVE' }")
    List<LeadActivity> findPositiveSentimentActivities();

    /**
     * Find activities with negative sentiment.
     *
     * @return list of negative sentiment activities
     */
    @Query("{ 'sentiment': 'NEGATIVE' }")
    List<LeadActivity> findNegativeSentimentActivities();

    /**
     * Find activities with next action scheduled.
     *
     * @return list of activities with next action
     */
    @Query("{ 'nextActionDate': { $exists: true, $ne: null } }")
    List<LeadActivity> findActivitiesWithNextAction();

    /**
     * Find activities with next action due before date.
     *
     * @param date the due date
     * @return list of activities due
     */
    @Query("{ 'nextActionDate': { $lte: ?0 } }")
    List<LeadActivity> findActivitiesWithNextActionDueBefore(Instant date);

    /**
     * Count activities by lead ID.
     *
     * @param leadId the lead ID
     * @return count of activities
     */
    @CountQuery("{ 'leadId': ?0 }")
    long countByLeadId(String leadId);

    /**
     * Count activities by type.
     *
     * @param type the activity type
     * @return count of activities
     */
    @CountQuery("{ 'type': ?0 }")
    long countByType(String type);

    /**
     * Count activities by performed by user.
     *
     * @param performedBy the user ID
     * @return count of activities
     */
    @CountQuery("{ 'performedBy': ?0 }")
    long countByPerformedBy(String performedBy);

    /**
     * Count activities by status.
     *
     * @param status the activity status
     * @return count of activities
     */
    @CountQuery("{ 'status': ?0 }")
    long countByStatus(String status);

    /**
     * Count pending activities.
     *
     * @return count of pending activities
     */
    @CountQuery("{ 'status': 'PENDING' }")
    long countPendingActivities();

    /**
     * Find all unique activity types.
     *
     * @return list of unique types
     */
    @Query("{ 'type': { $exists: true } }")
    List<String> findDistinctTypes();

    /**
     * Find all unique channels.
     *
     * @return list of unique channels
     */
    @Query("{ 'channel': { $exists: true } }")
    List<String> findDistinctChannels();

    /**
     * Find activities with engagement score greater than.
     *
     * @param score the minimum score
     * @return list of activities
     */
    @Query("{ 'engagementScore': { $gte: ?0 } }")
    List<LeadActivity> findByEngagementScoreGreaterThanEqual(Integer score);

    /**
     * Find activities by previous activity ID.
     *
     * @param previousActivityId the previous activity ID
     * @return list of follow-up activities
     */
    List<LeadActivity> findByPreviousActivityId(String previousActivityId);

    /**
     * Find activities by scheduled activity ID.
     *
     * @param scheduledActivityId the scheduled activity ID
     * @return list of activities
     */
    List<LeadActivity> findByScheduledActivityId(String scheduledActivityId);

    /**
     * Find activities by tag.
     *
     * @param tag the tag to search for
     * @return list of activities with the tag
     */
    @Query("{ 'tags': { $in: [?0] } }")
    List<LeadActivity> findByTag(String tag);

    /**
     * Find activities by multiple tags.
     *
     * @param tags list of tags
     * @return list of activities with any of the tags
     */
    @Query("{ 'tags': { $in: ?0 } }")
    List<LeadActivity> findByTagsIn(List<String> tags);

    /**
     * Find email activities (EMAIL_SENT, EMAIL_OPENED, EMAIL_CLICKED).
     *
     * @return list of email activities
     */
    @Query("{ 'type': { $in: ['EMAIL_SENT', 'EMAIL_OPENED', 'EMAIL_CLICKED', 'EMAIL_BOUNCED'] } }")
    List<LeadActivity> findEmailActivities();

    /**
     * Find call activities (CALL_ATTEMPTED, CALL_COMPLETED).
     *
     * @return list of call activities
     */
    @Query("{ 'type': { $in: ['CALL_ATTEMPTED', 'CALL_COMPLETED'] } }")
    List<LeadActivity> findCallActivities();

    /**
     * Find meeting activities (MEETING_SCHEDULED, MEETING_COMPLETED).
     *
     * @return list of meeting activities
     */
    @Query("{ 'type': { $in: ['MEETING_SCHEDULED', 'MEETING_COMPLETED'] } }")
    List<LeadActivity> findMeetingActivities();

    /**
     * Find web activities (WEBSITE_VISIT, FORM_SUBMITTED, CONTENT_DOWNLOADED).
     *
     * @return list of web activities
     */
    @Query("{ 'type': { $in: ['WEBSITE_VISIT', 'FORM_SUBMITTED', 'CONTENT_DOWNLOADED', 'PAGE_VIEW'] } }")
    List<LeadActivity> findWebActivities();

    /**
     * Find social engagement activities.
     *
     * @return list of social activities
     */
    @Query("{ 'type': { $in: ['SOCIAL_ENGAGEMENT', 'SOCIAL_LIKE', 'SOCIAL_SHARE', 'SOCIAL_COMMENT'] } }")
    List<LeadActivity> findSocialActivities();

    /**
     * Find activities by duration range.
     *
     * @param minDuration minimum duration in seconds
     * @param maxDuration maximum duration in seconds
     * @return list of activities
     */
    @Query("{ 'duration': { $gte: ?0, $lte: ?1 } }")
    List<LeadActivity> findByDurationBetween(Long minDuration, Long maxDuration);

    /**
     * Find activities with outcome.
     *
     * @return list of activities with outcome set
     */
    @Query("{ 'outcome': { $exists: true, $ne: null } }")
    List<LeadActivity> findActivitiesWithOutcome();

    /**
     * Find activities by outcome.
     *
     * @param outcome the outcome value
     * @return list of activities
     */
    List<LeadActivity> findByOutcome(String outcome);

    /**
     * Find overdue activities (pending with nextActionDate in the past).
     *
     * @return list of overdue activities
     */
    @Query("{ 'status': 'PENDING', 'nextActionDate': { $lt: ?0 } }")
    List<LeadActivity> findOverdueActivities(Instant now);

    /**
     * Find activities by content ID.
     *
     * @param contentId the content ID
     * @return list of activities
     */
    List<LeadActivity> findByContentId(String contentId);

    /**
     * Find all activities for multiple leads.
     *
     * @param leadIds list of lead IDs
     * @return list of activities
     */
    @Query("{ 'leadId': { $in: ?0 } }")
    List<LeadActivity> findByLeadIdIn(List<String> leadIds);

    /**
     * Find latest activity for each lead type.
     *
     * @param pageable pagination parameters
     * @return page of latest activities grouped by lead
     */
    @Query(value = "{}", sort = "{ 'timestamp': -1 }")
    Page<LeadActivity> findLatestActivities(Pageable pageable);

    /**
     * Count activities between timestamps.
     *
     * @param startDate start timestamp
     * @param endDate end timestamp
     * @return count of activities
     */
    @CountQuery("{ 'timestamp': { $gte: ?0, $lte: ?1 } }")
    long countByTimestampBetween(Instant startDate, Instant endDate);
}
