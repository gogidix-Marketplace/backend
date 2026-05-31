package com.gogidix.digitalmarketing.emailmarketing.domain.repository;

import com.gogidix.digitalmarketing.emailmarketing.domain.model.EmailCampaign;
import com.gogidix.digitalmarketing.shared.infrastructure.persistence.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.CountQuery;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * EmailCampaign Repository - Data access for Email Campaign entities
 *
 * <p>All queries automatically filter by current tenant via BaseRepository.</p>
 */
@Repository
public interface EmailCampaignRepository extends BaseRepository<EmailCampaign> {

    // Basic Queries

    /**
     * Find campaigns by name.
     */
    List<EmailCampaign> findByName(String name);

    /**
     * Find campaigns by name with pagination.
     */
    Page<EmailCampaign> findByName(String name, Pageable pageable);

    /**
     * Find campaigns by status.
     */
    List<EmailCampaign> findByStatus(String status);

    /**
     * Find campaigns by status with pagination.
     */
    Page<EmailCampaign> findByStatus(String status, Pageable pageable);

    /**
     * Find campaigns by campaign type.
     */
    List<EmailCampaign> findByCampaignType(String campaignType);

    /**
     * Find campaigns by template ID.
     */
    List<EmailCampaign> findByTemplateId(String templateId);

    /**
     * Find campaigns by list ID.
     */
    List<EmailCampaign> findByListId(String listId);

    /**
     * Find campaigns by owner ID.
     */
    List<EmailCampaign> findByOwnerId(String ownerId);

    /**
     * Find campaigns by owner with pagination.
     */
    Page<EmailCampaign> findByOwnerId(String ownerId, Pageable pageable);

    // Date Range Queries

    /**
     * Find campaigns scheduled between dates.
     */
    @Query("{ 'scheduledAt': { $gte: ?0, $lte: ?1 } }")
    List<EmailCampaign> findByScheduledAtBetween(Instant start, Instant end);

    /**
     * Find campaigns started between dates.
     */
    @Query("{ 'startedAt': { $gte: ?0, $lte: ?1 } }")
    List<EmailCampaign> findByStartedAtBetween(Instant start, Instant end);

    /**
     * Find campaigns completed between dates.
     */
    @Query("{ 'completedAt': { $gte: ?0, $lte: ?1 } }")
    List<EmailCampaign> findByCompletedAtBetween(Instant start, Instant end);

    /**
     * Find campaigns within period range.
     */
    @Query("{ 'startDate': { $lte: ?0 }, 'endDate': { $gte: ?0 } }")
    List<EmailCampaign> findByActivePeriod(Instant date);

    // Status Queries

    /**
     * Find active campaigns (SENDING or SCHEDULED).
     */
    @Query("{ 'status': { $in: ['SENDING', 'SCHEDULED'] } }")
    List<EmailCampaign> findActiveCampaigns();

    /**
     * Find draft campaigns.
     */
    List<EmailCampaign> findByStatusOrderByCreatedAtDesc(String status);

    /**
     * Find completed campaigns.
     */
    @Query("{ 'status': { $in: ['SENT', 'CANCELLED', 'FAILED'] } }")
    List<EmailCampaign> findCompletedCampaigns();

    /**
     * Find campaigns that are overdue.
     */
    @Query("{ 'status': 'SCHEDULED', 'scheduledAt': { $lt: ?0 } }")
    List<EmailCampaign> findOverdueCampaigns(Instant now);

    /**
     * Find campaigns ready to send.
     */
    @Query("{ 'status': 'SCHEDULED', 'scheduledAt': { $lte: ?0 } }")
    List<EmailCampaign> findReadyToSend(Instant now);

    // Template and List Queries

    /**
     * Find campaigns by template and status.
     */
    List<EmailCampaign> findByTemplateIdAndStatus(String templateId, String status);

    /**
     * Find campaigns by list and status.
     */
    List<EmailCampaign> findByListIdAndStatus(String listId, String status);

    /**
     * Find campaigns by template with pagination.
     */
    Page<EmailCampaign> findByTemplateId(String templateId, Pageable pageable);

