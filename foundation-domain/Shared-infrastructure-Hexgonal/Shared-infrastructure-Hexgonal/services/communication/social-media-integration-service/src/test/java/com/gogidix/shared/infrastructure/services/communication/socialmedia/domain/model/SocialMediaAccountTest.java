package com.gogidix.shared.infrastructure.services.communication.socialmedia.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SocialMediaAccount domain model.
 */
@DisplayName("SocialMediaAccount Domain Model Tests")
class SocialMediaAccountTest {

    @Test
    @DisplayName("Should create social media account with builder")
    void shouldCreateSocialMediaAccountWithBuilder() {
        TenantId tenantId = new TenantId("tenant123");

        SocialMediaAccount account = SocialMediaAccount.builder()
                .tenantId(tenantId)
                .domainContext("ecommerce")
                .userId(123L)
                .platform(SocialMediaAccount.SocialPlatform.FACEBOOK)
                .platformUserId("fb123")
                .username("testuser")
                .displayName("Test User")
                .followerCount(1000L)
                .build();

        assertEquals(tenantId, account.getTenantId());
        assertEquals("ecommerce", account.getDomainContext());
        assertEquals(123L, account.getUserId());
        assertEquals(SocialMediaAccount.SocialPlatform.FACEBOOK, account.getPlatform());
        assertEquals("fb123", account.getPlatformUserId());
        assertEquals("testuser", account.getUsername());
        assertEquals("Test User", account.getDisplayName());
        assertEquals(1000L, account.getFollowerCount());
    }

    @Test
    @DisplayName("Should create social media account with no-args constructor")
    void shouldCreateSocialMediaAccountWithNoArgsConstructor() {
        SocialMediaAccount account = new SocialMediaAccount();

        assertNotNull(account);
        assertNull(account.getTenantId());
        assertNull(account.getUsername());
        assertNull(account.getPlatform());
    }

    @Test
    @DisplayName("Should set and get all fields")
    void shouldSetAndGetAllFields() {
        TenantId tenantId = new TenantId("tenant123");
        LocalDateTime now = LocalDateTime.now();

        SocialMediaAccount account = new SocialMediaAccount();
        account.setTenantId(tenantId);
        account.setId("account123");
        account.setDomainContext("marketing");
        account.setUserId(456L);
        account.setPlatform(SocialMediaAccount.SocialPlatform.INSTAGRAM);
        account.setPlatformUserId("ig456");
        account.setUsername("instauser");
        account.setDisplayName("Instagram User");
        account.setProfileImageUrl("https://example.com/profile.jpg");
        account.setBio("Test bio");
        account.setFollowerCount(5000L);
        account.setFollowingCount(100L);
        account.setVerified(true);
        account.setSyncStatus(SocialMediaAccount.SyncStatus.ACTIVE);
        account.setAccessToken("access-token");
        account.setRefreshToken("refresh-token");
        account.setTokenExpiresAt(now.plusDays(30));
        account.setLastSyncAt(now);
        account.setIsActive(true);
        account.setPlatformConfig("{\"key\":\"value\"}");
        account.setCreatedAt(now);
        account.setUpdatedAt(now);

        assertEquals(tenantId, account.getTenantId());
        assertEquals("account123", account.getId());
        assertEquals("marketing", account.getDomainContext());
        assertEquals(456L, account.getUserId());
        assertEquals(SocialMediaAccount.SocialPlatform.INSTAGRAM, account.getPlatform());
        assertEquals("ig456", account.getPlatformUserId());
        assertEquals("instauser", account.getUsername());
        assertEquals("Instagram User", account.getDisplayName());
        assertEquals("https://example.com/profile.jpg", account.getProfileImageUrl());
        assertEquals("Test bio", account.getBio());
        assertEquals(5000L, account.getFollowerCount());
        assertEquals(100L, account.getFollowingCount());
        assertTrue(account.getVerified());
        assertEquals(SocialMediaAccount.SyncStatus.ACTIVE, account.getSyncStatus());
        assertEquals("access-token", account.getAccessToken());
        assertEquals("refresh-token", account.getRefreshToken());
        assertEquals(now.plusDays(30), account.getTokenExpiresAt());
        assertEquals(now, account.getLastSyncAt());
        assertTrue(account.getIsActive());
        assertEquals("{\"key\":\"value\"}", account.getPlatformConfig());
        assertEquals(now, account.getCreatedAt());
        assertEquals(now, account.getUpdatedAt());
    }

    @Test
    @DisplayName("Should handle all social platforms")
    void shouldHandleAllSocialPlatforms() {
        SocialMediaAccount.SocialPlatform[] platforms = SocialMediaAccount.SocialPlatform.values();

        assertEquals(6, platforms.length);
        assertEquals(SocialMediaAccount.SocialPlatform.FACEBOOK, SocialMediaAccount.SocialPlatform.valueOf("FACEBOOK"));
        assertEquals(SocialMediaAccount.SocialPlatform.TWITTER, SocialMediaAccount.SocialPlatform.valueOf("TWITTER"));
        assertEquals(SocialMediaAccount.SocialPlatform.INSTAGRAM, SocialMediaAccount.SocialPlatform.valueOf("INSTAGRAM"));
        assertEquals(SocialMediaAccount.SocialPlatform.LINKEDIN, SocialMediaAccount.SocialPlatform.valueOf("LINKEDIN"));
        assertEquals(SocialMediaAccount.SocialPlatform.YOUTUBE, SocialMediaAccount.SocialPlatform.valueOf("YOUTUBE"));
        assertEquals(SocialMediaAccount.SocialPlatform.TIKTOK, SocialMediaAccount.SocialPlatform.valueOf("TIKTOK"));
    }

