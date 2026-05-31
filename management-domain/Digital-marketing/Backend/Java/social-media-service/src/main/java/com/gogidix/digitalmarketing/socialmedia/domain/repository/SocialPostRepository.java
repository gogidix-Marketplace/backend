package com.gogidix.digitalmarketing.socialmedia.domain.repository;

import com.gogidix.digitalmarketing.socialmedia.domain.model.SocialPost;
import com.gogidix.digitalmarketing.shared.infrastructure.persistence.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.CountQuery;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * SocialPost Repository - Data access for social media posts
 *
 * <p>All queries automatically filter by current tenant via BaseRepository.</p>
 */
@Repository
public interface SocialPostRepository extends BaseRepository<SocialPost> {

    // ========== Account/Platform queries ==========

    /**
     * Find posts by account ID.
     *
     * @param accountId the account ID
     * @return list of posts
     */
    List<SocialPost> findByAccountId(String accountId);

    /**
     * Find posts by account ID with pagination.
     *
     * @param accountId the account ID
     * @param pageable pagination parameters
     * @return page of posts
     */
    Page<SocialPost> findByAccountId(String accountId, Pageable pageable);

    /**
     * Find posts by platform.
     *
     * @param platform the platform
     * @return list of posts
     */
    List<SocialPost> findByPlatform(String platform);

    /**
     * Find posts by platform with pagination.
     *
     * @param platform the platform
     * @param pageable pagination parameters
     * @return page of posts
     */
    Page<SocialPost> findByPlatform(String platform, Pageable pageable);

    /**
     * Find posts by platforms.
     *
     * @param platforms list of platforms
     * @return list of posts
     */
    List<SocialPost> findByPlatformIn(List<String> platforms);

    // ========== Status queries ==========

    /**
     * Find posts by status.
     *
     * @param status the status
     * @return list of posts
     */
    List<SocialPost> findByStatus(String status);

    /**
     * Find posts by status with pagination.
     *
     * @param status the status
     * @param pageable pagination parameters
     * @return page of posts
     */
    Page<SocialPost> findByStatus(String status, Pageable pageable);

    /**
     * Find draft posts.
     *
     * @return list of draft posts
     */
    @Query("{ 'status': 'DRAFT' }")
    List<SocialPost> findDraftPosts();

    /**
     * Find scheduled posts.
     *
     * @return list of scheduled posts
     */
    @Query("{ 'status': 'SCHEDULED' }")
    List<SocialPost> findScheduledPosts();

    /**
     * Find published posts.
     *
     * @return list of published posts
     */
    @Query("{ 'status': 'PUBLISHED' }")
    List<SocialPost> findPublishedPosts();

    /**
     * Find failed posts.
     *
     * @return list of failed posts
     */
    @Query("{ 'status': 'FAILED' }")
    List<SocialPost> findFailedPosts();

    /**
     * Find posts that can be retried.
     *
     * @return list of retryable posts
     */
    @Query("{ 'status': 'FAILED', 'retryCount': { $lt: 3 } }")
    List<SocialPost> findRetryablePosts();

    // ========== Scheduling queries ==========

    /**
     * Find posts due for publishing.
     *
     * @param now current time
     * @return list of due posts
     */
    @Query("{ 'status': 'SCHEDULED', 'scheduledAt': { $lte: ?0 } }")
    List<SocialPost> findDuePosts(Instant now);

    /**
     * Find posts scheduled between dates.
     *
     * @param start start date
     * @param end end date
     * @return list of posts
     */
    @Query("{ 'status': 'SCHEDULED', 'scheduledAt': { $gte: ?0, $lte: ?1 } }")
    List<SocialPost> findScheduledBetween(Instant start, Instant end);

    /**
     * Find posts scheduled after date.
     *
     * @param date the date
     * @return list of posts
     */
    List<SocialPost> findByScheduledAtAfter(Instant date);

    /**
     * Find posts scheduled before date.
     *
     * @param date the date
     * @return list of posts
     */
    List<SocialPost> findByScheduledAtBefore(Instant date);

    // ========== Approval queries ==========

    /**
     * Find posts by approval status.
     *
     * @param approvalStatus the approval status
     * @return list of posts
     */
    List<SocialPost> findByApprovalStatus(String approvalStatus);

    /**
     * Find posts pending approval.
     *
     * @return list of pending posts
     */
    @Query("{ 'approvalStatus': 'PENDING_APPROVAL' }")
    List<SocialPost> findPendingApproval();

    /**
     * Find posts approved by user.
     *
     * @param approvedBy the user who approved
     * @return list of approved posts
     */
    List<SocialPost> findByApprovedBy(String approvedBy);

    // ========== Campaign/Content queries ==========

    /**
     * Find posts by campaign ID.
     *
     * @param campaignId the campaign ID
     * @return list of posts
     */
    List<SocialPost> findByCampaignId(String campaignId);

    /**
     * Find posts by content library ID.
     *
     * @param contentLibraryId the content library ID
     * @return list of posts
     */
    List<SocialPost> findByContentLibraryId(String contentLibraryId);

    /**
     * Find posts with campaign ID.
     *
     * @param campaignId the campaign ID
     * @return list of posts
     */
    @Query("{ 'campaignId': { $ne: null, $eq: ?0 } }")
    List<SocialPost> findWithCampaignId(String campaignId);