    /**
     * Find campaigns by list with pagination.
     */
    Page<EmailCampaign> findByListId(String listId, Pageable pageable);

    // Tag Queries

    /**
     * Find campaigns by tag.
     */
    @Query("{ 'tags': { $in: [?0] } }")
    List<EmailCampaign> findByTag(String tag);

    /**
     * Find campaigns by multiple tags.
     */
    @Query("{ 'tags': { $in: ?0 } }")
    List<EmailCampaign> findByTagsIn(List<String> tags);

    /**
     * Find campaigns with all specified tags.
     */
    @Query("{ 'tags': { $all: ?0 } }")
    List<EmailCampaign> findByTagsAll(List<String> tags);

    // Search Queries

    /**
     * Search campaigns by name or subject.
     */
    @Query("{ $or: [ " +
            "{ 'name': { $regex: ?0, $options: 'i' } }, " +
            "{ 'subject': { $regex: ?0, $options: 'i' } }, " +
            "{ 'description': { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<EmailCampaign> search(String searchTerm);

    // Count Queries

    /**
     * Count campaigns by status.
     */
    @CountQuery("{ 'status': ?0 }")
    long countByStatus(String status);

    /**
     * Count campaigns by type.
     */
    @CountQuery("{ 'campaignType': ?0 }")
    long countByCampaignType(String campaignType);

    /**
     * Count active campaigns.
     */
    @CountQuery("{ 'status': { $in: ['SENDING', 'SCHEDULED'] } }")
    long countActiveCampaigns();

    /**
     * Count completed campaigns.
     */
    @CountQuery("{ 'status': { $in: ['SENT', 'CANCELLED', 'FAILED'] } }")
    long countCompletedCampaigns();

    // Visibility Queries

    /**
     * Find visible campaigns.
     */
    @Query("{ 'visible': true }")
    List<EmailCampaign> findVisibleCampaigns();

    /**
     * Find visible campaigns with pagination.
     */
    Page<EmailCampaign> findByVisibleTrue(Pageable pageable);

    // Priority Queries

    /**
     * Find campaigns by minimum priority.
     */
    @Query("{ 'priority': { $gte: ?0 } }")
    List<EmailCampaign> findByPriorityGreaterThanEqual(Integer minPriority);

    /**
     * Find campaigns ordered by priority.
     */
    List<EmailCampaign> findByStatusOrderByPriorityDescScheduledAtAsc(String status);

    // Related Campaigns

    /**
     * Find campaigns by parent campaign ID.
     */
    List<EmailCampaign> findByParentCampaignId(String parentCampaignId);

    /**
     * Find campaigns by variant name.
     */
    List<EmailCampaign> findByVariantName(String variantName);

    // Team Queries

    /**
     * Find campaigns by team ID.
     */
    List<EmailCampaign> findByTeamId(String teamId);

    /**
     * Find campaigns by team with pagination.
     */
    Page<EmailCampaign> findByTeamId(String teamId, Pageable pageable);

    // Date Queries

    /**
     * Find campaigns created between dates.
     */
    @Query("{ 'createdAt': { $gte: ?0, $lte: ?1 } }")
    List<EmailCampaign> findByCreatedAtBetween(Instant start, Instant end);

    /**
     * Find campaigns updated between dates.
     */
    @Query("{ 'updatedAt': { $gte: ?0, $lte: ?1 } }")
    List<EmailCampaign> findByUpdatedAtBetween(Instant start, Instant end);

    // Latest Campaigns

    /**
     * Find latest campaigns by status.
     */
    Page<EmailCampaign> findByStatusOrderByScheduledAtDesc(String status, Pageable pageable);

    /**
     * Find recent campaigns.
     */
    Page<EmailCampaign> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // Combined Queries

    /**
     * Find campaigns by status and type.
     */
    List<EmailCampaign> findByStatusAndCampaignType(String status, String campaignType);

    /**
     * Find campaigns by status and owner.
     */
    List<EmailCampaign> findByStatusAndOwnerId(String status, String ownerId);

    /**
     * Find campaigns by type with pagination.
     */
    Page<EmailCampaign> findByCampaignType(String campaignType, Pageable pageable);
}
