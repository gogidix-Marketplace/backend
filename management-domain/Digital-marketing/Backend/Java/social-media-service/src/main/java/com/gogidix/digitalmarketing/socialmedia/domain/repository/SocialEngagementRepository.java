package com.gogidix.digitalmarketing.socialmedia.domain.repository;

import com.gogidix.digitalmarketing.socialmedia.domain.model.SocialEngagement;
import com.gogidix.digitalmarketing.shared.infrastructure.persistence.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.CountQuery;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * SocialEngagement Repository - Data access for social engagement metrics
 *
 * <p>All queries automatically filter by current tenant via BaseRepository.</p>
 */
@Repository
public interface SocialEngagementRepository extends BaseRepository<SocialEngagement> {

    // ========== Post queries ==========

    /**
     * Find engagement by post ID.
     *
     * @param postId the post ID
     * @return list of engagement snapshots
     */
    List<SocialEngagement> findByPostId(String postId);

    /**
     * Find latest engagement by post ID.
     *
     * @param postId the post ID
     * @return optional latest engagement
     */
    @Query("{ 'postId': ?0, 'isLatest': true }")
    java.util.Optional<SocialEngagement> findLatestByPostId(String postId);

    /**
     * Find engagement by post IDs.
     *
     * @param postIds list of post IDs
     * @return list of engagement snapshots
     */
    List<SocialEngagement> findByPostIdIn(List<String> postIds);

    // ========== Platform/Account queries ==========

    /**
     * Find engagement by platform.
     *
     * @param platform the platform
     * @return list of engagement
     */
    List<SocialEngagement> findByPlatform(String platform);

    /**
     * Find engagement by platform with pagination.
     *
     * @param platform the platform
     * @param pageable pagination parameters
     * @return page of engagement
     */
    Page<SocialEngagement> findByPlatform(String platform, Pageable pageable);

    /**
     * Find engagement by account ID.
     *
     * @param accountId the account ID
     * @return list of engagement
     */
    List<SocialEngagement> findByAccountId(String accountId);

    /**
     * Find engagement by account ID with pagination.
     *
     * @param accountId the account ID
     * @param pageable pagination parameters
     * @return page of engagement
     */
    Page<SocialEngagement> findByAccountId(String accountId, Pageable pageable);

    // ========== Date queries ==========

    /**
     * Find engagement by engagement date.
     *
     * @param engagementDate the date
     * @return list of engagement
     */
    List<SocialEngagement> findByEngagementDate(Instant engagementDate);

    /**
     * Find engagement between dates.
     *
     * @param start start date
     * @param end end date
     * @return list of engagement
     */
    List<SocialEngagement> findByEngagementDateBetween(Instant start, Instant end);

    /**
     * Find engagement after date.
     *
     * @param date the date
     * @return list of engagement
     */
    List<SocialEngagement> findByEngagementDateAfter(Instant date);

    /**
     * Find engagement before date.
     *
     * @param date the date
     * @return list of engagement
     */
    List<SocialEngagement> findByEngagementDateBefore(Instant date);

    // ========== Latest snapshot queries ==========

    /**
     * Find all latest snapshots.
     *
     * @return list of latest engagement
     */
    List<SocialEngagement> findByIsLatestTrue();

    /**
     * Find latest engagement by platform.
     *
     * @param platform the platform
     * @return list of latest engagement
     */
    @Query("{ 'platform': ?0, 'isLatest': true }")
    List<SocialEngagement> findLatestByPlatform(String platform);

    /**
     * Find latest engagement for posts.
     *
     * @param postIds list of post IDs
     * @return list of latest engagement
     */
    @Query("{ 'postId': { $in: ?0 }, 'isLatest': true }")
    List<SocialEngagement> findLatestByPostIds(List<String> postIds);

    // ========== Performance queries ==========

    /**
     * Find high performing posts by engagement rate.
     *
     * @param threshold minimum engagement rate
     * @return list of high performing posts
     */
    @Query("{ 'engagementRate': { $gte: ?0 }, 'isLatest': true }")
    List<SocialEngagement> findHighPerforming(Double threshold);

    /**
     * Find top posts by total engagement.
     *
     * @param pageable pagination with sort
     * @return page of engagement
     */
    Page<SocialEngagement> findAllByIsLatestTrueOrderByTotalEngagementDesc(Pageable pageable);

    /**
     * Find top posts by likes.
     *
     * @param pageable pagination with sort
     * @return page of engagement
     */
    Page<SocialEngagement> findAllByIsLatestTrueOrderByLikesDesc(Pageable pageable);

    /**
     * Find top posts by shares.
     *
     * @param pageable pagination with sort
     * @return page of engagement
     */
    Page<SocialEngagement> findAllByIsLatestTrueOrderBySharesDesc(Pageable pageable);

    /**
     * Find top posts by views.
     *
     * @param pageable pagination with sort
     * @return page of engagement
     */
    Page<SocialEngagement> findAllByIsLatestTrueOrderByViewsDesc(Pageable pageable);

    // ========== Metric threshold queries ==========

    /**
     * Find posts with minimum likes.
     *
     * @param minLikes minimum likes
     * @return list of engagement
     */
    @Query("{ 'likes': { $gte: ?0 }, 'isLatest': true }")
    List<SocialEngagement> findByMinLikes(Long minLikes);

