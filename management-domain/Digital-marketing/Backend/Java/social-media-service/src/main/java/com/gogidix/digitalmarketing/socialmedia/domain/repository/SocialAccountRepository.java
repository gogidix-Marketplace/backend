package com.gogidix.digitalmarketing.socialmedia.domain.repository;

import com.gogidix.digitalmarketing.socialmedia.domain.model.SocialAccount;
import com.gogidix.digitalmarketing.shared.infrastructure.persistence.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.CountQuery;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * SocialAccount Repository - Data access for social media accounts
 *
 * <p>All queries automatically filter by current tenant via BaseRepository.</p>
 */
@Repository
public interface SocialAccountRepository extends BaseRepository<SocialAccount> {

    // ========== Platform queries ==========

    /**
     * Find accounts by platform.
     *
     * @param platform the platform
     * @return list of accounts
     */
    List<SocialAccount> findByPlatform(String platform);

    /**
     * Find accounts by platform with pagination.
     *
     * @param platform the platform
     * @param pageable pagination parameters
     * @return page of accounts
     */
    Page<SocialAccount> findByPlatform(String platform, Pageable pageable);

    /**
     * Find accounts by platforms.
     *
     * @param platforms list of platforms
     * @return list of accounts
     */
    List<SocialAccount> findByPlatformIn(List<String> platforms);

    // ========== Status queries ==========

    /**
     * Find accounts by status.
     *
     * @param status the status
     * @return list of accounts
     */
    List<SocialAccount> findByStatus(String status);

    /**
     * Find accounts by connection status.
     *
     * @param connectionStatus the connection status
     * @return list of accounts
     */
    List<SocialAccount> findByConnectionStatus(String connectionStatus);

    /**
     * Find active and connected accounts.
     *
     * @return list of active accounts
     */
    @Query("{ 'status': 'ACTIVE', 'connectionStatus': 'CONNECTED' }")
    List<SocialAccount> findActiveConnectedAccounts();

    /**
     * Find accounts that need attention (error or expired).
     *
     * @return list of accounts needing attention
     */
    @Query("{ '$or': [ " +
            "{ 'status': 'ERROR' }, " +
            "{ 'connectionStatus': 'DISCONNECTED' }, " +
            "{ 'connectionStatus': 'FAILED' } " +
            "] }")
    List<SocialAccount> findAccountsNeedingAttention();

    // ========== Username/Account queries ==========

    /**
     * Find account by username.
     *
     * @param username the username
     * @return optional account
     */
    Optional<SocialAccount> findByUsername(String username);

    /**
     * Find account by account ID on platform.
     *
     * @param accountId the account ID
     * @return optional account
     */
    Optional<SocialAccount> findByAccountId(String accountId);

    /**
     * Find account by platform and account ID.
     *
     * @param platform the platform
     * @param accountId the account ID
     * @return optional account
     */
    Optional<SocialAccount> findByPlatformAndAccountId(String platform, String accountId);

    /**
     * Search accounts by username pattern.
     *
     * @param pattern the search pattern
     * @return list of matching accounts
     */
    @Query("{ 'username': { $regex: ?0, $options: 'i' } }")
    List<SocialAccount> searchByUsername(String pattern);

    /**
     * Search accounts by display name pattern.
     *
     * @param pattern the search pattern
     * @return list of matching accounts
     */
    @Query("{ 'displayName': { $regex: ?0, $options: 'i' } }")
    List<SocialAccount> searchByDisplayName(String pattern);

    // ========== Primary/Verified queries ==========

    /**
     * Find primary accounts by platform.
     *
     * @param platform the platform
     * @return list of primary accounts
     */
    List<SocialAccount> findByPlatformAndPrimaryTrue(String platform);

    /**
     * Find all primary accounts.
     *
     * @return list of primary accounts
     */
    List<SocialAccount> findByPrimaryTrue();

    /**
     * Find verified accounts.
     *
     * @return list of verified accounts
     */
    List<SocialAccount> findByVerifiedTrue();