    @Test
    @DisplayName("Should handle all sync statuses")
    void shouldHandleAllSyncStatuses() {
        SocialMediaAccount.SyncStatus[] statuses = SocialMediaAccount.SyncStatus.values();

        assertEquals(5, statuses.length);
        assertEquals(SocialMediaAccount.SyncStatus.ACTIVE, SocialMediaAccount.SyncStatus.valueOf("ACTIVE"));
        assertEquals(SocialMediaAccount.SyncStatus.INACTIVE, SocialMediaAccount.SyncStatus.valueOf("INACTIVE"));
        assertEquals(SocialMediaAccount.SyncStatus.ERROR, SocialMediaAccount.SyncStatus.valueOf("ERROR"));
        assertEquals(SocialMediaAccount.SyncStatus.PENDING_SYNC, SocialMediaAccount.SyncStatus.valueOf("PENDING_SYNC"));
        assertEquals(SocialMediaAccount.SyncStatus.SYNCING, SocialMediaAccount.SyncStatus.valueOf("SYNCING"));
    }

    @Test
    @DisplayName("Should check if token is expired")
    void shouldCheckIfTokenIsExpired() {
        SocialMediaAccount account = new SocialMediaAccount();
        account.setTokenExpiresAt(LocalDateTime.now().minusDays(1));

        assertTrue(account.isTokenExpired());

        account.setTokenExpiresAt(LocalDateTime.now().plusDays(1));
        assertFalse(account.isTokenExpired());

        account.setTokenExpiresAt(null);
        assertFalse(account.isTokenExpired());
    }

    @Test
    @DisplayName("Should check if account needs sync")
    void shouldCheckIfAccountNeedsSync() {
        SocialMediaAccount account = new SocialMediaAccount();
        account.setLastSyncAt(LocalDateTime.now().minusHours(2));

        assertTrue(account.needsSync());

        account.setLastSyncAt(LocalDateTime.now().minusMinutes(30));
        assertFalse(account.needsSync());

        account.setLastSyncAt(null);
        assertTrue(account.needsSync());
    }

    @Test
    @DisplayName("Should get and set platform configuration")
    void shouldGetAndSetPlatformConfiguration() {
        SocialMediaAccount account = new SocialMediaAccount();

        account.setPlatformConfiguration(java.util.Map.of("apiKey", "abc123", "secret", "xyz789"));

        assertNotNull(account.getPlatformConfiguration());
        assertEquals(2, account.getPlatformConfiguration().size());
    }

    @Test
    @DisplayName("Should handle follower and following counts")
    void shouldHandleFollowerAndFollowingCounts() {
        SocialMediaAccount account = new SocialMediaAccount();
        account.setFollowerCount(10000L);
        account.setFollowingCount(500L);

        assertEquals(10000L, account.getFollowerCount());
        assertEquals(500L, account.getFollowingCount());
    }

    @Test
    @DisplayName("Should handle verified status")
    void shouldHandleVerifiedStatus() {
        SocialMediaAccount account = new SocialMediaAccount();

        account.setVerified(true);
        assertTrue(account.getVerified());

        account.setVerified(false);
        assertFalse(account.getVerified());
    }

    @Test
    @DisplayName("Should handle active status")
    void shouldHandleActiveStatus() {
        SocialMediaAccount account = new SocialMediaAccount();

        account.setIsActive(true);
        assertTrue(account.getIsActive());

        account.setIsActive(false);
        assertFalse(account.getIsActive());
    }

    @Test
    @DisplayName("Should handle access and refresh tokens")
    void shouldHandleAccessAndRefreshTokens() {
        SocialMediaAccount account = new SocialMediaAccount();
        account.setAccessToken("access-token-123");
        account.setRefreshToken("refresh-token-456");
        account.setTokenExpiresAt(LocalDateTime.now().plusDays(30));

        assertEquals("access-token-123", account.getAccessToken());
        assertEquals("refresh-token-456", account.getRefreshToken());
        assertNotNull(account.getTokenExpiresAt());
    }

    @Test
    @DisplayName("Should handle profile image URL")
    void shouldHandleProfileImageUrl() {
        SocialMediaAccount account = new SocialMediaAccount();
        account.setProfileImageUrl("https://example.com/avatar.jpg");

        assertEquals("https://example.com/avatar.jpg", account.getProfileImageUrl());
    }

    @Test
    @DisplayName("Should handle bio")
    void shouldHandleBio() {
        SocialMediaAccount account = new SocialMediaAccount();
        account.setBio("This is my social media account bio");

        assertEquals("This is my social media account bio", account.getBio());
    }

    @Test
    @DisplayName("Should handle domain context")
    void shouldHandleDomainContext() {
        SocialMediaAccount account = new SocialMediaAccount();
        account.setDomainContext("logistics");

        assertEquals("logistics", account.getDomainContext());
    }
}