    /**
     * Find posts with minimum shares.
     *
     * @param minShares minimum shares
     * @return list of engagement
     */
    @Query("{ 'shares': { $gte: ?0 }, 'isLatest': true }")
    List<SocialEngagement> findByMinShares(Long minShares);

    /**
     * Find posts with minimum comments.
     *
     * @param minComments minimum comments
     * @return list of engagement
     */
    @Query("{ 'comments': { $gte: ?0 }, 'isLatest': true }")
    List<SocialEngagement> findByMinComments(Long minComments);

    /**
     * Find posts with minimum views.
     *
     * @param minViews minimum views
     * @return list of engagement
     */
    @Query("{ 'views': { $gte: ?0 }, 'isLatest': true }")
    List<SocialEngagement> findByMinViews(Long minViews);

    /**
     * Find posts with minimum reach.
     *
     * @param minReach minimum reach
     * @return list of engagement
     */
    @Query("{ 'reach': { $gte: ?0 }, 'isLatest': true }")
    List<SocialEngagement> findByMinReach(Long minReach);

    // ========== Sentiment queries ==========

    /**
     * Find engagement by sentiment.
     *
     * @param sentiment the sentiment
     * @return list of engagement
     */
    @Query("{ 'sentiment': ?0, 'isLatest': true }")
    List<SocialEngagement> findBySentiment(String sentiment);

    /**
     * Find positive sentiment posts.
     *
     * @return list of positive engagement
     */
    @Query("{ 'sentiment': 'POSITIVE', 'isLatest': true }")
    List<SocialEngagement> findPositiveSentiment();

    /**
     * Find negative sentiment posts.
     *
     * @return list of negative engagement
     */
    @Query("{ 'sentiment': 'NEGATIVE', 'isLatest': true }")
    List<SocialEngagement> findNegativeSentiment();

    // ========== Sync status queries ==========

    /**
     * Find engagement by sync status.
     *
     * @param syncStatus the sync status
     * @return list of engagement
     */
    List<SocialEngagement> findBySyncStatus(String syncStatus);

    /**
     * Find pending sync engagement.
     *
     * @return list of pending engagement
     */
    @Query("{ 'syncStatus': 'PENDING' }")
    List<SocialEngagement> findPendingSync();

    /**
     * Find error sync engagement.
     *
     * @return list of error engagement
     */
    @Query("{ 'syncStatus': 'ERROR' }")
    List<SocialEngagement> findErrorSync();

    // ========== Data source queries ==========

    /**
     * Find engagement by data source.
     *
     * @param dataSource the data source
     * @return list of engagement
     */
    List<SocialEngagement> findByDataSource(String dataSource);

    // ========== External post queries ==========

    /**
     * Find engagement by external post ID.
     *
     * @param externalPostId the external post ID
     * @return list of engagement
     */
    List<SocialEngagement> findByExternalPostId(String externalPostId);

    /**
     * Find latest engagement by external post ID.
     *
     * @param externalPostId the external post ID
     * @return optional engagement
     */
    @Query("{ 'externalPostId': ?0, 'isLatest': true }")
    java.util.Optional<SocialEngagement> findLatestByExternalPostId(String externalPostId);

    // ========== Previous engagement queries ==========

    /**
     * Find engagement by previous engagement ID.
     *
     * @param previousEngagementId the previous engagement ID
     * @return list of engagement
     */
    List<SocialEngagement> findByPreviousEngagementId(String previousEngagementId);

    // ========== Aggregate queries ==========

    /**
     * Calculate total engagement for account.
     *
     * @param accountId the account ID
     * @return total engagement
     */
    @Query(value = "{ 'accountId': ?0, 'isLatest': true }", count = true)
    long countLatestByAccountId(String accountId);

    /**
     * Sum total engagement for posts.
     *
     * @param postIds list of post IDs
     * @return aggregated engagement
     */
    @Query("{ 'postId': { $in: ?0 }, 'isLatest': true }")
    List<SocialEngagement> findLatestForAggregation(List<String> postIds);

    // ========== Date range latest queries ==========

    /**
     * Find latest engagement between dates.
     *
     * @param start start date
     * @param end end date
     * @return list of engagement
     */
    @Query("{ 'engagementDate': { $gte: ?0, $lte: ?1 }, 'isLatest': true }")
    List<SocialEngagement> findLatestBetweenDates(Instant start, Instant end);

    // ========== Count queries ==========

    /**
     * Count engagement by post.
     *
     * @param postId the post ID
     * @return count of snapshots
     */
    @CountQuery("{ 'postId': ?0 }")
    long countByPostId(String postId);

    /**
     * Count engagement by platform.
     *
     * @param platform the platform
     * @return count of engagement
     */
    @CountQuery("{ 'platform': ?0 }")
    long countByPlatform(String platform);

    /**
     * Count latest engagement by platform.
     *
     * @param platform the platform
     * @return count of latest engagement
     */
    @CountQuery("{ 'platform': ?0, 'isLatest': true }")
    long countLatestByPlatform(String platform);

    // ========== Unique values ==========

    /**
     * Find all unique platforms.
     *
     * @return list of platforms
     */
    @Query("{ 'platform': { $exists: true } }")
    List<String> findDistinctPlatforms();

    /**
     * Find all unique sentiments.
     *
     * @return list of sentiments
     */
    @Query("{ 'sentiment': { $exists: true } }")
    List<String> findDistinctSentiments();
}