    // ========== Metric queries ==========

    /**
     * Find accounts with minimum follower count.
     *
     * @param minFollowers minimum followers
     * @return list of accounts
     */
    List<SocialAccount> findByFollowerCountGreaterThanEqual(Long minFollowers);

    /**
     * Find accounts with follower count in range.
     *
     * @param min minimum followers
     * @param max maximum followers
     * @return list of accounts
     */
    @Query("{ 'followerCount': { $gte: ?0, $lte: ?1 } }")
    List<SocialAccount> findByFollowerCountBetween(Long min, Long max);

    /**
     * Find accounts ordered by follower count.
     *
     * @param pageable pagination with sort
     * @return page of accounts
     */
    Page<SocialAccount> findAllByOrderByFollowerCountDesc(Pageable pageable);

    // ========== Token/Sync queries ==========

    /**
     * Find accounts with expiring tokens.
     *
     * @param threshold expiration threshold
     * @return list of accounts needing token refresh
     */
    @Query("{ 'tokenExpiresAt': { $lt: ?0 } }")
    List<SocialAccount> findAccountsNeedingTokenRefresh(Instant threshold);

    /**
     * Find accounts that need sync.
     *
     * @param threshold sync threshold
     * @return list of accounts needing sync
     */
    @Query("{ '$or': [ " +
            "{ 'lastSyncedAt': null }, " +
            "{ 'lastSyncedAt': { $lt: ?0 } } " +
            "] }")
    List<SocialAccount> findAccountsNeedingSync(Instant threshold);

    /**
     * Find accounts by last synced before date.
     *
     * @param date the date threshold
     * @return list of accounts
     */
    List<SocialAccount> findByLastSyncedAtBefore(Instant date);

    // ========== Capability queries ==========

    /**
     * Find accounts with specific capability.
     *
     * @param capability the capability
     * @return list of accounts
     */
    @Query("{ 'capabilities': { $in: [?0] } }")
    List<SocialAccount> findByCapability(String capability);

    /**
     * Find accounts with all specified capabilities.
     *
     * @param capabilities list of capabilities
     * @return list of accounts
     */
    @Query("{ 'capabilities': { $all: ?0 } }")
    List<SocialAccount> findByCapabilities(List<String> capabilities);

    // ========== Campaign/Content queries ==========

    /**
     * Find accounts associated with campaign.
     *
     * @param campaignId the campaign ID
     * @return list of accounts
     */
    @Query("{ 'metadata.campaignId': ?0 }")
    List<SocialAccount> findByCampaignId(String campaignId);

    // ========== Health queries ==========

    /**
     * Find accounts with minimum health score.
     *
     * @param minScore minimum score
     * @return list of accounts
     */
    List<SocialAccount> findByHealthScoreGreaterThanEqual(Integer minScore);

    /**
     * Find accounts with low health score.
     *
     * @param maxScore maximum score
     * @return list of accounts
     */
    List<SocialAccount> findByHealthScoreLessThan(Integer maxScore);

    // ========== Count queries ==========

    /**
     * Count accounts by platform.
     *
     * @param platform the platform
     * @return count of accounts
     */
    @CountQuery("{ 'platform': ?0 }")
    long countByPlatform(String platform);

    /**
     * Count accounts by status.
     *
     * @param status the status
     * @return count of accounts
     */
    long countByStatus(String status);

    /**
     * Count accounts by connection status.
     *
     * @param connectionStatus the connection status
     * @return count of accounts
     */
    long countByConnectionStatus(String connectionStatus);

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

    // ========== Search ==========

    /**
     * Search accounts by multiple fields.
     *
     * @param searchTerm the search term
     * @return list of matching accounts
     */
    @Query("{ $or: [ " +
            "{ 'username': { $regex: ?0, $options: 'i' } }, " +
            "{ 'displayName': { $regex: ?0, $options: 'i' } }, " +
            "{ 'accountId': { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<SocialAccount> search(String searchTerm);
}
