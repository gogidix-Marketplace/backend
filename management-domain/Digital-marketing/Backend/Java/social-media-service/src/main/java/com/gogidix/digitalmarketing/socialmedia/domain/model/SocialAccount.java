package com.gogidix.digitalmarketing.socialmedia.domain.model;

import com.gogidix.digitalmarketing.shared.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * SocialAccount - Connected social media account
 *
 * <p>Represents a connected social media account (Facebook, Twitter, Instagram, LinkedIn, etc.)
 * for a tenant. Stores connection details, authentication tokens, and account metadata.</p>
 *
 * <p>Tenant Isolation: All queries MUST filter by tenantId</p>
 */
@Document(collection = "social_accounts")
@TypeAlias("social_account")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "account_tenant_platform_idx", def = "{'tenantId': 1, 'platform': 1, 'accountId': 1}")
public class SocialAccount extends BaseEntity {

    /**
     * Platform type (FACEBOOK, TWITTER, INSTAGRAM, LINKEDIN, TIKTOK, YOUTUBE, PINTEREST)
     */
    @Indexed
    private String platform;

    /**
     * Account ID/handle on the platform
     */
    @Indexed
    private String accountId;

    /**
     * Account name/username
     */
    @Indexed
    private String username;

    /**
     * Display name
     */
    private String displayName;

    /**
     * Profile/avatar URL
     */
    private String profileImageUrl;

    /**
     * Profile URL
     */
    private String profileUrl;

    /**
     * OAuth access token (encrypted)
     */
    private String accessToken;

    /**
     * OAuth refresh token (encrypted)
     */
    private String refreshToken;

    /**
     * Token expiration time
     */
    private Instant tokenExpiresAt;

    /**
     * Account status (ACTIVE, INACTIVE, EXPIRED, SUSPENDED, ERROR)
     */
    @Indexed
    private String status;

    private Boolean isActive = true;

    /**
     * Connection status (CONNECTED, DISCONNECTED, PENDING, FAILED)
     */
    @Builder.Default
    private String connectionStatus = "PENDING";

    /**
     * Number of followers
     */
    private Long followerCount;

    /**
     * Number of following
     */
    private Long followingCount;

    /**
     * Number of posts
     */
    private Long postCount;

    /**
     * Account verification status
     */
    @Builder.Default
    private Boolean verified = false;

    /**
     * Primary account flag (can have multiple accounts per platform)
     */
    @Builder.Default
    private Boolean primary = false;

    /**
     * Account capabilities (posting, analytics, messaging, etc.)
     */
    private java.util.List<String> capabilities;

    /**
     * Additional platform-specific metadata
     */
    private Map<String, Object> metadata;

    /**
     * Last sync timestamp
     */
    private Instant lastSyncedAt;

    /**
     * Last error message
     */
    private String lastError;

    /**
     * Account connection score (0-100)
     */
    private Integer healthScore;

    /**
     * Create a new SocialAccount for a tenant.
     *
     * @param tenantId the tenant ID
     * @param platform the platform type
     * @param accountId the account ID on platform
     * @param username the username
     */
    public SocialAccount(String tenantId, String platform, String accountId, String username) {
        super(tenantId);
        this.platform = platform;
        this.accountId = accountId;
        this.username = username;
        this.status = "ACTIVE";
        this.connectionStatus = "PENDING";
        this.primary = false;
        this.verified = false;
    }

    /**
     * Check if account is active and connected.
     *
     * @return true if account can be used
     */
    public boolean isActiveAndConnected() {
        return "ACTIVE".equals(this.status) && "CONNECTED".equals(this.connectionStatus);
    }

    /**
     * Check if token needs refresh.
     *
     * @return true if token expires within 24 hours
     */
    public boolean needsTokenRefresh() {
        if (tokenExpiresAt == null) {
            return false;
        }
        Instant twentyFourHoursFromNow = Instant.now().plusSeconds(86400);
        return tokenExpiresAt.isBefore(twentyFourHoursFromNow);
    }

    /**
     * Check if account needs sync.
     *
     * @param thresholdHours hours since last sync
     * @return true if sync needed
     */
    public boolean needsSync(int thresholdHours) {
        if (lastSyncedAt == null) {
            return true;
        }
        Instant threshold = Instant.now().minusSeconds(thresholdHours * 3600L);
        return lastSyncedAt.isBefore(threshold);
    }

    /**
     * Update account metrics from platform data.
     *
     * @param followerCount follower count
     * @param followingCount following count
     * @param postCount post count
     */
    public void updateMetrics(Long followerCount, Long followingCount, Long postCount) {
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        this.postCount = postCount;
        this.lastSyncedAt = Instant.now();
        this.touch();
    }

    /**
     * Mark account as connected.
     */
    public void markAsConnected() {
        this.connectionStatus = "CONNECTED";
        this.status = "ACTIVE";
        this.lastSyncedAt = Instant.now();
        this.lastError = null;
        this.touch();
    }

    /**
     * Mark account as disconnected with error.
     *
     * @param error error message
     */
    public void markAsDisconnected(String error) {
        this.connectionStatus = "DISCONNECTED";
        this.status = "ERROR";
        this.lastError = error;
        this.touch();
    }

    /**
     * Calculate health score based on connection and metrics.
     *
     * @return health score (0-100)
     */
    public int calculateHealthScore() {
        int score = 100;

        if (!"CONNECTED".equals(connectionStatus)) {
            score -= 50;
        }
        if (!"ACTIVE".equals(status)) {
            score -= 30;
        }
        if (lastError != null) {
            score -= 20;
        }
        if (needsTokenRefresh()) {
            score -= 10;
        }

        this.healthScore = Math.max(0, Math.min(100, score));
        return this.healthScore;
    }

    /**
     * Add metadata to this account.
     *
     * @param key the metadata key
     * @param value the metadata value
     */
    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }

    /**
     * Get a metadata value.
     *
     * @param key the metadata key
     * @return the metadata value, or null if not set
     */
    public Object getMetadata(String key) {
        if (this.metadata == null) {
            return null;
        }
        return this.metadata.get(key);
    }
}