    // ========== Priority queries ==========

    /**
     * Find posts by priority.
     *
     * @param priority the priority
     * @return list of posts
     */
    List<SocialPost> findByPriority(String priority);

    /**
     * Find high priority posts.
     *
     * @return list of high priority posts
     */
    @Query("{ 'priority': { $in: ['HIGH', 'URGENT'] } }")
    List<SocialPost> findHighPriorityPosts();

    /**
     * Find urgent posts.
     *
     * @return list of urgent posts
     */
    @Query("{ 'priority': 'URGENT' }")
    List<SocialPost> findUrgentPosts();

    // ========== Tag/Category queries ==========

    /**
     * Find posts with tag.
     *
     * @param tag the tag
     * @return list of posts
     */
    @Query("{ 'tags': { $in: [?0] } }")
    List<SocialPost> findByTag(String tag);

    /**
     * Find posts with any of the tags.
     *
     * @param tags list of tags
     * @return list of posts
     */
    @Query("{ 'tags': { $in: ?0 } }")
    List<SocialPost> findByTagsIn(List<String> tags);

    /**
     * Find posts with all tags.
     *
     * @param tags list of tags
     * @return list of posts
     */
    @Query("{ 'tags': { $all: ?0 } }")
    List<SocialPost> findByTagsAll(List<String> tags);

    /**
     * Find posts by category.
     *
     * @param category the category
     * @return list of posts
     */
    @Query("{ 'categories': { $in: [?0] } }")
    List<SocialPost> findByCategory(String category);

    // ========== Creation method queries ==========

    /**
     * Find posts by creation method.
     *
     * @param creationMethod the creation method
     * @return list of posts
     */
    List<SocialPost> findByCreationMethod(String creationMethod);

    /**
     * Find AI-generated posts.
     *
     * @return list of AI posts
     */
    @Query("{ 'creationMethod': 'AI_GENERATED' }")
    List<SocialPost> findAIGeneratedPosts();

    // ========== External post queries ==========

    /**
     * Find post by external post ID.
     *
     * @param externalPostId the external ID
     * @return optional post
     */
    @Query("{ 'externalPostId': ?0 }")
    java.util.Optional<SocialPost> findByExternalPostId(String externalPostId);

    // ========== Published date queries ==========

    /**
     * Find posts published between dates.
     *
     * @param start start date
     * @param end end date
     * @return list of posts
     */
    @Query("{ 'status': 'PUBLISHED', 'publishedAt': { $gte: ?0, $lte: ?1 } }")
    List<SocialPost> findPublishedBetween(Instant start, Instant end);

    /**
     * Find posts published after date.
     *
     * @param date the date
     * @return list of posts
     */
    List<SocialPost> findByPublishedAtAfter(Instant date);

    /**
     * Find posts published before date.
     *
     * @param date the date
     * @return list of posts
     */
    List<SocialPost> findByPublishedAtBefore(Instant date);

    // ========== Media queries ==========

    /**
     * Find posts with media.
     *
     * @return list of posts with media
     */
    @Query("{ 'mediaUrls': { $exists: true, $ne: [] } }")
    List<SocialPost> findPostsWithMedia();

    /**
     * Find posts without media.
     *
     * @return list of posts without media
     */
    @Query("{ '$or': [ " +
            "{ 'mediaUrls': null }, " +
            "{ 'mediaUrls': { $size: 0 } } " +
            "] }")
    List<SocialPost> findPostsWithoutMedia();

    // ========== Count queries ==========

    /**
     * Count posts by account.
     *
     * @param accountId the account ID
     * @return count of posts
     */
    @CountQuery("{ 'accountId': ?0 }")
    long countByAccountId(String accountId);

    /**
     * Count posts by status.
     *
     * @param status the status
     * @return count of posts
     */
    long countByStatus(String status);

    /**
     * Count posts by platform.
     *
     * @param platform the platform
     * @return count of posts
     */
    @CountQuery("{ 'platform': ?0 }")
    long countByPlatform(String platform);

    /**
     * Count posts by campaign.
     *
     * @param campaignId the campaign ID
     * @return count of posts
     */
    @CountQuery("{ 'campaignId': ?0 }")
    long countByCampaignId(String campaignId);

    // ========== Unique values ==========

    /**
     * Find all unique platforms.
     *
     * @return list of platforms
     */
    @Query("{ 'platform': { $exists: true } }")
    List<String> findDistinctPlatforms();

    /**
     * Find all unique statuses.
     *
     * @return list of statuses
     */
    @Query("{ 'status': { $exists: true } }")
    List<String> findDistinctStatuses();

    /**
     * Find all unique tags.
     *
     * @return list of tags
     */
    @Query("{ 'tags': { $exists: true } }")
    List<String> findDistinctTags();

    // ========== Search ==========

    /**
     * Search posts by content.
     *
     * @param searchTerm the search term
     * @return list of matching posts
     */
    @Query("{ 'content': { $regex: ?0, $options: 'i' } }")
    List<SocialPost> searchByContent(String searchTerm);

    /**
     * Search posts by multiple fields.
     *
     * @param searchTerm the search term
     * @return list of matching posts
     */
    @Query("{ $or: [ " +
            "{ 'content': { $regex: ?0, $options: 'i' } }, " +
            "{ 'linkTitle': { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<SocialPost> search(String searchTerm);
}
